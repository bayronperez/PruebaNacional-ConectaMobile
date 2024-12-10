package com.example.conectamobile;

public class User {
    private String name;
    private String email;

    // Constructor vacio necesario para Firebase
    public User() {}

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
