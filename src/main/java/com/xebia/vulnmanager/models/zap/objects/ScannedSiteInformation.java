package com.xebia.vulnmanager.models.zap.objects;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "ScannedSiteInformation")
@Entity
public class ScannedSiteInformation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "zap_report_id", nullable = false)
    private ZapReport zapReport;

    private String name;
    private String host;
    private int port;
    private boolean ssl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isSsl() {
        return ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }
}
