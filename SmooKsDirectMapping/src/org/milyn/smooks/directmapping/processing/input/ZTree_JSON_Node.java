
package org.milyn.smooks.directmapping.processing.input;

public class ZTree_JSON_Node {

	
	  private int id;
	  private int pId;
	  private String name;
	  private String xpath;
	  private boolean open;
	  private boolean isFolder;
	  private int occurance;
	  

	
	  public ZTree_JSON_Node(int id, int pId, String name, String xpath) {
	    this.id = id;
	    this.pId = pId;
	    this.setName(name);
	    this.setXpath(xpath);
	    this.open = false;
	    this.setFolder(false);
	    this.setOccurance(1);
	  }
	  
	  
	  public ZTree_JSON_Node(int id, int pId, String name, String xpath, boolean open,  boolean isFolder) {
		    this.id = id;
		    this.pId = pId;
		    this.setName(name);
		    this.setXpath(xpath);
		    this.open = open;
		    this.setFolder(isFolder);
		    this.setOccurance(1);
		  }


	public String getXpath() {
		return xpath;
	}


	public void setXpath(String xpath) {
		this.xpath = xpath;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getOccurance() {
		return occurance;
	}


	public void setOccurance(int occurance) {
		this.occurance = occurance;
	}


	public boolean isFolder() {
		return isFolder;
	}


	public void setFolder(boolean isFolder) {
		this.isFolder = isFolder;
	}
	 	  
	  
}
