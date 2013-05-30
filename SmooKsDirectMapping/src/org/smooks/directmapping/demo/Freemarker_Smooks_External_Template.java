package org.smooks.directmapping.demo;


import org.milyn.SmooksException;
import org.smooks.directmapping.mapping.model.JSONMappingModelBuilder;
import org.smooks.directmapping.model.ModelBuilder;
import org.smooks.directmapping.model.ModelBuilderException;

import org.smooks.directmapping.model.xml.XSDModelBuilder;
import org.xml.sax.SAXException;



import java.io.BufferedReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import javax.xml.transform.stream.StreamSource;



public class Freemarker_Smooks_External_Template {

	/**
	 * @param argsx
	 */
	 public static void main(String[] args) throws IOException, SAXException, SmooksException, InterruptedException
	    {
		 
		 	ModelBuilder builderXML;
		    JSONMappingModelBuilder jsonmodel;
		    List<String> elements = new ArrayList<String>(); 
		 /**
		 //XML
		 
		 String xml = getStringFromInputStream(new FileInputStream("input/input-message.xml"));
	 	  
	   
		
		
	   
		
		
		try {
			
			builderXML = new XMLSampleModelBuilder(xml);	
			builderXML.configureModel();
			jsonmodel = new JSONMappingModelBuilder(builderXML.buildModel().getDocumentElement());
			System.out.println(jsonmodel.getJSON().toString());
		} catch (Exception e) {
			
		}
		
		
		
		
		
		
		
		String file = null;
		file = "input/PO.xsd";
		XSDModelBuilder xsdModelBuilder;
		try {
			xsdModelBuilder = new XSDModelBuilder(URI.createFileURI(file));
			Set<String> elementNames = xsdModelBuilder.getRootElementNames();
			Iterator<String> it = elementNames.iterator();
			while (it.hasNext()) {
				String name = it.next();
				elements.add(name);
			
			}
			
			((XSDModelBuilder)xsdModelBuilder).setRootElementName("purchaseOrder");
			xsdModelBuilder.configureModel();
			jsonmodel = new JSONMappingModelBuilder(xsdModelBuilder.buildModel().getDocumentElement());
			System.out.println(jsonmodel.getJSON().toString());
			
			
		} catch (ModelBuilderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		**/
		//XSD
		XSDModelBuilder builderXSD;
		 // ModelBuilder builderXSD;
		     
		     String xsd = getStringFromInputStream(new FileInputStream("input/PO.xsd")); 	     
		     try {
				builderXSD = new XSDModelBuilder(xsd);
				Set<String> elementNames = builderXSD.getRootElementNames();
				Iterator<String> it = elementNames.iterator();
				while (it.hasNext()) {
					String name = it.next();
					elements.add(name);
					System.out.println(name);
				}
				((XSDModelBuilder)builderXSD).setRootElementName("purchaseOrder");
				
				jsonmodel = new JSONMappingModelBuilder(builderXSD.buildModel().getDocumentElement());
				System.out.println(jsonmodel.getJSON().toString());
			} catch (ModelBuilderException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		  
		       
			
			
		     
		    		     
		    	 
		    }
	 
	 
	    
	// convert InputStream to String
		private static String getStringFromInputStream(InputStream is) {
	 
			BufferedReader br = null;
			StringBuilder sb = new StringBuilder();
	 
			String line;
			try {
	 
				br = new BufferedReader(new InputStreamReader(is));
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
	 
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
	 
			return sb.toString();
	 
		}
	 
}
	 