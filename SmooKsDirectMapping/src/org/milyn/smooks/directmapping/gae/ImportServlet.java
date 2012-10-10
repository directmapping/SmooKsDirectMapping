package org.milyn.smooks.directmapping.gae;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.xpath.XPathExpressionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smooks.templating.mapping.model.JSONMappingModelBuilder;
import org.smooks.templating.mapping.model.util.Functions;
import org.smooks.templating.mapping.model.util.MappingObject;
import org.smooks.templating.mapping.model.util.Mappings;
import org.smooks.templating.model.ModelBuilder;
import org.smooks.templating.model.xml.XMLSampleModelBuilder;
import org.smooks.templating.template.exception.InvalidMappingException;
import org.smooks.templating.template.freemarker.FreeMarkerTemplateBuilder;
import org.xml.sax.SAXException;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

@SuppressWarnings("serial")
public class ImportServlet extends HttpServlet {

	private static final Logger logger = Logger.getLogger(ImportServlet.class
			.getCanonicalName());

	private static String CONTENT_TYPE = "text/plain";
	private static String CONTENT_LENGTH = "Content-Length";
	private static int RESPONSE_CODE = 200;
	private static String FILENAME_PARAM = "qqfile";

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.sendRedirect("/");
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		String name = req.getParameter(FILENAME_PARAM);
		String json = null;
		PrintWriter print = res.getWriter();

		InputStream stream = req.getInputStream();
		OutputStream out = null;

		// You now have the filename (item.getName() and the
		// contents (which you can read from stream). Here we just
		// print them back out to the servlet output stream, but you
		// will probably want to do something more interesting (for
		// example, wrap them in a Blob and commit them to the
		// datastore).
		int len;
		byte[] buffer = new byte[(int) (8192)];
		while ((len = stream.read(buffer, 0, buffer.length)) != -1) {
		}
		json = new String(buffer, "UTF-8");

		/*
		 * logger.log(Level.WARNING, "JSON : " + json);
		 * 
		 * JSONArray the_json_array = null; JSONObject myjson = null; try {
		 * myjson = new JSONObject(json); } catch (JSONException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } try {
		 * the_json_array = myjson.getJSONArray("mapping");
		 * 
		 * //the_json_array
		 * 
		 * JsonObject jsonObj = new JsonObject(); jsonObj.addProperty("success",
		 * "true"); jsonObj.add("mapping", null); jsonObj.add("functions",
		 * null); jsonObj.addProperty("source", myjson.getString("source"));
		 * jsonObj.addProperty("destination", myjson.getString("destination"));
		 * 
		 * returnJSON(jsonObj, print); } catch (JSONException e) { // TODO
		 * Auto-generated catch block e.printStackTrace();
		 * 
		 * } catch (SAXException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * 
		 * 
		 * // JsonParser parser = new JsonParser(); // JsonArray o =
		 * (JsonArray)parser.parse(json); Gson gson = new Gson();
		 * 
		 * 
		 * 
		 * //convert the json string back to object MappingObject obj =
		 * gson.fromJson(json, MappingObject.class);
		 */

		Gson gson = new Gson();
		MappingObject obj = null;
		try {

			JsonReader reader = new JsonReader(new StringReader(json));
			reader.setLenient(true);

			obj = gson.fromJson(reader, MappingObject.class);

			JsonElement element = gson.toJsonTree(obj.getMapping(),
					new TypeToken<List<Mappings>>() {
					}.getType());

			if (!element.isJsonArray()) {
				// fail appropriately
				throw new IOException();
			}

			JsonArray jsonArray = element.getAsJsonArray();

			JsonElement elemfunctions = gson.toJsonTree(obj.getFunctions(),
					new TypeToken<List<Functions>>() {
					}.getType());

			if (!elemfunctions.isJsonArray()) {
				// fail appropriately
				throw new IOException();
			}

			JsonArray functionsArray = elemfunctions.getAsJsonArray();

			String sourceXML = getStoredXML(obj.getSource(), "sourceXML");
			String destinationXML = getStoredXML(obj.getDestination(),
					"destinationXML");

			JsonObject jsonObj = new JsonObject();
			jsonObj.addProperty("success", "true");
			jsonObj.add("mapping", jsonArray);
			jsonObj.add("functions", functionsArray);
			jsonObj.addProperty("sourceXML", obj.getSource());
			jsonObj.addProperty("destinationXML", obj.getDestination());
			jsonObj.addProperty("source", processXMLtoJSON(sourceXML));
			jsonObj.addProperty("destination", processXMLtoJSON(destinationXML));

			returnJSON(jsonObj, print);

		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void returnJSON(JsonObject jsonObj, PrintWriter out)
			throws IOException, SAXException {

		out.write(jsonObj.toString());

	}

	private void returnJSON(String source, String destination,
			String sourceXMLKey, String destinationXMLKey, PrintWriter out)
			throws IOException, SAXException {

		JsonObject json = new JsonObject();
		json.addProperty("source", source);
		json.addProperty("destination", destination);
		json.addProperty("sourceXML", sourceXMLKey);
		json.addProperty("destinationXML", destinationXMLKey);
		out.write(json.toString());

	}

	private void returnJSON(String json, PrintWriter out) throws IOException,
			SAXException {

		out.write(json);

	}

	private void writeResponse(PrintWriter writer, String failureReason,
			String json) {
		if (failureReason == null) {
			String test = "{\"success\": true , " + json.substring(1);
			writer.print(test);
		} else {
			writer.print("{\"error\": \"" + failureReason + "\"}");
		}
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

	private String processXMLtoJSON(String xml) {
		ModelBuilder builder;
		JSONMappingModelBuilder jsonmodel;
		try {

			builder = new XMLSampleModelBuilder(xml);
			builder.configureModel();
			jsonmodel = new JSONMappingModelBuilder(builder.buildModel()
					.getDocumentElement());
			return jsonmodel.getJSON();

		} catch (Exception e) {
			logger.log(Level.WARNING,
					"XML to JSON XML problem occured : " + e.getMessage());
			return "";
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

}
