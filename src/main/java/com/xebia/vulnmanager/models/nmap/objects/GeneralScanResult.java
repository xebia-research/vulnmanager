package com.xebia.vulnmanager.models.nmap.objects;

import java.io.Serializable;

public class GeneralScanResult implements Serializable {
    private String numberOfScannedHosts;
    private String numberOfHostUp;
    private String numberOfHostDown;
    private String timeElapsed;

    public GeneralScanResult(final String numberOfScannedHosts, final String numberOfHostUp,
                             final String numberOfHostDown, final String timeElapsed) {
        this.numberOfScannedHosts = numberOfScannedHosts;
        this.numberOfHostUp = numberOfHostUp;
        this.numberOfHostDown = numberOfHostDown;
        this.timeElapsed = timeElapsed;
    }

    public String getNumberOfScannedHosts() {
        return numberOfScannedHosts;
    }

    public String getNumberOfHostUp() {
        return numberOfHostUp;
    }

    public String getNumberOfHostDown() {
        return numberOfHostDown;
    }

    public String getTimeElapsed() {
        return timeElapsed;
    }
}
