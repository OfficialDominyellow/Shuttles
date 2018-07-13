package com.shuttles.shuttlesapp.vo;

import com.google.gson.Gson;

/**
 * Created by domin on 2018-05-27.
 */

public class OptionElementVO {
    //gson variables
    private int option_id;
    private String option_name;
    private int option_price;

    //Not gson variables
    private transient boolean addition;
    private transient int resId;

    public int getOption_id() {
        return option_id;
    }

    public void setOption_id(int option_id) {
        this.option_id = option_id;
    }

    public String getOption_name() {
        return option_name;
    }

    public void setOption_name(String option_name) {
        this.option_name = option_name;
    }

    public int getOption_price() {
        return option_price;
    }

    public void setOption_price(int option_price) {
        this.option_price = option_price;
    }


    public boolean isAddition() {
        return addition;
    }

    public void setAddition(boolean addition) {
        this.addition = addition;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    @Override
    public String toString() {
        /*
        String str = "";
        str += "option_id : " + getOption_id() + "\n";
        str += "option_name : " + getOption_name() + "\n";
        str += "option_price : " + getOption_id() + "\n";
        str += "Addition : " + isAddition() + "\n";
        str += "resId : " + getResId() + "\n";
        return str;
        */
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
