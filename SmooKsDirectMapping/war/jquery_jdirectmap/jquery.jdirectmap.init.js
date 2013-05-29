
	function storeGrid(){
			var rowIds = $('#mapping_list').jqGrid('getDataIDs');
			jQuery.fn.jDirectMapTreeInit.mapping = [];
			// iterate through the rows and delete each of them
			
			
			jQuery.each(rowIds, function(i, val) {
			      
			      var currRowData =  $("#mapping_list").jqGrid('getRowData',  rowIds[i]);
				    
				    jQuery.fn.jDirectMapTreeInit.mapping.push({"id": currRowData.id , "from":  currRowData.sparam, "to": currRowData.dparam,"rowid" : rowIds[i] });
			
			    });

					
			//Session persistency 
			sessvars.mapping = jQuery.fn.jDirectMapTreeInit.mapping;
			
			if(jQuery.fn.jDirectMapTreeInit.functions == null)
				{
				jQuery.fn.jDirectMapTreeInit.functions = [];
				}
			
		}
	

	function createUploader(){            
	    var uploader = new qq.FileUploader({
	        element: $('#basicUploadSuccessExample')[0],
	        action: "/import",
	        allowedExtensions: ['json'],
	        sizeLimit: 50000,
	        uploadButtonText:'Import already existing project.',//text for uploader button
             onComplete: function(id, fileName, request){
	        	
	    	
							
	    		sessvars.$.clearMem();
	   	
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
	        		location.reload();  
	        	
						 
				
	        }
	    });
	}
	

	
	
