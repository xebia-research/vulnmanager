package com.xebia.vulnmanager.nmap.objects;

import java.io.Serializable;
import java.util.List;

public class NMapReport implements Serializable {
    private NMapGeneralInformation scanData;
    private List<HostDetails> hostDetails;

    public NMapReport(final NMapGeneralInformation scanData, final List<HostDetails> hostDetails) {
        this.scanData = scanData;
        this.hostDetails = hostDetails;
    }

    public NMapGeneralInformation getScanData() {
        return scanData;
    }

    public List<HostDetails> getHostDetails() {
        return hostDetails;
    }
}
