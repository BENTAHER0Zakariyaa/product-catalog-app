package com.example.productcatalogapp.classes;

public class ProductImage {

    private Integer id;
    private Integer productId;
    private String fileName;
    private String filePath;
    private String name;
    private String path;

    public ProductImage(){}

    public ProductImage(Integer id, Integer productId, String fileName, String filePath, String name, String path) {
        this.setId(id);
        this.setProductId(productId);
        this.setFileName(fileName);
        this.setFilePath(filePath);
        this.setName(name);
        this.setPath(path);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
