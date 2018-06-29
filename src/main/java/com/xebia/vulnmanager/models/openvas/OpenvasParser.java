package com.xebia.vulnmanager.models.openvas;

import com.xebia.vulnmanager.models.openvas.objects.NetworkVulnerabilityTest;
import com.xebia.vulnmanager.models.openvas.objects.OpenvasReport;
import com.xebia.vulnmanager.models.openvas.objects.OvResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
        retReport.setFileId(mainElement.getAttribute("id"));

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
        result.setResultId(report);

        // Get a report
        Element nvtNode = (Element) resultNode.getElementsByTagName("nvt").item(0);
        NetworkVulnerabilityTest nvt = getNvtFromNode(result, nvtNode);

        result.setNvt(nvt);

        // Add result to the report.
        report.addResult(result);
        return report;
    }

    /**
     * Get a Network Vulnerability test from a nvt node <nvt></nvt>
     * @param nvtNode The xml Element containing <nvt></nvt>
     * @return A Network Vulnerability Test from a xml node to represent it in code
     */
    private NetworkVulnerabilityTest getNvtFromNode(OvResult result, Element nvtNode) {
        NetworkVulnerabilityTest retNvt = new NetworkVulnerabilityTest();
        String type = nvtNode.getElementsByTagName("type").item(0).getTextContent();
        String name = nvtNode.getElementsByTagName("name").item(0).getTextContent();
        String family = nvtNode.getElementsByTagName("family").item(0).getTextContent();
        String cvssBase = nvtNode.getElementsByTagName("cvss_base").item(0).getTextContent();
        String cve = nvtNode.getElementsByTagName("cve").item(0).getTextContent();
        String tags = nvtNode.getElementsByTagName("tags").item(0).getTextContent();

        String[] tagsSplit = tags.split("\\|");
        for (String tag : tagsSplit) {
            if (tag.split("=").length > 0) {
                String[] res = tag.split("=");
                switch (res[0]) {
                    case "summary":
                        retNvt.setTagsSummary(res[1]);
                        break;
                    case "vuldetect":
                        retNvt.setTagsVulDetect(res[1]);
                        break;
                    case "solution":
                        retNvt.setTagsSolution(res[1]);
                        break;
                    case "affected":
                        retNvt.setTagsAffected(res[1]);
                        break;
                    case "insight":
                        retNvt.setTagsInsight(res[1]);
                        break;
                    case "impact":
                        retNvt.setTagsImpact(res[1]);
                        break;
                    case "solution_type":
                        retNvt.setTagsSolutionType(res[1]);
                        break;
                    case "qod_type":
                        retNvt.setTagsQodType(res[1]);
                        break;
                    default:
                        break;
                }
            }
        }

        retNvt.setTags(tags);
        retNvt.setType(type);
        retNvt.setCve(cve);
        retNvt.setCvssBase(cvssBase);
        retNvt.setFamily(family);
        retNvt.setName(name);
        retNvt.setResult(result);

        return retNvt;
    }
}
