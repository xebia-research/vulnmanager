package com.xebia.vulnmanager.models.company;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Team implements Serializable {
    private String name;
    private List<Person> teamMembers;

    public Team(final String name) {
        this.name = name;
        teamMembers = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Person> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(List<Person> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public void addTeamMember(Person p) {
        teamMembers.add(p);
    }
}
