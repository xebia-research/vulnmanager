package com.xebia.vulnmanager.nmap.objects;

import java.io.Serializable;

public class NMapScanTask implements Serializable {
    private String taskName;
    private String taskExtraInfo;

    public NMapScanTask(final String taskName, final String taskExtraInfo) {
        this.taskName = taskName;
        this.taskExtraInfo = taskExtraInfo;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskExtraInfo() {
        return taskExtraInfo;
    }
}
