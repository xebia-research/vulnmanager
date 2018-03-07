package com.xebia.vulnmanager.nmap;

import com.xebia.vulnmanager.nmap.objects.HostDetails;
import com.xebia.vulnmanager.nmap.objects.AddressDetails;
import com.xebia.vulnmanager.nmap.objects.StateDetails;
import com.xebia.vulnmanager.nmap.objects.HostPorts;
import com.xebia.vulnmanager.nmap.objects.ServiceDetails;
import com.xebia.vulnmanager.nmap.objects.TimingData;
import com.xebia.vulnmanager.nmap.objects.ExtraReason;
import com.xebia.vulnmanager.nmap.objects.HostNamesDetails;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * The class NMapParser has functions for parsing a given document
 */

public class NMapParser {
    private static final String PARSER_LITERAL_STATE = "state";
    private static final String PARSER_LITERAL_HOST = "host";
    private static final String PARSER_LITERAL_START_TIME = "starttime";
    private static final String PARSER_LITERAL_STATUS = "status";
    private static final String PARSER_LITERAL_ADDRESS = "address";
    private static final String PARSER_LITERAL_HOST_NAMES = "hostnames";
    private static final String PARSER_LITERAL_PORTS = "ports";
    private static final String PARSER_LITERAL_TIMES = "times";
    private static final String PARSER_LITERAL_REASON = "reason";
    private static final String PARSER_LITERAL_REASON_TTL = "reason_ttl";
    private static final String PARSER_LITERAL_NAME = "name";
    private static final String PARSER_LITERAL_ADDR = "addr";
    private static final String PARSER_LITERAL_ADDR_TYPE = "addrtype";
    private static final String PARSER_LITERAL_HOST_NAME = "hostname";
    private static final String PARSER_LITERAL_TYPE = "type";
    private static final String PARSER_LITERAL_PORT = "port";
    private static final String PARSER_LITERAL_EXTRA_PORTS = "extraports";
    private static final String PARSER_LITERAL_PROTOCOL = "protocol";
    private static final String PARSER_LITERAL_PORTID = "portid";
    private static final String PARSER_LITERAL_SERVICE = "service";
    private static final String PARSER_LITERAL_METHOD = "method";
    private static final String PARSER_LITERAL_CONF = "conf";
    private static final String PARSER_LITERAL_COUNT = "count";
    private static final String PARSER_LITERAL_EXTRA_REASONS = "extrareasons";
    private static final String PARSER_LITERAL_SRTT = "srtt";
    private static final String PARSER_LITERAL_RTTVAR = "rttvar";
    private static final String PARSER_LITERAL_TO = "to";

    public static void parseNMapDocument(Document nMapDoc) {
//        getReportData(nMapDoc);
//        List<HostDetails> hosts = getHostsFromDocument(nMapDoc);
//        NMapReport nMapReport = new NMapReport();

    }

    private static void getReportData(Document nMapDoc) {

    }

