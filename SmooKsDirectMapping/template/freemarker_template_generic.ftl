<?xml version="1.0"?>
<smooks-resource-list xmlns="http://www.milyn.org/xsd/smooks-1.1.xsd" xmlns:core="http://www.milyn.org/xsd/smooks/smooks-core-1.3.xsd" xmlns:ftl="http://www.milyn.org/xsd/smooks/freemarker-1.1.xsd">
<ftl:freemarker applyOnElement="#document">
<ftl:template><![CDATA[<?xml version="1.0" xmlns="ns1:http://tempuri.org/po.xsd" encoding="UTF-8"?>
<purchaseOrder>
    <shipTo country='${.vars["purchaseOrder"]["shipTo"]["@country"][0]!}'>
        <name>${.vars["purchaseOrder"]["shipTo"]["name"][0]!}</name>        
        <street>${.vars["purchaseOrder"]["shipTo"]["street"][0]!}</street>        
        <city>${.vars["purchaseOrder"]["shipTo"]["city"][0]!}</city>        
        <state>${.vars["purchaseOrder"]["shipTo"]["state"][0]!}</state>        
        <zip>${.vars["purchaseOrder"]["shipTo"]["zip"][0]!}</zip>        
    </shipTo>    
    <billTo>
        <name>#required</name>        
        <street>#required</street>        
        <city>#required</city>        
        <state>#required</state>        
        <zip>#required</zip>        
    </billTo>    
    <items>
    </items>    
</purchaseOrder>]]></ftl:template>
</ftl:freemarker>
<resource-config selector="#document">
<resource>org.milyn.delivery.DomModelCreator</resource>
</resource-config>
</smooks-resource-list>