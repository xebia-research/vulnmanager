package com.xebia.vulnmanager.models.request;

import java.io.Serializable;

public class CompanyReq implements Serializable {
    private String name;

    public CompanyReq() {
        // empty ctor
    }

    public CompanyReq(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
