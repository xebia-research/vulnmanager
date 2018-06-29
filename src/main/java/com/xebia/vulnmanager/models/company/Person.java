package com.xebia.vulnmanager.models.company;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xebia.vulnmanager.models.comments.Comment;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Person implements Serializable {
    private String username;
    private String password;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(columnDefinition = "text")
    private String apiKey;

    @ManyToMany(mappedBy = "teamMembers")
    @JsonBackReference
    private List<Team> projects;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = true) // Column that will be used to keep track of the parent
    @JsonBackReference // A back reference to keep json from infinite looping
    private Company company;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    //@JsonManagedReference
    @JsonBackReference
    private List<Comment> comments = new ArrayList<>();


    public Person() {
        this.username = "NO NAME SET";
        this.projects = new ArrayList<>();
    }

    public Person(final String username,
                  final String password,
                  final Company company) {
        this.username = username;
        this.password = password;
        this.setCompany(company);
    }

    public Person(final String username) {
        this.username = username;
        this.projects = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonIgnore
    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @JsonIgnore
    public PersonDetail getDetailedPerson() {
        return new PersonDetail(username, id, apiKey);
    }

}
