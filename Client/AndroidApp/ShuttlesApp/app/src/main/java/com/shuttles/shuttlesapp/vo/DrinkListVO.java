package com.shuttles.shuttlesapp.vo;

import android.graphics.drawable.Drawable;

/**
 * Created by domin on 2018-01-28.
 */

public class DrinkListVO extends Product{
    private String coffee_id;
    private String name;
    private String price;
    private String state;

    public String getCoffee_id() {
        return coffee_id;
    }

    public void setCoffee_id(String coffee_id) {
        this.coffee_id = coffee_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String toString(){
        return "[ id =" + coffee_id + ", name =" + name + ", price =" + price + ", state =" + state + "]";
    }
}