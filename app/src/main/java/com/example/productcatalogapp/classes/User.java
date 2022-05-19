package com.example.productcatalogapp.classes;

import java.time.LocalDateTime;

public class User {

    private int id;
    private String username;
    private String token;
    private Boolean isConnected;

    public User() {}

    public User(int id, String username, String token, Boolean isConnected) {
        this.setId(id);
        this.setUsername(username);
        this.setToken(token);
        this.setConnected(isConnected);
    }

    public User(String username, String token, Boolean isConnected) {
        this.setUsername(username);
        this.setToken(token);
        this.setConnected(isConnected);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
