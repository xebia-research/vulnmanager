package com.xebia.vulnmanager.models.zap.objects;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.xebia.vulnmanager.models.company.Team;
import com.xebia.vulnmanager.models.generic.GenericMultiReport;

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

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false) // Column that will be used to keep track of the parent
    @JsonBackReference // A backrefrence to keep json from infinite looping
    private Team team;

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

    public GenericMultiReport getGenericReport() {
        GenericMultiReport multiReport = new GenericMultiReport();

        for (ScannedSiteInformation ssi : scannedSitesInformation) {
            multiReport.addReports(ssi.getGenericReport());
        }
        return multiReport;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
