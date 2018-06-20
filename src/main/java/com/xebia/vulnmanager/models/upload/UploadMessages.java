package com.xebia.vulnmanager.models.upload;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UploadMessages implements Serializable {
    private List<String> successfullyUploadedMsgs = new ArrayList<>();
    private List<String> errorUploadingMsgs = new ArrayList<>();

    public List<String> getSuccessfullyUploadedMsgs() {
        return successfullyUploadedMsgs;
    }

    public void setSuccessfullyUploadedMsgs(List<String> successfullyUploadedMsgs) {
        this.successfullyUploadedMsgs = successfullyUploadedMsgs;
    }

    public void setErrorUploadingMsgs(List<String> errorUploadingMsgs) {
        this.errorUploadingMsgs = errorUploadingMsgs;
    }

    public List<String> getErrorUploadingMsgs() {
        return errorUploadingMsgs;
    }
}
