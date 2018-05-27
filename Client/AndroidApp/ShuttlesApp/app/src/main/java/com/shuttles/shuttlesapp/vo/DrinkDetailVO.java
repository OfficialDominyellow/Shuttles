package com.shuttles.shuttlesapp.vo;

/**
 * Created by domin on 2018-01-28.
 */

public class DrinkDetailVO {
    private String name;
    private int price;
    private int size; //regular, large?
    private OptionElementVO option;

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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public OptionElementVO getOption() {
        return option;
    }

    public void setOption(OptionElementVO option) {
        this.option = option;
    }
}
