package com.example.productcatalogapp.classes;

public class Category {

    private Integer id;
    private Integer parentId;
    private String name;

    public Category(){}

    public Category(Integer id, String name, Integer parentId){
        this.setId(id);
        this.setName(name);
        this.setParentId(parentId);
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
