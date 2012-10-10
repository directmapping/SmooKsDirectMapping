package org.smooks.templating.demo;


import org.milyn.Smooks;
import org.milyn.SmooksException;
import org.smooks.templating.mapping.model.util.MappingObject;
import org.xml.sax.SAXException;

import com.google.gson.Gson;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;



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
	 
	 
		/* Gson gson = new Gson();
		 int[] ints = {1, 2, 3, 4, 5};
		 String[] strings = {"abc", "def", "ghi"};
		 String jsons = "{\"source\":\"agp4bWwtdG8teG1scg8LEglzb3VyY2VYTUwYJQw\",\"destination\":\"agp4bWwtdG8teG1schQLEg5kZXN0aW5hdGlvblhNTBgmDA\",\"mapping\":\"[\n  {\n  \"id\": \"1\",\n    \"from\": \"/shiporder/shipto/name\",\n    \"to\": \"/catalog/book/author\",\n    \"rowid\": \"1\"\n  },\n  {\n    \"id\": \"2\",\n    \"from\": \"/shiporder/item/quantity\",\n    \"to\": \"/catalog/book/genre\",\n    \"rowid\": \"2\"\n  },\n  {\n    \"id\": \"3\",\n    \"from\": \"/shiporder/item\",\n    \"to\": \"/catalog/book\",\n    \"rowid\": \"3\"\n  },\n  {\n    \"id\": \"4\",\n    \"from\": \"/shiporder\",\n    \"to\": \"/catalog\",\n    \"rowid\": \"4\"\n  },\n  {\n    \"id\": \"5\",\n    \"from\": \"/shiporder/item/quantity\",\n    \"to\": \"/catalog/book/publish_date\",\n    \"rowid\": \"5\"\n  },\n  {\n    \"id\": \"6\",\n    \"from\": \"/shiporder/item/price\",\n    \"to\": \"/catalog/book/description\",\n    \"rowid\": \"6\"\n  },\n  {\n    \"id\": \"7\",\n    \"from\": \"/shiporder/@orderid\",\n    \"to\": \"/catalog/book/@id\",\n    \"rowid\": \"7\"\n  }\n]\",\"functions\":\"undefined\"}";
		
		 String jsonText = jsons;
		  JSONParser parser = new JSONParser();
		  ContainerFactory containerFactory = new ContainerFactory(){
		    public List creatArrayContainer() {
		      return new LinkedList();
		    }

		    public Map createObjectContainer() {
		      return new LinkedHashMap();
		    }
		                        
		  };
		                
		  try{
		    Map json = (Map)parser.parse(jsonText, containerFactory);
		    Iterator iter = json.entrySet().iterator();
		    System.out.println("==iterate result==");
		    while(iter.hasNext()){
		      Map.Entry entry = (Map.Entry)iter.next();
		      System.out.println(entry.getKey() + "=>" + entry.getValue());
		    }
		                        
		    System.out.println("==toJSONString()==");
		    System.out.println(JSONValue.toJSONString(json));
		  }
		  catch(ParseException pe){
		    System.out.println(pe);
		  }*/
	    
	 
}
	 
	class Data {
	    private String source;
	    private String destination;
	    private List<Maps> mapping;
	    private List<Functions> functions;

	   
	    public String toString() {
	        return String.format("source:%s,destination:%s,mapping:%s,functions:%s", source, destination, mapping, functions);
	    }


		public String getSource() {
			return source;
		}


		public void setSource(String source) {
			this.source = source;
		}


		public String getDestination() {
			return destination;
		}


		public void setDestination(String destination) {
			this.destination = destination;
		}


		public List<Maps> getMapping() {
			return mapping;
		}


		public void setMapping(List<Maps> mapping) {
			this.mapping = mapping;
		}


		public List<Functions> getFunctions() {
			return functions;
		}


		public void setFunctions(List<Functions> functions) {
			this.functions = functions;
		}
	}

	class Maps {
		
		   private String id;
		    private String from;

			   private String to;
			    private String rowid;
	}
	
	class Functions {
		
		   private String id;
		    private String destination;
		    
	
	}
