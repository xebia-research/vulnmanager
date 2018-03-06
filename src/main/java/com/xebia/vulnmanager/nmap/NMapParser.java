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
//        NMapReport nMapReport = new NMapReport();

//        nMapReport.setReportData(getReportData(nMapDoc));
//        nMapReport.setHostDetails(getHostsFromDocument(nMapDoc));
        getReportData(nMapDoc);
        getHostsFromDocument(nMapDoc);
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
                HostDetails hostDetails = new HostDetails();

                for (int x = 0; x < hostDataList.getLength(); x++) {
                    Node currentHostDetail = hostDataList.item(x);
                    hostDetails = updateHostDetails(hostDetails, currentHostDetail);
                }
                allHostDetails.add(hostDetails);
            }
        }
        return allHostDetails;
    }

    /**
     * The current Host is updated with details from the currentHostDetail.
     * There could be a different details in a current host detail,
     * so in this function also the current detail is filtered.
     *
     * @param hostDetails       The details of one host, that are updated
     * @param currentHostDetail The current host detail is one detail of one host.
     *                          With this data the current HostDetails are updated.
     * @return The updated hostDetails are returned
     */
    private static HostDetails updateHostDetails(HostDetails hostDetails, Node currentHostDetail) {
        NamedNodeMap currentChildAttributes = currentHostDetail.getAttributes();
        String currentNodeName = currentHostDetail.getNodeName();

        if (currentNodeName.equals(PARSER_LITERAL_STATUS)) {
            hostDetails = updateStatusDetails(hostDetails, currentChildAttributes);
        }

        if (currentNodeName.equals(PARSER_LITERAL_ADDRESS)) {
            hostDetails = updateAddressDetails(hostDetails, currentChildAttributes);
        }

        if (currentNodeName.equals(PARSER_LITERAL_HOST_NAMES)) {
            hostDetails = updateHostNamesDetails(hostDetails, currentHostDetail.getChildNodes());
        }

        if (currentNodeName.equals(PARSER_LITERAL_PORTS)) {
            hostDetails = updatePortDetails(hostDetails, currentHostDetail.getChildNodes());
        }

        if (currentNodeName.equals(PARSER_LITERAL_TIMES)) {
            hostDetails = updateTimingData(hostDetails, currentChildAttributes);
        }
        return hostDetails;
    }

    /**
     * Add the status details of a hostDetails object
     *
     * @param hostDetails      Host detail where status details are added.
     * @param statusAttributes The details of the status of a host.
     * @return The updated hostDetails are returned.
     */
    private static HostDetails updateStatusDetails(HostDetails hostDetails, NamedNodeMap statusAttributes) {
        String state = statusAttributes.getNamedItem(PARSER_LITERAL_STATE).getNodeValue();
        String reason = statusAttributes.getNamedItem(PARSER_LITERAL_REASON).getNodeValue();
        String reasonTtl = statusAttributes.getNamedItem(PARSER_LITERAL_REASON_TTL).getNodeValue();

        StateDetails stateDetails = new StateDetails(state, reason, reasonTtl);
        hostDetails.setStateDetails(stateDetails);
        return hostDetails;
    }

    /**
     * Add the address details of a hostDetails object
     *
     * @param hostDetails       Host detail where address details are added.
     * @param addressAttributes The details of the address of a host.
     * @return The updated hostDetails are returned.
     */
    private static HostDetails updateAddressDetails(HostDetails hostDetails, NamedNodeMap addressAttributes) {
        String address = addressAttributes.getNamedItem(PARSER_LITERAL_ADDR).getNodeValue();
        String addressType = addressAttributes.getNamedItem(PARSER_LITERAL_ADDR_TYPE).getNodeValue();

        AddressDetails addressDetails = new AddressDetails(address, addressType);
        hostDetails.setAddressDetails(addressDetails);

        return hostDetails;
    }

    private static HostDetails updateHostNamesDetails(HostDetails hostDetails, NodeList hostNamesList) {
        List<HostNamesDetails.HostNameDetails> hostNameDetailsList = new ArrayList<HostNamesDetails.HostNameDetails>();

        for (int x = 0; x < hostNamesList.getLength(); x++) {
            if (hostNamesList.item(x).getNodeName().equals(PARSER_LITERAL_HOST_NAME)) {
                NamedNodeMap hostNameNode = hostNamesList.item(x).getAttributes();

                String hostName = hostNameNode.getNamedItem(PARSER_LITERAL_NAME).getNodeValue();
                String hostType = hostNameNode.getNamedItem(PARSER_LITERAL_TYPE).getNodeValue();
                hostNameDetailsList.add(new HostNamesDetails.HostNameDetails(hostName, hostType));
            }
        }

        HostNamesDetails hostNamesDetails = new HostNamesDetails(hostNameDetailsList);
        hostDetails.setHostNamesDetails(hostNamesDetails);
        return hostDetails;
    }

    private static HostDetails updatePortDetails(HostDetails hostDetails, NodeList portsAttributes) {
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
        HostPorts hostPorts = new HostPorts();
        hostPorts.setPorts(hostPortsDetailsList);
        hostPorts.setExtraPorts(extraPortsDetailsList);

        hostDetails.setHostPorts(hostPorts);

        return hostDetails;
    }

    private static HostPorts.Port getPort(Node currentNode) {
        HostPorts.Port currentPort = new HostPorts.Port();

        NamedNodeMap portAttributesNode = currentNode.getAttributes();
        String protocol = portAttributesNode.getNamedItem(PARSER_LITERAL_PROTOCOL).getNodeValue();
        String portId = portAttributesNode.getNamedItem(PARSER_LITERAL_PORTID).getNodeValue();

        currentPort.setProtocol(protocol);
        currentPort.setPortId(portId);

        NodeList portChildNodes = currentNode.getChildNodes();
        for (int i = 0; i < portChildNodes.getLength(); i++) {
            Node currentChildNode = portChildNodes.item(i);

            NamedNodeMap portAttributes = currentChildNode.getAttributes();
            if (currentChildNode.getNodeName().equals(PARSER_LITERAL_STATE)) {
                StateDetails stateDetails = getStateDetails(portAttributes);
                currentPort.setState(stateDetails);
            } else if (currentChildNode.getNodeName().equals(PARSER_LITERAL_SERVICE)) {
                ServiceDetails serviceDetails = getServiceDetails(portAttributes);
                currentPort.setServiceDetails(serviceDetails);
            }
        }
        return currentPort;
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

        ExtraReason extraReason = new ExtraReason();
        NodeList extraPortNodes = currentNode.getChildNodes();
        for (int i = 0; i < extraPortNodes.getLength(); i++) {
            Node extraReasonNode = extraPortNodes.item(i);
            if (extraReasonNode != null && extraReasonNode.getNodeName().equals(PARSER_LITERAL_EXTRA_REASONS)) {
                extraReason.setReason(extraReasonNode.getAttributes().getNamedItem(PARSER_LITERAL_REASON).getNodeValue());
                extraReason.setCount(extraReasonNode.getAttributes().getNamedItem(PARSER_LITERAL_COUNT).getNodeValue());
            }
        }
        return new HostPorts.ExtraPort(state, count, extraReason);
    }

    private static HostDetails updateTimingData(HostDetails hostDetails, NamedNodeMap timesDetails) {
        String smoothedRoundTripTime = timesDetails.getNamedItem(PARSER_LITERAL_SRTT).getNodeValue();
        String roundTripTimeVariance = timesDetails.getNamedItem(PARSER_LITERAL_RTTVAR).getNodeValue();
        String probeTimeout = timesDetails.getNamedItem(PARSER_LITERAL_TO).getNodeValue();

        hostDetails.setTimingData(new TimingData(smoothedRoundTripTime, roundTripTimeVariance, probeTimeout));

        return hostDetails;
    }
}
