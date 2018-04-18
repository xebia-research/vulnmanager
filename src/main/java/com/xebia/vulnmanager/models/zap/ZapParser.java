package com.xebia.vulnmanager.models.zap;

import com.xebia.vulnmanager.models.zap.objects.RiskInstance;
import com.xebia.vulnmanager.models.zap.objects.ScannedSiteInformation;
import com.xebia.vulnmanager.models.zap.objects.ZapAlertItem;
import com.xebia.vulnmanager.models.zap.objects.ZapReport;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class ZapParser {
    private ZapReport zapReport = new ZapReport();

    public ZapReport getZapReport(Document zapDocument) {
        setDateCreatedInformation(zapDocument);
        setSitesInformation(zapDocument);

        return zapReport;
    }

    private void setDateCreatedInformation(Document zapDocument) {
        String dateGenerated = zapDocument.getElementsByTagName("OWASPZAPReport").item(0).getAttributes().getNamedItem("generated").getNodeValue();
        zapReport.setDateGenerated(dateGenerated);
    }

    private void setSitesInformation(Document zapDocument) {
        List<ScannedSiteInformation> sitesInfo = new ArrayList<>();
        NodeList nodeListOfSites = zapDocument.getElementsByTagName("site");
        for (int x = 0; x < nodeListOfSites.getLength(); x++) {
            Node currentSiteNode = nodeListOfSites.item(x);

            ScannedSiteInformation siteInformation = new ScannedSiteInformation();

            siteInformation.setName(currentSiteNode.getAttributes().getNamedItem("name").getNodeValue());
            siteInformation.setHost(currentSiteNode.getAttributes().getNamedItem("host").getNodeValue());
            siteInformation.setPort(Integer.parseInt(currentSiteNode.getAttributes().getNamedItem("port").getNodeValue()));
            siteInformation.setSsl(Boolean.parseBoolean(currentSiteNode.getAttributes().getNamedItem("ssl").getNodeValue()));

            setAlertItemsOfSite(currentSiteNode.getChildNodes(), siteInformation);

            sitesInfo.add(siteInformation);
        }
        zapReport.setNumberOfSitesInReport(sitesInfo.size());
        zapReport.setScannedSitesInformation(sitesInfo);
    }

    private void setAlertItemsOfSite(NodeList siteInformationNodeList, ScannedSiteInformation scannedSiteInformation) {
        for (int i = 0; i < siteInformationNodeList.getLength(); i++) {
            NodeList nodeList = siteInformationNodeList.item(i).getChildNodes();

            if (siteInformationNodeList.item(i).getNodeName().equals("alerts")) {

                List<ZapAlertItem> alertItemsList = new ArrayList<>();
                for (int x = 0; x < nodeList.getLength(); x++) {
                    NodeList alertItemNodeList = nodeList.item(i).getChildNodes();

                    if (nodeList.item(i).getNodeName().equals("alertitem")) {
                        alertItemsList.add(getAlarmItem(alertItemNodeList));
                    }
                }
                scannedSiteInformation.setAlertItems(alertItemsList);
            }
        }
    }

    private ZapAlertItem getAlarmItem(NodeList alertItemNodeList) {
        ZapAlertItem zapAlertItem = new ZapAlertItem();
        for (int x = 0; x < alertItemNodeList.getLength(); x++) {
            String currentAttributeName = alertItemNodeList.item(x).getNodeName();
            NodeList currentAlertItemAttributes = alertItemNodeList.item(x).getChildNodes();

            if (currentAlertItemAttributes.getLength() == 0) {
                continue;
            }
            String attributeValue = currentAlertItemAttributes.item(0).getNodeValue();
            switch (currentAttributeName) {
                case "name":
                    zapAlertItem.setName(attributeValue);
                    break;
                case "riskcode":
                    zapAlertItem.setRiskCode(Integer.parseInt(attributeValue));
                    break;
                case "confidence":
                    zapAlertItem.setConfidence(Integer.parseInt(attributeValue));
                    break;
                case "riskdesc":
                    zapAlertItem.setShortDescription(attributeValue);
                    break;
                case "desc":
                    attributeValue = removeHtmlAndWhitespace(attributeValue);
                    zapAlertItem.setDescription(attributeValue);
                    break;
                case "instances":
                    zapAlertItem.setInstanceList(getInstanceList(currentAlertItemAttributes));
                    break;
                case "count":
                    zapAlertItem.setInstanceCount(Integer.parseInt(attributeValue));
                    break;
                case "solution":
                    attributeValue = removeHtmlAndWhitespace(attributeValue);
                    zapAlertItem.setSolution(attributeValue);
                    break;
                case "otherinfo":
                    attributeValue = removeHtmlAndWhitespace(attributeValue);
                    zapAlertItem.setOtherInfo(attributeValue);
                    break;
                case "reference":
                    attributeValue = removeHtmlAndWhitespace(attributeValue);
                    zapAlertItem.setReference(attributeValue);
                    break;
                case "cweid":
                    zapAlertItem.setCweId(Integer.parseInt(attributeValue));
                    break;
                case "wascid":
                    zapAlertItem.setWascId(Integer.parseInt(attributeValue));
                    break;
                case "sourceid":
                    zapAlertItem.setSourceId(Integer.parseInt(attributeValue));
                    break;
                default:
                    break;
            }
        }
        return zapAlertItem;
    }

    private String removeHtmlAndWhitespace(String htmlString) {
        // Remove HTML code
        htmlString = htmlString.replaceAll("<.*?>", "");
        // Remove multiple whitespace with one whitespace
        htmlString = htmlString.replaceAll("\\s{2,}", " ").trim();
        return htmlString;
    }

    private List<RiskInstance> getInstanceList(NodeList riskInstanceNodeList) {
        List<RiskInstance> instanceList = new ArrayList<>();
        for (int i = 0; i < riskInstanceNodeList.getLength(); i++) {
            Node riskInstanceNode = riskInstanceNodeList.item(i);
            String nodeName = riskInstanceNode.getNodeName();

            if (nodeName.equals("instance")) {
                instanceList.add(getRiskInstance(riskInstanceNode));
            }
        }
        return instanceList;
    }

    private RiskInstance getRiskInstance(Node riskInstanceNode) {
        RiskInstance riskInstance = new RiskInstance();
        for (int x = 0; x < riskInstanceNode.getChildNodes().getLength(); x++) {
            Node instanceAttribute = riskInstanceNode.getChildNodes().item(x);
            String riskAttributeName = instanceAttribute.getNodeName();
            Node riskAttributeValue = instanceAttribute.getChildNodes().item(0);

            switch (riskAttributeName) {
                case "uri":
                    // Trim is for removing whitespace
                    riskInstance.setUri(riskAttributeValue.getNodeValue().trim());
                    break;
                case "method":
                    // Trim is for removing whitespace
                    riskInstance.setMethod(riskAttributeValue.getNodeValue().trim());
                    break;
                case "param":
                    // Trim is for removing whitespace
                    riskInstance.setParam(riskAttributeValue.getNodeValue().trim());
                    break;
                default:
                    break;
            }
        }
        return riskInstance;
    }
}
