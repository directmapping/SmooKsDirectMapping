/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, JBoss Inc., and others contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 * (C) 2005-2006, JBoss Inc.
 */
package org.smooks.templating.template.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;

import org.eclipse.emf.common.util.URI;
import org.milyn.javabean.decoders.DateDecoder;
import org.smooks.templating.model.ModelBuilder;
import org.smooks.templating.model.ModelBuilderException;
import org.smooks.templating.model.ModelBuilder.ElementType;
import org.smooks.templating.model.xml.XMLSampleModelBuilder;
import org.smooks.templating.template.CollectionMapping;
import org.smooks.templating.template.ValueMapping;
import org.smooks.templating.template.exception.InvalidMappingException;
import org.smooks.templating.template.exception.TemplateBuilderException;
import org.smooks.templating.template.freemarker.FreeMarkerTemplateBuilder;
import org.smooks.templating.template.xml.XMLFreeMarkerTemplateBuilder;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * SmooKs utility methods.
 * 
 * @author <a href="mailto:mskackov@ google mail .com">michal skackov</a>
 */
public class SmooksFMUtil {

	public static InputStream getSmooksConfiguration(String template) throws IOException
	{
		Writer templateWriter = new StringWriter();
		
		templateWriter.write("<?xml version=\"1.0\"?>");
		templateWriter.write('\n');
		templateWriter.write("<smooks-resource-list xmlns=\"http://www.milyn.org/xsd/smooks-1.1.xsd\" xmlns:core=\"http://www.milyn.org/xsd/smooks/smooks-core-1.3.xsd\" xmlns:ftl=\"http://www.milyn.org/xsd/smooks/freemarker-1.1.xsd\">");
		templateWriter.write('\n');
		templateWriter.write("<ftl:freemarker applyOnElement=\"#document\">");
		templateWriter.write('\n');	
		templateWriter.write("<ftl:template>");
		templateWriter.write("<![CDATA[");

		// ADD FREEMARKER TEMPLATE
		templateWriter.write(template);
	

		templateWriter.write("]]>");
		templateWriter.write("</ftl:template>");
		templateWriter.write('\n');
		templateWriter.write("</ftl:freemarker>");
		templateWriter.write('\n');
		templateWriter.write("<resource-config selector=\"#document\">");
		templateWriter.write('\n');
		templateWriter.write("<resource>org.milyn.delivery.DomModelCreator</resource>");
		templateWriter.write('\n');
		templateWriter.write("</resource-config>");
		templateWriter.write('\n');
		templateWriter.write("</smooks-resource-list>");
		
		InputStream is = new ByteArrayInputStream(templateWriter.toString().getBytes());
		return is;
	}

	public static Writer getSmooksConfigurationWriter(String template) throws IOException
	{
		Writer templateWriter = new StringWriter();
		
		templateWriter.write("<?xml version=\"1.0\"?>");
		templateWriter.write('\n');
		templateWriter.write("<smooks-resource-list xmlns=\"http://www.milyn.org/xsd/smooks-1.1.xsd\" xmlns:core=\"http://www.milyn.org/xsd/smooks/smooks-core-1.3.xsd\" xmlns:ftl=\"http://www.milyn.org/xsd/smooks/freemarker-1.1.xsd\">");
		templateWriter.write('\n');
		templateWriter.write("<ftl:freemarker applyOnElement=\"#document\">");
		templateWriter.write('\n');	
		templateWriter.write("<ftl:template>");
		templateWriter.write("<![CDATA[");

		// ADD FREEMARKER TEMPLATE
		templateWriter.write(template);
	

		templateWriter.write("]]>");
		templateWriter.write("</ftl:template>");
		templateWriter.write('\n');
		templateWriter.write("</ftl:freemarker>");
		templateWriter.write('\n');
		templateWriter.write("<resource-config selector=\"#document\">");
		templateWriter.write('\n');
		templateWriter.write("<resource>org.milyn.delivery.DomModelCreator</resource>");
		templateWriter.write('\n');
		templateWriter.write("</resource-config>");
		templateWriter.write('\n');
		templateWriter.write("</smooks-resource-list>");
		
		
		return templateWriter;
	}
	
