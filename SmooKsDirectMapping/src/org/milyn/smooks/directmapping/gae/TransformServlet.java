package org.milyn.smooks.directmapping.gae;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
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

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathExpressionException;

import org.milyn.FilterSettings;
import org.milyn.Smooks;
import org.milyn.StreamFilterType;
import org.milyn.delivery.DomModelCreator;
import org.milyn.templating.TemplatingConfiguration;
import org.milyn.templating.freemarker.FreeMarkerTemplateProcessor;
import org.smooks.templating.model.ModelBuilderException;
import org.smooks.templating.template.exception.TemplateBuilderException;
import org.smooks.templating.template.util.SmooksFMUtil;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;

@SuppressWarnings("serial")
public class TransformServlet extends HttpServlet {

	private static final Logger logger = Logger
			.getLogger(TransformServlet.class.getCanonicalName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.sendRedirect("/");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		doSmooksTransformation(req, resp);

	}

	private void doSmooksTransformation(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		String source = req.getParameter("sourceXML");
		String destination = req.getParameter("destinationXML");
		String mapping = URLDecoder
				.decode(req.getParameter("mapping"), "UTF-8");
		String functions = URLDecoder.decode(req.getParameter("functions"),
				"UTF-8");
		String action = req.getParameter("action");

		String template = "";
		Smooks smooks = null;
		String sourceXML = getStoredXML(source, "sourceXML");
		String destinationXML = getStoredXML(destination, "destinationXML");
		Writer outWriter = new StringWriter();
		StreamResult resultStream = new StreamResult(outWriter);

		try {

			template = SmooksFMUtil.createTemplate(sourceXML, mapping,
					functions, destinationXML);

			if (action.equals("export_template")) {
				prepareXMLFile(resp,
						SmooksFMUtil.getSmooksConfigurationWriter(template),
						"SmooksConfig");
			} else {

				smooks = new Smooks();

				smooks.addVisitor(new DomModelCreator(), "$document");
				smooks.addVisitor(new FreeMarkerTemplateProcessor(
						new TemplatingConfiguration(template)), "$document");
				smooks.setFilterSettings(new FilterSettings(
						StreamFilterType.DOM));

				StreamSource sourceStream = new StreamSource(
						new ByteArrayInputStream(sourceXML.getBytes("utf-8")));

				// SmooKs transformation
				smooks.filterSource(sourceStream, resultStream);
				prepareXMLFile(resp, resultStream.getWriter(), destination);

			}

		} catch (XPathExpressionException e2) {
			// TODO Auto-generated catch block
			logger.log(Level.SEVERE, "Exception happening when processing key "
					+ source, e2);
			throw new RuntimeException("Failed to process XML document", e2);
		} catch (InvocationTargetException e2) {
			// TODO Auto-generated catch block
			logger.log(Level.SEVERE, "Exception happening when processing key "
					+ source, e2);
			throw new RuntimeException("Failed to process XML document", e2);
		} catch (ModelBuilderException e2) {
			// TODO Auto-generated catch block
			logger.log(Level.SEVERE, "Exception happening when processing key "
					+ source, e2);
			throw new RuntimeException("Failed to process XML document", e2);
		} catch (TemplateBuilderException e2) {
			// TODO Auto-generated catch block
			logger.log(Level.SEVERE, "Exception happening when processing key "
					+ source, e2);
			throw new RuntimeException("Failed to process XML document", e2);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Exception happening when processing key "
					+ source, e);
			throw new RuntimeException("Failed to process XML document", e);
		} finally {
			if (!action.equals("export_template") && smooks != null) {
				smooks.close();
			}
		}

	}

	private String getStoredXML(String keyString, String propertyName) {

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Key key = KeyFactory.stringToKey(keyString);

		try {
			Entity entity = datastore.get(key);
			Text content = (Text) entity.getProperty("content");

			return content.getValue();
		} catch (EntityNotFoundException e) {
			logger.log(Level.SEVERE, "Exception happening when processing key "
					+ key, e);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Exception happening when processing key "
					+ key, e);
			throw new RuntimeException("Failed to process XML document", e);
		}
		return "";
	}

	private void prepareXMLFile(HttpServletResponse resp, Writer xml,
			String destination) {
		try {

			ByteArrayInputStream b = new ByteArrayInputStream(xml.toString()
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
					+ "transformation_result_" + destination + ".xml" + "\"");

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
