package com.xebia.vulnmanager.models.openvas.objects;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;

@Entity
public class OpenvasReport implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String fileId;
    private String timeDone;
    private ArrayList<OvResult> results;

    public OpenvasReport() {
        results = new ArrayList<OvResult>();
    }

    public ArrayList<OvResult> getResults() {
        return results;
    }

    public void setResults(ArrayList<OvResult> results) {
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

    @Override
    public String toString() {
        StringBuffer resultsInfo = new StringBuffer();
        for (int i = 0; i < results.size(); i++) {
            OvResult res = results.get(i);
            resultsInfo.append(res.toString());
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
