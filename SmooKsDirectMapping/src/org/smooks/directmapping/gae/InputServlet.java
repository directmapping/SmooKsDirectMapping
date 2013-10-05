package org.smooks.directmapping.gae;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smooks.directmapping.mapping.model.JSONMappingModel;
import org.smooks.directmapping.model.ModelBuilder;
import org.smooks.directmapping.model.xml.XMLSampleModelBuilder;
import org.smooks.directmapping.model.xml.XSDModelBuilder;
import org.xml.sax.SAXException;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.datastore.Transaction;
import com.google.gson.JsonObject;



@SuppressWarnings("serial")
public class InputServlet extends HttpServlet {

	private static final Logger logger = Logger.getLogger(InputServlet.class.getCanonicalName());
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp){
		
		try {
			
			String action = URLDecoder.decode(req.getParameter("action"), "UTF-8");
			String sourceXML = URLDecoder.decode(req.getParameter("sourceXML"), "UTF-8");
			String targetXML = URLDecoder.decode(req.getParameter("targetXML"), "UTF-8");
			String sourceXSD = URLDecoder.decode(req.getParameter("sourceXSD"), "UTF-8");
			String targetXSD = URLDecoder.decode(req.getParameter("targetXSD"), "UTF-8");
			String sourceRootElement = URLDecoder.decode(req.getParameter("sourceRootElement"), "UTF-8");
			String targetRootElement = URLDecoder.decode(req.getParameter("targetRootElement"), "UTF-8");
			String sourceXMLkey = "";
			String targetXMLkey = "";
			String sourceXSDkey = "";
			String targetXSDkey = "";
			
			if (sourceXML != null && sourceXML.length() > 0){
				sourceXMLkey = storeFile(sourceXML, "sourceXML" );
			}
		 	
			if (targetXML != null && targetXML.length() > 0) {
				targetXMLkey = storeFile(targetXML, "targetXML" );
			}
			
			if (sourceXSD != null && sourceXSD.length() > 0 ){ 
				sourceXSDkey = storeFile(sourceXSD, "sourceXSD" );
			}
			
			if(targetXSD != null && targetXSD.length() > 0) {
				targetXSDkey = storeFile(targetXSD, "targetXSD" );
			}
			
			if(action.equals("xsd_input")){
				returnJSON(processXSDtoJSON(sourceXSD,  sourceRootElement), processXSDtoJSON(targetXSD, targetRootElement),sourceXMLkey, targetXMLkey, sourceXSDkey, targetXSDkey, sourceRootElement,targetRootElement,  "success", resp.getWriter());
			}
			else if (action.equals("xml_input")){
				
				returnJSON(processXMLtoJSON(sourceXML), processXMLtoJSON(targetXML),sourceXMLkey, targetXMLkey, sourceXSDkey, targetXSDkey, sourceRootElement,targetRootElement, "success", resp.getWriter());
			}
			else {
				resp.sendRedirect("/");
			}
			
			
			
	        
			
			} catch (Exception e) {
			logger.log(Level.SEVERE, "Exception happening during XML document processing", e);
			
				try {
					returnJSON("", "","", "","", "","", "", "Exception happening during XML document processing " + e.getMessage(), resp.getWriter());
				} catch (IOException | SAXException e1) {
					logger.log(Level.SEVERE, "Exception happening during XML document processing", e1);
					try {
						
						resp.sendRedirect("/");
					} catch (IOException e2) {
			      	logger.log(Level.SEVERE, "Exception happening during XML document processing", e2);
					}
				}
			
			} 
	}

	
	
	
	
	private void returnJSON(String sourceJSON, String targetJSON, String sourceXMLKey, String targetXMLKey, String sourceXSDKey, String targetXSDKey, String sourceRootElement, String targetRootElement,String msg, PrintWriter out) throws IOException, SAXException {
		
		 JsonObject json = new JsonObject();
		 json.addProperty("sourceJSON", sourceJSON);
		 json.addProperty("targetJSON", targetJSON);
		 json.addProperty("sourceXMLKey", sourceXMLKey);
		 json.addProperty("targetXMLKey", targetXMLKey);
		 json.addProperty("sourceXSDKey", sourceXSDKey);
		 json.addProperty("targetXSDKey", targetXSDKey);
		 json.addProperty("sourceRootElement", sourceRootElement);
		 json.addProperty("targetRootElement", targetRootElement);
		 json.addProperty("msg", msg);
		 out.write(json.toString());
     		
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.sendRedirect("/");
	}
	
	
	
	private String processXMLtoJSON(String xml) {
		ModelBuilder builder;
		JSONMappingModel jsonmodel;
		try {
			
			builder = new XMLSampleModelBuilder(xml);	
			builder.configureModel();
			jsonmodel = new JSONMappingModel(builder.buildModel().getDocumentElement());
			return jsonmodel.getJSON();
			
		} catch (Exception e) {
			 logger.log(Level.WARNING, "XML to JSON XML problem occured : " + e.getMessage()); 
			 return "";
		  }
		
		
	}
	
	
	
	private String processXSDtoJSON(String xsd, String rootElement) {
		XSDModelBuilder builder;
		JSONMappingModel jsonmodel;
		try {
			
			builder = new XSDModelBuilder(xsd,rootElement);
			
			/** DEBUG **
			List<String> elements = new ArrayList<String>(); 
			Set<String> elementNames = builder.getRootElementNames();
			Iterator<String> it = elementNames.iterator();
			while (it.hasNext()) {
				String name = it.next();
				elements.add(name);
				 logger.log(Level.INFO, "XSD root elements : " + name);
			}
			** DEBUG **/
		
			
			jsonmodel = new JSONMappingModel(builder.buildModel().getDocumentElement());
			return jsonmodel.getJSON();
			
		} catch (Exception e) {
			 logger.log(Level.WARNING, "XML to JSON XML problem occured : " + e.getMessage()); 
			 return "";
		  }
		
		
	}
	
	
	  /**
		 * store XML
		 * 
		 * @param content
		 */
		private String storeFile(String content, String entity_type) {
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
	
	
	