package com.xebia.vulnmanager.util;

import com.xebia.vulnmanager.models.clair.ClairParser;
import com.xebia.vulnmanager.models.clair.objects.ClairReport;
import com.xebia.vulnmanager.models.nmap.NMapParser;
import com.xebia.vulnmanager.models.nmap.objects.NMapReport;
import com.xebia.vulnmanager.models.openvas.OpenvasParser;
import com.xebia.vulnmanager.models.openvas.objects.OpenvasReport;
import com.xebia.vulnmanager.models.zap.ZapParser;
import com.xebia.vulnmanager.models.zap.objects.ZapReport;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Iterator;

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
            String fileString = getStringFromFile(parseFile);
            // This does not start with '<', so check if it is json and parse that to xml and then to Document object
            if (!fileString.startsWith("<")) {
                JSONObject jsonObject = new JSONObject(fileString);
                String xmlString = jsonToXmlString(jsonObject);

                doc = stringToDom(xmlString);
            } else {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

                // NMAP has a doctype, almost every XEE attack are from the doctype, so we remove the doctype
                if (fileString.equalsIgnoreCase("<nmaprun scanner=\"nmap\"")) {
                    parseFile = removeDoctypeFromNmapFile(fileString);
                }

                String feature = null;
                // This is the PRIMARY defense. If DTDs (doctypes) are disallowed, almost all XML entity attacks are prevented
                // Xerces 2 only - http://xerces.apache.org/xerces2-j/features.html#disallow-doctype-decl
                feature = "http://apache.org/xml/features/disallow-doctype-decl";
                factory.setFeature(feature, true);

                // If you can't completely disable DTDs, then at least do the following:
                // Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-general-entities
                // Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-general-entities
                // JDK7+ - http://xml.org/sax/features/external-general-entities
                feature = "http://xml.org/sax/features/external-general-entities";
                factory.setFeature(feature, false);

                // Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-parameter-entities
                // Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-parameter-entities
                // JDK7+ - http://xml.org/sax/features/external-parameter-entities
                feature = "http://xml.org/sax/features/external-parameter-entities";
                factory.setFeature(feature, false);

                // Disable external DTDs as well
                feature = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
                factory.setFeature(feature, false);

                // and these as well, per Timothy Morgan's 2014 paper: "XML Schema, DTD, and Entity Attacks"
                factory.setXIncludeAware(false);
                factory.setExpandEntityReferences(false);

                // Parse the file to a Document
                doc = factory.newDocumentBuilder().parse(parseFile);
            }
        } catch (SAXException e) {
            LOGGER.error(e.toString());
        } catch (IOException e) {
            LOGGER.error(e.toString());
        } catch (NullPointerException e) {
            LOGGER.error(e.toString());
        } catch (ParserConfigurationException e) {
            LOGGER.error(e.toString());
        }

        return doc;
    }

    private static String getStringFromFile(File parseFile) throws IOException {
        return FileUtils.readFileToString(parseFile, "UTF-8");
    }

    private static String jsonToXmlString(JSONObject jsonObject) {
        Iterator<String> jsonKeys = jsonObject.keys();
        String xmlRoot = getXmlRootElementName(jsonKeys);

        if (xmlRoot == null) {
            return null;
        }

        // JsonObject to XML string
        String xml = XML.toString(jsonObject);
        StringBuilder stringBuilder = new StringBuilder();
        // Add rootElement to xml string, this does XML toString not add
        xml = stringBuilder
                .append("<?xml version=\"1.0\"?>")
                .append("<").append(xmlRoot).append(">")
                .append(xml)
                .append("</").append(xmlRoot).append(">")
                .toString();
        return xml;
    }

    private static String getXmlRootElementName(Iterator<String> jsonKeys) {
        boolean isCorrectScanner = true;

        String[] clairJsonKeys = {"image", "unapproved", "vulnerabilities"};
        while (jsonKeys.hasNext()) {
            String currentKey = jsonKeys.next();
            if (!Arrays.asList(clairJsonKeys).contains(currentKey)) {
                isCorrectScanner = false;
                break;
            }
        }

        if (isCorrectScanner) {
            return "ClairReport";
        }

        return null;
    }

    private static Document stringToDom(String xmlString) throws ParserConfigurationException, IOException, SAXException, NullPointerException {
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(xmlString));

        return db.parse(is);
    }

    private static File removeDoctypeFromNmapFile(String fileString) throws IOException {
        fileString = fileString.replaceFirst("<!DOCTYPE[^>\\[]*(\\[[^]]*])?>", "");
        fileString = fileString.replaceFirst("<\\?xml-stylesheet[^>\\[]*(\\\\[[^]]*])?>", "");
        File parseFile = new File("");
        FileUtils.writeStringToFile(parseFile, fileString, "UTF-8");

        return parseFile;
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
        } else if (currentTypeOfScan.equalsIgnoreCase("ClairReport")) {
            // This function parses the given Document
            ClairParser clairParser = new ClairParser();
            return clairParser.getClairReport(testReportDoc);
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
        if (testReportDoc == null || testReportDoc.getDocumentElement() == null) {
            return ReportType.UNKNOWN;
        }
        String currentTypeOfScan = testReportDoc.getDocumentElement().getTagName();

        if (currentTypeOfScan != null) {
            if (currentTypeOfScan.equalsIgnoreCase("report")) {
                return ReportType.OPENVAS;
            } else if (currentTypeOfScan.equalsIgnoreCase("nmaprun")) {
                return ReportType.NMAP;
            } else if (currentTypeOfScan.equalsIgnoreCase("OWASPZAPReport")) {
                return ReportType.ZAP;
            } else if (currentTypeOfScan.equalsIgnoreCase("ClairReport")) {
                return ReportType.CLAIR;
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

    public static ClairReport getClairReportFromObject(Object parsedDocument) throws ClassCastException {
        try {
            if (!(parsedDocument instanceof ClairReport)) {
                throw new ClassCastException("Object was not of type ClairReport");
            }
        } catch (ClassCastException exception) {
            LOGGER.error(exception.getMessage());
            return null;
        }
        return (ClairReport) parsedDocument;
    }
}
