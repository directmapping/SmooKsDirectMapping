package org.smooks.templating.demo;

/*
 Milyn - Copyright (C) 2006 - 2010

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License (version 2.1) as published by the Free Software
 Foundation.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

 See the GNU Lesser General Public License for more details:
 http://www.gnu.org/licenses/lgpl.txt
 */


import org.eclipse.emf.common.util.URI;
import org.milyn.Smooks;
import org.milyn.SmooksException;
import org.smooks.templating.model.ModelBuilder;
import org.smooks.templating.model.ModelBuilderException;
import org.smooks.templating.model.ModelBuilder.ElementType;
import org.smooks.templating.model.xml.XMLSampleModelBuilder;
import org.smooks.templating.template.exception.InvalidMappingException;
import org.smooks.templating.template.exception.TemplateBuilderException;
import org.smooks.templating.template.freemarker.FreeMarkerTemplateBuilder;
import org.smooks.templating.template.xml.XMLFreeMarkerTemplateBuilder;
import org.smooks.templating.template.CollectionMapping;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathExpressionException;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
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

/**
 * 
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 * 
 */
public class Freemarker_Smooks_Test {
	public static void main(String[] args) throws IOException, SAXException,
			SmooksException, InterruptedException {
		
			try {
				String template = getModelBuilderTarget("target/target.xml","source/source.xml");
				Smooks smooks = new Smooks(getSmooksConfiguration(template));
				StreamSource sourceXML = new StreamSource(new FileInputStream("source/source.xml"));
				smooks.filterSource(sourceXML, new StreamResult(System.out));
				smooks.close(); 
				System.out.println(template);
			} catch (XPathExpressionException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (InvocationTargetException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (ModelBuilderException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (TemplateBuilderException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (Exception e){
				e.printStackTrace();
			}
		
	}
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
		templateWriter.write(template);
		
		/**templateWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		templateWriter.write("<catalog>");
		templateWriter.write("    <book id='${.vars[\"shiporder\"][\"item-i\"][\"title-t\"][0]!}'>");
		templateWriter.write("       <author>${shiporder.@orderid[0]!}</author>        ");
		templateWriter.write("        <title>${.vars[\"shiporder\"][\"ship-to\"][\"ci-ty\"][0]!}</title>        ");
		templateWriter.write("        <genre>${.vars[\"shiporder\"][\"ship-to\"][\"name\"][0]!}</genre>        ");
		templateWriter.write("    <publish_date>${.vars[\"shiporder\"][\"ship-to\"][\"country\"][0]!}</publish_date>");
		templateWriter.write("       <description>${.vars[\"shiporder\"][\"ship-to\"][\"address\"][0]!}</description>");        
		templateWriter.write("   </book>    ");
		templateWriter.write("</catalog>");
		**/
		
		
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
	
	/**
	 * @param contents
	 * @param newFilePath
	 * @param source 
	 * @return
	 * @throws InvocationTargetException
	 * @throws IOException
	 * @throws ModelBuilderException
	 * @throws TemplateBuilderException
	 * @throws XPathExpressionException 
	 * @throws TransformerException 
	 */
	public static String getModelBuilderTarget(String newFilePath, String source)
			throws InvocationTargetException, IOException,
			ModelBuilderException, TemplateBuilderException, XPathExpressionException, TransformerException {
		FreeMarkerTemplateBuilder templateBuilder = null;
		FreeMarkerTemplateBuilder sourceBuilder = null;
		ModelBuilder builder;
		ModelBuilder sourcemodel;

		builder = new XMLSampleModelBuilder(URI.createFileURI(newFilePath),false);
		ModelBuilder.setStrictModel(builder.buildModel(), true)		;
		ModelBuilder.setEnforceCollectionSubMappingRules((Element) builder.buildModel().getDocumentElement(), true)		;
	
		builder.configureModel();
		
		sourcemodel = new XMLSampleModelBuilder(URI.createFileURI(source),false);
		ModelBuilder.setStrictModel(sourcemodel.buildModel(), true)		;
		ModelBuilder.setEnforceCollectionSubMappingRules((Element) sourcemodel.buildModel().getDocumentElement(), true)		;
		sourcemodel.configureModel();
		
		
	
		//printDocument(builder.buildModel(), System.out);
	
		templateBuilder = new XMLFreeMarkerTemplateBuilder(builder);
		sourceBuilder =  new XMLFreeMarkerTemplateBuilder(sourcemodel);
		String mapping = 	"[\n  {\n    \"id\": \"1\",\n    \"from\": \"/shiporder/@orderid\",\n    \"to\": \"/catalog/book/author\",\n    \"rowid\": \"1\"\n  },\n  {\n    \"id\": \"2\",\n    \"from\": \"/shiporder\",\n    \"to\": \"/catalog\",\n    \"rowid\": \"2\"\n  },\n  {\n    \"id\": \"3\",\n    \"from\": \"/shiporder/shipto/name\",\n    \"to\": \"/catalog/book/author\",\n    \"rowid\": \"3\"\n  },\n  {\n    \"id\": \"4\",\n    \"from\": \"/shiporder/item/title\",\n    \"to\": \"/catalog/book/@id\",\n    \"rowid\": \"4\"\n  },\n  {\n    \"id\": \"5\",\n    \"from\": \"/shiporder/shipto/country\",\n    \"to\": \"/catalog/book/publish_date\",\n    \"rowid\": \"5\"\n  },\n  {\n    \"id\": \"6\",\n    \"from\": \"/shiporder/shipto/address\",\n    \"to\": \"/catalog/book/description\",\n    \"rowid\": \"6\"\n  },\n  {\n    \"id\": \"7\",\n    \"from\": \"/shiporder/shipto/city\",\n    \"to\": \"/catalog/book/title\",\n    \"rowid\": \"7\"\n  },\n  {\n    \"id\": \"8\",\n    \"from\": \"/shiporder/shipto/name\",\n    \"to\": \"/catalog/book/genre\",\n    \"rowid\": \"8\"\n  }, {\n    \"id\": \"9\",\n    \"from\": \"/shiporder/item\",\n    \"to\": \"/catalog/book\",\n    \"rowid\": \"9\"\n  }  \n]";	
		templateBuilder = addMappping(templateBuilder, mapping, sourceBuilder);
		printDocument(templateBuilder.getModel(), System.out);
		templateBuilder.setNodeModelSource(true);
		return templateBuilder.buildTemplate();
		
	}

	public static FreeMarkerTemplateBuilder addMappping(FreeMarkerTemplateBuilder templateBuilder,  String mapping, FreeMarkerTemplateBuilder sourcemodel) throws InvalidMappingException, XPathExpressionException{
		
		JsonParser parser = new JsonParser();
		JsonArray o = (JsonArray)parser.parse(mapping);
		List<CollectionMapping> collectionMappings = new ArrayList<CollectionMapping>();
		
		
		Iterator<JsonElement> iterator = o.iterator();
		//TODO identify the collection mappings mapped to complex types create list ie collection mappings get the collection names as last part of xpath
		while(iterator.hasNext()){
			JsonObject json = (JsonObject)iterator.next();
		    String from = json.get("from").getAsString();
		    String to = json.get("to").getAsString();
		    Node source =  sourcemodel.getModelNode(from);
		    Node destination =  templateBuilder.getModelNode(to);
		    
		   
		    	if(source.getNodeType() == Node.ELEMENT_NODE) { 
		   if(ModelBuilder.getElementType((Element) source) == ElementType.complex && ModelBuilder.getMaxOccurs((Element) source) > 1){
			   
			   CollectionMapping collection = new CollectionMapping(from, destination, source.getNodeName());
			   collectionMappings.add(collection);
		   }
		   else{
			   templateBuilder.addValueMapping(from,destination);   
		   }
		    	}else
		    	{
		    		   templateBuilder.addValueMapping(from,destination);   
		    	}
		    		   
		}
		
		for(CollectionMapping collection : collectionMappings) {
			//TODO change it ?
			templateBuilder.addCollectionMapping(collection.getSrcPath(), (Element) collection.getMappingNode(), collection.getCollectionItemName());
			ModelBuilder.setCollectionVariable((Element) collection.getMappingNode(), collection.getCollectionItemName(), collection.getSrcPath());
		}
				
		return templateBuilder;
			
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