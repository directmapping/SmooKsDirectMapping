
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
	
function helper_init_xml(){
		$("#source_xml_area").val("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>	<shiporder orderid=\"889923\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"shiporder.xsd\"><orderperson>John Smith</orderperson>							  <shipto>							    <name>Ola Nordmann</name>							    <address>Langgt 23</address>							    <city>4000 Stavanger</city>							    <country>Norway</country>							  </shipto>							  <item>							    <title>Empire Burlesque</title>							    <note>Special Edition</note>							    <quantity>1</quantity>							    <price>10.90</price>							  </item>							  <item>							    <title>Hide your heart</title>							    <quantity>1</quantity>							    <price>9.90</price>							  </item>							</shiporder>");
	
	$("#dest_xml_area").val("<?xml version=\"1.0\"?> " +
				" <catalog> " +
				"  <book id=\"bk101\"> " +
				"   <author>Gambardella, Matthew</author> <title>XML Developer's Guide</title>  <genre>Computer</genre>   <price>44.95</price>    <publish_date>2000-10-01</publish_date>" +
				"      <description>An in-depth look at creating applications with XML.</description>   </book>   <book id=\"bk102\">  <author>Ralls, Kim</author>    <title>Midnight Rain</title>  <genre>Fantasy</genre>   <price>5.95</price>    <publish_date>2000-12-16</publish_date>    <description>A former architect battles corporate zombies,an evil sorceress, and her own childhood to become queen of the world.</description>  </book>  <book id=\"bk103\">	" +
				"   <author>Corets, Eva</author> <title>Maeve Ascendant</title>   <genre>Fantasy</genre><price>5.95</price> <publish_date>2000-11-17</publish_date><description>After the collapse of a nanotechnology						      society in England, the young survivors lay the						      foundation for a new society.</description>" +
		  " </book> " +
		  " <book id= \"bk104\"> " +
		   "   <author>Corets, Eva</author> " +
		    "  <title>Oberon's Legacy</title> " +
		     " <genre>Fantasy</genre> " +
		      "<price>5.95</price> " +
		    "  <publish_date>2001-03-10</publish_date> " +
		    "  <description>In post-apocalypse England, the mysterious " + 
		    "  agent known only as Oberon helps to create a new life " + 
		    "  for the inhabitants of London. Sequel to Maeve " + 
		    "  Ascendant.</description> " +
		  " </book> " +
		   " <book id=\"bk105\"> " +
		     " <author>Corets, Eva</author> " +
		      " <title>The Sundered Grail</title> " +
		     " <genre>Fantasy</genre> " +
		     " <price>5.95</price> " +
		     " <publish_date>2001-09-10</publish_date> " +
		     " <description>The two daughters of Maeve, half-sisters, " + 
		     " battle one another for control of England. Sequel to " + 
		     " Oberon's Legacy.</description> " +
		  " </book> " +
		  " <book id=\"bk106\"> " +
		  "    <author>Randall, Cynthia</author> " +
		  "    <title>Lover Birds</title> " +
		  "    <genre>Romance</genre> " +
		  "    <price>4.95</price> " +
		  "    <publish_date>2000-09-02</publish_date> " +
		  "    <description>When Carla meets Paul at an ornithology " + 
		  "    conference, tempers fly as feathers get ruffled.</description> " +
		  " </book> " +
		  " <book id=\"bk107\"> " +
		  "    <author>Thurman, Paula</author> " +
		  "    <title>Splish Splash</title> " +
		  "    <genre>Romance</genre> " +
		  "    <price>4.95</price> " +
		  "    <publish_date>2000-11-02</publish_date> " +
		  "    <description>A deep sea diver finds true love twenty " + 
		  "    thousand leagues beneath the sea.</description> " +
		  " </book> " +
		  " <book id=\"bk108\"> " +
		  "    <author>Knorr, Stefan</author> " +
		  "    <title>Creepy Crawlies</title> " +
		  "    <genre>Horror</genre> " +
		  "    <price>4.95</price> " +
		  "    <publish_date>2000-12-06</publish_date> " +
		  "    <description>An anthology of horror stories about roaches, " +
		  "    centipedes, scorpions  and other insects.</description> " +
		  " </book> " +
		  " <book id=\"bk109\"> " +
		  "    <author>Kress, Peter</author> " +
		  "    <title>Paradox Lost</title> " +
		  "    <genre>Science Fiction</genre> " +
		  "    <price>6.95</price> " +
		  "    <publish_date>2000-11-02</publish_date> " +
		  "    <description>After an inadvertant trip through a Heisenberg " +
		  "    Uncertainty Device, James Salway discovers the problems " + 
		  "    of being quantum.</description> " +
		  " </book> " +
		  " <book id=\"bk110\"> " +
		  "    <author>O'Brien, Tim</author> " +
		  "    <title>Microsoft .NET: The Programming Bible</title> " +
		  "    <genre>Computer</genre> " +
		  "    <price>36.95</price> " +
		  "    <publish_date>2000-12-09</publish_date> " +
		  "    <description>Microsoft's .NET initiative is explored in  " +
		  "    detail in this deep programmer's reference.</description> " +
		  " </book> " +
		  " <book id=\"bk111\"> " +
		  "    <author>O'Brien, Tim</author> " +
		  "    <title>MSXML3: A Comprehensive Guide</title> " +
		  "    <genre>Computer</genre> " +
		  "    <price>36.95</price> " +
		  "    <publish_date>2000-12-01</publish_date> " +
		  "    <description>The Microsoft MSXML3 parser is covered in  " +
		  "    detail, with attention to XML DOM interfaces, XSLT processing, " + 
		  "    SAX and more.</description> " +
		  " </book> " +
		  " <book id=\"bk112\"> " +
		  "    <author>Galos, Mike</author> " +
		  "    <title>Visual Studio 7: A Comprehensive Guide</title> " +
		  "    <genre>Computer</genre> " +
		  "    <price>49.95</price> " +
		  "    <publish_date>2001-04-16</publish_date> " +
		  "    <description>Microsoft Visual Studio 7 is explored in depth, " +
		  "    looking at how Visual Basic, Visual C++, C#, and ASP+ are " + 
		  "    integrated into a comprehensive development " + 
		  "    environment.</description> " +
		  " </book> " +
		" </catalog> " );
	
	
	
	
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
		+'			<p>Destination</p>'
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
		+'					<input type="button" id="deletedata" value="Delete" />'
		+'					<input type="button" id="moveup" value="Move up" />'
		+'					<input type="button" id="movedown"  value="Move down"  />'
		+'					<input type="button" id="clear"  value="Clear"  />'
		+'					<input type="button" id="collapse"  value="Collapse All"  />'
		+'					<input type="button" id="import"  value="Import"  />'
		+'					<input type="button" id="export"  value="Export"  />'
		+'					<input type="button" id="transform"  value="Transform"  />'
		+'					<input type="button" id="template"  value="Template"  />'
		+'			</div> '
		+'			</div>'
		+'			<br />'
		+'			<div class="ui-jqgrid-titlebar ui-corner-top ui-helper-clearfix" style="font-size:11px">'
		+'				<table id="function_table" class="ui-jqgrid-htable" border="0" cellpadding="0" cellspacing="0">'
		+'				<tr class="ui-state-default ui-jqgrid-hdiv"> '
		+'				<td width="25%" > Function Input </td>'
		+'				<td width="50%" > Function Script </td>'
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
		+'					<div id="dom_tree_destination" class="categoryDiv_destination">Drop Destination Elements <br/>  <br/>Function Output<br/><br/></div>'
		+'				</td>'			
		+'				</tr>'
		+'				<tr  valign="top">'
		+'				<td>'
		+'					<div id="par_tree_source"></div>'
		+'				</td>'
						
		+'				<td>'
		+'				<div id="par_tree_destination" ></div>'
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
		+'			<div id="tree_destination"></div>'
		+'		</td>'
		+'	</tr>'
		+'	</tbody>'
		+'	</table>'
			
			
			jQuery("#directmappping").html(html_jdirectmap);
			
	}