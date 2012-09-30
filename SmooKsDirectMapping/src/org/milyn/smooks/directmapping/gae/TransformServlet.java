package org.milyn.smooks.directmapping.gae;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;


import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathExpressionException;

import org.milyn.Smooks;
import org.smooks.templating.model.ModelBuilder;
import org.smooks.templating.model.ModelBuilderException;
import org.smooks.templating.model.xml.XMLSampleModelBuilder;
import org.smooks.templating.template.exception.InvalidMappingException;
import org.smooks.templating.template.exception.TemplateBuilderException;
import org.smooks.templating.template.freemarker.FreeMarkerTemplateBuilder;
import org.smooks.templating.template.xml.XMLFreeMarkerTemplateBuilder;
import org.xml.sax.SAXException;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@SuppressWarnings("serial")
public class TransformServlet extends HttpServlet {

	private static final Logger logger = Logger.getLogger(TransformServlet.class.getCanonicalName());
	

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.sendRedirect("/");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String source = req.getParameter("sourceXML");
		String destination = req.getParameter("destinationXML");
		String mapping = URLDecoder.decode(req.getParameter("mapping"), "UTF-8");
		String functions = URLDecoder.decode(req.getParameter("functions"), "UTF-8");
		String template ="";
		Smooks smooks = null;
		String sourceXML = getStoredXML(source,"sourceXML");
		String destinationXML = getStoredXML(destination,"destinationXML");
		InputStream configuration = null;
		Writer  outWriter = new StringWriter();
		
		
		
		
		try {
				 
			template = createTemplate(destinationXML, mapping);
			configuration = getSmooksConfiguration(template);
		} catch (InvocationTargetException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ModelBuilderException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (TemplateBuilderException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	
		} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		
		
			try {
				smooks = new Smooks(configuration);
				
				InputStream sourceXMLstream = new ByteArrayInputStream(sourceXML.getBytes("utf-8"));
				 StreamSource test = new StreamSource(new FileInputStream("source/source.xml"));
					
				StreamResult result = new StreamResult( outWriter );  
				//smooks.filterSource(new StreamSource(sourceXMLstream), result);
				smooks.filterSource(test, result);
				
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			finally { if(smooks!=null) { smooks.close(); } }
		
		
		
		
		 
		 //PUSH JSON FILE 
		 prepareXMLFile(resp, outWriter);
		

	}
	
	public static InputStream getSmooksConfiguration(String template) throws IOException
	{
		Writer templateWriter = new StringWriter();
		
		templateWriter.write("<?xml version=\"1.0\"?>");
		templateWriter.write('\n');
		templateWriter.write("<smooks-resource-list xmlns=\"http://www.milyn.org/xsd/smooks-1.1.xsd\" xmlns:core=\"http://www.milyn.org/xsd/smooks/smooks-core-1.3.xsd\" xmlns:ftl=\"http://www.milyn.org/xsd/smooks/freemarker-1.1.xsd\">");
		templateWriter.write('\n');
		templateWriter.write("<ftl:freemarker applyOnElement=\"#document\">");
		templateWriter.write('\n');	
		templateWriter.write("<ftl:template>");
		templateWriter.write("<![CDATA[");
		templateWriter.write(template);
		templateWriter.write("]]>");
		templateWriter.write("</ftl:template>");
		templateWriter.write('\n');
		templateWriter.write("</ftl:freemarker>");
		templateWriter.write('\n');
		templateWriter.write("<resource-config selector=\"#document\">");
		templateWriter.write('\n');
		templateWriter.write("<resource>org.milyn.delivery.DomModelCreator</resource>");
		templateWriter.write('\n');
		templateWriter.write("</resource-config>");
		templateWriter.write('\n');
		templateWriter.write("</smooks-resource-list>");
		
		InputStream is = new ByteArrayInputStream(templateWriter.toString().getBytes());
		return is;
	}
	
	
	private String createTemplate(String xml, String mapping) throws InvocationTargetException, 
	ModelBuilderException, TemplateBuilderException, XPathExpressionException {
		ModelBuilder builder;
		FreeMarkerTemplateBuilder templateBuilder = null;
		try {
			
			builder = new XMLSampleModelBuilder(xml);	
			builder.configureModel();
			
			
			templateBuilder = new XMLFreeMarkerTemplateBuilder(builder);

			templateBuilder = addMappping(templateBuilder, mapping);
			 
			 
			templateBuilder.setNodeModelSource(true);
			
			
			return templateBuilder.buildTemplate();
			
		} catch (Exception e) {
			 logger.log(Level.WARNING, "XML to JSON XML problem occured : " + e.getMessage()); 
			 return "";
		  }
		
		
	}
	
	public static FreeMarkerTemplateBuilder addMappping(FreeMarkerTemplateBuilder templateBuilder,  String mapping) throws InvalidMappingException, XPathExpressionException{
			 
			
			JsonParser parser = new JsonParser();
			JsonArray o = (JsonArray)parser.parse(mapping);
	
			Iterator<JsonElement> iterator = o.iterator();
			
			while(iterator.hasNext()){
				JsonObject json = (JsonObject)iterator.next();
			    String from = json.get("from").getAsString();
			    String to = json.get("to").getAsString();
			    
				templateBuilder.addValueMapping(from,templateBuilder.getModelNode(to));
				   
			}
		
					
			return templateBuilder;
				
				
			
		}
	
	
	private String getStoredXML(String keyString, String propertyName)
	{
		
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		
		Key key = KeyFactory.stringToKey(keyString);
		
			
			try {
				Entity entity = datastore.get(key);
				Text content = (Text) entity.getProperty("content");
				    
				
				return content.getValue();
			} catch (EntityNotFoundException e) {
				logger.log(Level.SEVERE, "Exception happening when processing key " + key, e);
			} catch (Exception e) {
				logger.log(Level.SEVERE, "Exception happening when processing key " + key, e);
				throw new RuntimeException("Failed to process EDI document", e);
			}
			return ""; 
	}

	
	
	private void prepareXMLFile(HttpServletResponse resp, Writer xml)
	{
		try {
		
			
			/*Document d = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new ByteArrayInputStream(source.getBytes("utf-8"))));
			
			
			DOMImplementationLS domImplLS = (DOMImplementationLS) d.getImplementation();
			LSSerializer serializer = domImplLS.createLSSerializer();
			String str = serializer.writeToString(d.getDocumentElement());
			*/
			/*
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transformer = transFactory.newTransformer();
			StringWriter buffer = new StringWriter();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.transform(new DOMSource(d),
			      new StreamResult(buffer));
			String str = buffer.toString();
			*/
			
		
	
		
		
		//  File                f        = new File(filename);
		  ByteArrayInputStream	b	 = new ByteArrayInputStream(xml.toString().getBytes("UTF-8"));
		  DataInputStream 		in	 = new DataInputStream(b);
			
	      int                 length   = 0;
	      ServletOutputStream op       = resp.getOutputStream();
	      ServletContext      context  = getServletConfig().getServletContext();
	      String              mimetype = "text/xml";// "text/xml" "application/xml";  //context.getMimeType( in );  
	      
	      	
	      
	      //
	      //  Set the response and go!
	      //
	      //
	      resp.setContentType( (mimetype != null) ? mimetype : "application/octet-stream" ); //"application/octet-stream"
	      //resp.setContentLength( (int) (8192) ); // TODO set to what?
	      resp.setHeader( "Content-Disposition", "attachment; filename=\"" + "transformation_result.xml" + "\"" );

	      
	      //
	      //  Stream to the requester.
	      //
	      byte[] bbuf = new byte[8192];
	    
	      while ((in != null) && ((length = in.read(bbuf)) != -1))
	      {
	          op.write(bbuf,0,length);
	      }

	      in.close();
	      op.flush();
	      op.close();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Exception happening when processing key " + "source.xml", e);
			throw new RuntimeException("Failed to process XML document", e);
		} 
	}
	

		
}
	
	
	