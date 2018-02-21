package com.shuttles.shuttlesapp.vo;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.shuttles.shuttlesapp.Utils.Constants;

/**
 * Created by daeyonglee on 2018. 2. 11..
 */

public class Product {
    private String name;
    private String price;
    private String description;

    private String picture_url;
    private String pictureFileName;
    private Drawable img;

    public void convertURLtoFileName(){
        if(picture_url!=null) {
            pictureFileName = picture_url.substring(picture_url.lastIndexOf('/') + 1, picture_url.length());
            Log.i(Constants.LOG_TAG,"File Name : "+pictureFileName);
        }
    }

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
}
