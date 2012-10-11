package org.milyn.smooks.directmapping.gae;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.xpath.XPathExpressionException;

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

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.sendRedirect("/");
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		String json = null;
		PrintWriter print = res.getWriter();

		InputStream stream = req.getInputStream();
		
		int len = 0;
		len = len + 0;
		
		byte[] buffer = new byte[(int) (8192)];
		while ((len = stream.read(buffer, 0, buffer.length)) != -1) {
		}
		json = new String(buffer, "UTF-8");


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
