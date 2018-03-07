package com.xebia.vulnmanager.nmap.objects;

import java.io.Serializable;
import java.time.LocalDateTime;

public class NMapReportData implements Serializable {
    private LocalDateTime scanDate;
    private String nMapArgument;
    private String nMapVersion;

    public NMapReportData(final String nMapVersion, final String nMapArgument, final LocalDateTime scanDate) {
        this.scanDate = scanDate;
        this.nMapVersion = nMapVersion;
        this.nMapArgument = nMapArgument;
    }

    public LocalDateTime getScanDate() {
        return scanDate;
    }

    public String getNMapVersion() {
        return nMapVersion;
    }

    public String getNMapArgument() {
        return nMapArgument;
    }


}
