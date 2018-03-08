package com.xebia.vulnmanager.models.nmap.objects;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.io.Serializable;
import java.util.List;

public class NMapReport implements Serializable {
    private NMapGeneralInformation scanData;

    private List<Host> hosts;

    public NMapReport(final NMapGeneralInformation scanData, final List<Host> hosts) {
        this.scanData = scanData;
        this.hosts = hosts;
    }

    public NMapGeneralInformation getScanData() {
        return scanData;
    }

    @JacksonXmlElementWrapper(localName = "hosts")
    @JacksonXmlProperty(localName = "host")
    public List<Host> getHosts() {
        return hosts;
    }
}
