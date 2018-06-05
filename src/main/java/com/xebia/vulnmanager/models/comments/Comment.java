package com.xebia.vulnmanager.models.comments;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.xebia.vulnmanager.models.company.Person;
import com.xebia.vulnmanager.models.generic.GenericResult;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class Comment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Column that will be used to keep track of the parent
    //@JsonBackReference // A backrefrence to keep json from infinite looping
    private Person user = new Person();

    @Column(columnDefinition = "text")
    private String content;
    //private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "result_id", nullable = false) // Column that will be used to keep track of the parent
    @JsonBackReference // A backrefrence to keep json from infinite looping
    private GenericResult parent;

    public Comment() {
        // Empty ctor
    }

    public Person getUser() {
        return user;
    }

    public void setUser(Person user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GenericResult getParent() {
        return parent;
    }

    public void setParent(GenericResult parent) {
        this.parent = parent;
    }
}
