package com.xebia.vulnmanager.nmap.objects;

import java.io.Serializable;

public class ExtraReason implements Serializable {
    private String reason;
    private String count;

    public String getReason() {
        return reason;
    }

    public String getCount() {
        return count;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
