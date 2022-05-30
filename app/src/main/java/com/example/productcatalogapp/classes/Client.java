package com.example.productcatalogapp.classes;

public class Client {

    private Integer id;
    private String fullName;
    private String city;
    private String address;
    private String mainPhoneNumber;
    private String secondPhoneNumber;
    private String moreInformation;


    public Client() {}

    public Client(Integer id, String fullName, String city, String address, String mainPhoneNumber, String secondPhoneNumber, String moreInformation) {
        this.setId(id);
        this.setFullName(fullName);
        this.setCity(city);
        this.setAddress(address);
        this.setMainPhoneNumber(mainPhoneNumber);
        this.setSecondPhoneNumber(secondPhoneNumber);
        this.setMoreInformation(moreInformation);
    }

    public Client(String fullName, String city, String address, String mainPhoneNumber, String secondPhoneNumber, String moreInformation) {
        this.setCity(city);
        this.setFullName(fullName);
        this.setAddress(address);
        this.setMainPhoneNumber(mainPhoneNumber);
        this.setSecondPhoneNumber(secondPhoneNumber);
        this.setMoreInformation(moreInformation);
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getMoreInformation() {
        return moreInformation;
    }

    public void setMoreInformation(String moreInformation) {
        this.moreInformation = moreInformation;
    }



}
