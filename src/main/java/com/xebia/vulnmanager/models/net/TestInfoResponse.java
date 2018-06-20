package com.xebia.vulnmanager.models.net;

import java.io.Serializable;

public class TestInfoResponse implements Serializable {
    private boolean company;
    private boolean reports;
    private boolean accounts;

    public TestInfoResponse() {
        // empty ctor
    }

    public boolean isAccounts() {
        return accounts;
    }

    public void setAccounts(boolean accounts) {
        this.accounts = accounts;
    }

    public boolean isCompany() {
        return company;
    }

    public void setCompany(boolean company) {
        this.company = company;
    }

    public boolean isReports() {
        return reports;
    }

    public void setReports(boolean reports) {
        this.reports = reports;
    }
}
