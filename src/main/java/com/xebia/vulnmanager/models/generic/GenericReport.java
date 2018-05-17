package com.xebia.vulnmanager.models.generic;

import com.xebia.vulnmanager.util.ReportType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GenericReport implements Serializable {
    private ReportType reportType;
    private float id;

    private List<GenericResult> genericResults = new ArrayList<>();

    public ReportType getReportType() {
        return reportType;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    public float getId() {
        return id;
    }

    public void setId(float id) {
        this.id = id;
    }

    public List<GenericResult> getGenericResults() {
        return genericResults;
    }

    public void setGenericResults(List<GenericResult> genericResults) {
        this.genericResults = genericResults;
    }

    public void addGenericResult(GenericResult result) {
        this.genericResults.add(result);
    }
}
