package org.milyn.smooks.directmapping.gae;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;

@SuppressWarnings("serial")
public class XMLDisplay extends HttpServlet {

	private static final Logger logger = Logger.getLogger(XMLDisplay.class.getCanonicalName());
	
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
			resp.setContentType("text/plain");
			resp.getWriter().println(xml);
		} catch (EntityNotFoundException e) {
			logger.log(Level.SEVERE, "Exception happening when processing key " + key, e);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Exception happening when processing key " + key, e);
			throw new RuntimeException("Failed to process EDI document", e);
		} 
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.sendRedirect("/");
	}
		
}
	
	
	