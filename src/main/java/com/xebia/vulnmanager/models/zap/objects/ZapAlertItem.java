package com.xebia.vulnmanager.models.zap.objects;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Table(name = "ZapAlertItem")
@Entity
public class ZapAlertItem implements Serializable {
    private static final String TEXT = "text";
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "report_id", nullable = false) // Column that will be used to keep track of the parent
    @JsonBackReference // A backrefrence to keep json from infinite looping
    private ZapReport zapReport;

    @Column(columnDefinition = TEXT)
    private String name;
    private int riskCode;
    private int confidence;

    @Column(columnDefinition = TEXT)
    private String shortDescription;
    @Column(columnDefinition = TEXT)
    private String description;

    @OneToMany(mappedBy = "zapAlertItem", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<RiskInstance> instance;
    private int instanceCount;

    @Column(columnDefinition = TEXT)
    private String solution;
    @Column(columnDefinition = TEXT)
    private String otherInfo;

    @Column(columnDefinition = TEXT)
    private String reference;
    private int cweId;
    private int wascId;
    private int sourceId;

    public ZapAlertItem() {
        // Empty constructor
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZapReport getZapReport() {
        return zapReport;
    }

    public void setZapReport(ZapReport zapReport) {
        this.zapReport = zapReport;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRiskCode() {
        return riskCode;
    }

    public void setRiskCode(int riskCode) {
        this.riskCode = riskCode;
    }

    public int getConfidence() {
        return confidence;
    }

    public void setConfidence(int confidence) {
        this.confidence = confidence;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<RiskInstance> getInstance() {
        return instance;
    }

    public void setInstance(List<RiskInstance> instance) {
        this.instance = instance;
    }

    public int getInstanceCount() {
        return instanceCount;
    }

    public void setInstanceCount(int instanceCount) {
        this.instanceCount = instanceCount;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public int getCweId() {
        return cweId;
    }

    public void setCweId(int cweId) {
        this.cweId = cweId;
    }

    public int getWascId() {
        return wascId;
    }

    public void setWascId(int wascId) {
        this.wascId = wascId;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }
}
