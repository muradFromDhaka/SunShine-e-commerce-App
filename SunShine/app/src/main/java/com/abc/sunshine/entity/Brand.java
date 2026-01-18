package com.abc.sunshine.entity;

public class Brand extends BaseEntity {

    private String name;
    private String description;
    private String logoUrl;

    public Brand() {
    }

    public Brand(String name, String description, String logoUrl) {
        this.name = name;
        this.description = description;
        this.logoUrl = logoUrl;
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

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
}
