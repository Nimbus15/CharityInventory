package com.example.fypproject.models;

import java.util.Date;

public class Transaction {
    private int ID;
    private String name;
    private String desc;
    private Date dateOfTransaction;
    private int quantity;

    public Transaction() {

    }

    public Transaction(int ID, String name, String desc,
                       Date dateOfTransaction, int quantity) {
        this.ID = ID;
        this.name = name;
        this.desc = desc;
        this.dateOfTransaction = dateOfTransaction;
        this.quantity = quantity;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getDateOfTransaction() {
        return dateOfTransaction;
    }

    public void setDateOfTransaction(Date dateOfTransaction) {
        this.dateOfTransaction = dateOfTransaction;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", dateOfTransaction=" + dateOfTransaction +
                ", quantity=" + quantity +
                '}';
    }
}
