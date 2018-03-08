package com.xebia.vulnmanager.models.company;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Company implements Serializable {
    // transient means don't serialize so it won't show in json respons
    private transient String authKey = "testauth";
    private String name;
    private ArrayList<Team> teams;

    public Company(final String name) {
        this.name = name;
        teams = new ArrayList<>();
    }

    @JsonIgnore
    @XmlTransient
    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(ArrayList<Team> teams) {
        this.teams = teams;
    }

    public void addTeam(Team team) {
        this.teams.add(team);
    }

    public Team findTeamByName(String name) {
        for (Team team : teams) {
            if (team.getName().equals(name)) {
                return team;
            }
        }
        return null;
    }
}
