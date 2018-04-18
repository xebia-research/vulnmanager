package com.xebia.vulnmanager.models.zap.objects;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Table(name = "ScannedSiteInformation")
@Entity
public class ScannedSiteInformation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "zap_report_id", nullable = false) // Column that will be used to keep track of the parent
    @JsonBackReference // A backrefrence to keep json from infinite looping
    private ZapReport zapReport;

    @OneToMany(mappedBy = "siteInformation", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ZapAlertItem> alertItems;

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

    public ZapReport getZapReport() {
        return zapReport;
    }

    public void setZapReport(ZapReport zapReport) {
        this.zapReport = zapReport;
    }

    public List<ZapAlertItem> getAlertItems() {
        return alertItems;
    }

    public void setAlertItems(List<ZapAlertItem> alertItems) {
        this.alertItems = alertItems;
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
