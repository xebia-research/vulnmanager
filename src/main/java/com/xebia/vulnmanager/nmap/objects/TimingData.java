package com.xebia.vulnmanager.nmap.objects;

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

    public void setSmoothedRoundTripTime(String smoothedRoundTripTime) {
        this.smoothedRoundTripTime = smoothedRoundTripTime;
    }

    public String getRoundTripTimeVariance() {
        return roundTripTimeVariance;
    }

    public void setRoundTripTimeVariance(String roundTripTimeVariance) {
        this.roundTripTimeVariance = roundTripTimeVariance;
    }

    public String getProbeTimeout() {
        return probeTimeout;
    }

    public void setProbeTimeout(String probeTimeout) {
        this.probeTimeout = probeTimeout;
    }
}
