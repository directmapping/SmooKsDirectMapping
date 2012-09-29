function helper_grid(table_element){
	

	
	var $table = $(table_element);	
	
	
	
	$table.jqGrid({        
				datatype: "local",
				colNames:['id','Source Parameter', 'Destination Parameter'],
				colModel:[
					{name:'id',index:'id', width:100 , sortable: false},
					{name:'sparam',index:'sparam', width:1000 , sortable: false},
					{name:'dparam',index:'dparam', width:1000, align:"right",  sortable: false}  		
					
				],
				defaults : {
					recordtext: "View {0} - {1} of {2}",
				    emptyrecords: "No records to view",
					loadtext: "Loading...",
					autowidth : "true",
					shrinktofit : "true"
				},
				sortname: 'id',
				width: "100%",
				height: "250",
				viewrecords: false,
				
				onSelectRow: function(id){ 
					  if(id){ 
						  
						  $("#tree_source").find('a').removeClass($.fn.zTree.consts.node.CURSELECTED);
						  $("#tree_destination").find('a').removeClass($.fn.zTree.consts.node.CURSELECTED);
													
						var streeObj = $.fn.zTree.getZTreeObj("tree_source");
						var snode = streeObj.getNodesByParam("xpath",  $table.getRowData(id).sparam);
						
						for( var i=0, l=snode.length; i<l; i++) {
								
								$("#" + snode[i].tId + $.fn.zTree.consts.id.A).addClass($.fn.zTree.consts.node.CURSELECTED);
						}
												
						
						streeObj.expandAll(true);
					
						
						var dtreeObj = $.fn.zTree.getZTreeObj("tree_destination");
						var dnode = dtreeObj.getNodesByParam("xpath",  $table.getRowData(id).dparam);
					
						for( var i=0, l=dnode.length; i<l; i++) {
							
							$("#" + dnode[i].tId + $.fn.zTree.consts.id.A).addClass($.fn.zTree.consts.node.CURSELECTED);
						}
						
						dtreeObj.expandAll(true);
		
						
					}
				},
				
				
				
				
				editurl:"someurl.php", // local processing
				pager: '#gridpager'  // remove the data
	});



	
	
	$("#deletedata").click(function(){
				var tableData = new Array();
				var ids = '';
				$table.delRowData($table.getGridParam('selrow'));
				ids = $table.getDataIDs();
					for(var i = 0; i < ids.length; i++){
						 tableData[i] = $table.getRowData(ids[i]);
						 tableData[i].id = i + 1;
					 }
				$table.clearGridData(false);
					for(i = 0; i < tableData.length; i++){
						 $table.addRowData(i + 1, tableData[i]);
					}
				});
	
	$("#clearfunction").click(function(){
		$("#par_tree_source").empty();
		$("#par_tree_destination").empty();
		$("#function_area").val("//Please specify function.\n//Example : \nout1 = in1 + in2;\nout2 = new Date();");
	});

	$("#createfunction").click(function(){
		
		var numberOfRecords = jQuery("#mapping_list").getGridParam("records");
		
		if($("#functionname").val() == ""){
			alert("Please specify unique funcation name");	
		}
		else if($("#par_tree_destination" ).find('span').length == 0){
			alert("Please specify at least one ouput parameter");	
			}
		else if($("#par_tree_source" ).find('span').length == 0){
			alert("Please specify at least one input parameter");				
		}
		else{
			jQuery("#mapping_list").jqGrid('addRowData',++numberOfRecords,{id: numberOfRecords, sparam: $("#functionname").val() +  " Input" , dparam: $("#functionname").val() +  " Output" });
			
			var input_param = new Array();
			var output_param = new Array();
			
			$("#par_tree_source" ).find('span').each(function(i,e){  input_param.push({name : $(this).text() , xpath : $(this).attr("xpath")  });  });
			$("#par_tree_destination" ).find('span').each(function(i,e){  output_param.push({name : $(this).text() , xpath : $(this).attr("xpath")});  });

			
			var functions = jQuery.fn.jDirectMapTreeInit.functions;
			if (functions === undefined)
				{
				functions = [];
				}
			
			
			functions.push({id: numberOfRecords, functionname: $("#functionname").val(), value : $("#function_area").val(), input : input_param, output : output_param});
			
			jQuery.fn.jDirectMapTreeInit.functions = functions ;
			
			
			// TODO add input parameters and output parameters
			$("#par_tree_source").empty();
			$("#par_tree_destination").empty();
			$("#functionname").val("");
			$("#function_area").val("//Please specify function.\n//Example : \nout1 = in1 + in2;\nout2 = new Date();");
		}
	});
	
	
	$("#getrow").click(function(){
				 var dataString = '';
				 var ids = $table.getDataIDs();
				 dataString += 'Selected Row: ' + $table.getGridParam('selrow') + '\nRow ID: ' + ids[$table.getGridParam('selrow') - 1];
				 alert(dataString);
				});	
	
	$("#getdata").click(function(){
				 var ids = $table.getDataIDs();
				 var dataString = '';
					 for(var i = 0; i < ids.length; i++){
						dataString += 'postion: ' + i + ' ' + ids[i] + '\n';
					 }
				 alert(dataString);
				 });
	
	$("#clear").click(function(){
					// get IDs of all the rows odf jqGrid 
					var rowIds = $table.jqGrid('getDataIDs');
					// iterate through the rows and delete each of them
					for(var i=0,len=rowIds.length;i<len;i++){
						var currRow = rowIds[i];
						$table.jqGrid('delRowData', currRow);
					}	
					
				 });
				 
	$("#import").click(function(){
				/*	// get IDs of all the rows odf jqGrid 
					var rowIds = $table.jqGrid('getDataIDs');
					// iterate through the rows and delete each of them
					for(var i=0,len=rowIds.length;i<len;i++){
						var currRow = rowIds[i];
						$table.jqGrid('delRowData', currRow);
					}	
					$("form").show();
					//$("#mapping_main").hide();
					
					https://github.com/valums/file-uploader
					*/
				 });
	
	
	$("#collapse").click(function(){
		$.fn.zTree.getZTreeObj("tree_source").expandAll(true);
		$.fn.zTree.getZTreeObj("tree_destination").expandAll(true);
		 });
	
	
	$("#export").click(function(){
					
			var rowIds = $('#mapping_list').jqGrid('getDataIDs');
			jQuery.fn.jDirectMapTreeInit.mapping = [];

			 var data=new Array();
			 
				//parameter object definition
				var param=function(name,value){
					this.name=name;
					this.value=value;
				}	
				
					 
				 
			// iterate through the rows and delete each of them
			for(var i=0,len=rowIds.length;i<len;i++){
			    var currRowData =  $("#mapping_list").jqGrid('getRowData',  rowIds[i]);
			    
			    jQuery.fn.jDirectMapTreeInit.mapping.push({"id": currRowData.id , "from":  currRowData.sparam, "to": currRowData.dparam,"rowid" : rowIds[i] });
					
			}  
			data[0] = new param("sourceXML",jQuery.fn.jDirectMapTreeInit.sourceKey);
			data[1] = new param("destinationXML",jQuery.fn.jDirectMapTreeInit.destinationKey);
			data[2] = new param("mapping", JSON.stringify(jQuery.fn.jDirectMapTreeInit.mapping, null, 2));
			data[3] = new param("functions", JSON.stringify(jQuery.fn.jDirectMapTreeInit.functions, null, 2));
			 //setting action as PUT
			 data[data.length]=new param('action','PUT');
			
			 	$.download("/transformXML",data); 
		
		 		//	$.download("/downloadXML",data);   // pass all the parameters
		
				//	helper_grid();
					
					


				 });				
				 
	$("#moveup").click(function(){
				   move('up');
				 });
				
	$("#movedown").click(function(){
				   move('down');
				 });

				 function move(direction){
		 if($table.getGridParam('selrow')){
		 var ids = $table.getDataIDs();
		 var temp = 0;
		 var currRow = ids[ $table.getGridParam('selrow') - 1 ];
		 if(direction === 'up' && currRow > 1){
		 var r1 = $table.getRowData(currRow-1);
		 var r2 = $table.getRowData(currRow);
		 $table.delRowData(currRow-1);
		 $table.delRowData(currRow);
		 temp = r1.id;
		 r1.id = r2.id;
		 r2.id = temp;
		 $table.addRowData(r2.id, r2);
		 $table.addRowData(r1.id, r1);
		 }
		 if(direction === 'down' && currRow < (ids.length)){
		 var r1 = $table.getRowData(currRow);
		 var r2 = $table.getRowData(parseInt(currRow)+1);
		 $table.delRowData(currRow);
		 $table.delRowData(parseInt(currRow)+1);
		 temp = r1.id;
		 r1.id = r2.id;
		 r2.id = temp;
		 $table.addRowData(r1.id, r1);
		 $table.addRowData(r2.id, r2);
		 }
		 // Sort the table   
		 $table.setGridParam({sortname:'id'}).trigger('reloadGrid');
		 }
	}
	
	}