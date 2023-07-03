package com.example.fypproject.models;

import java.io.Serializable;
import java.util.Date;

public class Transaction implements Serializable {
    private int itemId;
    private String tID;
    private String desc;
    private String date;
    private int quantity;

    public Transaction() {

    }

    public Transaction(int ID, String tID, String desc,
                       String dateOfTransaction, int quantity) {
        this.itemId = ID;
        this.tID = tID;
        this.desc = desc;
        this.date = dateOfTransaction;
        this.quantity = quantity;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void settID(String tID) {
        this.tID = tID;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getItemId() {
        return itemId;
    }

    public String gettID() {
        return tID;
    }

    public String getDesc() {
        return desc;
    }

    public String getDate() {
        return date;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "itemId=" + itemId +
                ", tID='" + tID + '\'' +
                ", desc='" + desc + '\'' +
                ", date='" + date + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
