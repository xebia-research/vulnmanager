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

/**
 * StateDetails Serializable.
 */
@Table(name = "StateDetails")
@Entity
public class StateDetails implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToOne
    @JoinColumn(name = "host_id") // Column that will be used to keep track of the parent. Could be null because we have two parents
    @JsonBackReference // A backrefrence to keep json from infinite looping
    private Host hostParent;

    @OneToOne
    @JoinColumn(name = "port_id") // Column that will be used to keep track of the parent. Could be null because we have two parents
    @JsonBackReference // A backrefrence to keep json from infinite looping
    private HostPorts.Port portParent;

    private String state;
    private String reason;
    private String reasonTtl;

    public StateDetails(final String state, final String reason, final String reasonTtl) {
        this.state = state;
        this.reason = reason;
        this.reasonTtl = reasonTtl;
    }

    public String getState() {
        return state;
    }

    public String getReason() {
        return reason;
    }

    public String getReasonTtl() {
        return reasonTtl;
    }

    public Long getId() {
        return id;
    }

    public Host getHostParent() {
        return hostParent;
    }

    public void setHostParent(Host hostParent) {
        this.hostParent = hostParent;
    }

    public HostPorts.Port getPortParent() {
        return portParent;
    }

    public void setPortParent(HostPorts.Port portParent) {
        this.portParent = portParent;
    }
}
