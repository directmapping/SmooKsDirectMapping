/**
 * JBoss, Home of Professional Open Source
 * Copyright 2009, JBoss Inc., and others contributors as indicated
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
 * (C) 2009, JBoss Inc.
 */
package org.smooks.directmapping.template;

import java.util.ArrayList;
import java.util.List;

//import org.eclipse.core.runtime.Assert;
import org.smooks.directmapping.model.ModelBuilder;
import org.w3c.dom.Node;

/**
 * 
 * Abstract Function.
 * <p/>
 * Represents a successful function. It also tells if the function requires
 * other model nodes to be hidden in the Editor view, so as to restrict functions to these nodes.
 * Extended by Michal Skackov
 * 
 *  @author Michal Skackov
 */
public abstract class Function {

	private String srcPath;
	private Node functionNode;
	private List<Node> hideNodes;
	private String collectionVariable = null;

    /**
     * Public constructor.
     * @param srcPath Source path.
     * @param functionNode The function node.
     */
    public Function(String srcPath, Node functionNode) {
     //   Assert.isNotNull(srcPath, "srcPath"); //$NON-NLS-1$
     //  Assert.isNotNull(functionNode, "functionNode"); //$NON-NLS-1$
        this.srcPath = srcPath;
        this.functionNode = functionNode;
    }

	public String getSrcPath() {
	    return srcPath;
	}
	
	// Extended by Michal Skackov
	public void setSrcPath(String srcPath) {
	    this.srcPath = srcPath;
	}

	public Node getFunctionNode() {
	    return functionNode;
	}

	public List<Node> getHideNodes() {
	    return hideNodes;
	}

	public void addHideNode(Node node) {
	 //   Assert.isNotNull(node, "node"); //$NON-NLS-1$
	
	    if(hideNodes == null) {
	        hideNodes = new ArrayList<Node>();
	    }
	
	    hideNodes.add(node);
	}

	public boolean isParentNodeFunction(Function function) {
		Node parentNode = ModelBuilder.getParentNode(function.getFunctionNode());
		
		while(parentNode != null) {
			if(parentNode == getFunctionNode()) {
				return true;
			}
			parentNode = ModelBuilder.getParentNode(parentNode);
		}
		
		return false;
	}

	public String getCollectionVariable() {
		return collectionVariable;
	}

	public void setCollectionVariable(String collectionVariable) {
		this.collectionVariable = collectionVariable;
	}
}