package org.smooks.directmapping.gae;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.xpath.XPathExpressionException;

import org.smooks.directmapping.template.exception.InvalidMappingException;
import org.smooks.directmapping.template.freemarker.FreeMarkerTemplateBuilder;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

@SuppressWarnings("serial")
public class ExportServlet extends HttpServlet {

	private static final Logger logger = Logger.getLogger(ExportServlet.class
			.getCanonicalName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.sendRedirect("/");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		
		String sourceXMLKey = req.getParameter("sourceXMLKey");
		String targetXMLKey = req.getParameter("targetXMLKey");
		String sourceXSDKey = req.getParameter("sourceXSDKey");
		String targetXSDKey = req.getParameter("targetXSDKey");
		String sourceRootElement = req.getParameter("sourceRootElement");
		String targetRootElement = req.getParameter("targetRootElement");
	    String mappingStr = URLDecoder.decode(req.getParameter("mapping"),
				"UTF-8");
		String functionStr = URLDecoder.decode(req.getParameter("functions"),
				"UTF-8");
		JsonArray mappings = null;
		JsonArray functions = null;
		JsonObject json = new JsonObject();
		json.addProperty("sourceXMLKey", sourceXMLKey);
		json.addProperty("targetXMLKey", targetXMLKey);
		json.addProperty("sourceXSDKey", sourceXSDKey);
		json.addProperty("targetXSDKey", targetXSDKey);
		json.addProperty("sourceRootElement",sourceRootElement);
		json.addProperty("targetRootElement", targetRootElement);


		try {
			JsonParser parser = new JsonParser();
			mappings = (JsonArray) parser.parse(mappingStr);

		} catch (JsonSyntaxException e) {
			mappings = null;
		}

		try {
			JsonParser parser = new JsonParser();
			functions = (JsonArray) parser.parse(functionStr);

		} catch (JsonSyntaxException e) {
			functions = null;
		}

		json.add("mapping", mappings);
		json.add("functions", functions);

		// PUSH JSON FILE
		prepareJSONFile(resp, json);

	}

	public static FreeMarkerTemplateBuilder addMappping(
			FreeMarkerTemplateBuilder templateBuilder, String mapping)
			throws InvalidMappingException, XPathExpressionException {

		JsonParser parser = new JsonParser();
		JsonArray o = (JsonArray) parser.parse(mapping);

		Iterator<JsonElement> iterator = o.iterator();

		while (iterator.hasNext()) {
			JsonObject json = (JsonObject) iterator.next();
			String from = json.get("from").getAsString();
			String to = json.get("to").getAsString();

			templateBuilder.addValueMapping(from,
					templateBuilder.getModelNode(to));

		}

		return templateBuilder;

	}

	private void prepareJSONFile(HttpServletResponse resp, JsonObject json) {
		try {

			ByteArrayInputStream b = new ByteArrayInputStream(json.toString()
					.getBytes("UTF-8"));
			DataInputStream in = new DataInputStream(b);

			int length = 0;
			ServletOutputStream op = resp.getOutputStream();
			// ServletContext context = getServletConfig().getServletContext();
			String mimetype = "text/json";

			//
			// Set the response and go!
			//
			//
			resp.setContentType((mimetype != null) ? mimetype
					: "application/octet-stream"); // "application/octet-stream"
			// resp.setContentLength( (int) (8192) ); // TODO set to what?
			resp.setHeader("Content-Disposition", "attachment; filename=\""
					+ "directmapping.json" + "\"");

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
