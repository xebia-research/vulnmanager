package com.xebia.vulnmanager.models.company;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.xebia.vulnmanager.models.comments.Comment;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Person implements Serializable {
    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany(mappedBy = "teamMembers")
    @JsonBackReference
    private List<Team> projects;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false) // Column that will be used to keep track of the parent
    @JsonBackReference // A backrefrence to keep json from infinite looping
    private Company company;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    //@JsonManagedReference
    @JsonBackReference
    private List<Comment> comments = new ArrayList<>();


    public Person() {
        this.name = "NO NAME SET";
        this.projects = new ArrayList<>();
    }

    public Person(final String name) {
        this.name = name;
        this.projects = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Team> getProjects() {
        return projects;
    }

    public void addTeam(Team t) {
        projects.add(t);
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Company getCompany() {
        return company;
    }
}
