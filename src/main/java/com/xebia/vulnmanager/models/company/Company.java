package com.xebia.vulnmanager.models.company;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Company implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String authKey = "testauth";
    private String name;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Team> teams;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Person> employees;

    protected Company() {

    }

    public Company(final String name) {
        this.name = name;
        teams = new ArrayList<>();
        this.employees = new ArrayList<>();
    }

    @JsonIgnore
    @XmlTransient
    public String getAuthKey() {
        return authKey;
    }

    @JsonIgnore
    @XmlTransient
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Person> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Person> employees) {
        this.employees = employees;
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
