package org.milyn.smooks.directmapping.gae;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;

import org.milyn.smooks.directmapping.processing.input.ZTree_JSON_Node;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.datastore.Transaction;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;



@SuppressWarnings("serial")
public class XMLToJSONDataModel extends HttpServlet {

	private static final Logger logger = Logger.getLogger(XMLToJSONDataModel.class.getCanonicalName());
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		try {
			
			String sourceXML = URLDecoder.decode(req.getParameter("source"), "UTF-8");
			String destinationXML = URLDecoder.decode(req.getParameter("destination"), "UTF-8");
			
			if (sourceXML != null && sourceXML.length() > 0 && destinationXML != null && destinationXML.length() > 0) {
				String sourcekey = storeXML(sourceXML, "sourceXML" );
				String destinationkey = storeXML(sourceXML, "destinationXML" );
				returnJSON(processXMLtoJSON(sourceXML), processXMLtoJSON(destinationXML),sourcekey, destinationkey, resp.getWriter());
			} else {
				resp.sendRedirect("/");
			}
			
			
			
	        
			
			} catch (Exception e) {
			logger.log(Level.SEVERE, "Exception happening during XML document processing", e);
			throw new RuntimeException("Failed to process XML document", e);
			} 
	}

	
	
	
	
	private void returnJSON(String source, String destination, String mapping, String functions, PrintWriter out) throws IOException, SAXException {
		
		 JsonObject json = new JsonObject();
		 json.addProperty("source", source);
		 json.addProperty("destination", destination);
		 json.addProperty("sourceXML", mapping);
		 json.addProperty("destinationXML", functions);
		 out.write(json.toString());
         
         
			
	}
	
	

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.sendRedirect("/");
	}
	
	
	
	private String processXMLtoJSON(String xml) {
		
		String json = "";
		try {
			
		
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));
			
			
			Collection<ZTree_JSON_Node> collection = new ArrayList<ZTree_JSON_Node>();
			Node root = doc.getDocumentElement();
			
			collection.add(new ZTree_JSON_Node(1, 0, root.getNodeName(), "/" + root.getNodeName(), true, true));
			if(root.hasAttributes())
			{
				vsTraverseAttr(root, collection, 1 , "/" + root.getNodeName());
			}
			
			vsTraverse(root, collection, 1 , "/" + root.getNodeName());
			//collection = addModelAnnotation(collection);
			
			
		
			Gson gson = new Gson();
			json = gson.toJson(collection);
		} catch (Exception e) {
			 logger.log(Level.WARNING, "XML to JSON XML problem occured : " + e.getMessage()); 
				
		  }
		return json;
		
	}
	
	
	
	/** recursive processing of xml elements bottom down 
	 *
	 * @input1 node to process
	 * @input2 json object to push data into
	 * @input3 parent xpath 
	 *
	 **/
	private void vsTraverse(Node node, Collection<ZTree_JSON_Node> collection, int pId, String xpath) {
		 
		    NodeList nodeList = node.getChildNodes();
		    for (int i = 0; i < nodeList.getLength(); i++) {
		       
		    	Node currentNode = nodeList.item(i);
		        String currentxpath = xpath +  "/" + currentNode.getNodeName();
		        
		        
		        
		        if(currentNode.hasChildNodes())
		        {
		        	int id = collection.size() + 1;
		        	collection =  addNodeToModel(new ZTree_JSON_Node(id, pId, currentNode.getNodeName(),currentxpath , true, true), collection);
					if(currentNode.hasAttributes())
					{				
						vsTraverseAttr(currentNode, collection, id , currentxpath);
					}
					vsTraverse(currentNode, collection, id , currentxpath);

			    
		        }
		        else
		        {
		        	if(currentNode.hasAttributes())
					{
		        		int id = collection.size() + 1;
		        		collection=  addNodeToModel(new ZTree_JSON_Node(id, pId, currentNode.getNodeName(),currentxpath , true, true),collection);
		            	vsTraverseAttr(currentNode, collection, id , currentxpath);
					}
		        	else if(currentNode.getNodeType() == Node.ELEMENT_NODE)
		        	{
		        		int id = collection.size() + 1;
		        		collection=  addNodeToModel(new ZTree_JSON_Node(id, pId, currentNode.getNodeName(),currentxpath),collection);
		     		   
		        	}
		        	
		        }
		             
		        
		        
		        
		    }
		}
	  

	  

	  
	  /** processing  attributes of xml element
	  *
	  * @input1 node to process
	  * @input2 parent xpath 
	  * @output array of attributes in json format
	  *
	  **/
	  private void vsTraverseAttr(Node node, Collection<ZTree_JSON_Node> collection, int pId, String xpath) {
		  //only when attributes exists else return null
			    NamedNodeMap nodeList = node.getAttributes();
				
			    int length = nodeList.getLength();
			    for( int i=0; i<length; i++) {
			        Attr attr = (Attr) nodeList.item(i);
			        String name = attr.getName();
			        
			    	collection=  addNodeToModel(new ZTree_JSON_Node(collection.size()+1, pId, "attr "  + name,xpath + "/@" +  name),collection);
			    	
			       
			    }
			        
			 
		
	  
	  }

	
	  private Collection<ZTree_JSON_Node>  addNodeToModel(ZTree_JSON_Node node, Collection<ZTree_JSON_Node> collection)
	  {
		  int i = 0;
		  Iterator<ZTree_JSON_Node> itrnode = collection.iterator();
	      while(itrnode.hasNext()) {
	         ZTree_JSON_Node element = itrnode.next();

	         if(node.getXpath().equalsIgnoreCase(element.getXpath()))
	         {
	        	 i++;
	        	 element.setOccurance(element.getOccurance() +  1);
	         }
	         
	         
	         
	      }
	      
	      if(i==0)
	      {
	    	  collection.add(node);
	      }
	      
		  return collection;
	  }
	  
	  
	  
	  
	  private Collection<ZTree_JSON_Node>  addModelAnnotation(Collection<ZTree_JSON_Node> collection)
	  {
		  Iterator<ZTree_JSON_Node> itrnode = collection.iterator();
	      while(itrnode.hasNext()) {
	         ZTree_JSON_Node element = itrnode.next();

	         if(element.isFolder())
	         {
	        	 element.setName(element.getName() + " [1.." + element.getOccurance() + "]");
	         }
	         
	         
	         
	      }
	      
	    
		  return collection;
	  }
	 
	  
	  /**
		 * store XML
		 * 
		 * @param content
		 */
		private String storeXML(String content, String entity_type) {
			DatastoreService datastore = DatastoreServiceFactory
					.getDatastoreService();
			Transaction transaction = datastore.beginTransaction();
			Entity entity = new Entity(entity_type);
			entity.setProperty("content", new Text(content));
			Key key = datastore.put(entity);
			logger.log(Level.INFO, "Content processing key " + KeyFactory.keyToString(key) + "value " +  new Text(content) );
			
			transaction.commit();
			return KeyFactory.keyToString(key);
		}
	  

	
	}
	
	
	