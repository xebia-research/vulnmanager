package com.xebia.vulnmanager.models.clair.objects;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xebia.vulnmanager.models.company.Team;
import com.xebia.vulnmanager.models.generic.GenericMultiReport;
import com.xebia.vulnmanager.models.generic.GenericReport;
import com.xebia.vulnmanager.util.ReportType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Table(name = "ClairResults")
@Entity
public class ClairReport implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonProperty("image")
    private String dockerImage;

    // This is a non-standard relationship map, so we need to tell jpa that this is of class String
    @ElementCollection(targetClass = String.class)
    @JsonProperty("unapproved")
    private List<String> unapproved;

    @OneToMany(mappedBy = "clairReport", cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonProperty("vulnerabilities")
    private List<ClairVulnerability> clairVulnerabilities;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false) // Column that will be used to keep track of the parent
    @JsonBackReference // A backrefrence to keep json from infinite looping
    private Team team;

    public ClairReport() {
        // Empty constructor
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDockerImage() {
        return dockerImage;
    }

    public void setDockerImage(String dockerImage) {
        this.dockerImage = dockerImage;
    }

    public List<String> getUnapproved() {
        return unapproved;
    }

    public void setUnapproved(List<String> unapproved) {
        this.unapproved = unapproved;
    }

    public List<ClairVulnerability> getClairVulnerabilities() {
        return clairVulnerabilities;
    }

    public void setClairVulnerabilities(List<ClairVulnerability> clairVulnerabilities) {
        this.clairVulnerabilities = clairVulnerabilities;
    }

    public GenericMultiReport getGenericReport() {
        GenericMultiReport multiReport = new GenericMultiReport();

        GenericReport report = new GenericReport();
        report.setReportType(ReportType.CLAIR);
        report.setId(getId());

        for (ClairVulnerability cv : clairVulnerabilities) {
            report.addGenericResult(cv.getGenericResult());
        }

        multiReport.addReports(report);

        return multiReport;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
