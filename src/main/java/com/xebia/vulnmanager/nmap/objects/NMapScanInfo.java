package com.xebia.vulnmanager.nmap.objects;

import java.io.Serializable;
import java.util.List;

public class NMapScanInfo implements Serializable {
    private String scanType;
    private String scanProtocol;
    private String scanNumberOfServices;
    private String scanServices;
    private List<NMapScanTask> scanTaskList;

    public NMapScanInfo(final String scanType, final String scanProtocol,
                        final String scanNumberOfServices, final String scanServices, final List<NMapScanTask> scanTaskList) {
        this.scanType = scanType;
        this.scanProtocol = scanProtocol;
        this.scanNumberOfServices = scanNumberOfServices;
        this.scanServices = scanServices;
        this.scanTaskList = scanTaskList;
    }

    public String getScanType() {
        return scanType;
    }

    public String getScanProtocol() {
        return scanProtocol;
    }

    public String getScanNumberOfServices() {
        return scanNumberOfServices;
    }

    public String getScanServices() {
        return scanServices;
    }

    public List<NMapScanTask> getScanTaskList() {
        return scanTaskList;
    }
}
