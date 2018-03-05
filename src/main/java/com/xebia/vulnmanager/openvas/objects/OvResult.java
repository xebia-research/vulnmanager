package com.xebia.vulnmanager.openvas.objects;

import java.io.Serializable;

public class OvResult implements Serializable {
    private String port;
    private String name;
    private String description;
    private String threat;
    private String severity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThreat() {
        return threat;
    }

    public void setThreat(String threat) {
        this.threat = threat;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    @Override
    public String toString() {
        return "OvResult{"
                + "port='"
                + port
                + '\''
                + ", name='"
                + name
                + '\''
                + "description= " + description
                + '}';
    }
}
