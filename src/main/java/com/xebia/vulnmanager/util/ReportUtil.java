package com.xebia.vulnmanager.util;

import com.xebia.vulnmanager.models.openvas.OpenvasParser;
import com.xebia.vulnmanager.models.openvas.objects.OpenvasReport;
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
    public static OpenvasReport parseDocument(Document testReportDoc) {
        String currentTypeOfScan = testReportDoc.getDocumentElement().getTagName();

        if (currentTypeOfScan.equals("report")) {
            // This function parses the given Document
            OpenvasParser parser = new OpenvasParser();
            return parser.getOpenvasReport(testReportDoc);
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

        if (currentTypeOfScan.equals("report")) {
            return ReportType.OPENVAS;
        }
        return ReportType.UNKNOWN;
    }

    /**
     * Turn a node into a string
     * @param node Node to set to a string
     * @param omitXmlDeclaration if the xml declartion needs to be omitted
     * @param prettyPrint if the string should use indentation
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

}