    /**
     * All the hosts are extracted from the NMap report and saved in a list of hosts
     *
     * @param nMapDoc Document of NMap report
     */
    private static List<HostDetails> getHostsFromDocument(Document nMapDoc) {
        NodeList hostList = nMapDoc.getElementsByTagName(PARSER_LITERAL_HOST);

        List<HostDetails> allHostDetails = new ArrayList<HostDetails>();
        // Todo: Change the for loops to streams
        for (int i = 0; i < hostList.getLength(); i++) {
            Node hostNode = hostList.item(i);
            if (hostNode.getAttributes().getNamedItem(PARSER_LITERAL_START_TIME) != null) {
                NodeList hostDataList = hostNode.getChildNodes();

                StateDetails stateDetails = null;
                AddressDetails addressDetails = null;
                HostNamesDetails hostNamesDetails = null;
                HostPorts hostPorts = null;
                TimingData timingData = null;

                for (int x = 0; x < hostDataList.getLength(); x++) {
                    Node currentHostDetail = hostDataList.item(x);

                    NamedNodeMap currentChildAttributes = currentHostDetail.getAttributes();
                    String currentNodeName = currentHostDetail.getNodeName();

                    if (currentNodeName.equals(PARSER_LITERAL_STATUS)) {
                        stateDetails = getStatusDetails(currentChildAttributes);
                    } else if (currentNodeName.equals(PARSER_LITERAL_ADDRESS)) {
                        addressDetails = getAddressDetails(currentChildAttributes);
                    } else if (currentNodeName.equals(PARSER_LITERAL_HOST_NAMES)) {
                        hostNamesDetails = getHostNamesDetails(currentHostDetail.getChildNodes());
                    } else if (currentNodeName.equals(PARSER_LITERAL_PORTS)) {
                        hostPorts = getPortDetails(currentHostDetail.getChildNodes());
                    } else if (currentNodeName.equals(PARSER_LITERAL_TIMES)) {
                        timingData = getTimingData(currentChildAttributes);
                    }
                }
                allHostDetails.add(new HostDetails(stateDetails, addressDetails, hostNamesDetails, hostPorts, timingData));
            }
        }
        return allHostDetails;
    }

    /**
     * Add the status details of a hostDetails object
     *
     * @param statusAttributes The details of the status of a host.
     * @return The updated hostDetails are returned.
     */
    private static StateDetails getStatusDetails(NamedNodeMap statusAttributes) {
        String state = statusAttributes.getNamedItem(PARSER_LITERAL_STATE).getNodeValue();
        String reason = statusAttributes.getNamedItem(PARSER_LITERAL_REASON).getNodeValue();
        String reasonTtl = statusAttributes.getNamedItem(PARSER_LITERAL_REASON_TTL).getNodeValue();

        return new StateDetails(state, reason, reasonTtl);
    }

    /**
     * Add the address details of a hostDetails object
     *
     * @param addressAttributes The details of the address of a host.
     * @return The updated hostDetails are returned.
     */
    private static AddressDetails getAddressDetails(NamedNodeMap addressAttributes) {
        String address = addressAttributes.getNamedItem(PARSER_LITERAL_ADDR).getNodeValue();
        String addressType = addressAttributes.getNamedItem(PARSER_LITERAL_ADDR_TYPE).getNodeValue();

        return new AddressDetails(address, addressType);
    }

    private static HostNamesDetails getHostNamesDetails(NodeList hostNamesList) {
        List<HostNamesDetails.HostNameDetails> hostNameDetailsList = new ArrayList<HostNamesDetails.HostNameDetails>();

        for (int x = 0; x < hostNamesList.getLength(); x++) {
            if (hostNamesList.item(x).getNodeName().equals(PARSER_LITERAL_HOST_NAME)) {
                NamedNodeMap hostNameNode = hostNamesList.item(x).getAttributes();

                String hostName = hostNameNode.getNamedItem(PARSER_LITERAL_NAME).getNodeValue();
                String hostType = hostNameNode.getNamedItem(PARSER_LITERAL_TYPE).getNodeValue();
                hostNameDetailsList.add(new HostNamesDetails.HostNameDetails(hostName, hostType));
            }
        }

        return new HostNamesDetails(hostNameDetailsList);
    }

    private static HostPorts getPortDetails(NodeList portsAttributes) {
        List<HostPorts.Port> hostPortsDetailsList = new ArrayList<HostPorts.Port>();
        List<HostPorts.ExtraPort> extraPortsDetailsList = new ArrayList<HostPorts.ExtraPort>();

        for (int x = 0; x < portsAttributes.getLength(); x++) {
            Node currentNode = portsAttributes.item(x);

            if (currentNode.getNodeName().equals(PARSER_LITERAL_PORT)) {
                HostPorts.Port currentPort = getPort(currentNode);
                hostPortsDetailsList.add(currentPort);

            } else if (currentNode.getNodeName().equals(PARSER_LITERAL_EXTRA_PORTS)) {
                HostPorts.ExtraPort extraPort = getExtraPorts(currentNode);
                extraPortsDetailsList.add(extraPort);
            }
        }

        return new HostPorts(hostPortsDetailsList, extraPortsDetailsList);
    }

