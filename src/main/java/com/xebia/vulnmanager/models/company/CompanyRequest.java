package com.xebia.vulnmanager.models.company;

import java.io.Serializable;

public class CompanyRequest implements Serializable {
    private String companyName;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
