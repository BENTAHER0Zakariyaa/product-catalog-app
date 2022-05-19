package com.example.productcatalogapp.classes;

import java.util.ArrayList;

public class Product {

    private int id;
    private String label;
    private String description;
    private float price;
    private ArrayList<ProductImage> images;
    private Category category;

    // Constructors
    public Product(){
        this.images = new ArrayList<ProductImage>();
    }

    public Product(int id, String label, String description, float price, Category category) {
        this.setId(id);
        this.setLabel(label);
        this.setDescription(description);
        this.setPrice(price);
        this.setCategory(category);
        this.images = new ArrayList<ProductImage>();
    }

    public Product(String label, String description, float price, Category category) {
        this.setLabel(label);
        this.setDescription(description);
        this.setPrice(price);
        this.setCategory(category);
        this.images = new ArrayList<ProductImage>();
    }

    // Getters / Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    // Methods
    public void addImage(ProductImage image){
        this.images.add(image);
    }

}
