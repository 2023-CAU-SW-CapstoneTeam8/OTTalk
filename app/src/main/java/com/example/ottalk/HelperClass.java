package com.example.ottalk;

public class HelperClass {

    String name, email, password, state;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public HelperClass(String name, String email, String password, String state) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.state = state;
    }

    public HelperClass() {
    }

}
