package com.xebia.vulnmanager.models.comments;

import com.xebia.vulnmanager.models.company.Person;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Comment implements Serializable {
    private Person user;
    private String content;
    private LocalDateTime createdAt;

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
}
