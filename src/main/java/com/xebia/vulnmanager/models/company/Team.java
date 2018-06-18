package com.xebia.vulnmanager.models.company;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.xebia.vulnmanager.models.openvas.objects.OpenvasReport;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Team implements Serializable {
    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false) // Column that will be used to keep track of the parent
    @JsonBackReference // A backrefrence to keep json from infinite looping
    private Company company;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "team_person",
            joinColumns = @JoinColumn(name = "team_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "person_id", referencedColumnName = "id")
    )
    private List<Person> teamMembers;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<OpenvasReport> reports;

    protected Team() {
        // Do nothing
    }

    public Team(final String name) {
        this.name = name;
        teamMembers = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<Person> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(List<Person> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public boolean addTeamMember(Person p) {
        boolean added = false;

        for (Person person : teamMembers) {
            if (person.getId().equals(p.getId())) {
                return added;
            }
        }

        p.addTeam(this);
        teamMembers.add(p);
        added = true;
        return added;
    }

}
