package com.example.productcatalogapp.classes;

public class CartLine {

    private Product product;
    private Integer quantity;

    public CartLine() {}

    public CartLine(Product product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public float getTotalPrice(){
        return quantity * product.getPrice();
    }


    public void IncrementQuantity(){
        this.quantity++;
    }
    public void DecrementQuantity(){
        this.quantity--;
    }

}
