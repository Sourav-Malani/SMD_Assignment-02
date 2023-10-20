package com.ass2.i190434_190528.Helper;


public class HelperClass {

    String name, email, username, password, country, city, phone;

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

    public String getCountry() {
        return country;
    }
    public void setCountry(String country){this.country = country;}

    public void setCity(String city){this.city = city;}
    public String getCity() {
        return city;
    }

    public void setPhone(String phone){this.city = phone;}
    public String getPhone() {
        return phone;
    }

    public HelperClass(String name, String email, String username, String password, String country, String city, String phone) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.country = country;
        this.city = city;
        this.phone = phone;
    }

    public HelperClass() {
    }
}
