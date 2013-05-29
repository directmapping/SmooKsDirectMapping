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
package org.smooks.directmapping.template.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;

import org.eclipse.emf.common.util.URI;

import org.smooks.directmapping.mapping.model.util.Functions;
import org.smooks.directmapping.mapping.model.util.FunctionsObject;
import org.smooks.directmapping.mapping.model.util.FunctionsValues;
import org.smooks.directmapping.mapping.model.util.MappingObject;
import org.smooks.directmapping.model.ModelBuilder;
import org.smooks.directmapping.model.ModelBuilderException;
import org.smooks.directmapping.model.ModelBuilder.ElementType;
import org.smooks.directmapping.model.xml.XMLSampleModelBuilder;
import org.smooks.directmapping.template.CollectionMapping;
import org.smooks.directmapping.template.exception.InvalidMappingException;
import org.smooks.directmapping.template.exception.TemplateBuilderException;
import org.smooks.directmapping.template.freemarker.FreeMarkerTemplateBuilder;
import org.smooks.directmapping.template.xml.XMLFreeMarkerTemplateBuilder;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

/**
 * SmooKs utility methods.
 * 
 * @author <a href="mailto:mskackov-at-google-mail">michal skackov</a>
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
	 * @param target
	 * @return
	 * @throws InvocationTargetException
	 * @throws IOException
	 * @throws ModelBuilderException
	 * @throws TemplateBuilderException
	 * @throws XPathExpressionException 
	 * @throws TransformerException 
	 */
	public static String createTemplateURI( String source, String mapping, String target)
			throws InvocationTargetException, IOException,
			ModelBuilderException, TemplateBuilderException, XPathExpressionException, TransformerException {
		FreeMarkerTemplateBuilder targetBuilder = null;
		FreeMarkerTemplateBuilder sourceBuilder = null;
		ModelBuilder targetodel;
		ModelBuilder sourceModel;

		targetodel = new XMLSampleModelBuilder(URI.createFileURI(target),false);
		ModelBuilder.setStrictModel(targetodel.buildModel(), true)		;
		ModelBuilder.setEnforceCollectionSubMappingRules((Element) targetodel.buildModel().getDocumentElement(), true)		;
	
		targetodel.configureModel();
		
		sourceModel = new XMLSampleModelBuilder(URI.createFileURI(source),false);
		ModelBuilder.setStrictModel(sourceModel.buildModel(), true)		;
		ModelBuilder.setEnforceCollectionSubMappingRules((Element) sourceModel.buildModel().getDocumentElement(), true)		;
		sourceModel.configureModel();
		
		
	
		//printDocument(builder.buildModel(), System.out);
	
		targetBuilder = new XMLFreeMarkerTemplateBuilder(targetodel);
		sourceBuilder =  new XMLFreeMarkerTemplateBuilder(sourceModel);
		targetBuilder = addMappping(sourceBuilder, mapping, targetBuilder);
		//printDocument(targetBuilder.getModel(), System.out);
		targetBuilder.setNodeModelSource(true);
		return targetBuilder.buildTemplate();
		
	}
	
	public static String createTemplate(String sourceXML, String mapping, String functions, String targetXML)
			throws InvocationTargetException, ModelBuilderException,
			TemplateBuilderException, XPathExpressionException, IOException {
		FreeMarkerTemplateBuilder targetBuilder = null;
		FreeMarkerTemplateBuilder sourceBuilder = null;
		ModelBuilder targetModel;
		ModelBuilder sourceModel;

		targetModel = new XMLSampleModelBuilder(targetXML);
		ModelBuilder.setStrictModel(targetModel.buildModel(), true)		;
		ModelBuilder.setEnforceCollectionSubMappingRules((Element) targetModel.buildModel().getDocumentElement(), true)		;
	
		targetModel.configureModel();
		
		sourceModel = new XMLSampleModelBuilder(sourceXML);
		ModelBuilder.setStrictModel(sourceModel.buildModel(), true)		;
		ModelBuilder.setEnforceCollectionSubMappingRules((Element) sourceModel.buildModel().getDocumentElement(), true)		;
		sourceModel.configureModel();
		
	
		targetBuilder = new XMLFreeMarkerTemplateBuilder(targetModel);
		sourceBuilder =  new XMLFreeMarkerTemplateBuilder(sourceModel);
		targetBuilder = addMappping(sourceBuilder, mapping, targetBuilder);
		targetBuilder = addFunctions(sourceBuilder, functions, targetBuilder);
		targetBuilder.setNodeModelSource(true);
		return targetBuilder.buildTemplate();

		

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

	public static FreeMarkerTemplateBuilder addMappping(FreeMarkerTemplateBuilder sourceBuilder,  String mapping,   FreeMarkerTemplateBuilder targetBuilder) throws InvalidMappingException, XPathExpressionException{
		
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
		    Node target =  targetBuilder.getModelNode(to);
		    
		   
		    	if(source.getNodeType() == Node.ELEMENT_NODE) { 
		   if(ModelBuilder.getElementType((Element) source) == ElementType.complex && ModelBuilder.getMaxOccurs((Element) source) > 1){
			   
			   CollectionMapping collection = new CollectionMapping(from, target, source.getNodeName());
			   collectionMappings.add(collection);
		   }
		   else{
			   targetBuilder.addValueMapping(from,target);   
		   }
		    	}else
		    	{
		    		   targetBuilder.addValueMapping(from,target);   
		    	}
		    }	   
		}
		
		for(CollectionMapping collection : collectionMappings) {
			//TODO change it ?
			targetBuilder.addCollectionMapping(collection.getSrcPath(), (Element) collection.getMappingNode(), collection.getCollectionItemName());
			ModelBuilder.setCollectionVariable((Element) collection.getMappingNode(), collection.getCollectionItemName(), collection.getSrcPath());
		}
				
		return targetBuilder;
			
	}
	
	
	public static FreeMarkerTemplateBuilder addFunctions(FreeMarkerTemplateBuilder sourceBuilder,  String functions,   FreeMarkerTemplateBuilder targetBuilder) throws InvalidMappingException, XPathExpressionException{
		Gson gson = new Gson();
		Functions[] obj = null;
		
		JsonReader reader = new JsonReader(new StringReader(functions));
		reader.setLenient(true);

		obj = gson.fromJson(reader, Functions[].class);

		List<CollectionMapping> collectionMappings = new ArrayList<CollectionMapping>();
		
		
		for (Functions iterator : obj){ 
		
		//TODO identify the collection mappings mapped to complex types create list ie collection mappings get the collection names as last part of xpath
		
			Functions function = iterator;
			String functionName = function.getFunctionname();
			String functionValue = function.getValue();
			
			Iterator<FunctionsValues> inputparamIterator = function.getInput().iterator();
			while(inputparamIterator.hasNext()){
				FunctionsValues inputparam = inputparamIterator.next();
				
			}
			FunctionsValues output = null;
			Iterator<FunctionsValues> outputparamIterator = function.getOutput().iterator();
			while(outputparamIterator.hasNext()){
				 output = outputparamIterator.next();
				
			}
			
			//TODO case when 1 output param
			
			//TODO case when more output param 
			
			/*String from = "";
		    String to = output.getXpath();
		    Node target =  targetBuilder.getModelNode(to);
			   	
		    
		    
		    
		    Node source =  sourceBuilder.getModelNode(from);
		    
		   
		    	if(source.getNodeType() == Node.ELEMENT_NODE) { 
					   if(ModelBuilder.getElementType((Element) source) == ElementType.complex && ModelBuilder.getMaxOccurs((Element) source) > 1){
						   
						   CollectionMapping collection = new CollectionMapping(from, target, source.getNodeName());
						   collectionMappings.add(collection);
					   }
					   else{
						   targetBuilder.addValueMapping(from,target);   
					   }
		    	}else
		    	{
		    		   targetBuilder.addValueMapping(from,target);   
		    	}
		    }	   
		
		
		for(CollectionMapping collection : collectionMappings) {
			//TODO change it ?
			targetBuilder.addCollectionMapping(collection.getSrcPath(), (Element) collection.getMappingNode(), collection.getCollectionItemName());
			ModelBuilder.setCollectionVariable((Element) collection.getMappingNode(), collection.getCollectionItemName(), collection.getSrcPath());
		}
				*/
		}
		return targetBuilder;
			
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