package com.xebia.vulnmanager.models.openvas.requests;

import java.io.Serializable;

public class NvtRequest implements Serializable {
    private String userId;
    private boolean falsePositive;

    public NvtRequest() {
        // empty ctor
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isFalsePositive() {
        return falsePositive;
    }

    public void setFalsePositive(boolean falsePositive) {
        this.falsePositive = falsePositive;
    }
}
