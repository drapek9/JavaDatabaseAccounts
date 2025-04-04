package com.example;

public class User {
    private int id;
    private String name;
    private String username;
    private String password;

    public String getPassword() {
        return password;
    }
    public User setPassword(String password) {
        this.password = password;
        return this;
    }
    public String getUsername() {
        return username;
    }
    public User setUsername(String username) {
        this.username = username;
        return this;
    }
    public int getId() {
        return id;
    }
    public User setId(int id) {
        this.id = id;
        return this;
    }
    
    public String getName() {
        return name;
    }
    public User setName(String name) {
        this.name = name;
        return this;
    }
}
