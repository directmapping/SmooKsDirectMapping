package org.smooks.directmapping.gae;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.xpath.XPathExpressionException;

import org.smooks.directmapping.mapping.model.JSONMappingModelBuilder;
import org.smooks.directmapping.mapping.model.util.Functions;
import org.smooks.directmapping.mapping.model.util.MappingObject;
import org.smooks.directmapping.mapping.model.util.Mappings;
import org.smooks.directmapping.model.ModelBuilder;
import org.smooks.directmapping.model.xml.XMLSampleModelBuilder;
import org.smooks.directmapping.model.xml.XSDModelBuilder;
import org.smooks.directmapping.template.exception.InvalidMappingException;
import org.smooks.directmapping.template.freemarker.FreeMarkerTemplateBuilder;
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

			String sourceXML = ""; 
			String targetXML = "";
			String sourceXSD = "";
			String targetXSD = "";
			boolean schemaXSD = true;
			
			if (obj.getSourceXMLKey() != null && obj.getSourceXMLKey().length() > 0){
				sourceXML = getStoreFile(obj.getSourceXMLKey(), "sourceXML");
			}
			if (obj.getTargetXMLKey() != null && obj.getTargetXMLKey().length() > 0){
				targetXML = getStoreFile(obj.getTargetXMLKey(), "targetXML");
			}
			if (obj.getSourceXSDKey() != null && obj.getSourceXSDKey().length() > 0){
				sourceXSD = getStoreFile(obj.getSourceXSDKey(), "sourceXSD");
			}else{
				schemaXSD = false;
			}
			if (obj.getTargetXSDKey() != null && obj.getTargetXSDKey().length() > 0){
				targetXSD = getStoreFile(obj.getTargetXSDKey(), "targetXSD");
			}else{
				schemaXSD = false;
			}
			
			
			String sourceRootElement = obj.getSourceRootElement();
			String targetRootElement = obj.getTargetRootElement();

			
			JsonObject jsonObj = new JsonObject();
			jsonObj.addProperty("success", "true");
			jsonObj.add("mapping", jsonArray);
			jsonObj.add("functions", functionsArray);
			jsonObj.addProperty("sourceXMLKey", obj.getSourceXMLKey());
			jsonObj.addProperty("targetXMLKey", obj.getTargetXMLKey());
			jsonObj.addProperty("sourceXSDKey", obj.getSourceXSDKey());
			jsonObj.addProperty("targetXSDKey", obj.getTargetXSDKey());
			jsonObj.addProperty("sourceRootElement", sourceRootElement);
			jsonObj.addProperty("targetRootElement", targetRootElement);
		
			
			if(schemaXSD){
				jsonObj.addProperty("sourceJSON", processXSDtoJSON(sourceXSD,sourceRootElement));
				jsonObj.addProperty("targetJSON", processXSDtoJSON(targetXSD,targetRootElement));
			}else{
				jsonObj.addProperty("sourceJSON", processXMLtoJSON(sourceXML));
				jsonObj.addProperty("targetJSON", processXMLtoJSON(targetXML));
			}
			returnJSON(jsonObj, print);

		} catch (JsonSyntaxException e) {
			logger.log(Level.SEVERE, "Exception happening during import", e);
			
		} catch (SAXException e) {
			logger.log(Level.SEVERE, "Exception happening during import", e);
			
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

	private String processXSDtoJSON(String xsd, String rootElement) {
		XSDModelBuilder builder;
		JSONMappingModelBuilder jsonmodel;
		try {
			
			builder = new XSDModelBuilder(xsd);
			
			/** DEBUG **/
			List<String> elements = new ArrayList<String>(); 
			Set<String> elementNames = builder.getRootElementNames();
			Iterator<String> it = elementNames.iterator();
			while (it.hasNext()) {
				String name = it.next();
				elements.add(name);
				 logger.log(Level.INFO, "XSD root elements : " + name);
			}
			/** DEBUG **/
			((XSDModelBuilder)builder).setRootElementName(rootElement);
			
			jsonmodel = new JSONMappingModelBuilder(builder.buildModel().getDocumentElement());
			return jsonmodel.getJSON();
			
		} catch (Exception e) {
			 logger.log(Level.WARNING, "XML to JSON XML problem occured : " + e.getMessage()); 
			 return "";
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

	private String getStoreFile(String keyString, String propertyName) {

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
