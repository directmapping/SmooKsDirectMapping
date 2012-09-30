package org.milyn.smooks.directmapping.gae;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.datastore.Transaction;

@SuppressWarnings("serial")
public class SmooKsDirectMappingServlet extends HttpServlet {

	private static final Logger logger = Logger.getLogger(SmooKsDirectMappingServlet.class.getCanonicalName());
	

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.sendRedirect("/");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String source = req.getParameter("source");
		//String destination = req.getParameter("destination");
		
		if (source != null && source.length() > 0) {
			String sourcekey = processXML(source);
			resp.sendRedirect("/xml/" + sourcekey);
		} else {
			resp.sendRedirect("/");
		}
	}
		
		/**
		 * Process XML
		 * 
		 * @param content
		 */
		private String processXML(String content) {
			DatastoreService datastore = DatastoreServiceFactory
					.getDatastoreService();
			Transaction transaction = datastore.beginTransaction();
			Entity entity = new Entity("SourceXML");
			entity.setProperty("content", new Text(content));
			Key key = datastore.put(entity);
			logger.log(Level.INFO, "Content processing key " + KeyFactory.keyToString(key) + "value " +  new Text(content) );
			
			transaction.commit();
			return KeyFactory.keyToString(key);
		}
		
		
	}
	
	
	