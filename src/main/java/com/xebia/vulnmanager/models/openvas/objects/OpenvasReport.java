package com.xebia.vulnmanager.models.openvas.objects;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class OpenvasReport implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String fileId;
    private String timeDone;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OvResult> results;

    public OpenvasReport() {
        results = new ArrayList<>();
    }


    public List<OvResult> getResults() {
        return results;
    }

    public void setResults(List<OvResult> results) {
        this.results = results;
    }

    public void addResult(OvResult result) {
        results.add(result);
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getTimeDone() {
        return timeDone;
    }

    public void setTimeDone(String timeDone) {
        this.timeDone = timeDone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        StringBuffer resultsInfo = new StringBuffer();
        for (OvResult result : results) {
            resultsInfo.append(result);
        }

        return "OpenvasReport{"
                +
                "results found= " + results.size()
                +
                "results=" + resultsInfo
                +
                '}';
    }
}
