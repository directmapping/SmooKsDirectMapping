

jQuery(document).ready(function(){

							
	
							if(sessvars.sourceXML!=null)
							{
								helper_init_xml();
								createUploader();
								jQuery.fn.jDirectMapTreeInit.sourceKey = sessvars.sourceXML;
								jQuery.fn.jDirectMapTreeInit.targetKey = sessvars.targetXML;
								jQuery.fn.jDirectMapTreeInit.mapping = sessvars.mapping;
								jQuery.fn.jDirectMapTreeInit.functions = sessvars.functions;
								
								jQuery.fn.jDirectMapTreeInit(sessvars.source, $("#tree_source"),"source");
					 			jQuery.fn.jDirectMapTreeInit(sessvars.target, $("#tree_target"),"target");
					 		
					 			
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
								$("#dest_xml_area").val("");
								$("#source_xml_area").attr('rows', '15');
								$("#dest_xml_area").attr('rows', '15');
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
								$("#dest_xml_area").attr('rows', '25');
								$("#input_data").show();
								$("#mapping_main").hide();
								$("#tranform_input_data").hide();
								$("#xsd_input_data").hide();
								$("#xmlsubmit").show();
								$("#button-xsd").text("Import XSD");
								$("#source_xml_text").text("Source XML:");
								$("#target_xml_text").text("Target XML:");

								
								
							});
							
							$("#buttons-transform").click(function(){
								
								$("#input_data").hide();
								$("#tranform_input_data").show();
								$("#xsd_input_data").hide();
								
							});
							
							$("#xsdsubmit").click(function(){
								
							
								var sourceXSD = $("#source_xsd_area").val();
								if($("#source_xml_area").val().length < 1)
								{
									// get root elements as drop down list to select as this is required or determine the root if only one is present
									var xsddata = $.parseXML(sourceXSD);
									var _root = $(xsddata).children(':first-child');
												
									if(_root.find('> element').length > 1)
										{
										
										}
									else
										{
									var rootname =  _root.find('> element').attr('name')
								}
								}
								
									
								
							});
							
							$("#transformsubmit").click(function(){
								
								alert('transformsubmit');
							});
							
							
							
							$("#xmlsubmit").click(function(){  
								
							//parameter object definition
							var param=function(name,value){
								this.name=name;
								this.value=value;
							}	
							
							 var data=new Array();
							 

								data[0] = new param("source",$("#source_xml_area").val());
								data[1] = new param("target",$("#dest_xml_area").val());
								data[2] = new param("sourceXML", "");
								data[3] = new param("targetXML", "");
								
								 //setting action as PUT
								 data[data.length]=new param('action','PUT');
								 //making the ajax call
								
								 //$.post("/downloadXML",data); 
								
								//  download file test  $.download("/downloadXML",data);
								 
								 /** $.post("/toXML",data);  		url : "/toXML",**/
								 $.ajax({
										url : "/input",
										type : "POST",
										data:data,
										dataType:"json",
										success : function(request) {
										
											
											  
											jQuery.fn.jDirectMapTreeInit(jQuery.parseJSON(request["source"]), $("#tree_source"),"source");
								 			jQuery.fn.jDirectMapTreeInit(jQuery.parseJSON(request["target"]), $("#tree_target"),"target");
								 			jQuery.fn.jDirectMapTreeInit.sourceKey = request["sourceXML"];
								 			jQuery.fn.jDirectMapTreeInit.targetKey = request["targetXML"];
								 			jQuery.fn.jDirectMapTreeInit.mapping = request["mapping"];
						            		jQuery.fn.jDirectMapTreeInit.functions = request["functions"];
										  
						            		sessvars.sourceXML = jQuery.fn.jDirectMapTreeInit.sourceKey;
						        			sessvars.targetXML = jQuery.fn.jDirectMapTreeInit.targetKey;
						        			sessvars.mapping = jQuery.fn.jDirectMapTreeInit.mapping;
						        			sessvars.functions =  jQuery.fn.jDirectMapTreeInit.functions;
						        			sessvars.source = jQuery.parseJSON(request["source"]);
											sessvars.target = jQuery.parseJSON(request["target"]);
									
							
				            		helper_grid("#mapping_list");	
				            		helper_ui_xml_to_map();
				            	
				            	
														},
								 
										error: function(){
											    alert('Problem occured during upload. Please try again! ');
											  }
										 
								});
			
						}); 	
							}
								
					});
 
		
   function helper_ui_xml_to_map(){
	   
		$("#input_data").hide();
		$("#tranform_input_data").hide();
		$("#xsd_input_data").hide();
		$("#button-xsd").hide();
		$("#button-transform").hide();
		
		$('#mapping_list').hideCol("id")
		$("#mapping_main").show();
		
		$("#function_area").val("//Please specify function. FreeMarker syntax \n//Example : \n \"Hello ${in1}!\ See attached invoice for book ${in2}\"");
		jQuery.fn.jDirectMapTreeInit.editor = CodeMirror.fromTextArea(document.getElementById("function_area"), {
		       lineNumbers: true,
		       matchBrackets: true
		     });
		$("#function_table").css('width','100%')   
		  
		$("#mapping_list").setGridWidth($(document).width()*0.40);
		$("#mapping_list").setGridWidth($(document).width()*0.40);
		$("#mapping_list").css('width','100%'); 
		$("#gbox_mapping_list").css('width','100%'); 
		$("#gview_mapping_list").css('width','100%'); 
		$("#gridpager").css('width','100%');
		$("#mapping_list").css('width','100%');
		$(".ui-jqgrid-hdiv").css('width','100%');
		$(".ui-jqgrid-htable").css('width','100%');
		$(".ui-jqgrid-bdiv").css('width','100%');
	}
	
	
	
	
	
	

	
	