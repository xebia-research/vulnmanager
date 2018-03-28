package com.xebia.vulnmanager.models.nmap.objects;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.GenerationType;
import java.io.Serializable;

@Table(name = "NMapScanTask")
@Entity
public class GeneralScanResult implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "nMapReport_id") // Column that will be used to keep track of the parent
    @JsonBackReference // A back reference to keep json from infinite looping
    private NMapGeneralInformation nMapGeneralInformationParent;

    private String numberOfScannedHosts;
    private String numberOfHostUp;
    private String numberOfHostDown;
    private String timeElapsed;

    public GeneralScanResult(final String numberOfScannedHosts, final String numberOfHostUp,
                             final String numberOfHostDown, final String timeElapsed) {
        this.numberOfScannedHosts = numberOfScannedHosts;
        this.numberOfHostUp = numberOfHostUp;
        this.numberOfHostDown = numberOfHostDown;
        this.timeElapsed = timeElapsed;
    }

    public String getNumberOfScannedHosts() {
        return numberOfScannedHosts;
    }

    public String getNumberOfHostUp() {
        return numberOfHostUp;
    }

    public String getNumberOfHostDown() {
        return numberOfHostDown;
    }

    public String getTimeElapsed() {
        return timeElapsed;
    }

    public Long getId() {
        return id;
    }

    public NMapGeneralInformation getnMapGeneralInformationParent() {
        return nMapGeneralInformationParent;
    }

    public void setNMapGeneralInformationParent(NMapGeneralInformation nMapGeneralInformationParent) {
        this.nMapGeneralInformationParent = nMapGeneralInformationParent;
    }
}
