package com.shuttles.shuttlesapp.vo;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by domin on 2018-03-11.
 */

public class OrderRequestVO {
    final private String TAG = "OrderRequestVO";
    private static OrderRequestVO instance;

    private OrderRequestVO(){}

    public static OrderRequestVO getInstance(){
        if(instance == null){
            instance = new OrderRequestVO();
        }
        return instance;
    }

    private String user_id;
    private String order_address;
    private String order_comment;
    private int order_price;

    private List<DrinkElementVO> coffee = new ArrayList<>();
    private transient int drinkObjectId = 0;
    private List<FoodElementVO> food = new ArrayList<>();
    private transient int foodObjectId = 0;

    public boolean isValid(){
        return user_id != null && order_address != null && order_price != 0 && (coffee.size() != 0 || food.size() != 0);
    }

    public boolean isValidCart(){
        return coffee.size() >= 1;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public void addCoffee(String coffeeName, int coffeeId, int coffeeCount, int coffeeOrgPrice, int coffeeUnitPrice, List<OptionElementVO> optionList){
        Log.i(TAG, "add Coffee : " + coffeeName + ", " + coffeeId + ", " + coffeeCount + ", " + coffeeOrgPrice + ", " + coffeeUnitPrice );
        int price = coffeeUnitPrice;
        price *= coffeeCount;
        this.order_price += price;
        getCoffee().add(new DrinkElementVO(coffeeName, coffeeId, coffeeCount, coffeeOrgPrice, coffeeUnitPrice, drinkObjectId++, optionList));
    }

    public void addFood(String foodName, int foodId, int foodCount, int foodOrgPrice, int foodUnitPrice, List<OptionElementVO> optionList){
        Log.i(TAG, "add Food : " + foodName + ", " + foodId + ", " + foodCount + ", " + foodOrgPrice + ", " + foodUnitPrice );
        int price = foodUnitPrice;
        price *= foodCount;
        this.order_price += price;
        getFood().add(new FoodElementVO(foodName, foodId, foodCount, foodOrgPrice, foodUnitPrice, foodObjectId++, optionList));
    }

    public void increaseProductByTypeAndOid(int type, int oid){
        if(type == OrderProductListVO.COFFEE){
            for(int i=0; i<coffee.size(); i++){
                if(coffee.get(i).getOid() == oid){
                    coffee.get(i).increaseCnt();
                    order_price += coffee.get(i).getPrice();
                    break;
                }
            }
        }
        else if(type == OrderProductListVO.SPECIAL_FOOD){
            for(int i=0; i<food.size(); i++){
                if(food.get(i).getOid() == oid){
                    food.get(i).increaseCnt();
                    order_price += food.get(i).getPrice();
                    break;
                }
            }
        }
        else{
            Log.e(TAG, "Invalid type");
        }
    }

    public void decreaseProductByTypeAndOid(int type, int oid){
        if(type == OrderProductListVO.COFFEE){
            for(int i=0; i<coffee.size(); i++){
                if(coffee.get(i).getOid() == oid){
                    coffee.get(i).decreaseCnt();
                    order_price -= coffee.get(i).getPrice();
                    break;
                }
            }
        }
        else if(type == OrderProductListVO.SPECIAL_FOOD){
            for(int i=0; i<food.size(); i++){
                if(food.get(i).getOid() == oid){
                    food.get(i).decreaseCnt();
                    order_price -= food.get(i).getPrice();
                    break;
                }
            }
        }
        else{
            Log.e(TAG, "Invalid type");
        }
    }

    public void removeDrinkByOid(int oid){
        int idx = -1;
        for(int i=0; i<coffee.size(); i++){
            if(coffee.get(i).getOid() == oid){
                idx = i;
                break;
            }
        }
        if(idx != -1) {
            this.order_price -= coffee.get(idx).getCount() * coffee.get(idx).getPrice();
            coffee.remove(idx);
        }
    }

    public void removeFoodByOid(int oid){
        int idx = -1;
        for(int i=0; i<food.size(); i++){
            if(food.get(i).getOid() == oid){
                idx = i;
                break;
            }
        }
        if(idx != -1) {
            this.order_price -= food.get(idx).getCount() * food.get(idx).getPrice();
            food.remove(idx);
        }
    }

    public void clearOrderRequestVO(){
        coffee.clear();
        food.clear();
        order_price = 0;
        drinkObjectId = 0;
        foodObjectId = 0;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getOrder_address() {
        return order_address;
    }

    public void setOrder_address(String order_address) {
        this.order_address = order_address;
    }

    public List<DrinkElementVO> getCoffee() {
        return coffee;
    }

    public void setCoffee(List<DrinkElementVO> coffee) {
        this.coffee = coffee;
    }

    public List<FoodElementVO> getFood() {
        return food;
    }

    public void setFood(List<FoodElementVO> food) {
        this.food = food;
    }

    public void setOrderPrice(int orderPrice){
        this.order_price = orderPrice;
    }

    public int getOrderPrice(){
        return order_price;
    }

    public String getOrder_comment() {
        return order_comment;
    }

    public void setOrder_comment(String order_comment) {
        this.order_comment = order_comment;
    }
}
