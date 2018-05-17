package com.tekapic;

import java.io.Serializable;

public class User implements Serializable {

//    private static final long serialVersionUID = 9178463713495656548L;
    private String mobileNumber;
    private String email;
    private String username;
    private String fullName;
    private String password;

    public User() {
        this.mobileNumber = "none";
        this.email = "none";
        this.username = "none";
        this.fullName = "none";
        this.password = "none";
    }

    public User(String mobileNumber, String email, String username, String fullName, String password) {
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.username = username;
        this.fullName = fullName;
        this.password = password;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}