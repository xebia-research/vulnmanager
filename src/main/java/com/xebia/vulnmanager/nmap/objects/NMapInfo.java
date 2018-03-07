package com.xebia.vulnmanager.nmap.objects;

import java.io.Serializable;
import java.time.LocalDateTime;

public class NMapInfo implements Serializable {
    private LocalDateTime scanDateTime;
    private String nMapCalledArgument;
    private String nMapVersion;

    public NMapInfo(final LocalDateTime scanDateTime, final String nMapVersion, final String nMapCalledArgument) {
        this.scanDateTime = scanDateTime;
        this.nMapCalledArgument = nMapCalledArgument;
        this.nMapVersion = nMapVersion;
    }

    public LocalDateTime getScanDateTime() {
        return scanDateTime;
    }

    public String getnMapCalledArgument() {
        return nMapCalledArgument;
    }

    public String getnMapVersion() {
        return nMapVersion;
    }
}
