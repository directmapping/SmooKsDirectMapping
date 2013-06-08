package org.smooks.directmapping.mapping.model.util;

import java.util.ArrayList;
import java.util.List;

public class MappingObject {

	  private String sourceXMLKey;
	  private String targetXMLKey;
	  private String sourceXSDKey;
	  private String targetXSDKey;
	  private String sourceRootElement;
	  private String targetRootElement;
	  private List<Mappings> mapping;
	  private List<Functions> functions;


	  public MappingObject() {
		  List<Functions> functions = new ArrayList<Functions>();
		  List<Mappings> mapping = new ArrayList<Mappings>();
			
		    this.sourceXMLKey = "";
		    this.targetXMLKey =  "";
		    this.mapping = mapping;
		    this.functions = functions;
		  }
	  
	  public MappingObject(String source, String target,  List<Mappings> mappings,  List<Functions> functions) {
		   
		    this.sourceXMLKey = source;
		    this.targetXMLKey =  target;
		    this.mapping = mappings;
		    this.functions = functions;
		  }
	 
	  public String getSourceXMLKey() {
		return sourceXMLKey;
	}




	public void setSourceXMLKey(String sourceXMLKey) {
		this.sourceXMLKey = sourceXMLKey;
	}




	public String getTargetXMLKey() {
		return targetXMLKey;
	}




	public void setTargetXMLKey(String targetXMLKey) {
		this.targetXMLKey = targetXMLKey;
	}




	public List<Mappings> getMapping() {
		return mapping;
	}




	public void setMapping(List<Mappings> mapping) {
		this.mapping = mapping;
	}




	public List<Functions> getFunctions() {
		return functions;
	}




	public void setFunctions(List<Functions> functions) {
		this.functions = functions;
	}

	public String getSourceXSDKey() {
		return sourceXSDKey;
	}

	public void setSourceXSDKey(String sourceXSDKey) {
		this.sourceXSDKey = sourceXSDKey;
	}

	public String getTargetXSDKey() {
		return targetXSDKey;
	}

	public void setTargetXSDKey(String targetXSDKey) {
		this.targetXSDKey = targetXSDKey;
	}

	public String getSourceRootElement() {
		return sourceRootElement;
	}

	public void setSourceRootElement(String sourceRootElement) {
		this.sourceRootElement = sourceRootElement;
	}

	public String getTargetRootElement() {
		return targetRootElement;
	}

	public void setTargetRootElement(String targetRootElement) {
		this.targetRootElement = targetRootElement;
	}




	



	  
}
