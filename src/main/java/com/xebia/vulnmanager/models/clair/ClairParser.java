package com.xebia.vulnmanager.models.clair;

import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.xebia.vulnmanager.models.clair.objects.ClairReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringWriter;

public class ClairParser {
    private final Logger logger = LoggerFactory.getLogger("ClairParser");

    public ClairReport getClairReport(Document clairDocument) {
        String clairString;
        try {
            clairString = documentToString(clairDocument);
        } catch (TransformerException e) {
            logger.error(e.getMessage());
            return null;
        }

        ClairReport clairReport;
        try {
            JacksonXmlModule module = new JacksonXmlModule();
            module.setDefaultUseWrapper(false);
            clairReport = new XmlMapper(module).readValue(clairString, ClairReport.class);
        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        }

        return clairReport;
    }

    private String documentToString(Document clairDocument) throws TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();

        // Do not add an xml declaration, that is already done.
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        // Method used for output
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        // Transformer may add extra indents
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        // Preferred character encoding
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

        StringWriter sw = new StringWriter();
        // Transform the xml code to a string writer.
        transformer.transform(new DOMSource(clairDocument), new StreamResult(sw));
        return sw.toString();
    }
}
