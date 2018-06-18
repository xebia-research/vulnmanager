package com.xebia.vulnmanager.models.nmap.objects;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.xebia.vulnmanager.models.company.Team;
import com.xebia.vulnmanager.models.generic.GenericMultiReport;
import com.xebia.vulnmanager.models.generic.GenericReport;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Table(name = "NMapReport")
@Entity
public class NMapReport implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToOne(mappedBy = "nMapReportParent", cascade = CascadeType.ALL)
    @JsonManagedReference
    private NMapGeneralInformation scanData;

    @OneToMany(mappedBy = "nMapReportParent", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Host> hosts;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false) // Column that will be used to keep track of the parent
    @JsonBackReference // A backrefrence to keep json from infinite looping
    private Team team;

    public NMapReport() {
        // Do nothing
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

    public void setId(Long id) {
        this.id = id;
    }

    public GenericMultiReport getMultiReport() {
        GenericMultiReport multiReport = new GenericMultiReport();

        for (Host host : hosts) {
            GenericReport report = host.getGeneralReport();
            multiReport.addReports(report);
        }
        return multiReport;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
