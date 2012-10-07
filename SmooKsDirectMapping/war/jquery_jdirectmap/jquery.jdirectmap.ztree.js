

jQuery(document).ready(function(){

							
							if(sessvars.sourceXML!=null)
							{
								helper_init_xml();
							
								jQuery.fn.jDirectMapTreeInit.sourceKey = sessvars.sourceXML;
								jQuery.fn.jDirectMapTreeInit.destinationKey = sessvars.destinationXML;
								jQuery.fn.jDirectMapTreeInit.mapping = sessvars.mapping;
								jQuery.fn.jDirectMapTreeInit.functions = sessvars.functions;
								
								jQuery.fn.jDirectMapTreeInit(sessvars.source, $("#tree_source"),"source");
					 			jQuery.fn.jDirectMapTreeInit(sessvars.destination, $("#tree_destination"),"destination");
					 		
					 			
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
								$("#par_tree_destination").empty();
								$("#functionname").val("");
								$("#function_area").val("");
			            		
							}
							else{							
							
							helper_init_xml();
							$("#mapping_main").hide();
							$("#xmlsubmit").click(function(){  
							
							//parameter object definition
							var param=function(name,value){
								this.name=name;
								this.value=value;
							}	
							
							 var data=new Array();
							 

								data[0] = new param("source",$("#source_xml_area").val());
								data[1] = new param("destination",$("#dest_xml_area").val());
								data[2] = new param("sourceXML", "");
								data[3] = new param("destinationXML", "");
								
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
								 			jQuery.fn.jDirectMapTreeInit(jQuery.parseJSON(request["destination"]), $("#tree_destination"),"destination");
								 			jQuery.fn.jDirectMapTreeInit.sourceKey = request["sourceXML"];
								 			jQuery.fn.jDirectMapTreeInit.destinationKey = request["destinationXML"];
								 			jQuery.fn.jDirectMapTreeInit.mapping = request["mapping"];
						            		jQuery.fn.jDirectMapTreeInit.functions = request["functions"];
										  
						            		sessvars.sourceXML = jQuery.fn.jDirectMapTreeInit.sourceKey;
						        			sessvars.destinationXML = jQuery.fn.jDirectMapTreeInit.destinationKey;
						        			sessvars.mapping = jQuery.fn.jDirectMapTreeInit.mapping;
						        			sessvars.functions =  jQuery.fn.jDirectMapTreeInit.functions;
						        			sessvars.source = jQuery.parseJSON(request["source"]);
											sessvars.destination = jQuery.parseJSON(request["destination"]);
									
							
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
		$('#mapping_list').hideCol("id")
		$("#mapping_main").show();
		
		$("#function_area").val("//Please specify function.\n//Example : \nout1 = in1 + in2;\nout2 = new Date();");
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
	
	
	
	
	
	

	
	