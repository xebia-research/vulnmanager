package com.xebia.vulnmanager.models.nmap.objects;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "extraReason")
@Entity
public class ExtraReason implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToOne
    @JoinColumn(name = "extra_port_id", nullable = false) // Column that will be used to keep track of the parent
    @JsonBackReference // A back reference keep json from infinite looping
    private HostPorts.ExtraPort extraPortsParent;

    private String reason;
    private String count;

    protected ExtraReason() {
        // JPA constructor
    }

    public ExtraReason(final String reason, final String count) {
        this.reason = reason;
        this.count = count;
    }

    public String getReason() {
        return reason;
    }

    public String getCount() {
        return count;
    }

    public Long getId() {
        return id;
    }
    @JsonBackReference // A backrefrence to keep json from infinite looping
    public HostPorts.ExtraPort getExtraPortParent() {
        return extraPortsParent;
    }

    public void setExtraPortParent(HostPorts.ExtraPort extraPortsParent) {
        this.extraPortsParent = extraPortsParent;
    }
}
