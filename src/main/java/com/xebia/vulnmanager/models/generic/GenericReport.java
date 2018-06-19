package com.xebia.vulnmanager.models.generic;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.xebia.vulnmanager.models.company.Team;
import com.xebia.vulnmanager.util.ReportType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class GenericReport implements Serializable {
    private ReportType reportType;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<GenericResult> genericResults = new ArrayList<>();

    @Column(name = "date_parsed")
    private Date parsedDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "team_id", nullable = false) // Column that will be used to keep track of the parent
    @JsonBackReference // A backrefrence to keep json from infinite looping
    private Team team;

    public GenericReport() {
        parsedDate = new Date();
    }

    public ReportType getReportType() {
        return reportType;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<GenericResult> getGenericResults() {
        return genericResults;
    }

    public void setGenericResults(List<GenericResult> genericResults) {
        this.genericResults = genericResults;
    }

    public void addGenericResult(GenericResult result) {
        result.setReport(this);
        this.genericResults.add(result);
    }

    public void setParsedDate(Date parsedDate) {
        this.parsedDate = parsedDate;
    }

    public Date getParsedDate() {
        return parsedDate;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }
}
