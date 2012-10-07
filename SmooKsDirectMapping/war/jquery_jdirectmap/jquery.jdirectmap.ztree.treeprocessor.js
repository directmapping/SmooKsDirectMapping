/*
 * jDirectMapTreeProcessor Class
 * version: 1.0 (03-18-2012)
 * 
 * Copyright (c) 2012 Michal Skackov (directmapping.appspot.com/jdirectmap)
 * 
 * @requires jQuery v1.7.1 or later
 * @requires jsTree 1.0-rc1 or later
 *
 * @extending UIMTreeProcessor version: 1.0 (11-16-2010)  Copyright (c) 2010 Vlad Shamgin (uimonster.com)
 *
 * @todo Supports JSON to JSON transformation 
 *	 jsTree 
 *	 zTree 
 *	 Dynatree
 * @todo Supports XML to JSON transformation  
 *	jsTree
 *	zTree
 *	Dynatree
 * @todo Supports XSD to JSON transformation
 *	jsTree
 *	zTree
 *	Dynatree
 *
 * Examples and documentation at: directmapping.appspot.com/jdirectmap
 * Dual licensed under the MIT and GPL licenses:
 *   http://www.opensource.org/licenses/mit-license.php
 *   http://www.gnu.org/licenses/gpl.html
 


 function jDirectMapTreeProcessor(data, tree_element) {
	this.tree_element = null;
	this.data = data;
	this.tree_element = tree_element;
}
*/
//(function($) {

jQuery.fn.jDirectMapTreeInit = function(data,tree_element,type){
	
		var sourceKey;
		var destinationKey;
		var mapping = [];
		var functions =  [];
		
		var setting = {
			edit: {
				enable: true,
				showRemoveBtn: false,
				showRenameBtn: false
			},
			view: {
				showIcon: false
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			callback: {
				beforeDrag: beforeDrag,
				beforeDrop: beforeDrop,
				onDrop: dropFunction
			},
			
		};
		
		
	
		
		
		
		function dropFunction(e, treeId, treeNodes, targetNode, moveType) {
			var domId = "dom_" + treeId;
			var parId = "par_" + treeId;
			if (moveType == null && (domId == e.target.id)) {
								
				var newDom = $("#" + parId ).find("span[domId=" + treeId + treeNodes[0].id + "]");
				if (newDom.length > 0) {
					newDom.removeClass("domBtn_Disabled");
					newDom.addClass("domBtn");
				} else {
					var id = $("#" + parId ).find('span').length + 1;
				
					
					if(treeId == "tree_source") {
						id = "in" + id;
						$("#" + parId).append("<span class='domBtn_source'   domId='" + treeId + id +   "' xpath='"   + treeNodes[0].xpath + "'>" +  id + ": " + treeNodes[0].xpath + "</span>");
					}
					else if(treeId == "tree_destination") {
						id = "out" + id;
						$("#" + parId).append("<span class='domBtn_destination'   domId='" + treeId + id +  "' xpath='"   + treeNodes[0].xpath + "'>" +  id + ": " + treeNodes[0].xpath + "</span>");
						
					}
					
					
					
					
				}
				
			} else if ( $(e.target).parents(".domBtnDiv").length > 0) {
				if(treeId = "tree_source") {
					alert("Input parameters accept only elements from source tree");
				}
				else if(treeId = "tree_destination") {
					alert("Ouput parameters accept only elements from detination tree");
				}
				else {
					alert("Only tree elements allowed to be droped here");
				}
			}
		}
		
		
		
		
		function beforeDrag(treeId, treeNodes) {
			for (var i=0,l=treeNodes.length; i<l; i++) {
				if (treeNodes[i].drag === false) {
					return false;
				}
			}
			return true;
		}
		function beforeDrop(treeId, treeNodes, targetNode, moveType) {
		
			
			      if(jQuery.fn.jDirectMapTreeInit.mapping ==null){
			    		  jQuery.fn.jDirectMapTreeInit.mapping = [];
			      }
			    		  
					var numberOfRecords = jQuery("#mapping_list").getGridParam("records");
					
						if(type == "source" && treeId == "tree_destination") {
								jQuery("#mapping_list").jqGrid('addRowData',++numberOfRecords,{id: numberOfRecords, sparam: treeNodes[0].xpath, dparam: targetNode.xpath  } );
								$("#tree_source").find('a').removeClass($.fn.zTree.consts.node.CURSELECTED);
							    jQuery.fn.jDirectMapTreeInit.mapping.push({"id": numberOfRecords , "from":  treeNodes[0].xpath, "to": targetNode.xpath,"rowid" : numberOfRecords });
								
								
						}
						else if (type == "destination" && treeId == "tree_source") {
								jQuery("#mapping_list").jqGrid('addRowData',++numberOfRecords,{id: numberOfRecords, sparam: targetNode.xpath , dparam: treeNodes[0].xpath } );
								$("#tree_destination").find('a').removeClass($.fn.zTree.consts.node.CURSELECTED);
								jQuery.fn.jDirectMapTreeInit.mapping.push({"id": numberOfRecords , "from": targetNode.xpath , "to": treeNodes[0].xpath,"rowid" : numberOfRecords });
									
						}
						
						sessvars.mapping = jQuery.fn.jDirectMapTreeInit.mapping;
	        	
		    	
					
			return false;
				
			
		
		}
		
		
		
			$.fn.zTree.init(tree_element, setting, data);
			
			// add CSS class
			tree_element.addClass("ztree");
			
			

}

//})(jQuery)





