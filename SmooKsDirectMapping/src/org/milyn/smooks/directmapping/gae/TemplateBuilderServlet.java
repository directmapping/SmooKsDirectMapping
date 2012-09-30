package org.milyn.smooks.directmapping.gae;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@SuppressWarnings("serial")
public class TemplateBuilderServlet extends HttpServlet {

	private static final Logger logger = Logger.getLogger(TemplateBuilderServlet.class.getCanonicalName());
	

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.sendRedirect("/");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	/*	String source = req.getParameter("sourceXML");
		String destination = req.getParameter("destinationXML");
		String mapping = URLDecoder.decode(req.getParameter("mapping"), "UTF-8");
		String functions = URLDecoder.decode(req.getParameter("functions"), "UTF-8");
		
		*/
		 
		 
		 
		
		try {
		
			JsonParser parser = new JsonParser();
			JsonObject o = (JsonObject)parser.parse("{\"a\": \"A\"}");

			
				} catch (Exception e) {
			logger.log(Level.SEVERE, "Exception happening when processing key " + "source.xml", e);
			throw new RuntimeException("Failed to process XML document", e);
		} 
	}
	


		
	}
	
	
	