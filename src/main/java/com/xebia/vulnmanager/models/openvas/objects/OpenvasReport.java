package com.xebia.vulnmanager.models.openvas.objects;

import java.io.Serializable;
import java.util.ArrayList;


public class OpenvasReport implements Serializable {
    private String id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
