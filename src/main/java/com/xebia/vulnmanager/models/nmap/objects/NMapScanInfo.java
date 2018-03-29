package com.xebia.vulnmanager.models.nmap.objects;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.io.Serializable;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Lob;
import javax.persistence.CascadeType;
import javax.persistence.GenerationType;

import java.util.List;

@Table(name = "NMapScanInfo")
@Entity
public class NMapScanInfo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "nMapReport_id", nullable = false) // Column that will be used to keep track of the parent
    @JsonBackReference // A back reference to keep json from infinite looping
    private NMapGeneralInformation nMapGeneralInformationParent;

    private String scanType;
    private String scanProtocol;
    private String scanNumberOfServices;
    @Lob
    private String scanServices;

    @OneToMany(mappedBy = "nMapScanInfo", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<NMapScanTask> scanTaskList;

    public NMapScanInfo() {
        // Default constructor
    }

    public NMapScanInfo(final String scanType, final String scanProtocol,
                        final String scanNumberOfServices, final String scanServices, final List<NMapScanTask> scanTaskList) {
        this.scanType = scanType;
        this.scanProtocol = scanProtocol;
        this.scanNumberOfServices = scanNumberOfServices;
        this.scanServices = scanServices;
        this.scanTaskList = scanTaskList;
    }

    public String getScanType() {
        return scanType;
    }

    public String getScanProtocol() {
        return scanProtocol;
    }

    public String getScanNumberOfServices() {
        return scanNumberOfServices;
    }

    public String getScanServices() {
        return scanServices;
    }

    @JacksonXmlElementWrapper(localName = "scanTaskList")
    @JacksonXmlProperty(localName = "task")
    public List<NMapScanTask> getScanTaskList() {
        return scanTaskList;
    }

    public long getId() {
        return id;
    }

    public NMapGeneralInformation getnMapGeneralInformationParent() {
        return nMapGeneralInformationParent;
    }

    public void setNMapGeneralInformationParent(NMapGeneralInformation nMapGeneralInformationParent) {
        this.nMapGeneralInformationParent = nMapGeneralInformationParent;
    }
}
