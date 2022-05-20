package com.example.productcatalogapp.classes;

import java.time.LocalDateTime;

public class User {

    private Integer id;
    private String username;
    private String password;
    private String token;
    private Boolean isConnected;

    public User() {}

    public User(Integer id, String username, String password,  String token, Boolean isConnected) {
        this.setId(id);
        this.setUsername(username);
        this.setPassword(password);
        this.setToken(token);
        this.setConnected(isConnected);
    }

    public User(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }

    public User(String username, String token, Boolean isConnected) {
        this.setUsername(username);
        this.setPassword(password);
        this.setToken(token);
        this.setConnected(isConnected);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getConnected() {
        return isConnected;
    }

    public void setConnected(Boolean connected) {
        isConnected = connected;
    }

    @Override
    public String toString() {
        return "User (id=" + this.getId() + ", username='" + this.getUsername() + "', token='" + this.getToken() + "', isConnected=" + this.getConnected() + ")";
    }
}
