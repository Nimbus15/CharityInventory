package com.example.fypproject.models;

import com.example.fypproject.enums.Type;

public class Account {
    private String name, phone;
    private String password;

    public Account() {
    }

    public Account(String name, String phone, String password) {
        this.name = name;
        this.phone = phone;
        this.password = password;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Account{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