    private static HostPorts.Port getPort(Node currentNode) {
        NamedNodeMap portAttributesNode = currentNode.getAttributes();

        String protocol = portAttributesNode.getNamedItem(PARSER_LITERAL_PROTOCOL).getNodeValue();
        String portId = portAttributesNode.getNamedItem(PARSER_LITERAL_PORTID).getNodeValue();

        StateDetails stateDetails = null;
        ServiceDetails serviceDetails = null;

        NodeList portChildNodes = currentNode.getChildNodes();
        for (int i = 0; i < portChildNodes.getLength(); i++) {
            Node currentChildNode = portChildNodes.item(i);

            NamedNodeMap portAttributes = currentChildNode.getAttributes();
            if (currentChildNode.getNodeName().equals(PARSER_LITERAL_STATE)) {
                stateDetails = getStateDetails(portAttributes);
            } else if (currentChildNode.getNodeName().equals(PARSER_LITERAL_SERVICE)) {
                serviceDetails = getServiceDetails(portAttributes);
            }
        }

        return new HostPorts.Port(protocol, portId, stateDetails, serviceDetails);
    }

    private static StateDetails getStateDetails(NamedNodeMap stateDetails) {
        String state = stateDetails.getNamedItem(PARSER_LITERAL_STATE).getNodeValue();
        String reason = stateDetails.getNamedItem(PARSER_LITERAL_REASON).getNodeValue();
        String reasonTtl = stateDetails.getNamedItem(PARSER_LITERAL_REASON_TTL).getNodeValue();
        return new StateDetails(state, reason, reasonTtl);
    }

    private static ServiceDetails getServiceDetails(NamedNodeMap serviceDetails) {
        String serviceName = serviceDetails.getNamedItem(PARSER_LITERAL_NAME).getNodeValue();
        String serviceMethod = serviceDetails.getNamedItem(PARSER_LITERAL_METHOD).getNodeValue();
        String serviceConf = serviceDetails.getNamedItem(PARSER_LITERAL_CONF).getNodeValue();
        return new ServiceDetails(serviceName, serviceMethod, serviceConf);
    }

    private static HostPorts.ExtraPort getExtraPorts(Node currentNode) {
        NamedNodeMap extraPortAttributes = currentNode.getAttributes();

        String state = extraPortAttributes.getNamedItem(PARSER_LITERAL_STATE).getNodeValue();
        String count = extraPortAttributes.getNamedItem(PARSER_LITERAL_COUNT).getNodeValue();

        ExtraReason extraReason = null;
        NodeList extraPortNodes = currentNode.getChildNodes();
        for (int i = 0; i < extraPortNodes.getLength(); i++) {
            Node extraReasonNode = extraPortNodes.item(i);
            if (extraReasonNode != null && extraReasonNode.getNodeName().equals(PARSER_LITERAL_EXTRA_REASONS)) {
                String reason = extraReasonNode.getAttributes().getNamedItem(PARSER_LITERAL_REASON).getNodeValue();
                String extraCount = extraReasonNode.getAttributes().getNamedItem(PARSER_LITERAL_COUNT).getNodeValue();
                extraReason = new ExtraReason(reason, extraCount);
            }
        }
        return new HostPorts.ExtraPort(state, count, extraReason);
    }

    private static TimingData getTimingData(NamedNodeMap timesDetails) {
        String smoothedRoundTripTime = timesDetails.getNamedItem(PARSER_LITERAL_SRTT).getNodeValue();
        String roundTripTimeVariance = timesDetails.getNamedItem(PARSER_LITERAL_RTTVAR).getNodeValue();
        String probeTimeout = timesDetails.getNamedItem(PARSER_LITERAL_TO).getNodeValue();

        return new TimingData(smoothedRoundTripTime, roundTripTimeVariance, probeTimeout);
    }
}
