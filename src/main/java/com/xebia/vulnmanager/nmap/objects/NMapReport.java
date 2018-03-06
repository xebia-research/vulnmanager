package com.xebia.vulnmanager.nmap.objects;

import java.io.Serializable;

public class NMapReport implements Serializable {
    private NMapReportData reportData;
    private HostDetails hostDetails;

    public NMapReportData getReportData() {
        return reportData;
    }

    public HostDetails getHostDetails() {
        return hostDetails;
    }

    public void setReportData(NMapReportData reportData) {
        this.reportData = reportData;
    }

    public void setHostDetails(HostDetails hostDetails) {
        this.hostDetails = hostDetails;
    }
}
