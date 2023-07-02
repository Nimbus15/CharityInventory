package com.example.fypproject.models;

import java.io.Serializable;
import java.util.Date;

public class Transaction implements Serializable {
    private int ID;
    private String tID;
    private String desc;
    private Date dateOfTransaction;

    public Transaction() {

    }

    public Transaction(int ID, String tID, String desc,
                       Date dateOfTransaction, int quantity) {
        this.ID = ID;
        this.tID = tID;
        this.desc = desc;
        this.dateOfTransaction = dateOfTransaction;
        this.quantity = quantity;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void settID(String tID) {
        this.tID = tID;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setDateOfTransaction(Date dateOfTransaction) {
        this.dateOfTransaction = dateOfTransaction;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getID() {
        return ID;
    }

    public String gettID() {
        return tID;
    }

    public String getDesc() {
        return desc;
    }

    public Date getDateOfTransaction() {
        return dateOfTransaction;
    }

    public int getQuantity() {
        return quantity;
    }

    private int quantity;
}
