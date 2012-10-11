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


import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.milyn.FilterSettings;
import org.milyn.Smooks;
import org.milyn.SmooksException;
import org.milyn.StreamFilterType;
import org.milyn.delivery.DomModelCreator;
import org.milyn.templating.TemplatingConfiguration;
import org.milyn.templating.freemarker.FreeMarkerTemplateProcessor;
import org.smooks.templating.model.ModelBuilderException;
import org.smooks.templating.template.exception.TemplateBuilderException;
import org.smooks.templating.template.util.SmooksFMUtil;

import org.xml.sax.SAXException;


import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathExpressionException;



/**
 * 
 * @author Michal Skackov
 * 
 */
public class Freemarker_Smooks_Test {
	public static void main(String[] args) throws IOException, SAXException,
			SmooksException, InterruptedException {
		
			try {
				String mapping = 	"[\n  {\n    \"id\": \"1\",\n    \"from\": \"/shiporder/@orderid\",\n    \"to\": \"/catalog/book/author\",\n    \"rowid\": \"1\"\n  },\n  {\n    \"id\": \"2\",\n    \"from\": \"/shiporder\",\n    \"to\": \"/catalog\",\n    \"rowid\": \"2\"\n  },\n  {\n    \"id\": \"3\",\n    \"from\": \"/shiporder/shipto/name\",\n    \"to\": \"/catalog/book/author\",\n    \"rowid\": \"3\"\n  },\n  {\n    \"id\": \"4\",\n    \"from\": \"/shiporder/item/title\",\n    \"to\": \"/catalog/book/@id\",\n    \"rowid\": \"4\"\n  },\n  {\n    \"id\": \"5\",\n    \"from\": \"/shiporder/shipto/country\",\n    \"to\": \"/catalog/book/publish_date\",\n    \"rowid\": \"5\"\n  },\n  {\n    \"id\": \"6\",\n    \"from\": \"/shiporder/shipto/address\",\n    \"to\": \"/catalog/book/description\",\n    \"rowid\": \"6\"\n  },\n  {\n    \"id\": \"7\",\n    \"from\": \"/shiporder/shipto/city\",\n    \"to\": \"/catalog/book/title\",\n    \"rowid\": \"7\"\n  },\n  {\n    \"id\": \"8\",\n    \"from\": \"/shiporder/shipto/name\",\n    \"to\": \"/catalog/book/genre\",\n    \"rowid\": \"8\"\n  }, {\n    \"id\": \"9\",\n    \"from\": \"/shiporder/item\",\n    \"to\": \"/catalog/book\",\n    \"rowid\": \"9\"\n  }  \n]";	
				
				String template = SmooksFMUtil.createTemplateURI("source/source.xml", mapping, "target/target.xml");
			    Smooks smooks = new Smooks();

			    smooks.addVisitor(new DomModelCreator(), "$document");
			    smooks.addVisitor(new FreeMarkerTemplateProcessor(new TemplatingConfiguration(template)),"$document");
			    smooks.setFilterSettings(new FilterSettings(StreamFilterType.DOM));
			  	    
			    
						
				//		new Smooks(SmooksFMUtil.getSmooksConfiguration(template));
				StreamSource sourceXML = new StreamSource(new FileInputStream("source/source.xml"));
				smooks.filterSource(sourceXML, new StreamResult(System.out));
				smooks.close();
				System.out.println();
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
	
	
	


	
	

	
	
}