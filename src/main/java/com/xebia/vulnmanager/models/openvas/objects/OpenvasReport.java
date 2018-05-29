package com.xebia.vulnmanager.models.openvas.objects;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.xebia.vulnmanager.models.company.Team;
import com.xebia.vulnmanager.models.generic.GenericMultiReport;
import com.xebia.vulnmanager.models.generic.GenericReport;
import com.xebia.vulnmanager.util.ReportType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class OpenvasReport implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String fileId;
    private String timeDone;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OvResult> results;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false) // Column that will be used to keep track of the parent
    @JsonBackReference // A backrefrence to keep json from infinite looping
    private Team team;

    public OpenvasReport() {
        results = new ArrayList<>();
    }

    public List<OvResult> getResults() {
        return results;
    }

    public void setResults(List<OvResult> results) {
        this.results = results;
    }

    public void addResult(OvResult result) {
        results.add(result);
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getTimeDone() {
        return timeDone;
    }

    public void setTimeDone(String timeDone) {
        this.timeDone = timeDone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GenericMultiReport getGenericMultiReport() {
        GenericReport report = new GenericReport();
        report.setReportType(ReportType.OPENVAS);
        for (OvResult res : results) {
            report.addGenericResult(res.getGenericResult());
        }
        GenericMultiReport multiReport = new GenericMultiReport();
        multiReport.addReports(report);
        return multiReport;
    }

    @Override
    public String toString() {
        StringBuffer resultsInfo = new StringBuffer();
        for (OvResult result : results) {
            resultsInfo.append(result);
        }

        return "OpenvasReport{"
                +
                "results found= " + results.size()
                +
                "results=" + resultsInfo
                +
                '}';
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
