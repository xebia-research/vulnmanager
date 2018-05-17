package com.xebia.vulnmanager.models.openvas.requests;

import java.io.Serializable;

public class NvtRequest implements Serializable {
    private String userId;
    private boolean isFalsePositive;

    public NvtRequest() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isFalsePositive() {
        return isFalsePositive;
    }

    public void setFalsePositive(boolean falsePositive) {
        isFalsePositive = falsePositive;
    }
}
