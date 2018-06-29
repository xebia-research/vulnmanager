package com.xebia.vulnmanager.models.nmap.objects;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
import javax.persistence.GenerationType;
import java.io.Serializable;

@Table(name = "TimingData")
@Entity
public class TimingData implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToOne
    @JoinColumn(name = "host_id", nullable = false) // Column that will be used to keep track of the parent
    @JsonBackReference // A backrefrence to keep json from infinite looping
    private Host hostParent;

    private String smoothedRoundTripTime;
    private String roundTripTimeVariance;
    private String probeTimeout;


    protected TimingData() {
        // JPA constructor
    }

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

    public Long getId() {
        return id;
    }

    @JsonBackReference // A backrefrence to keep json from infinite looping
    public Host getHostParent() {
        return hostParent;
    }

    public void setHostParent(Host hostParent) {
        this.hostParent = hostParent;
    }
}
