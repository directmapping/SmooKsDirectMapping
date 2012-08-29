

jQuery(document).ready(function(){

							
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
										
											
											//helper_read_xml();
											  
											jQuery.fn.jDirectMapTreeInit(jQuery.parseJSON(request["source"]), $("#tree_source"),"source");
								 			jQuery.fn.jDirectMapTreeInit(jQuery.parseJSON(request["destination"]), $("#tree_destination"),"destination");
								 			jQuery.fn.jDirectMapTreeInit.sourceKey = request["sourceXML"];
								 			jQuery.fn.jDirectMapTreeInit.destinationKey = request["sourceXML"];
											 

							    	
							
				            		helper_grid("#mapping_list");	
				            		helper_ui_xml_to_map();
				            		    
								 var mapping = request["mapping"];
								 var functions = request["functions"];
								 
														},
								 
										error: function(){
											    alert('Problem occured during upload. Please try again! ');
											  }
										 
								});
			
						}); 		
								
					});
 
		
   function helper_ui_xml_to_map(){
	   
		$("#input_data").hide();
		$('#mapping_list').hideCol("id")
		$("#mapping_main").show();
		
		$("#function_area").val("//Please specify function.\n//Example : \nout1 = in1 + in2;\nout2 = new Date();");
		   var editor = CodeMirror.fromTextArea(document.getElementById("function_area"), {
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
	
	
	
	
	
	

	
	