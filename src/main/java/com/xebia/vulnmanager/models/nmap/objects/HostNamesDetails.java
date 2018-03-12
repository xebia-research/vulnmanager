package com.xebia.vulnmanager.models.nmap.objects;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.io.Serializable;
import java.util.List;

/**
 * AddressDetails Serializable.
 */

public class HostNamesDetails implements Serializable {
    private List<HostNameDetails> hostNameDetails;

    public HostNamesDetails(final List<HostNameDetails> hostNameDetails) {
        this.hostNameDetails = hostNameDetails;
    }

    @JacksonXmlElementWrapper(localName = "hostNameList")
    @JacksonXmlProperty(localName = "hostName")
    public List<HostNameDetails> getHostNameDetails() {
        return hostNameDetails;
    }

    public static class HostNameDetails implements Serializable {
        private String hostName;
        private String hostType;

        public HostNameDetails(final String hostName, final String hostType) {
            this.hostName = hostName;
            this.hostType = hostType;
        }

        public String getHostName() {
            return hostName;
        }

        public String getHostType() {
            return hostType;
        }
    }
}
