package com.xebia.vulnmanager.models.nmap.objects;

import java.io.Serializable;
import java.time.LocalDateTime;

public class NMapInfo implements Serializable {
    private LocalDateTime scanDateTime;
    private String calledArgument;
    private String nMapVersion;

    public NMapInfo(final LocalDateTime scanDateTime, final String nMapVersion, final String nMapCalledArgument) {
        this.scanDateTime = scanDateTime;
        this.calledArgument = nMapCalledArgument;
        this.nMapVersion = nMapVersion;
    }

    public LocalDateTime getScanDateTime() {
        return scanDateTime;
    }

    public String getcalledArgument() {
        return calledArgument;
    }

    public String getnMapVersion() {
        return nMapVersion;
    }
}
