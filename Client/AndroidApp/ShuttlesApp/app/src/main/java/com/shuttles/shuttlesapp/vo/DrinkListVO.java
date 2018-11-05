package com.shuttles.shuttlesapp.vo;

/**
 * Created by domin on 2018-01-28.
 */

public class DrinkListVO extends Product{
    private String coffee_id;
    private int isTodayMenu;

    public String getCoffee_id() {
        return coffee_id;
    }

    public void setCoffee_id(String coffee_id) {
        this.coffee_id = coffee_id;
    }

    @Override
    public String getID() {
        return coffee_id;
    }

    public int isTodayMenu() {
        return isTodayMenu;
    }

    public void setTodayMenu(int todayMenu) {
        isTodayMenu = todayMenu;
    }
}