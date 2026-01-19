package com.abc.sunshine.entity;

import java.io.Serializable;

public class Brand implements Serializable {

    private Long id;
    private String name;
    private String description;

    public Brand() {
    }

    public Brand(String name, String description) {
        this.name = name;
        this.description = description;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
