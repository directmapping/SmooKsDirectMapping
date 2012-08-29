package org.milyn.smooks.directmapping.gae;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
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
import com.google.gson.JsonObject;

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
		  ByteArrayInputStream	b	 = new ByteArrayInputStream(json.toString().getBytes("UTF-8"));
		  DataInputStream 		in	 = new DataInputStream(b);
			
	      int                 length   = 0;
	      ServletOutputStream op       = resp.getOutputStream();
	      ServletContext      context  = getServletConfig().getServletContext();
	      String              mimetype = "text/json";// "text/xml" "application/xml";  //context.getMimeType( in );  
	      
	      	
	      
	      //
	      //  Set the response and go!
	      //
	      //
	      resp.setContentType( (mimetype != null) ? mimetype : "application/octet-stream" ); //"application/octet-stream"
	      resp.setContentLength( (int) (8192) ); // TODO set to what?
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
	
/*
   *  Sends a file to the ServletResponse output stream.  Typically
   *  you want the browser to receive a different name than the
   *  name the file has been saved in your local database, since
   *  your local names need to be unique.
   *
   *  @param req The request
   *  @param resp The response
   *  @param filename The name of the file you want to download.
   *  @param original_filename The name the browser should receive.
 */

		
	}
	
	
	