package com.shuttles.shuttlesapp.vo;

import android.graphics.drawable.Drawable;

/**
 * Created by domin on 2018-01-28.
 */

public class CartListVO {
    public static final int COFFEE = 0;
    public static final int SPECIAL_FOOD = 1;

    private Drawable img;
    private String name;
    private int price;
    private int type = COFFEE;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
