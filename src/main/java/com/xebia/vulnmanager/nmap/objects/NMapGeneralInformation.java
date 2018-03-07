package com.xebia.vulnmanager.nmap.objects;

import java.io.Serializable;

public class NMapGeneralInformation implements Serializable {
    private NMapInfo nMapInfo;
    private NMapScanInfo nMapScanInfo;
    private GeneralScanResult generalScanResult;

    public NMapGeneralInformation(final NMapInfo nMapInfo, final NMapScanInfo nMapScanInfo,
                                  final GeneralScanResult generalScanResult) {
        this.nMapInfo = nMapInfo;
        this.nMapScanInfo = nMapScanInfo;
        this.generalScanResult = generalScanResult;
    }

    public NMapInfo getnMapInfo() {
        return nMapInfo;
    }

    public NMapScanInfo getnMapScanInfo() {
        return nMapScanInfo;
    }

    public GeneralScanResult getGeneralScanResult() {
        return generalScanResult;
    }
}
