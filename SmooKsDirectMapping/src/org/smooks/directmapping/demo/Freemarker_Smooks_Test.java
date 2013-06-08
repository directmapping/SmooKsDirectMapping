package org.smooks.directmapping.demo;

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
import org.smooks.directmapping.model.ModelBuilderException;
import org.smooks.directmapping.template.exception.TemplateBuilderException;
import org.smooks.directmapping.template.util.SmooksFMUtil;

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
				
				Smooks smooks = new Smooks("template/smooks.conf");
			  	    
			    
						
				//		new Smooks(SmooksFMUtil.getSmooksConfiguration(template));
				StreamSource sourceXML = new StreamSource(new FileInputStream("input/input-message_test.xml"));
				
				smooks.filterSource(sourceXML, new StreamResult(System.out));
				smooks.close();
				System.out.println();
			
		
			} catch (Exception e){
				e.printStackTrace();
			}
		
	}
	public static String removeXmlStringNamespace(String xmlString) {
		  return xmlString.replaceAll("xmlns.*?(\"|\').*?(\"|\')", ""); /* remove xmlns declaration */
		  
		}
	
	


	
	

	
	
}