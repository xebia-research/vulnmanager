package com.xebia.vulnmanager.models.company;

import java.io.Serializable;

public class Person implements Serializable {
    private String name;

    public Person(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
