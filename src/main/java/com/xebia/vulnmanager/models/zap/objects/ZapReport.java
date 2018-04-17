package com.xebia.vulnmanager.models.zap.objects;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Table(name = "ZapResults")
@Entity
public class ZapReport implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "zapReport", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ZapAlertItem> alertItems;

    private String dateGenerated;

    @OneToOne(mappedBy = "zapReport", cascade = CascadeType.ALL)
    private ScannedSiteInformation scannedSiteInformation;

    public ZapReport() {
        // Empty constructor
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ZapAlertItem> getAlertItems() {
        return alertItems;
    }

    public void setAlerItems(List<ZapAlertItem> alertItems) {
        this.alertItems = alertItems;
    }

    public String getDateGenerated() {
        return dateGenerated;
    }

    public void setDateGenerated(String dateGenerated) {
        this.dateGenerated = dateGenerated;
    }

    public ScannedSiteInformation getScannedSiteInformation() {
        return scannedSiteInformation;
    }

    public void setScannedSiteInformation(ScannedSiteInformation scannedSiteInformation) {
        this.scannedSiteInformation = scannedSiteInformation;
    }
}
