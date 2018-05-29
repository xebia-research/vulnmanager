package com.xebia.vulnmanager.models.openvas.objects;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.xebia.vulnmanager.models.generic.GenericResult;
import com.xebia.vulnmanager.util.ReportType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "OpenvasResults")
@Entity
public class OvResult implements Serializable {

    @ManyToOne
    @JoinColumn(name = "report_id", nullable = false) // Column that will be used to keep track of the parent
    @JsonBackReference // A backrefrence to keep json from infinite looping
    private OpenvasReport report;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String port;
    private String name;
    // Specifiy columnDefenition to text to store text with more then 255 chars
    @Column(columnDefinition = "text")
    private String description;
    private String threat;
    private String severity;

    @OneToOne(mappedBy = "result", cascade = CascadeType.ALL)
    private NetworkVulnerabilityTest nvt;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThreat() {
        return threat;
    }

    public void setThreat(String threat) {
        this.threat = threat;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public NetworkVulnerabilityTest getNvt() {
        return nvt;
    }

    public void setNvt(NetworkVulnerabilityTest nvt) {
        this.nvt = nvt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public OpenvasReport getReport() {
        return report;
    }

    public void setResultId(OpenvasReport report) {
        this.report = report;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public GenericResult getGenericResult() {
        GenericResult result = new GenericResult();
        result.setType(ReportType.OPENVAS);
        result.setCve(nvt.getCve());
        result.setDescription(description);
        result.setThread(threat);
        result.setName(getName());
        result.setPort(port);
        return result;
    }

    @Override
    public String toString() {
        return "OvResult{"
                + "port='"
                + port
                + '\''
                + ", name='"
                + name
                + '\''
                + "description= " + description
                + "NetworkVulnerabilityTest= " + nvt.toString()
                + '}';
    }
}
