package com.shuttles.shuttlesapp.vo;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

/**
 * Created by domin on 2018-05-27.
 */

//used for cart, order history...
public class OrderProductListVO {
    public static final int COFFEE = 0;
    public static final int SPECIAL_FOOD = 1;

    private Drawable img;
    private String productName;
    private int price;
    private int unitPrice;
    private int count;
    private ArrayList<OptionElementVO> optionList;
    private int type = COFFEE;

    private int oid; //OrderRequestVO 에서 drink, food의 고유 id. 장바구니에서 삭제할 때 필요

    public OrderProductListVO(String productName, int price, int unitPrice, int count, ArrayList<OptionElementVO> optionList, int oid) {
        this.productName = productName;
        this.price = price;
        this.unitPrice = unitPrice;
        this.count = count;
        this.optionList = optionList;
        this.oid = oid;
    }

    public Drawable getImg() {
        return img;
    }

    public void setImg(Drawable img) {
        this.img = img;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<OptionElementVO> getOptionList() {
        return optionList;
    }

    public void setOptionList(ArrayList<OptionElementVO> optionList) {
        this.optionList = optionList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public String getOptionString(){
        String ret = "";
        for(OptionElementVO e : optionList){
            ret += e.getOption_name() + " ";
        }
        return ret;
    }
}
