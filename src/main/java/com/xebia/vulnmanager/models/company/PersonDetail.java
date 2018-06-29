package com.xebia.vulnmanager.models.company;

import java.io.Serializable;

public class PersonDetail implements Serializable {
    private String username;

    private Long id;

    private String apiKey;

    public PersonDetail(final String username, final Long id, final String apiKey) {
        this.username = username;
        this.id = id;
        this.apiKey = apiKey;
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

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

}
