package com.xebia.vulnmanager.NMapReport;

import com.xebia.vulnmanager.NMapReport.Objects.AddressDetails;
import com.xebia.vulnmanager.NMapReport.Objects.HostDetails;
import com.xebia.vulnmanager.NMapReport.Objects.StatusDetails;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The class NMapParser has functions for parsing a given document
 */

public class NMapParser {
    /**
     * All the hosts are extracted from the NMap report and saved in a list of hosts
     *
     * @param nMapDoc Document of NMap report
     */
    public static void getHostsOfDocument(Document nMapDoc) {
        NodeList hostList = nMapDoc.getElementsByTagName("host");

        // Todo: Change the for loops to streams
        for (int i = 0; i < hostList.getLength(); i++) {
            NodeList childrenHostList = hostList.item(i).getChildNodes();
            System.out.println();
            HostDetails hostDetails = new HostDetails();

            for (int x = 0; x < childrenHostList.getLength(); x++) {
                Node currentHostDetail = childrenHostList.item(x);
                hostDetails = updateHostDetails(hostDetails, currentHostDetail);
            }
        }
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

        if (currentNodeName.equals("status")) {
            hostDetails = updateStatusDetails(hostDetails, currentChildAttributes);
        }

        if (currentNodeName.equals("address")) {
            hostDetails = updateAddressDetails(hostDetails, currentChildAttributes);
        }

        if (currentNodeName.equals("hostnames")) {
            hostDetails = updateHostNamesDetails(hostDetails, currentChildAttributes);
        }

        if (currentNodeName.equals("ports")) {

        }

        if (currentNodeName.equals("times")) {

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
        String state = statusAttributes.getNamedItem("state").getNodeName();
        String reason = statusAttributes.getNamedItem("reason").getNodeValue();
        String reasonTtl = statusAttributes.getNamedItem("reason_ttl").getNodeValue();

        StatusDetails statusDetails = new StatusDetails(state, reason, reasonTtl);
        hostDetails.setStatusDetails(statusDetails);
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
        String address = addressAttributes.getNamedItem("addr").getNodeValue();
        String addressType = addressAttributes.getNamedItem("addrtype").getNodeValue();

        AddressDetails addressDetails = new AddressDetails(address, addressType);
        hostDetails.setAddressDetails(addressDetails);
        return hostDetails;
    }

    /**
     * Add the host names to a hostDetails object
     *
     * @param hostDetails         Host detail where host names are added.
     * @param HostNamesAttributes The details of the host names of a host.
     * @return The updated hostDetails are returned.
     */

    private static HostDetails updateHostNamesDetails(HostDetails hostDetails, NamedNodeMap HostNamesAttributes) {
        // TODO: Search and add, what child's hostNames has
        return hostDetails;
    }
}
