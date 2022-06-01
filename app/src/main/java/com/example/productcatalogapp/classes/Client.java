package com.example.productcatalogapp.classes;

public class Client {

    private Integer id;
    private String name;
    private String town;
    private String email;
    private String address;
    private String mainPhoneNumber;
    private String secondPhoneNumber;

    public Client() {}

    public Client(Integer id, String name, String town, String email, String address, String mainPhoneNumber, String secondPhoneNumber) {
        this.setId(id);
        this.setName(name);
        this.setTown(town);
        this.setEmail(email);
        this.setAddress(address);
        this.setMainPhoneNumber(mainPhoneNumber);
        this.setSecondPhoneNumber(secondPhoneNumber);
    }

    public Client(String name, String town, String email, String address, String mainPhoneNumber, String secondPhoneNumber) {
        this.setTown(town);
        this.setName(name);
        this.setEmail(email);
        this.setAddress(address);
        this.setMainPhoneNumber(mainPhoneNumber);
        this.setSecondPhoneNumber(secondPhoneNumber);
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

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMainPhoneNumber() {
        return mainPhoneNumber;
    }

    public void setMainPhoneNumber(String mainPhoneNumber) {
        this.mainPhoneNumber = mainPhoneNumber;
    }

    public String getSecondPhoneNumber() {
        return secondPhoneNumber;
    }

    public void setSecondPhoneNumber(String secondPhoneNumber) {
        this.secondPhoneNumber = secondPhoneNumber;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", town='" + town + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", mainPhoneNumber='" + mainPhoneNumber + '\'' +
                ", secondPhoneNumber='" + secondPhoneNumber + '\'' +
                '}';
    }
}
