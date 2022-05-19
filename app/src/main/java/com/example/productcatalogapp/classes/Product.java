package com.example.productcatalogapp.classes;

import java.util.ArrayList;

public class Product {

    private int id;
    private String label;
    private String description;
    private float price;
    private ArrayList<String> images;

    public Product(){
        this.images = new ArrayList<String>();
    }

    public Product(int id, String label, String description, float price) {
        this.setId(id);
        this.setLabel(label);
        this.setDescription(description);
        this.setPrice(price);
        this.images = new ArrayList<String>();
    }

    public Product(String label, String description, float price) {
        this.setLabel(label);
        this.setDescription(description);
        this.setPrice(price);
        this.images = new ArrayList<String>();
    }

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

    public void addImage(String imageUrl){
        this.images.add(imageUrl);
    }

}
