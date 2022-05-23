package com.example.productcatalogapp.classes;

public class ProductImage {

    private Integer id;
    private String fileName;
    private String filePath;
    private String name;
    private String path;

    public ProductImage(){}

    public ProductImage(Integer id, String fileName, String filePath, String name, String path) {
        this.setId(id);
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
