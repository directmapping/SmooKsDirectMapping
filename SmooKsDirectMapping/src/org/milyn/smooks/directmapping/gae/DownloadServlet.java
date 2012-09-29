package org.milyn.smooks.directmapping.gae;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;


import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;

import org.smooks.templating.mapping.model.JSONMappingModelBuilder;
import org.smooks.templating.model.ModelBuilder;
import org.smooks.templating.model.ModelBuilderException;
import org.smooks.templating.model.xml.XMLSampleModelBuilder;
import org.smooks.templating.template.exception.InvalidMappingException;
import org.smooks.templating.template.exception.TemplateBuilderException;
import org.smooks.templating.template.freemarker.FreeMarkerTemplateBuilder;
import org.smooks.templating.template.xml.XMLFreeMarkerTemplateBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.datastore.Transaction;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@SuppressWarnings("serial")
public class DownloadServlet extends HttpServlet {

	private static final Logger logger = Logger.getLogger(DownloadServlet.class.getCanonicalName());
	

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
	
	
		 JsonObject json = new JsonObject();
		 json.addProperty("source", source);
		 json.addProperty("destination", destination);
		 json.addProperty("mapping", mapping);
		 json.addProperty("functions", functions);
		 
		 
		 //PUSH JSON FILE 
		 prepareJSONFile(resp, json);
		

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
	
	
	
	
	private void prepareJSONFile(HttpServletResponse resp, JsonObject json)
	{
		try {
		
		
		
		
		  ByteArrayInputStream	b	 = new ByteArrayInputStream(json.toString().getBytes("UTF-8"));
		  DataInputStream 		in	 = new DataInputStream(b);
			
	      int                 length   = 0;
	      ServletOutputStream op       = resp.getOutputStream();
	      ServletContext      context  = getServletConfig().getServletContext();
	      String              mimetype = "text/json";
	      
	      	
	      
	      //
	      //  Set the response and go!
	      //
	      //
	      resp.setContentType( (mimetype != null) ? mimetype : "application/octet-stream" ); //"application/octet-stream"
	      //resp.setContentLength( (int) (8192) ); // TODO set to what?
	      resp.setHeader( "Content-Disposition", "attachment; filename=\"" + "directmapping.json" + "\"" );

	      
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
	
	
	