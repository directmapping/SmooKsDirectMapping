package org.smooks.templating.demo;


import org.milyn.SmooksException;
import org.smooks.templating.mapping.model.util.MappingObject;
import org.xml.sax.SAXException;

import com.google.gson.Gson;


import java.io.IOException;



public class Freemarker_Smooks_External_Template {

	/**
	 * @param argsx
	 */
	 public static void main(String[] args) throws IOException, SAXException, SmooksException, InterruptedException
	    {
	
		 
		 
		    String json =  "{\"source\":\"agp4bWwtdG8teG1scg8LEglzb3VyY2VYTUwYSQw\",\"destination\":\"agp4bWwtdG8teG1scg8LEglzb3VyY2VYTUwYSQw\",\"mapping\":[\n  {\n    \"id\": \"1\",\n    \"from\": \"/shiporder/item/price\",\n    \"to\": \"/catalog/book/price\",\n    \"rowid\": \"1\"\n  },\n  {\n    \"id\": \"2\",\n    \"from\": \"/shiporder/item/quantity\",\n    \"to\": \"/catalog/book/@id\",\n    \"rowid\": \"2\"\n  },\n  {\n    \"id\": \"3\",\n    \"from\": \"/shiporder/shipto/name\",\n    \"to\": \"/catalog/book/author\",\n    \"rowid\": \"3\"\n  },\n  {\n    \"id\": \"4\",\n    \"from\": \"/shiporder/item/title\",\n    \"to\": \"/catalog/book/title\",\n    \"rowid\": \"4\"\n  },\n  {\n    \"id\": \"5\",\n    \"from\": \"/shiporder/shipto/city\",\n    \"to\": \"/catalog/book/genre\",\n    \"rowid\": \"5\"\n  }\n],\"functions\":[]}";

		    
		    Gson gson = new Gson();
		     
		    
			
	 		//convert the json string back to object
	 		MappingObject obj = gson.fromJson(json, MappingObject.class);
		        // Now do the magic.
		        //Data data = new Gson().fromJson(json, Data.class);

		        // Show it.
		     System.out.println(gson.toJson(obj));
		    }
	 
	 
	    
	 
}
	 