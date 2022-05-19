package com.example.productcatalogapp.classes;

import java.time.LocalDateTime;

public class User {

    private int id;
    private String username;
    private String password;
    private LocalDateTime lastLogin;

    public User() {}

    public User(int id, String username, String password, LocalDateTime lastLogin) {
        this.setId(id);
        this.setUsername(username);
        this.setPassword(password);
        this.setLastLogin(lastLogin);
    }

    public User(String username, String password, LocalDateTime lastLogin) {
        this.setUsername(username);
        this.setPassword(password);
        this.setLastLogin(lastLogin);
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Override
    public String toString() {
        return "User (id=" + id + ", username='" + username + "', password='" + password + "', lastLogin=" + lastLogin + ")";
    }
}
