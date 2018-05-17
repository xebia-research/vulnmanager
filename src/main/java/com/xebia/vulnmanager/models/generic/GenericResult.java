package com.xebia.vulnmanager.models.generic;

import com.xebia.vulnmanager.util.ReportType;

import java.io.Serializable;

public class GenericResult implements Serializable {
    private Long id;
    private ReportType type;
    private String name;
    private String description;
    private String info;
    private String cve;
    private String thread;
    private String port;
    private String knownSolution;
    private String url;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReportType getType() {
        return type;
    }

    public void setType(ReportType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getCve() {
        return cve;
    }

    public void setCve(String cve) {
        this.cve = cve;
    }

    public String getThread() {
        return thread;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getKnownSolution() {
        return knownSolution;
    }

    public void setKnownSolution(String knownSolution) {
        this.knownSolution = knownSolution;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
