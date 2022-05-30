package com.example.productcatalogapp.classes;

public class CommandLine {

    private Integer id;
    private Command command;
    private Product product;
    private Integer quantity;

    public CommandLine() {}

    public CommandLine(Integer id, Command command, Product product, Integer quantity) {
        this.setId(id);
        this.setCommand(command);
        this.setProduct(product);
        this.setQuantity(quantity);
    }

    public CommandLine(Command command, Product product, Integer quantity) {
        this.setCommand(command);
        this.setProduct(product);
        this.setQuantity(quantity);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
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
}
