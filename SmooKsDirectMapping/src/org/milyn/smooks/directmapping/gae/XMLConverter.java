package org.milyn.smooks.directmapping.gae;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import com.google.appengine.api.datastore.EntityNotFoundException;


import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;



@SuppressWarnings("serial")
public class XMLConverter extends HttpServlet {

	private static final Logger logger = Logger.getLogger(XMLConverter.class.getCanonicalName());
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String ctx = req.getRequestURL().toString();
		String keyStr = ctx.substring(ctx.lastIndexOf('/') + 1, ctx.length());
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.stringToKey(keyStr);
		try {
			Entity entity = datastore.get(key);
			Text content = (Text) entity.getProperty("content");
			String xml = content.getValue();
			resp.setContentType("application/json");
			Document d = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));
			Gson gsonBuilder = new GsonBuilder().create();
			String [] test = {"foo","100","1000","21","true","null"};
	        String json = gsonBuilder.toJson(test)   ;
	        
	        
	        
			returnJSON("source", json, "mapping", "functions", resp.getWriter());
		} catch (EntityNotFoundException e) {
			logger.log(Level.SEVERE,"Exception happening when processing key " + key, e);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Exception happening when processing key " + key, e);
			throw new RuntimeException("Failed to process XML document", e);
		} 
	}

	
	
	
	
	private void returnJSON(String source, String destination, String mapping, String functions, PrintWriter out) throws IOException, SAXException {
		
		 JsonObject json = new JsonObject();
		 json.addProperty("source", source);
		 json.addProperty("destination", destination);
		 json.addProperty("mapping", mapping);
		 json.addProperty("functions", functions);
		 String test = json.toString();
		 logger.log(Level.INFO, "XMLConverter writing XML " + test); //json.toString());
		
			
         out.write(json.toString());
         
         
			
	}
	
	private void parseXML(String xml, PrintWriter out) throws IOException, SAXException {
		logger.log(Level.INFO, "XMLConverter writing XML " + xml);
		
		out.println(xml);
		
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.sendRedirect("/");
	}

	
	/*
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String ctx = req.getRequestURL().toString();
		String keyStr = ctx.substring(ctx.lastIndexOf('/') + 1, ctx.length());
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.stringToKey(keyStr);
		try {
			Entity entity = datastore.get(key);
			Text content = (Text) entity.getProperty("content");
			String xml = content.getValue();
			resp.setContentType("text/xml");
		//	resp.getWriter().println("<?xml version=\"1.0\"?>");
			parseXML(xml, resp.getWriter());
		} catch (EntityNotFoundException e) {
			logger.log(Level.SEVERE,"Exception happening when processing key " + key, e);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Exception happening when processing key " + key, e);
			throw new RuntimeException("Failed to process XML document", e);
		} 
	}*/
	
	
	}
	
	
	