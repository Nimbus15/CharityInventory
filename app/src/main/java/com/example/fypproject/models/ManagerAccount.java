package com.example.fypproject.models;

public class ManagerAccount extends Account{
    private String name, phone;
    private String password;
//    private String image;
//    private Type accountType;

    public ManagerAccount() {
    }

//    public Account(String name, String phone, String password, String image, Type accountType) {
//        this.name = name;
//        this.phone = phone;
//        this.password = password;
//        this.image = image;
//        this.accountType = accountType;
//    }

    public ManagerAccount(String name, String phone, String password) {
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

}
