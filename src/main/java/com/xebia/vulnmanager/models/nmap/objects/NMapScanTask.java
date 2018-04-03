package com.xebia.vulnmanager.models.nmap.objects;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
import javax.persistence.GenerationType;
import java.io.Serializable;

@Table(name = "NMapScanTask")
@Entity
public class NMapScanTask implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToOne
    @JoinColumn(name = "scan_info_id") // Column that will be used to keep track of the parent
    @JsonBackReference // A back reference to keep json from infinite looping
    private NMapScanInfo nMapScanInfo;

    private String taskName;
    private String taskExtraInfo;

    public NMapScanTask() {
        // Default Constructor
    }

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

    public Long getId() {
        return id;
    }

    @JsonBackReference // A backrefrence to keep json from infinite looping
    public NMapScanInfo getnMapScanInfo() {
        return nMapScanInfo;
    }

    public void setNMapScanInfo(NMapScanInfo nMapScanInfo) {
        this.nMapScanInfo = nMapScanInfo;
    }
}
