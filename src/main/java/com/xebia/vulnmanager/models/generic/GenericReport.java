package com.xebia.vulnmanager.models.generic;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.xebia.vulnmanager.util.ReportType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
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
}
