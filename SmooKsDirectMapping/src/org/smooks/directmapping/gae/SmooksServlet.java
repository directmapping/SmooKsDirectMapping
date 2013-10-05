package org.smooks.directmapping.gae;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URLDecoder;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.milyn.FilterSettings;
import org.milyn.Smooks;
import org.milyn.StreamFilterType;
import org.milyn.delivery.DomModelCreator;
import org.milyn.templating.TemplatingConfiguration;
import org.milyn.templating.freemarker.FreeMarkerTemplateProcessor;
import org.smooks.directmapping.gae.util.SmooksFMUtil;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


@SuppressWarnings("serial")
public class SmooksServlet extends HttpServlet {
	private static DocumentBuilder docBuilder;
	private Document doc;
	private static final Logger logger = Logger
			.getLogger(SmooksServlet.class.getCanonicalName());

	
	static {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		docBuilderFactory.setNamespaceAware(true);
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new RuntimeException("Messages.XMLSampleModelBuilder_UnexpectedXMLException", e);
		}
	}
	
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.sendRedirect("/");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String sourceXML = URLDecoder.decode(req.getParameter("sourceXML"), "UTF-8");
		String smooksconfig = URLDecoder.decode(req.getParameter("template"), "UTF-8");
		Smooks smooks = null;
		Writer outWriter = new StringWriter();
		StreamResult resultStream = new StreamResult(outWriter);
		smooks = new Smooks();
	
		try {
				
			
			try {
			 doc = docBuilder.parse(new InputSource(new ByteArrayInputStream(smooksconfig.getBytes("utf-8"))));
			} catch (SAXException e) {
				logger.log(Level.SEVERE, "Exception happening when processing template "
						+ smooksconfig, e);
			
			}
			String template = doc.getDocumentElement().getElementsByTagName("ftl:template").item(0).getTextContent() ;
			
			if (template != null && template.length() > 0) {
				
			
			smooks.addVisitor(new DomModelCreator(), "$document");
			smooks.addVisitor(new FreeMarkerTemplateProcessor(
					new TemplatingConfiguration(template)), "$document");
			smooks.setFilterSettings(new FilterSettings(StreamFilterType.DOM));
			
			
		
		
			// remove namespaces without prefix
			sourceXML = SmooksFMUtil.removeXmlStringNamespaceWithouthPrefix(sourceXML);
			StreamSource sourceStream = new StreamSource(
					new ByteArrayInputStream(sourceXML.getBytes("utf-8")));
	
			// SmooKs transformation
			smooks.filterSource(sourceStream, resultStream);
			
			
			prepareResponseFile(resp, resultStream.getWriter(), "result");
			
		
			}
			
		} catch (ParserConfigurationException e1) {
			
			e1.printStackTrace();
		}

	}

	
	private void prepareResponseFile(HttpServletResponse resp, Writer responseFile,
			String target) {
		try {

			ByteArrayInputStream b = new ByteArrayInputStream(responseFile.toString()
					.getBytes("UTF-8"));
			DataInputStream in = new DataInputStream(b);

			int length = 0;
			ServletOutputStream op = resp.getOutputStream();
			String mimetype = "text/xml";// "text/xml" "application/xml";
											// //context.getMimeType( in );

			//
			// Set the response and go!
			//
			//
			resp.setContentType((mimetype != null) ? mimetype
					: "application/octet-stream"); // "application/octet-stream"
			// resp.setContentLength( (int) (8192) ); 
			resp.setHeader("Content-Disposition", "attachment; filename=\""
					+ "transformation_result_" + target + ".xml" + "\"");

			//
			// Stream to the requester.
			//
			byte[] bbuf = new byte[8192];

			while ((in != null) && ((length = in.read(bbuf)) != -1)) {
				op.write(bbuf, 0, length);
			}

			in.close();
			op.flush();
			op.close();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Exception happening when processing key "
					+ "source.xml", e);
			throw new RuntimeException("Failed to process XML document", e);
		}
	}

}
