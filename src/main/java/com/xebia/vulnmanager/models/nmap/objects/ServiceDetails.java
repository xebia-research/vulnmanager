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

@Table(name = "serviceDetails")
@Entity
public class ServiceDetails implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToOne
    @JoinColumn(name = "port_id", nullable = false) // Column that will be used to keep track of the parent
    @JsonBackReference // A backrefrence to keep json from infinite looping
    private HostPorts.Port portParent;

    private String serviceName;
    private String serviceMethod;
    private String serviceConf;

    public ServiceDetails(final String serviceName, final String serviceMethod, final String serviceConf) {
        this.serviceName = serviceName;
        this.serviceMethod = serviceMethod;
        this.serviceConf = serviceConf;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getServiceMethod() {
        return serviceMethod;
    }

    public String getServiceConf() {
        return serviceConf;
    }

    public Long getId() {
        return id;
    }

    public HostPorts.Port getPortParent() {
        return portParent;
    }

    public void setPortParent(HostPorts.Port portParent) {
        this.portParent = portParent;
    }
}
