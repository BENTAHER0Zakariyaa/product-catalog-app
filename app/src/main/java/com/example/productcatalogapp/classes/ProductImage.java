package com.example.productcatalogapp.classes;

public class ProductImage {

    private int id;
    private String path;

    public ProductImage(){}

    public ProductImage(int id, String path) {
        this.setId(id);
        this.setPath(path);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
