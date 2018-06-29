package com.xebia.vulnmanager.models.generic;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.xebia.vulnmanager.models.comments.Comment;
import com.xebia.vulnmanager.util.ReportType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class GenericResult implements Serializable {
    private static final String TEXTCOLDEF = "text";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "report_id", nullable = false) // Column that will be used to keep track of the parent
    @JsonBackReference // A backrefrence to keep json from infinite looping
    private GenericReport report;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Comment> comments = new ArrayList<>();

    @Column(columnDefinition = TEXTCOLDEF)
    private ReportType type;
    @Column(columnDefinition = TEXTCOLDEF)
    private String name;
    @Column(columnDefinition = TEXTCOLDEF)
    private String description;
    @Column(columnDefinition = TEXTCOLDEF)
    private String info;
    @Column(columnDefinition = TEXTCOLDEF)
    private String cve;
    @Column(columnDefinition = TEXTCOLDEF)
    private String severity;
    @Column(columnDefinition = TEXTCOLDEF)
    private String port;
    @Column(columnDefinition = TEXTCOLDEF)
    private String knownSolution;
    @Column(columnDefinition = TEXTCOLDEF)
    private String url;
    @Column(columnDefinition = "boolean")
    private boolean falsePositive;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReportType getType() {
        return type;
    }

    public void setType(ReportType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getCve() {
        return cve;
    }

    public void setCve(String cve) {
        this.cve = cve;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getKnownSolution() {
        return knownSolution;
    }

    public void setKnownSolution(String knownSolution) {
        this.knownSolution = knownSolution;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public GenericReport getReport() {
        return report;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setReport(GenericReport report) {
        this.report = report;
    }

    public boolean isFalsePositive() {
        return falsePositive;
    }

    public void setFalsePositive(boolean falsePositive) {
        this.falsePositive = falsePositive;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;

        for (Comment comment : comments) {
            comment.setParent(this);
        }
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }
}