	/**
	 * @param source
	 * @param mapping
	 * @param destination
	 * @return
	 * @throws InvocationTargetException
	 * @throws IOException
	 * @throws ModelBuilderException
	 * @throws TemplateBuilderException
	 * @throws XPathExpressionException 
	 * @throws TransformerException 
	 */
	public static String createTemplateURI( String source, String mapping, String destination)
			throws InvocationTargetException, IOException,
			ModelBuilderException, TemplateBuilderException, XPathExpressionException, TransformerException {
		FreeMarkerTemplateBuilder destinationBuilder = null;
		FreeMarkerTemplateBuilder sourceBuilder = null;
		ModelBuilder destinationModel;
		ModelBuilder sourceModel;

		destinationModel = new XMLSampleModelBuilder(URI.createFileURI(destination),false);
		ModelBuilder.setStrictModel(destinationModel.buildModel(), true)		;
		ModelBuilder.setEnforceCollectionSubMappingRules((Element) destinationModel.buildModel().getDocumentElement(), true)		;
	
		destinationModel.configureModel();
		
		sourceModel = new XMLSampleModelBuilder(URI.createFileURI(source),false);
		ModelBuilder.setStrictModel(sourceModel.buildModel(), true)		;
		ModelBuilder.setEnforceCollectionSubMappingRules((Element) sourceModel.buildModel().getDocumentElement(), true)		;
		sourceModel.configureModel();
		
		
	
		//printDocument(builder.buildModel(), System.out);
	
		destinationBuilder = new XMLFreeMarkerTemplateBuilder(destinationModel);
		sourceBuilder =  new XMLFreeMarkerTemplateBuilder(sourceModel);
		destinationBuilder = addMappping(sourceBuilder, mapping, destinationBuilder);
		printDocument(destinationBuilder.getModel(), System.out);
		destinationBuilder.setNodeModelSource(true);
		return destinationBuilder.buildTemplate();
		
	}
	
	public static String createTemplate(String sourceXML, String mapping, String destinationXML)
			throws InvocationTargetException, ModelBuilderException,
			TemplateBuilderException, XPathExpressionException, IOException {
		FreeMarkerTemplateBuilder destinationBuilder = null;
		FreeMarkerTemplateBuilder sourceBuilder = null;
		ModelBuilder destinationModel;
		ModelBuilder sourceModel;

		destinationModel = new XMLSampleModelBuilder(destinationXML);
		ModelBuilder.setStrictModel(destinationModel.buildModel(), true)		;
		ModelBuilder.setEnforceCollectionSubMappingRules((Element) destinationModel.buildModel().getDocumentElement(), true)		;
	
		destinationModel.configureModel();
		
		sourceModel = new XMLSampleModelBuilder(sourceXML);
		ModelBuilder.setStrictModel(sourceModel.buildModel(), true)		;
		ModelBuilder.setEnforceCollectionSubMappingRules((Element) sourceModel.buildModel().getDocumentElement(), true)		;
		sourceModel.configureModel();
		
	
		destinationBuilder = new XMLFreeMarkerTemplateBuilder(destinationModel);
		sourceBuilder =  new XMLFreeMarkerTemplateBuilder(sourceModel);
		destinationBuilder = addMappping(sourceBuilder, mapping, destinationBuilder);
		destinationBuilder.setNodeModelSource(true);
		return destinationBuilder.buildTemplate();

		

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

	public static FreeMarkerTemplateBuilder addMappping(FreeMarkerTemplateBuilder sourceBuilder,  String mapping,   FreeMarkerTemplateBuilder destinationBuilder) throws InvalidMappingException, XPathExpressionException{
		
		JsonParser parser = new JsonParser();
		JsonArray o = (JsonArray)parser.parse(mapping);
		List<CollectionMapping> collectionMappings = new ArrayList<CollectionMapping>();
		
		
		Iterator<JsonElement> iterator = o.iterator();
		//TODO identify the collection mappings mapped to complex types create list ie collection mappings get the collection names as last part of xpath
		while(iterator.hasNext()){
			JsonObject json = (JsonObject)iterator.next();
		    String from = json.get("from").getAsString();
		    String to = json.get("to").getAsString();
		    if(!(from.contains(" Input") || to.contains(" Output"))){
		    	
		    
		    
		    
		    Node source =  sourceBuilder.getModelNode(from);
		    Node destination =  destinationBuilder.getModelNode(to);
		    
		   
		    	if(source.getNodeType() == Node.ELEMENT_NODE) { 
		   if(ModelBuilder.getElementType((Element) source) == ElementType.complex && ModelBuilder.getMaxOccurs((Element) source) > 1){
			   
			   CollectionMapping collection = new CollectionMapping(from, destination, source.getNodeName());
			   collectionMappings.add(collection);
		   }
		   else{
			   destinationBuilder.addValueMapping(from,destination);   
		   }
		    	}else
		    	{
		    		   destinationBuilder.addValueMapping(from,destination);   
		    	}
		    }	   
		}
		
		for(CollectionMapping collection : collectionMappings) {
			//TODO change it ?
			destinationBuilder.addCollectionMapping(collection.getSrcPath(), (Element) collection.getMappingNode(), collection.getCollectionItemName());
			ModelBuilder.setCollectionVariable((Element) collection.getMappingNode(), collection.getCollectionItemName(), collection.getSrcPath());
		}
				
		return destinationBuilder;
			
	}
	
	
	public static void printDocument(org.w3c.dom.Document document,
			OutputStream out) throws IOException, TransformerException {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(
				"{http://xml.apache.org/xslt}indent-amount", "4");

		transformer.transform(new DOMSource(document), new StreamResult(
				new OutputStreamWriter(out, "UTF-8")));
	}
}