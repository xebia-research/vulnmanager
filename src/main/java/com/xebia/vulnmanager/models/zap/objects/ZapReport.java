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

    private String dateGenerated;
    private int numberOfSitesInReport;

    @OneToMany(mappedBy = "zapReport", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ScannedSiteInformation> scannedSitesInformation;

    public ZapReport() {
        // Empty constructor
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDateGenerated() {
        return dateGenerated;
    }

    public void setDateGenerated(String dateGenerated) {
        this.dateGenerated = dateGenerated;
    }

    public int getNumberOfSitesInReport() {
        return numberOfSitesInReport;
    }

    public void setNumberOfSitesInReport(int numberOfSitesInReport) {
        this.numberOfSitesInReport = numberOfSitesInReport;
    }

    public List<ScannedSiteInformation> getScannedSitesInformation() {
        return scannedSitesInformation;
    }

    public void setScannedSitesInformation(List<ScannedSiteInformation> scannedSitesInformation) {
        this.scannedSitesInformation = scannedSitesInformation;
    }
}
