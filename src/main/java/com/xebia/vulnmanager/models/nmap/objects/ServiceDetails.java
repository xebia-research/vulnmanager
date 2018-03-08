package com.xebia.vulnmanager.models.nmap.objects;

import java.io.Serializable;

public class ServiceDetails implements Serializable {
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
}
