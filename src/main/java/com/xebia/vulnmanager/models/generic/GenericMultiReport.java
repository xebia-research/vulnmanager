package com.xebia.vulnmanager.models.generic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GenericMultiReport implements Serializable {
    private List<GenericReport> reports = new ArrayList<>();

    public GenericMultiReport() {
        // empty ctor
    }

    public GenericMultiReport(final List<GenericReport> reports) {
        this.reports = reports;
    }

    public List<GenericReport> getReports() {
        return reports;
    }

    public void addReports(GenericReport reports) {
        this.reports.add(reports);
    }
}
