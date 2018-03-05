package com.xebia.vulnmanager.util;

import com.xebia.vulnmanager.openvas.OpenvasParser;
import com.xebia.vulnmanager.openvas.objects.OpenvasReport;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReportUtil {
    private static final Logger LOGGER = Logger.getLogger("ReportUtil");
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
        } catch (SAXException e) {
            LOGGER.log(Level.FINE, e.toString());
        } catch (IOException e) {
            LOGGER.log(Level.FINE, e.toString());
        } catch (ParserConfigurationException e) {
            LOGGER.log(Level.FINE, e.toString());
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

}
