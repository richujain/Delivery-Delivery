package com.example.deliverydeliverycourier;

public class ModelClass {
    String status;
    String date;
    String imageurl;
    String offeredAmount;
    String key;

    public ModelClass() {
    }

    public ModelClass(String status, String date, String imageurl, String offeredAmount,String key) {
        this.status = status;
        this.date = date;
        this.imageurl = imageurl;
        this.offeredAmount = offeredAmount;
        this.key = key;
    }

    public String getStatus() {
        return status;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getOfferedAmount() {
        return offeredAmount;
    }

    public void setOfferedAmount(String offeredAmount) {
        this.offeredAmount = offeredAmount;
    }
}
