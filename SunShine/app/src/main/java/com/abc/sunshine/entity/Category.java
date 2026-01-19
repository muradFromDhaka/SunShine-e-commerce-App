package com.abc.sunshine.entity;

import java.io.Serializable;

public class Category implements Serializable {
    private long id;
    private String name;
    private String description;
    private long brandId;

    private String imageUrl; // 🔹 single image

    public Category() { }

    public Category(String name, String description, long brandId, String imageUrl) {
        this.name = name;
        this.description = description;
        this.brandId = brandId;
        this.imageUrl = imageUrl;
    }

    // Getters & Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public long getBrandId() { return brandId; }
    public void setBrandId(long brandId) { this.brandId = brandId; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}