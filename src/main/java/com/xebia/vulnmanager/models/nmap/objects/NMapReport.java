package com.xebia.vulnmanager.models.nmap.objects;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.OneToMany;
import javax.persistence.GenerationType;
import javax.persistence.CascadeType;
import java.io.Serializable;
import java.util.List;

@Table(name = "NMapReport")
@Entity
public class NMapReport implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private NMapGeneralInformation scanData;

    @OneToMany(mappedBy = "nMapReportParent", cascade = CascadeType.ALL)
    private List<Host> hosts;

    public NMapReport() {
    }

    public NMapReport(final NMapGeneralInformation scanData, final List<Host> hosts) {
        this.scanData = scanData;
        this.hosts = hosts;
    }

    public void setHosts(List<Host> hosts) {
        this.hosts = hosts;
    }

    public void setScanData(NMapGeneralInformation scanData) {
        this.scanData = scanData;
    }

    public NMapGeneralInformation getScanData() {
        return scanData;
    }

    @JacksonXmlElementWrapper(localName = "hosts")
    @JacksonXmlProperty(localName = "host")
    public List<Host> getHosts() {
        return hosts;
    }

    public Long getId() {
        return id;
    }
}
