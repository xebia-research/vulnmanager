package com.xebia.vulnmanager.openvas;

import com.xebia.vulnmanager.openvas.objects.OpenvasReport;
import com.xebia.vulnmanager.openvas.objects.OvResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * The class NMapParser has functions for parsing a given document
 */

public class OpenvasParser {

    /**
     * Parse a openvas xml report
     *
     * @param openvasDoc Document of openvas report
     */
    public OpenvasReport getOpenvasReport(Document openvasDoc) {
        OpenvasReport retReport = new OpenvasReport();

        Element mainElement = openvasDoc.getDocumentElement();

        retReport.setTimeDone(mainElement.getElementsByTagName("creation_time").item(0).getTextContent());
        retReport.setId(mainElement.getAttribute("id"));

        NodeList reportList = mainElement.getElementsByTagName("report");
        Element reports = (Element) reportList.item(0);
        NodeList resultList = reports.getElementsByTagName("results").item(0).getChildNodes();

        for (int i = 0; i < resultList.getLength(); i++) {
            if (resultList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element result = (Element) resultList.item(i);
                retReport = handleResult(retReport, result);
            }
        }

        //System.out.println(retReport.toString());
        return retReport;
    }

    /**
     * Handle a result node everything between <result></result>
     * @param report The report with all the information
     * @param resultNode The node with the result tag
     * @return
     */
    private OpenvasReport handleResult(OpenvasReport report, Element resultNode) {
        //System.out.println(toString(resultNode, true, true));

        // Get specific information
        String port = resultNode.getElementsByTagName("port").item(0).getTextContent();
        String name = resultNode.getElementsByTagName("name").item(0).getTextContent();
        String description = resultNode.getElementsByTagName("description").item(0).getTextContent();
        String severity = resultNode.getElementsByTagName("severity").item(0).getTextContent();
        String threat = resultNode.getElementsByTagName("threat").item(0).getTextContent();

        // Set all the results in an object
        OvResult result = new OvResult();
        result.setPort(port);
        result.setName(name);
        result.setDescription(description);
        result.setSeverity(severity);
        result.setThreat(threat);

        // Add result to the report.
        report.addResult(result);
        return report;
    }

    /**
     * Turn a node into a string
     * @param node Node to set to a string
     * @param omitXmlDeclaration if the xml declartion needs to be omitted
     * @param prettyPrint if the string should use indentation
     * @return returns a string with xml representation of the node
     */
    public String toString(Node node, boolean omitXmlDeclaration, boolean prettyPrint) {
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
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }
}
