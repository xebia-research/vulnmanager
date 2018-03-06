package com.xebia.vulnmanager;

import com.xebia.vulnmanager.nmap.NMapParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@EnableAutoConfiguration
public class HelloWorldController {
    private static final Logger LOGGER = Logger.getLogger("HelloWorldController");

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    String home() {
        return "This is the home screen. You can go to an id, and you can also go to ping.";
    }

    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    @ResponseBody
    String ping() {
        return "pong";
    }

    // {} is to get a integer as an id in this case
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    // With PathVariable you can get a specific variable in this case id
    String test(@PathVariable("id") int id) throws IOException {
        return "The chosen id is:" + id;
    }

    /**
     * This function parses a File to a Document, the document could be parsed
     *
     * @param parseFile The file that is converted to a Document variable.
     * @return The document that is going to be parsed.
     */
    private static Document getDocumentFromFile(File parseFile) {
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
    private static void parseDocument(Document testReportDoc) {
        String currentTypeOfScan = testReportDoc.getDocumentElement().getTagName();

        if (currentTypeOfScan.equals("nmaprun")) {
            // This function parses the given Document
            NMapParser.parseNMapDocument(testReportDoc);
        }
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(HelloWorldController.class, args);

        Document currentDoc = getDocumentFromFile(new File("exampleFiles/nmap.xml"));
        parseDocument(currentDoc);
    }
}
