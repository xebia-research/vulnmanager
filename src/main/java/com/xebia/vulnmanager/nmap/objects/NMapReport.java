package com.xebia.vulnmanager.nmap.objects;

import java.io.Serializable;
import java.util.List;

public class NMapReport implements Serializable {
    private NMapReportData reportData;
    private List<HostDetails> hostDetails;

    public NMapReport(final NMapReportData reportData, final List<HostDetails> hostDetails) {
        this.reportData = reportData;
        this.hostDetails = hostDetails;
    }

    public NMapReportData getReportData() {
        return reportData;
    }

    public List<HostDetails> getHostDetails() {
        return hostDetails;
    }
}
