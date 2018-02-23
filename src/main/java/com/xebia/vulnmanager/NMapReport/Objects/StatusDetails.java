package com.xebia.vulnmanager.NMapReport.Objects;

import java.io.Serializable;

/**
 * StatusDetails Serializable.
 */
public class StatusDetails implements Serializable {
    private String state;
    private String reason;
    private String reasonTtl;

    public StatusDetails(String state, String reason, String reasonTtl) {
        this.state = state;
        this.reason = reason;
        this.reasonTtl = reasonTtl;
    }

    public String getState() {
        return state;
    }

    public String getReason() {
        return reason;
    }

    public String getReasonTtl() {
        return reasonTtl;
    }
}
