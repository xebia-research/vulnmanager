package com.xebia.vulnmanager.nmap;

import com.xebia.vulnmanager.nmap.objects.AddressDetails;
import com.xebia.vulnmanager.nmap.objects.StateDetails;
import com.xebia.vulnmanager.nmap.objects.HostPorts;
import com.xebia.vulnmanager.nmap.objects.ServiceDetails;
import com.xebia.vulnmanager.nmap.objects.TimingData;
import com.xebia.vulnmanager.nmap.objects.ExtraReason;
import com.xebia.vulnmanager.nmap.objects.HostNamesDetails;
import com.xebia.vulnmanager.nmap.objects.HostDetails;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class HostsParserHelper {
    /**
     * All the hosts are extracted from the NMap report and saved in a list of hosts
     *
     * @param nMapDoc Document of NMap report
     */
    public static List<HostDetails> getHostsFromDocument(Document nMapDoc) {
        NodeList hostList = nMapDoc.getElementsByTagName(NMapConstants.PARSER_LITERAL_HOST);

        List<HostDetails> allHostDetails = new ArrayList<>();
        // Todo: Change the for loops to streams
        for (int i = 0; i < hostList.getLength(); i++) {
            Node hostNode = hostList.item(i);
            if (hostNode.getAttributes().getNamedItem(NMapConstants.PARSER_LITERAL_START_TIME) != null) {
                NodeList hostDataList = hostNode.getChildNodes();

                allHostDetails.add(parseHostDataFromNodeList(hostDataList));
            }
        }
        return allHostDetails;
    }

    private static HostDetails parseHostDataFromNodeList(NodeList hostDataList) {
        StateDetails stateDetails = null;
        AddressDetails addressDetails = null;
        HostNamesDetails hostNamesDetails = null;
        HostPorts hostPorts = null;
        TimingData timingData = null;

        for (int x = 0; x < hostDataList.getLength(); x++) {
            Node currentHostDetail = hostDataList.item(x);

            NamedNodeMap currentChildAttributes = currentHostDetail.getAttributes();
            String currentNodeName = currentHostDetail.getNodeName();

            switch (currentNodeName) {
                case NMapConstants.PARSER_LITERAL_STATUS:
                    stateDetails = getStatusDetails(currentChildAttributes);
                    break;
                case NMapConstants.PARSER_LITERAL_ADDRESS:
                    addressDetails = getAddressDetails(currentChildAttributes);
                    break;
                case NMapConstants.PARSER_LITERAL_HOST_NAMES:
                    hostNamesDetails = getHostNamesDetails(currentHostDetail.getChildNodes());
                    break;
                case NMapConstants.PARSER_LITERAL_PORTS:
                    hostPorts = getPortDetails(currentHostDetail.getChildNodes());
                    break;
                case NMapConstants.PARSER_LITERAL_TIMES:
                    timingData = getTimingData(currentChildAttributes);
                    break;
                default:
                    break;
            }
        }
        return new HostDetails(stateDetails, addressDetails, hostNamesDetails, hostPorts, timingData);
    }

    /**
     * Add the status details of a hostDetails object
     *
     * @param statusAttributes The details of the status of a host.
     * @return The updated hostDetails are returned.
     */
    private static StateDetails getStatusDetails(NamedNodeMap statusAttributes) {
        String state = statusAttributes.getNamedItem(NMapConstants.PARSER_LITERAL_STATE).getNodeValue();
        String reason = statusAttributes.getNamedItem(NMapConstants.PARSER_LITERAL_REASON).getNodeValue();
        String reasonTtl = statusAttributes.getNamedItem(NMapConstants.PARSER_LITERAL_REASON_TTL).getNodeValue();

        return new StateDetails(state, reason, reasonTtl);
    }

    /**
     * Add the address details of a hostDetails object
     *
     * @param addressAttributes The details of the address of a host.
     * @return The updated hostDetails are returned.
     */
    private static AddressDetails getAddressDetails(NamedNodeMap addressAttributes) {
        String address = addressAttributes.getNamedItem(NMapConstants.PARSER_LITERAL_ADDR).getNodeValue();
        String addressType = addressAttributes.getNamedItem(NMapConstants.PARSER_LITERAL_ADDR_TYPE).getNodeValue();

        return new AddressDetails(address, addressType);
    }

    private static HostNamesDetails getHostNamesDetails(NodeList hostNamesList) {
        List<HostNamesDetails.HostNameDetails> hostNameDetailsList = new ArrayList<>();

        for (int x = 0; x < hostNamesList.getLength(); x++) {
            if (hostNamesList.item(x).getNodeName().equals(NMapConstants.PARSER_LITERAL_HOST_NAME)) {
                NamedNodeMap hostNameNode = hostNamesList.item(x).getAttributes();

                String hostName = hostNameNode.getNamedItem(NMapConstants.PARSER_LITERAL_NAME).getNodeValue();
                String hostType = hostNameNode.getNamedItem(NMapConstants.PARSER_LITERAL_TYPE).getNodeValue();
                hostNameDetailsList.add(new HostNamesDetails.HostNameDetails(hostName, hostType));
            }
        }

        return new HostNamesDetails(hostNameDetailsList);
    }

    private static HostPorts getPortDetails(NodeList portsAttributes) {
        List<HostPorts.Port> hostPortsDetailsList = new ArrayList<>();
        List<HostPorts.ExtraPort> extraPortsDetailsList = new ArrayList<>();

        for (int x = 0; x < portsAttributes.getLength(); x++) {
            Node currentNode = portsAttributes.item(x);

            if (currentNode.getNodeName().equals(NMapConstants.PARSER_LITERAL_PORT)) {
                HostPorts.Port currentPort = getPort(currentNode);
                hostPortsDetailsList.add(currentPort);

            } else if (currentNode.getNodeName().equals(NMapConstants.PARSER_LITERAL_EXTRA_PORTS)) {
                HostPorts.ExtraPort extraPort = getExtraPorts(currentNode);
                extraPortsDetailsList.add(extraPort);
            }
        }

        return new HostPorts(hostPortsDetailsList, extraPortsDetailsList);
    }

    private static HostPorts.Port getPort(Node currentNode) {
        NamedNodeMap portAttributesNode = currentNode.getAttributes();

        String protocol = portAttributesNode.getNamedItem(NMapConstants.PARSER_LITERAL_PROTOCOL).getNodeValue();
        String portId = portAttributesNode.getNamedItem(NMapConstants.PARSER_LITERAL_PORTID).getNodeValue();

        StateDetails stateDetails = null;
        ServiceDetails serviceDetails = null;

        NodeList portChildNodes = currentNode.getChildNodes();
        for (int i = 0; i < portChildNodes.getLength(); i++) {
            Node currentChildNode = portChildNodes.item(i);

            NamedNodeMap portAttributes = currentChildNode.getAttributes();
            if (currentChildNode.getNodeName().equals(NMapConstants.PARSER_LITERAL_STATE)) {
                stateDetails = getStateDetails(portAttributes);
            } else if (currentChildNode.getNodeName().equals(NMapConstants.PARSER_LITERAL_SERVICE)) {
                serviceDetails = getServiceDetails(portAttributes);
            }
        }

        return new HostPorts.Port(protocol, portId, stateDetails, serviceDetails);
    }

    private static StateDetails getStateDetails(NamedNodeMap stateDetails) {
        String state = stateDetails.getNamedItem(NMapConstants.PARSER_LITERAL_STATE).getNodeValue();
        String reason = stateDetails.getNamedItem(NMapConstants.PARSER_LITERAL_REASON).getNodeValue();
        String reasonTtl = stateDetails.getNamedItem(NMapConstants.PARSER_LITERAL_REASON_TTL).getNodeValue();
        return new StateDetails(state, reason, reasonTtl);
    }

    private static ServiceDetails getServiceDetails(NamedNodeMap serviceDetails) {
        String serviceName = serviceDetails.getNamedItem(NMapConstants.PARSER_LITERAL_NAME).getNodeValue();
        String serviceMethod = serviceDetails.getNamedItem(NMapConstants.PARSER_LITERAL_METHOD).getNodeValue();
        String serviceConf = serviceDetails.getNamedItem(NMapConstants.PARSER_LITERAL_CONF).getNodeValue();
        return new ServiceDetails(serviceName, serviceMethod, serviceConf);
    }

    private static HostPorts.ExtraPort getExtraPorts(Node currentNode) {
        NamedNodeMap extraPortAttributes = currentNode.getAttributes();

        String state = extraPortAttributes.getNamedItem(NMapConstants.PARSER_LITERAL_STATE).getNodeValue();
        String count = extraPortAttributes.getNamedItem(NMapConstants.PARSER_LITERAL_COUNT).getNodeValue();

        ExtraReason extraReason = null;
        NodeList extraPortNodes = currentNode.getChildNodes();
        for (int i = 0; i < extraPortNodes.getLength(); i++) {
            Node extraReasonNode = extraPortNodes.item(i);
            if (extraReasonNode != null && extraReasonNode.getNodeName().equals(NMapConstants.PARSER_LITERAL_EXTRA_REASONS)) {
                String reason = extraReasonNode.getAttributes().getNamedItem(NMapConstants.PARSER_LITERAL_REASON).getNodeValue();
                String extraCount = extraReasonNode.getAttributes().getNamedItem(NMapConstants.PARSER_LITERAL_COUNT).getNodeValue();
                extraReason = new ExtraReason(reason, extraCount);
            }
        }
        return new HostPorts.ExtraPort(state, count, extraReason);
    }

    private static TimingData getTimingData(NamedNodeMap timesDetails) {
        String smoothedRoundTripTime = timesDetails.getNamedItem(NMapConstants.PARSER_LITERAL_SRTT).getNodeValue();
        String roundTripTimeVariance = timesDetails.getNamedItem(NMapConstants.PARSER_LITERAL_RTTVAR).getNodeValue();
        String probeTimeout = timesDetails.getNamedItem(NMapConstants.PARSER_LITERAL_TO).getNodeValue();

        return new TimingData(smoothedRoundTripTime, roundTripTimeVariance, probeTimeout);
    }
}
