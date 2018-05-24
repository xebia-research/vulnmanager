package com.xebia.vulnmanager.models.nmap.objects;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.io.Serializable;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.GenerationType;

@Table(name = "NMapInfo")
@Entity
public class NMapInfo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "nMapReport_id", nullable = false) // Column that will be used to keep track of the parent
    @JsonBackReference // A back reference to keep json from infinite looping
    private NMapGeneralInformation nMapGeneralInformationParent;

    private String scanDateTime;
    private String calledArgument;
    private String nMapVersion;

    public NMapInfo() {
        // Default Constructor
    }

    public NMapInfo(final String scanDateTime, final String nMapVersion, final String nMapCalledArgument) {
        this.scanDateTime = scanDateTime;
        this.calledArgument = nMapCalledArgument;
        this.nMapVersion = nMapVersion;
    }

    public String getScanDateTime() {
        return scanDateTime;
    }

    public String getcalledArgument() {
        return calledArgument;
    }

    public String getnMapVersion() {
        return nMapVersion;
    }

    public long getId() {
        return id;
    }

    @JsonBackReference // A backrefrence to keep json from infinite looping
    public NMapGeneralInformation getnMapGeneralInformationParent() {
        return nMapGeneralInformationParent;
    }

    public void setNMapGeneralInformationParent(NMapGeneralInformation nMapGeneralInformationParent) {
        this.nMapGeneralInformationParent = nMapGeneralInformationParent;
    }
}
