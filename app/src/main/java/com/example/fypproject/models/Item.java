package com.example.fypproject.models;

import java.io.Serializable;

public class Item implements Serializable {
    private int ID;
    private String name;
    private String desc;
    private String category;
    private int quantity;
    private int minQuantity;
    private String brand;
    private String barcode;
    private String image;
    private String notes;
    private float price;
    private String approved;

    public Item() {
    }

    public Item(int ID, String name, String desc, String category,
                int quantity, int minQuantity, String brand, String barcode, String image, String notes, float price, String approved) {
        this.ID = ID;
        this.name = name;
        this.desc = desc;
        this.category = category;
        this.quantity = quantity;
        this.minQuantity = minQuantity;
        this.brand = brand;
        this.barcode = barcode;
        this.image = image;
        this.notes = notes;
        this.price = price;
        this.approved = approved;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(int minQuantity) {
        this.minQuantity = minQuantity;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getImage() {
        if(image == null)
            return  "";
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    @Override
    public String toString() {
        return "Item{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", category='" + category + '\'' +
                ", quantity=" + quantity +
                ", minQuantity=" + minQuantity +
                ", brand='" + brand + '\'' +
                ", barcode='" + barcode + '\'' +
                ", image=" + image +
                ", notes='" + notes + '\'' +
                ", price=" + price +
                ", approved='" + approved + '\'' +
                '}';
    }
}
