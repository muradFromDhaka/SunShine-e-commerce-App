package com.abc.sunshine.entity;

import java.util.List;

public class Product {

    private Long id;
    private String name;
    private String description;
    private double price;
    private double discountPrice;
    private String sku;
    private int reviewsCount;
    private int quantity;
    private long categoryId;
    private long brandId;
    private List<String> imageUrls;
    private boolean isActive;

    // Default constructor
    public Product() {
    }

    // Constructor without ID (INSERT)
    public Product(
            String name,
            String description,
            double price,
            double discountPrice,
            String sku,
            int reviewsCount,
            int quantity,
            long categoryId,
            long brandId,
            List<String> imageUrls,
            boolean isActive
    ) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.discountPrice = discountPrice;
        this.sku = sku;
        this.reviewsCount = reviewsCount;
        this.quantity = quantity;
        this.categoryId = categoryId;
        this.brandId = brandId;
        this.imageUrls = imageUrls;
        this.isActive = isActive;
    }

    // Constructor with ID (FETCH)
    public Product(
            Long id,
            String name,
            String description,
            double price,
            double discountPrice,
            String sku,
            int reviewsCount,
            int quantity,
            long categoryId,
            long brandId,
            List<String> imageUrls,
            boolean isActive
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.discountPrice = discountPrice;
        this.sku = sku;
        this.reviewsCount = reviewsCount;
        this.quantity = quantity;
        this.categoryId = categoryId;
        this.brandId = brandId;
        this.imageUrls = imageUrls;
        this.isActive = isActive;
    }

    // -------- getters & setters --------


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public String getSku() {
        return sku;
    }

    public int getReviewsCount() {
        return reviewsCount;
    }

    public int getQuantity() {
        return quantity;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public long getBrandId() {
        return brandId;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public boolean isActive() {
        return isActive;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setReviewsCount(int reviewsCount) {
        this.reviewsCount = reviewsCount;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public void setBrandId(long brandId) {
        this.brandId = brandId;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
