package com.xebia.vulnmanager.models.nmap.objects;

import java.io.Serializable;

public class ExtraReason implements Serializable {
    private String reason;
    private String count;

    public ExtraReason(final String reason, final String count) {
        this.reason = reason;
        this.count = count;
    }

    public String getReason() {
        return reason;
    }

    public String getCount() {
        return count;
    }
}
