package com.xebia.vulnmanager.models.company;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class PersonLogin implements Serializable {
    private String username;
    private String password;
    private String companyName;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public PersonLogin() {
    }

    public PersonLogin(final String username, final String password, final String companyName) {
        this.username = username;
        this.password = password;
        this.companyName = companyName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
