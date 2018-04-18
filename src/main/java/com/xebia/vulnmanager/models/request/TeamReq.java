package com.xebia.vulnmanager.models.request;

import java.io.Serializable;

public class TeamReq implements Serializable {
    private String name;

    public TeamReq() {
        // empty ctor
    }

    public TeamReq(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
