package com.t7.consumer;
import com.google.gson.annotations.SerializedName;


public class CardItem {
    @SerializedName("Name")

    private String name;
    @SerializedName("Price")

    private String price;
    @SerializedName("Description")

    private String description;
    @SerializedName("Manufacturer")
    private String manufacturer;

    public CardItem(String name, String price, String description, String manufacturer) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.name = manufacturer;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }
    public String getManufacturer() {
        return manufacturer;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
}

