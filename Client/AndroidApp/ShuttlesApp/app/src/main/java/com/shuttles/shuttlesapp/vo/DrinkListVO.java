package com.shuttles.shuttlesapp.vo;

import android.graphics.drawable.Drawable;

/**
 * Created by domin on 2018-01-28.
 */

public class DrinkListVO {
    private Drawable img;
    private String name;
    private int price;

    public Drawable getImg() {
        return img;
    }

    public void setImg(Drawable img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
