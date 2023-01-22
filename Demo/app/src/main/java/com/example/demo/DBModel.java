package com.example.demo;

import android.graphics.Bitmap;

public class DBModel {

    private int id;
    private String name, number;
    private Bitmap image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }
}
