package com.xebia.vulnmanager.nmap.objects;

import java.io.Serializable;
import java.util.List;

public class NMapReport implements Serializable {
    private NMapReportData reportData;
    private List<HostDetails> hostDetails;

    public NMapReportData getReportData() {
        return reportData;
    }

    public List<HostDetails> getHostDetails() {
        return hostDetails;
    }

    public void setReportData(NMapReportData reportData) {
        this.reportData = reportData;
    }

    public void setHostDetails(List<HostDetails> hostDetails) {
        this.hostDetails = hostDetails;
    }
}