function helper_init_xml(){
		$("#source_xml_area").val("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>	<shiporder orderid=\"889923\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"shiporder.xsd\"><orderperson>John Smith</orderperson>							  <shipto>							    <name>Ola Nordmann</name>							    <address>Langgt 23</address>							    <city>4000 Stavanger</city>							    <country>Norway</country>							  </shipto>							  <item>							    <title>Empire Burlesque</title>							    <note>Special Edition</note>							    <quantity>12</quantity>							    <price>12.90</price>							  </item>							  <item>							    <title>Hide your heart</title>							    <quantity>1</quantity>							    <price>9.90</price>							  </item>							</shiporder>");
	
		$("#dest_xml_area").val(						"<?xml version=\"1.0\"?>" +
				"<PurchaseOrders>" +
				  "<PurchaseOrder PurchaseOrderNumber=\"99503\" OrderDate=\"1999-10-20\">" +
				    "<Address Type=\"Shipping\">" +
				     " <Name>Ellen Adams</Name>" +
				      "<Street>123 Maple Street</Street>" +
				     " <City>Mill Valley</City>" +
				      "<State>CA</State>" +
				     " <Zip>10999</Zip>" +
				    "  <Country>USA</Country>" +
				   " </Address>" +
				   " <Address Type=\"Billing\">" +
				      "<Name>Tai Yee</Name>" +
				      "<Street>8 Oak Avenue</Street>" +
				      "<City>Old Town</City>" +
				      "<State>PA</State>" +
				      "<Zip>95819</Zip>" +
				     " <Country>USA</Country>" +
				    "</Address>" +
				    "<DeliveryNotes>Please leave packages in shed by driveway.</DeliveryNotes>" +
				    "<Items>" +
				      "<Item PartNumber=\"872-AA\">" +
				        "<ProductName>Lawnmower</ProductName>" +
				       " <Quantity>1</Quantity>" +
				      "  <USPrice>148.95</USPrice>" +
				      "  <Comment>Confirm this is electric</Comment>" +
				      "</Item>" +
				      "<Item PartNumber=\"926-AA\">" +
				        "<ProductName>Baby Monitor</ProductName>" +
				       " <Quantity>2</Quantity>" +
				       " <USPrice>39.98</USPrice>" +
				       " <ShipDate>1999-05-21</ShipDate>" +
				      "</Item>" +
				    "</Items>" +
				  "</PurchaseOrder>" +
				  "<PurchaseOrder PurchaseOrderNumber=\"99505\" OrderDate=\"1999-10-22\">" +
				    "<Address Type=\"Shipping\">" +
				      "<Name>Cristian Osorio</Name>" +
				      "<Street>456 Main Street</Street>" +
				      "<City>Buffalo</City>" +
				      "<State>NY</State>" +
				      "<Zip>98112</Zip>" +
				     " <Country>USA</Country>" +
				   " </Address>" +
				    "<Address Type=\"Billing\">" +
				      "<Name>Cristian Osorio</Name>" +
				      "<Street>456 Main Street</Street>" +
				      "<City>Buffalo</City>" +
				      "<State>NY</State>" +
				      "<Zip>98112</Zip>" +
				      "<Country>USA</Country>" +
				    "</Address>" +
				    "<DeliveryNotes>Please notify me before shipping.</DeliveryNotes>" +
				    "<Items>" +
				      "<Item PartNumber=\"456-NM\">" +
				        "<ProductName>Power Supply</ProductName>" +
				        "<Quantity>1</Quantity>" +
				        "<USPrice>45.99</USPrice>" +
				      "</Item>" +
				    "</Items>" +
				 " </PurchaseOrder>" +
				 " <PurchaseOrder PurchaseOrderNumber=\"99504\" OrderDate=\"1999-10-22\">" +
				    "<Address Type=\"Shipping\">" +
				      "<Name>Jessica Arnold</Name>" +
				      "<Street>4055 Madison Ave</Street>" +
				      "<City>Seattle</City>" +
				      "<State>WA</State>" +
				     " <Zip>98112</Zip>" +
				     " <Country>USA</Country>" +
				   " </Address>" +
				   " <Address Type=\"Billing\">" +
				      "<Name>Jessica Arnold</Name>" +
				      "<Street>4055 Madison Ave</Street>" +
				      "<City>Buffalo</City>" +
				      "<State>NY</State>" +
				      "<Zip>98112</Zip>" +
				      "<Country>USA</Country>" +
				    "</Address>" +
				    "<Items>" +
				      "<Item PartNumber=\"898-AZ\">" +
				       " <ProductName>Computer Keyboard</ProductName>" +
				       " <Quantity>1</Quantity>" +
				       " <USPrice>29.99</USPrice>" +
				      "</Item>" +
				      "<Item PartNumber=\"898-AM\">" +
				      "  <ProductName>Wireless Mouse</ProductName>" +
				     "   <Quantity>1</Quantity>" +
				    "    <USPrice>14.99</USPrice>" +
				   "   </Item>" +
				  "  </Items>" +
				  "</PurchaseOrder>" +
				"</PurchaseOrders>" 
				 );
	
	
	
	
	var html_jdirectmap = '<table id="mapping_main">'
		+'	<thead>'
		+'	<tr>'
		+'		<th>'
		+'			<p>Source</p>'
		+'		</th>'
		+'		<th>	'
		+'			<p>Mapped Elements</p>'
		+'		</th>'
		+'		<th>'
		+'			<p>Target</p>'
		+'		</th>'
		+'	</tr>'
		+'	</thead>'
		+'	<tbody>'
		+'	<tr valign="top">'
		+'		<td style="width:25%">'
		+'			<div id="tree_source"> </div>'
		+'		</td>'
		+'		<td style="width:45%">'
		+'			<div id="mapping_list_div" style="width:100%">'
		+'			<table id="mapping_list"></table>'
					
		+'			<div id="gridpager">	'
		+'					<input type="button" id="reset"  value="Reset Project"  />'
		+'					<input type="button" id="deletedata" value="Delete Row" />'
		+'					<input type="button" id="moveup" value="Move up" />'
		+'					<input type="button" id="movedown"  value="Move down"  />'
		+'					<input type="button" id="clear"  value="Clear Mapping"  />'
		+'					<input type="button" id="collapse"  value="Collapse All"  />'
		
		+'					<input type="button" id="export"  value="Export Project"  />'
		+'					<input type="button" id="transform"  value="Transform XML"  />'
		+'					<input type="button" id="template"  value="Export Smooks Configuration"  />'
		+'			</div> '
		+'			</div>'
		+'			<br />'
		+'			<div class="ui-jqgrid-titlebar ui-corner-top ui-helper-clearfix" style="font-size:11px">'
		+'				<table id="function_table" class="ui-jqgrid-htable" border="0" cellpadding="0" cellspacing="0">'
		+'				<tr class="ui-state-default ui-jqgrid-hdiv"> '
		+'				<td width="25%" > Function Input </td>'
		+'				<td width="50%" > Function Script (FreeMarker Syntax)</td>'
		+'				<td width="25%" > Function Output</td>'
		+'				</tr>'
		+'				<tr valign="top" >'
		+'				<td>'
		+'					<div id="dom_tree_source" class="categoryDiv_source">Drop Source Elements  <br/> <br/> Function Input<br/><br/></div>'

		+'				</td>'
		+'				<td rowspan="2">'
		+'				<div id="function_body" style="width:100%;height:100%">'
		+'				<textarea id="function_area" name="function_area"  ></textarea>'
		+'				</div>'
		+'				</td>'
		+'				<td>'
		+'					<div id="dom_tree_target" class="categoryDiv_target">Drop Target Elements <br/>  <br/>Function Output<br/><br/></div>'
		+'				</td>'			
		+'				</tr>'
		+'				<tr  valign="top">'
		+'				<td>'
		+'					<div id="par_tree_source"></div>'
		+'				</td>'
						
		+'				<td>'
		+'				<div id="par_tree_target" ></div>'
		+'				</td>'
		+'				</tr>'
		+'				</table>	'
		+'				<div class="ui-state-default ui-jqgrid-pager ui-corner-bottom ui-widget" >'
							
		+'				Please speficy unique function name : '
		+'					<input type="text" id="functionname" value="Function_Name"  />'
		+'					<input type="button" id="createfunction" value="Create"  />'
		+'					<input type="button" id="clearfunction" value="Clear" />'
		+'				</div>'
				
						
		+'		</div>'
				
		+'		</td>'
		+'		<td style="width:25%">'
		+'			<div id="tree_target"></div>'
		+'		</td>'
		+'	</tr>'
		+'	</tbody>'
		+'	</table>'
			
			
			jQuery("#directmappping").html(html_jdirectmap);
	
	
	
			
	}



