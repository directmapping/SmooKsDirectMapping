package org.smooks.directmapping.gae;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathExpressionException;

import org.milyn.FilterSettings;
import org.milyn.Smooks;
import org.milyn.StreamFilterType;
import org.milyn.delivery.DomModelCreator;
import org.milyn.templating.TemplatingConfiguration;
import org.milyn.templating.freemarker.FreeMarkerTemplateProcessor;
import org.smooks.directmapping.model.ModelBuilderException;
import org.smooks.directmapping.template.exception.TemplateBuilderException;
import org.smooks.directmapping.template.util.SmooksFMUtil;
import org.xml.sax.SAXException;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;

@SuppressWarnings("serial")
public class SmooksTransformServlet extends HttpServlet {

	private static final Logger logger = Logger
			.getLogger(SmooksTransformServlet.class.getCanonicalName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.sendRedirect("/");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String sourceXML = URLDecoder.decode(req.getParameter("sourceXML"), "UTF-8");
		String template = URLDecoder.decode(req.getParameter("template"), "UTF-8");
		Smooks smooks = null;
		Writer outWriter = new StringWriter();
		StreamResult resultStream = new StreamResult(outWriter);

		
		try {
			InputStream is = new ByteArrayInputStream(template.getBytes("utf-8"));
			smooks = new Smooks();
			smooks.addConfigurations(is);
			
		} catch (SAXException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		
		// remove namespaces without prefix
		sourceXML = SmooksFMUtil.removeXmlStringNamespaceWithouthPrefix(sourceXML);
		StreamSource sourceStream = new StreamSource(
				new ByteArrayInputStream(sourceXML.getBytes("utf-8")));

		// SmooKs transformation
		smooks.filterSource(sourceStream, resultStream);
		
		
		prepareResponseFile(resp, resultStream.getWriter(), "result");
	}

	
	private void prepareResponseFile(HttpServletResponse resp, Writer responseFile,
			String target) {
		try {

			ByteArrayInputStream b = new ByteArrayInputStream(responseFile.toString()
					.getBytes("UTF-8"));
			DataInputStream in = new DataInputStream(b);

			int length = 0;
			ServletOutputStream op = resp.getOutputStream();
			String mimetype = "text/xml";// "text/xml" "application/xml";
											// //context.getMimeType( in );

			//
			// Set the response and go!
			//
			//
			resp.setContentType((mimetype != null) ? mimetype
					: "application/octet-stream"); // "application/octet-stream"
			// resp.setContentLength( (int) (8192) ); // TODO set to what?
			resp.setHeader("Content-Disposition", "attachment; filename=\""
					+ "transformation_result_" + target + ".xml" + "\"");

			//
			// Stream to the requester.
			//
			byte[] bbuf = new byte[8192];

			while ((in != null) && ((length = in.read(bbuf)) != -1)) {
				op.write(bbuf, 0, length);
			}

			in.close();
			op.flush();
			op.close();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Exception happening when processing key "
					+ "source.xml", e);
			throw new RuntimeException("Failed to process XML document", e);
		}
	}

}
