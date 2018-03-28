package com.xebia.vulnmanager.models.nmap.objects;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.GenerationType;
import javax.persistence.CascadeType;
import java.io.Serializable;

@Table(name = "NMapGeneralInformation")
@Entity
public class NMapGeneralInformation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "nMapReport_id", nullable = false) // Column that will be used to keep track of the parent
    @JsonBackReference // A back reference to keep json from infinite looping
    private NMapReport nMapReportParent; // NMapReport for host to know

    @OneToOne(mappedBy = "nMapGeneralInformationParent", cascade = CascadeType.ALL)
    private NMapInfo nMapInfo;
    @OneToOne(mappedBy = "nMapGeneralInformationParent", cascade = CascadeType.ALL)
    private NMapScanInfo nMapScanInfo;
    @OneToOne(mappedBy = "nMapGeneralInformationParent", cascade = CascadeType.ALL)
    private GeneralScanResult generalScanResult;

    public NMapGeneralInformation(final NMapInfo nMapInfo, final NMapScanInfo nMapScanInfo,
                                  final GeneralScanResult generalScanResult) {
        this.nMapInfo = nMapInfo;
        this.nMapScanInfo = nMapScanInfo;
        this.generalScanResult = generalScanResult;
    }

    public NMapInfo getnMapInfo() {
        return nMapInfo;
    }

    public NMapScanInfo getnMapScanInfo() {
        return nMapScanInfo;
    }

    public GeneralScanResult getGeneralScanResult() {
        return generalScanResult;
    }

    public long getId() {
        return id;
    }

    public NMapReport getParentReport() {
        return nMapReportParent;
    }

    public void setParentNMapReport(NMapReport nMapReport) {
        this.nMapReportParent = nMapReport;
    }
}
