package com.xebia.vulnmanager.nmap.objects;

import java.io.Serializable;

/**
 * StatusDetails Serializable.
 */
public class StatusDetails implements Serializable {
    private String state;
    private String reason;
    private String reasonTtl;

    public StatusDetails(final String state, final String reason, final String reasonTtl) {
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
