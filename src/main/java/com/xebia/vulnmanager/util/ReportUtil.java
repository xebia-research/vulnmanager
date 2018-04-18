package com.xebia.vulnmanager.util;

import com.xebia.vulnmanager.models.nmap.objects.NMapReport;
import com.xebia.vulnmanager.models.openvas.OpenvasParser;
import com.xebia.vulnmanager.models.nmap.NMapParser;
import com.xebia.vulnmanager.models.openvas.objects.OpenvasReport;
import com.xebia.vulnmanager.models.zap.ZapParser;
import com.xebia.vulnmanager.models.zap.objects.ZapReport;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReportUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger("ReportUtil");

    /**
     * This function parses a File to a Document, the document could be parsed
     *
     * @param parseFile The file that is converted to a Document variable.
     * @return The document that is going to be parsed.
     */
    public static Document getDocumentFromFile(File parseFile) {
        Document doc = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // Parse the file to a Document
            doc = factory.newDocumentBuilder().parse(parseFile);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            LOGGER.error(e.toString());
        }
        return doc;
    }

    /**
     * This function parses the given Document. The function checks by the document element, what kind of scan this is.
     * It could be a Nmap-, Openvas-, Clair and Owasp Zap scan.
     *
     * @param testReportDoc The Test document that has to be parsed.
     */
    public static Object parseDocument(Document testReportDoc) {
        String currentTypeOfScan = testReportDoc.getDocumentElement().getTagName();

        if (currentTypeOfScan.equalsIgnoreCase("report")) {
            // This function parses the given Document
            OpenvasParser parser = new OpenvasParser();
            return parser.getOpenvasReport(testReportDoc);
        } else if (currentTypeOfScan.equalsIgnoreCase("nmaprun")) {
            // This function parses the given Document
            NMapParser nMapParser = new NMapParser();
            return nMapParser.getNMapReport(testReportDoc);
        } else if (currentTypeOfScan.equalsIgnoreCase("OWASPZAPReport")) {
            // This function parses the given Document
            ZapParser zapParser = new ZapParser();
            return zapParser.getZapReport(testReportDoc);
        }
        return null;
    }

    /**
     * This function parses the given Document. The function checks by the document element, what kind of scan this is.
     * It could be a Nmap-, Openvas-, Clair and Owasp Zap scan.
     *
     * @param testReportDoc The Test document that has to be parsed.
     */
    public static ReportType checkDocumentType(Document testReportDoc) {
        String currentTypeOfScan = testReportDoc.getDocumentElement().getTagName();

        if (currentTypeOfScan != null) {
            if (currentTypeOfScan.equalsIgnoreCase("report")) {
                return ReportType.OPENVAS;
            } else if (currentTypeOfScan.equalsIgnoreCase("nmaprun")) {
                return ReportType.NMAP;
            } else if (currentTypeOfScan.equalsIgnoreCase("OWASPZAPReport")) {
                return ReportType.ZAP;
            }
        }
        return ReportType.UNKNOWN;
    }

    /**
     * Turn a node into a string
     *
     * @param node               Node to set to a string
     * @param omitXmlDeclaration if the xml declartion needs to be omitted
     * @param prettyPrint        if the string should use indentation
     * @return returns a string with xml representation of the node
     */
    public static String toString(Node node, boolean omitXmlDeclaration, boolean prettyPrint) {
        if (node == null) {
            throw new IllegalArgumentException("node is null.");
        }

        try {
            // Remove unwanted whitespaces
            node.normalize();
            XPath xpath = XPathFactory.newInstance().newXPath();
            XPathExpression expr = xpath.compile("//text()[normalize-space()='']");
            NodeList nodeList = (NodeList) expr.evaluate(node, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); ++i) {
                Node nd = nodeList.item(i);
                nd.getParentNode().removeChild(nd);
            }

            // Create and setup transformer
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            if (omitXmlDeclaration) {
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            }

            if (prettyPrint) {
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            }

            // Turn the node into a string
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(node), new StreamResult(writer));
            return writer.toString();
        } catch (TransformerException | XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    public static OpenvasReport getOpenvasReportFromObject(Object parsedDocument) throws ClassCastException {
        try {
            if (!(parsedDocument instanceof OpenvasReport)) {
                throw new ClassCastException("Object was not of type OpenvasReport");
            }
        } catch (ClassCastException exception) {
            LOGGER.error(exception.getMessage());
            return null;
        }

        return (OpenvasReport) parsedDocument;
    }

    public static NMapReport getNMapReportFromObject(Object parsedDocument) throws ClassCastException {
        try {
            if (!(parsedDocument instanceof NMapReport)) {
                throw new ClassCastException("Object was not of type NMapReport");
            }
        } catch (ClassCastException exception) {
            LOGGER.error(exception.getMessage());
            return null;
        }
        return (NMapReport) parsedDocument;
    }

    public static ZapReport getZapReportFromObject(Object parsedDocument) throws ClassCastException {
        try {
            if (!(parsedDocument instanceof ZapReport)) {
                throw new ClassCastException("Object was not of type ZapReport");
            }
        } catch (ClassCastException exception) {
            LOGGER.error(exception.getMessage());
            return null;
        }
        return (ZapReport) parsedDocument;
    }
}
