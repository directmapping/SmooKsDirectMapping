

jQuery(document).ready(function(){

							
	
							if(sessvars.sourceJSON!=null)
							{
								helper_init_xml();
								createUploader();
								jQuery.fn.jDirectMapTreeInit.sourceXMLKey = sessvars.sourceXMLKey;
								jQuery.fn.jDirectMapTreeInit.targetXMLKey = sessvars.targetXMLKey;
								jQuery.fn.jDirectMapTreeInit.mapping = sessvars.mapping;
								jQuery.fn.jDirectMapTreeInit.functions = sessvars.functions;
								
								jQuery.fn.jDirectMapTreeInit(sessvars.sourceJSON, $("#tree_source"),"source");
					 			jQuery.fn.jDirectMapTreeInit(sessvars.targetJSON, $("#tree_target"),"target");
					 			  
			            		jQuery.fn.jDirectMapTreeInit.sourceXSDKey = sessvars.sourceXSDKey;
			        			jQuery.fn.jDirectMapTreeInit.targetXSDKey = sessvars.targetXSDKey;
			        			jQuery.fn.jDirectMapTreeInit.sourceRootElement =sessvars.sourceRootElement;
			        			jQuery.fn.jDirectMapTreeInit.targetRootElement = sessvars.targetRootElement;
			        			
					 			
					 			helper_grid("#mapping_list");	
			            		helper_ui_xml_to_map();
			            		
			            		
			            		if(sessvars.mapping!=null)
								{
			            			var map = sessvars.mapping;
					            	
			            			
			            		for(var i=0,len=map.length;i<len;i++){
			            			jQuery.fn.jDirectMapTreeInit.mapping = [];
			        			    jQuery.fn.jDirectMapTreeInit.mapping.push({"id": map[i]['id'] , "from":  map[i]['from'], "to": map[i]['to'],"rowid" : map[i]['id']});
			        			    jQuery("#mapping_list").jqGrid('addRowData',map[i]['id'],{id: map[i]['id'], sparam: map[i]['from'], dparam: map[i]['to']  } );
							
											
			        			} 
			            		}
			            		
			            		
			            		$("#par_tree_source").empty();
								$("#par_tree_target").empty();
								$("#functionname").val("");
								$("#function_area").val("");
			            		
							}
							else{							
							
							helper_init_xml();
							createUploader();
							$("#mapping_main").hide();
							$("#tranform_input_data").hide();
							$("#xsd_input_data").hide();
							
							$("#button-xsd").toggle(function(){
								
								helper_init_xml();
								$("#source_xml_area").val("");
								$("#target_xml_area").val("");
								$("#source_xml_area").attr('rows', '15');
								$("#target_xml_area").attr('rows', '15');
								$("#xmlsubmit").hide();
								$("#input_data").show();
								$("#tranform_input_data").hide();
								$("#xsd_input_data").show();
								$("#button-xsd").text("Import only from XML");
								$("#source_xml_text").text("Source Sample XML (Optional for Tranformation) :");
								$("#target_xml_text").text("Target Sample XML (Optional for Tranformation) :");
								$("#mapping_main").hide();

							}, function() {
								
								helper_init_xml();
								$("#source_xml_area").attr('rows', '25');
								$("#target_xml_area").attr('rows', '25');
								$("#input_data").show();
								$("#mapping_main").hide();
								$("#tranform_input_data").hide();
								$("#xsd_input_data").hide();
								$("#xmlsubmit").show();
								$("#button-xsd").text("Import XSD");
								$("#source_xml_text").text("Source XML:");
								$("#target_xml_text").text("Target XML:");

								
								
							});
							
							$("#button-transform").click(function(){
								
								$("#input_data").hide();
								$("#tranform_input_data").show();
								$("#xsd_input_data").hide();
								sessvars.$.clearMem();
								$("#source_transform_area").val("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
									    "	<purchaseOrder>" +
									    "	    <shipTo country='US'>" +
										     "	        <name>Alice Smith</name>" +
											     "	        <street>123 Maple Street</street>" +
											     "	        <city>Mill Valley</city>" +
											     "	        <state>CA</state>" +
											     "	        <zip>90952</zip>" +
											     "	    </shipTo>" +
										     "	    <billTo>" +
										     "	        <name>Smooks Direct Mapping - US</name>" +
											     "	        <street>1919 Connecticut Ave NW</street>" +
											     "	        <city>Washington</city>" +
											     "	        <state>DC</state>" +
											     "	        <zip>20009</zip>" +
											     "    </billTo>" +
										     "	    <items>" +
										     "	    </items>" +
										     "</purchaseOrder>");

										
										$("#template_transform_area").val("<smooks-resource-list xmlns=\"http://www.milyn.org/xsd/smooks-1.1.xsd" xmlns:core="http://www.milyn.org/xsd/smooks/smooks-core-1.3.xsd\" xmlns:ftl=\"http://www.milyn.org/xsd/smooks/freemarker-1.1.xsd\">" +
										     "	<ftl:freemarker applyOnElement=\"#document\">" +
									    "	<ftl:template><![CDATA[<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
									    "	<Order>" +
									    "	    <address country='${.vars[\"purchaseOrder\"][\"shipTo\"][\"@country\"][0]!}'>" +
										     "        <name>${.vars[\"purchaseOrder\"][\"shipTo\"][\"name\"][0]!}</name>        " +
											     "	        <street>${.vars[\"purchaseOrder\"][\"shipTo\"][\"street\"][0]!}</street>        " +
											     "	        <city>${.vars[\"purchaseOrder\"][\"shipTo\"][\"city\"][0]!}</city>" +
											     "	        <state>${.vars[\"purchaseOrder\"][\"shipTo\"][\"state\"][0]!}</state>" +
											     "       <zip>${.vars[\"purchaseOrder\"][\"shipTo\"][\"zip\"][0]!}</zip>" +
											     "	    </address>" +
										     "  <billTo>" +
										     "    <name>Smooks Direct Mapping - EU</name>" +
											     "      <street>Wiedner Hauptstrasse 12</street>" +
											     "   <city>Vienna</city>" +
											     "     <country>Austria</country>" +
											     "     <blz>A-1104</blz>" +
											     "   </billTo>" +
										     "   <items>" +
										     "   </items>" +
										     "</Order>]]>" +
									    "</ftl:template>" +
									    "</ftl:freemarker>" +
									    "<resource-config selector=\"#document\">" +
									    "<resource>org.milyn.delivery.DomModelCreator</resource>" +
									    "</resource-config>" +
									    "</smooks-resource-list>");
										
								
							});
							
							
							
							$("#xsdsubmit").click(function(){
								
								try{
									
									
									//parameter object definition
									var param=function(name,value){
										this.name=name;
										this.value=value;
									}	
									
									 var data=new Array();
									 
										
									  

									
										
										
										
							
								var pop_up_div_element ="#popup";
							    $(pop_up_div_element).empty();
								var sourceRootElement = "";
								var targetRootElement = "";
								var sourceXSD = $("#source_xsd_area").val();
								var targetXSD = $("#target_xsd_area").val();
								var sourceXML = $("#source_xml_area").val();
								var targetXML = $("#target_xml_area").val();
								var show_popup = false;
								
								if(sourceXML.length < 1){
											var xsddata = $.parseXML(sourceXSD);
											var _root = $(xsddata).children(':first-child');
											if(_root.find('> element').length > 1){
												// no xml example and more than one top element 
												// get root elements as drop down list to select as this is required or determine the root if only one is present
												$(pop_up_div_element).append('<p> Source Schema Root : </p><select id="root_dropdown_source"></select>');
												show_popup = true;
											  	var _ch = _root.find('> element');
											  	//for each element - element child of @input1 node 
												for(var i=0; i<_ch.length; i++){
													$(pop_up_div_element).children("#root_dropdown_source").append( new Option(_ch[i].attributes.name.value,_ch[i].attributes.name.value));	
												}
											}										
											else{
												// get the root element
												sourceRootElement  =  _root.find('> element').attr('name');
											}
								}else{
									// get the root element from XML
									sourceRootElement = $($.parseXML(sourceXML)).children(':first-child')[0].nodeName;								
								}
																															
								// TARGET
								if(targetXML.length < 1)
								{
 
											var xsddata = $.parseXML(targetXSD);
											var _root = $(xsddata).children(':first-child');
											 // no xml example and more than one top element
											// get root elements as drop down list to select as this is required or determine the root if only one is present
											if(_root.find('> element').length > 1){
																								$(pop_up_div_element).append('<p> Target Schema Root : </p> <select id="root_dropdown_target"></select>');
												show_popup = true;
												var _ch = _root.find('> element');
												//for each element - element child of @input1 node 
												for(var i=0; i<_ch.length; i++){
													$(pop_up_div_element).children("#root_dropdown_target").append( new Option(_ch[i].attributes.name.value,_ch[i].attributes.name.value));	
												}
											}
											else{
												// get the root element
												targetRootElement  =  _root.find('> element').attr('name');
											}
								}else{
									// get the root element from XML
									targetRootElement = $($.parseXML(targetXML)).children(':first-child')[0].nodeName;								
									}
								
								
								if(show_popup)
									{
								$(pop_up_div_element).dialog({
									// display drop down pop up message to show
									  autoOpen: true,
								      height: 250,
								      width: 250,
								      modal: true,
								      title:   'Schema Details',
								     buttons: {
								          "Confirm": function() {
								        	if($(this).children("#root_dropdown_source").length  > 0){
								        		sourceRootElement = $(this).children("#root_dropdown_source").find(":selected").text();
								          	}
								        	if($(this).children("#root_dropdown_target").length  > 0){
								        		targetRootElement = $(this).children("#root_dropdown_target").find(":selected").text();
								        	}
								
								            $(this).dialog("close");
								          }										        
								        },
								        close: function() {
								            								        	
											$(pop_up_div_element).empty();
											
											
											data[0] = new param("sourceXML",sourceXML);
											data[1] = new param("targetXML",targetXML);
											data[2] = new param("sourceXSD", sourceXSD);
											data[3] = new param("targetXSD", targetXSD);
											data[4] = new param("sourceRootElement", sourceRootElement);
											data[5] = new param("targetRootElement", targetRootElement);
											data[data.length]=new param('action','xml_input');
											 //making the ajax call
											 $.ajax({
													url : "/input",
													type : "POST",
													data:data,
													dataType:"json",
													success : function(request) {
													
														
														  
														jQuery.fn.jDirectMapTreeInit(jQuery.parseJSON(request["sourceJSON"]), $("#tree_source"),"source");
											 			jQuery.fn.jDirectMapTreeInit(jQuery.parseJSON(request["targetJSON"]), $("#tree_target"),"target");
											 			jQuery.fn.jDirectMapTreeInit.sourceXMLKey = request["sourceXMLKey"];
											 			jQuery.fn.jDirectMapTreeInit.targetXMLKey = request["targetXMLKey"];
											 			jQuery.fn.jDirectMapTreeInit.sourceXSDKey = request["sourceXSDKey"];
											 			jQuery.fn.jDirectMapTreeInit.targetXSDKey = request["targetXSDKey"];
											 			jQuery.fn.jDirectMapTreeInit.sourceRootElement = request["sourceRootElement"];
											 			jQuery.fn.jDirectMapTreeInit.targetRootElement = request["targetRootElement"];
											 			jQuery.fn.jDirectMapTreeInit.mapping = request["mapping"];
									            		jQuery.fn.jDirectMapTreeInit.functions = request["functions"];
													  
									            		sessvars.sourceXMLKey = jQuery.fn.jDirectMapTreeInit.sourceXMLKey;
									        			sessvars.targetXMLKey = jQuery.fn.jDirectMapTreeInit.targetXMLKey;
									        			sessvars.sourceXSDKey = jQuery.fn.jDirectMapTreeInit.sourceXSDKey;
									        			sessvars.targetXSDKey = jQuery.fn.jDirectMapTreeInit.targetXSDKey;
									        			sessvars.sourceRootElement = jQuery.fn.jDirectMapTreeInit.sourceRootElement;
									        			sessvars.targetRootElement = jQuery.fn.jDirectMapTreeInit.targetRootElement;
									        			sessvars.mapping = jQuery.fn.jDirectMapTreeInit.mapping;
									        			sessvars.functions =  jQuery.fn.jDirectMapTreeInit.functions;
									        			sessvars.sourceJSON = jQuery.parseJSON(request["sourceJSON"]);
														sessvars.targetJSON = jQuery.parseJSON(request["targetJSON"]);
												
												
										
							            		helper_grid("#mapping_list");	
							            		helper_ui_xml_to_map();
							            	
							            	
																	},
											 
													error: function(){
																helper_ui_msg('Problem occured during upload. Please try again!', ' Service Unavailable');
														  }
													 
											});
						
											
								        }
								});
									}
								else{
												        		
				        		data[0] = new param("sourceXML",sourceXML);
								data[1] = new param("targetXML",targetXML);
								data[2] = new param("sourceXSD", sourceXSD);
								data[3] = new param("targetXSD", targetXSD);
								data[4] = new param("sourceRootElement", sourceRootElement);
								data[5] = new param("targetRootElement", targetRootElement);
								data[data.length]=new param('action','xsd_input');
								 //making the ajax call
								 $.ajax({
										url : "/input",
										type : "POST",
										data:data,
										dataType:"json",
										success : function(request) {
										
											
											  
											jQuery.fn.jDirectMapTreeInit(jQuery.parseJSON(request["sourceJSON"]), $("#tree_source"),"source");
								 			jQuery.fn.jDirectMapTreeInit(jQuery.parseJSON(request["targetJSON"]), $("#tree_target"),"target");
								 			jQuery.fn.jDirectMapTreeInit.sourceXMLKey = request["sourceXMLKey"];
								 			jQuery.fn.jDirectMapTreeInit.targetXMLKey = request["targetXMLKey"];
								 			jQuery.fn.jDirectMapTreeInit.sourceXSDKey = request["sourceXSDKey"];
								 			jQuery.fn.jDirectMapTreeInit.targetXSDKey = request["targetXSDKey"];
								 			jQuery.fn.jDirectMapTreeInit.sourceRootElement = request["sourceRootElement"];
								 			jQuery.fn.jDirectMapTreeInit.targetRootElement = request["targetRootElement"];
								 			jQuery.fn.jDirectMapTreeInit.mapping = request["mapping"];
						            		jQuery.fn.jDirectMapTreeInit.functions = request["functions"];
										  
						            		sessvars.sourceXMLKey = jQuery.fn.jDirectMapTreeInit.sourceXMLKey;
						        			sessvars.targetXMLKey = jQuery.fn.jDirectMapTreeInit.targetXMLKey;
						        			sessvars.sourceXSDKey = jQuery.fn.jDirectMapTreeInit.sourceXSDKey;
						        			sessvars.targetXSDKey = jQuery.fn.jDirectMapTreeInit.targetXSDKey;
						        			sessvars.sourceRootElement = jQuery.fn.jDirectMapTreeInit.sourceRootElement;
						        			sessvars.targetRootElement = jQuery.fn.jDirectMapTreeInit.targetRootElement;
						        			sessvars.mapping = jQuery.fn.jDirectMapTreeInit.mapping;
						        			sessvars.functions =  jQuery.fn.jDirectMapTreeInit.functions;
						        			sessvars.sourceJSON = jQuery.parseJSON(request["sourceJSON"]);
											sessvars.targetJSON = jQuery.parseJSON(request["targetJSON"]);
									
									
							
				            		helper_grid("#mapping_list");	
				            		helper_ui_xml_to_map();
				            	
				            	
														},
								 
										error: function(xhr, ajaxOptions, thrownError){
													helper_ui_msg('Problem occured during upload. Please try again!' +  ' '  + xhr.status + '     ' + thrownError, ' Service Unavailable');
											  }
										 
								});
			
				        	}
								}catch(ex){
									helper_ui_msg(ex.message, ex.name);
								}
								
							});
						
							
							

							$("#transformsubmit").click(function(){
								
								//parameter object definition
								var param=function(name,value){
									this.name=name;
									this.value=value;
								}	
								
								 var data=new Array();
								 
								 
								 var sourceXML = $("#source_transform_area").val();
								 var template = $("#template_transform_area").val();
								 
								 data[0] = new param("sourceXML",sourceXML);
								 data[1] = new param("template",template);
								 
								 $.download("/smooks",data); 
									
							});
							
							$("#xmlsubmit").click(function(){  
								
							//parameter object definition
							var param=function(name,value){
								this.name=name;
								this.value=value;
							}	
							
							 var data=new Array();
							 
							  var sourceXML = $("#source_xml_area").val();
							  var targetXML = $("#target_xml_area").val();
							  var sourceRootElement = $($.parseXML(sourceXML)).children(':first-child')[0].nodeName;								
							  var targetRootElement = $($.parseXML(targetXML)).children(':first-child')[0].nodeName;								
								
							  

								data[0] = new param("sourceXML",sourceXML);
								data[1] = new param("targetXML",targetXML);
								data[2] = new param("sourceXSD", "");
								data[3] = new param("targetXSD", "");
								data[4] = new param("sourceRootElement", sourceRootElement);
								data[5] = new param("targetRootElement", targetRootElement);
								data[data.length]=new param('action','xml_input');
								 //making the ajax call
								
							
								 $.ajax({
										url : "/input",
										type : "POST",
										data:data,
										dataType:"json",
										success : function(request) {
										
											
											  
											jQuery.fn.jDirectMapTreeInit(jQuery.parseJSON(request["sourceJSON"]), $("#tree_source"),"source");
								 			jQuery.fn.jDirectMapTreeInit(jQuery.parseJSON(request["targetJSON"]), $("#tree_target"),"target");
								 			jQuery.fn.jDirectMapTreeInit.sourceXMLKey = request["sourceXMLKey"];
								 			jQuery.fn.jDirectMapTreeInit.targetXMLKey = request["targetXMLKey"];
								 			jQuery.fn.jDirectMapTreeInit.sourceXSDKey = request["sourceXSDKey"];
								 			jQuery.fn.jDirectMapTreeInit.targetXSDKey = request["targetXSDKey"];
								 			jQuery.fn.jDirectMapTreeInit.sourceRootElement = request["sourceRootElement"];
								 			jQuery.fn.jDirectMapTreeInit.targetRootElement = request["targetRootElement"];
								 			jQuery.fn.jDirectMapTreeInit.mapping = request["mapping"];
						            		jQuery.fn.jDirectMapTreeInit.functions = request["functions"];
										  
						            		sessvars.sourceXMLKey = jQuery.fn.jDirectMapTreeInit.sourceXMLKey;
						        			sessvars.targetXMLKey = jQuery.fn.jDirectMapTreeInit.targetXMLKey;
						        			sessvars.sourceXSDKey = jQuery.fn.jDirectMapTreeInit.sourceXSDKey;
						        			sessvars.targetXSDKey = jQuery.fn.jDirectMapTreeInit.targetXSDKey;
						        			sessvars.sourceRootElement = jQuery.fn.jDirectMapTreeInit.sourceRootElement;
						        			sessvars.targetRootElement = jQuery.fn.jDirectMapTreeInit.targetRootElement;
						        			sessvars.mapping = jQuery.fn.jDirectMapTreeInit.mapping;
						        			sessvars.functions =  jQuery.fn.jDirectMapTreeInit.functions;
						        			sessvars.sourceJSON = jQuery.parseJSON(request["sourceJSON"]);
											sessvars.targetJSON = jQuery.parseJSON(request["targetJSON"]);
									
									
							
				            		helper_grid("#mapping_list");	
				            		helper_ui_xml_to_map();
				            	
				            	
														},
								 
										error: function(){
													helper_ui_msg('Problem occured during upload. Please try again!', ' Service Unavailable');
											  }
										 
								});
			
							}); 	
						
							}
		
 
	
	
							
							
});
	

	
	