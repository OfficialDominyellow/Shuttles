package com.shuttles.shuttlesapp.vo;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by domin on 2018-03-11.
 */

public class OrderRequestVO {
    private static OrderRequestVO instance = null;

    private OrderRequestVO(){}

    public static OrderRequestVO getInstance(){
        if(instance == null){
            instance = new OrderRequestVO();
        }
        return instance;
    }

    private String user_id;
    private String order_address;
    private int order_totalPrice;

    private List<DrinkElementVO> coffee = new ArrayList<>();
    private transient int drinkObjectId = 0;
    private List<FoodElementVO> food = new ArrayList<>();
    private transient int foodObjectId = 0;

    public boolean isValid(){
        return user_id != null && order_address != null && order_totalPrice != 0 && (coffee.size() != 0 || food.size() != 0);
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public void addCoffee(String coffeeName, int coffeeId, int coffeeCount, int coffeeOrgPrice, int coffeeUnitPrice, List<OptionElementVO> optionList){
        int price = coffeeUnitPrice;
        price *= coffeeCount;
        this.order_totalPrice += price;
        getCoffee().add(new DrinkElementVO(coffeeName, coffeeId, coffeeCount, coffeeOrgPrice, coffeeUnitPrice, drinkObjectId++, optionList));
    }

    public void addFood(String foodName, int foodId, int foodCount, int foodOrgPrice, int foodUnitPrice, List<OptionElementVO> optionList){
        int price = foodUnitPrice;
        price *= foodUnitPrice;
        this.order_totalPrice += price;
        getFood().add(new FoodElementVO(foodName, foodId, foodCount, foodOrgPrice, foodUnitPrice, foodObjectId++, optionList));
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
            food.remove(idx);
        }
    }

    public void clearOrderRequestVO(){
        coffee.clear();
        food.clear();
        order_totalPrice = 0;
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

    public int getOrder_totalPrice() {
        return order_totalPrice;
    }

    @Deprecated
    public void setOrder_totalPrice(int order_totalPrice) {
        this.order_totalPrice = order_totalPrice;
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

}
