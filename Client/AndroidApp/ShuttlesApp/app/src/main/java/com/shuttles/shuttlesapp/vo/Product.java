package com.shuttles.shuttlesapp.vo;

import android.graphics.drawable.Drawable;
import com.shuttles.shuttlesapp.Utils.Utils;

/**
 * Created by daeyonglee on 2018. 2. 11..
 */

public abstract  class Product {
    private String name;
    private String price;
    private String picture_url;
    private String picture_version;
    private String state;
    private String description;
    private String type;
    private String pictureFileName;
    private Drawable img;
    private int isAvailable;//0 is available

    public void convertURLtoFileName(){
        pictureFileName = Utils.convertURLtoFileName(picture_url);
    }

    public abstract String getID();

    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    public String getPictureFileName() {
        return pictureFileName;
    }

    public void setPictureFileName(String pictureFileName) {
        this.pictureFileName = pictureFileName;
    }

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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture_version() {
        return picture_version;
    }

    public void setPicture_version(String picture_version) {
        this.picture_version = picture_version;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(int isAvailable) {
        this.isAvailable = isAvailable;
    }
}
