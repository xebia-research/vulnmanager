package com.xebia.vulnmanager.models.nmap.objects;

import java.io.Serializable;

public class TimingData implements Serializable {
    private String smoothedRoundTripTime;
    private String roundTripTimeVariance;
    private String probeTimeout;

    public TimingData(final String smoothedRoundTripTime, final String roundTripTimeVariance, final String probeTimeout) {
        this.smoothedRoundTripTime = smoothedRoundTripTime;
        this.roundTripTimeVariance = roundTripTimeVariance;
        this.probeTimeout = probeTimeout;
    }

    public String getSmoothedRoundTripTime() {
        return smoothedRoundTripTime;
    }

    public String getRoundTripTimeVariance() {
        return roundTripTimeVariance;
    }

    public String getProbeTimeout() {
        return probeTimeout;
    }
}
