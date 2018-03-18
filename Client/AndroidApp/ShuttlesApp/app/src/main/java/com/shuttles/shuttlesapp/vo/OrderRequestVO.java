package com.shuttles.shuttlesapp.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by domin on 2018-03-11.
 */

public class OrderRequestVO {
    private String user_id;
    private String order_address;
    private String order_totalPrice;

    private List<CoffeeElement> coffee = new ArrayList<>();
    private List<FoodElement> food = new ArrayList<>();

    public void addCoffee(String coffeeName, int coffeeCount, int coffeeUnitPrice, List<String> optionNameList){
        getCoffee().add(new CoffeeElement(coffeeName, coffeeCount, coffeeUnitPrice, optionNameList));
    }

    public void addFood(String foodName, int foodCount, int foodUnitPrice, List<String> optionNameList){
        getFood().add(new FoodElement(foodName, foodCount, foodUnitPrice, optionNameList));
    }

    public void clearOrderRequestVO(){
        coffee.clear();
        food.clear();
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

    public String getOrder_totalPrice() {
        return order_totalPrice;
    }

    public void setOrder_totalPrice(String order_totalPrice) {
        this.order_totalPrice = order_totalPrice;
    }

    public List<CoffeeElement> getCoffee() {
        return coffee;
    }

    public void setCoffee(List<CoffeeElement> coffee) {
        this.coffee = coffee;
    }

    public List<FoodElement> getFood() {
        return food;
    }

    public void setFood(List<FoodElement> food) {
        this.food = food;
    }

    private class CoffeeElement{
        private String name;
        private int count;
        private int price;

        private List<OptionElement> option = new ArrayList<>();

        CoffeeElement(String coffeeName, int coffeeCount, int coffeeUnitPrice, List<String> optionNameList){
            name = coffeeName;
            count = coffeeCount;
            price = coffeeUnitPrice;

            for(String s : optionNameList){
                addDrinkOption(s);
            }
        }

        private void addDrinkOption(String drinkOptionName){
            getOption().add(new OptionElement(drinkOptionName));
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public List<OptionElement> getOption() {
            return option;
        }

        public void setOption(List<OptionElement> option) {
            this.option = option;
        }

        private class OptionElement{
            private String name;

            OptionElement(String drinkOptionName){
                name = drinkOptionName;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }

    private class FoodElement{
        private String name;
        private int count;
        private int price;

        private List<OptionElement> option = new ArrayList<>();

        FoodElement(String foodName, int foodCount, int foodUnitPrice, List<String> optionNameList){
            name = foodName;
            count = foodCount;
            price = foodUnitPrice;

            for(String s : optionNameList){
                addFoodOption(s);
            }
        }

        private void addFoodOption(String foodOptionName){
            getOption().add(new OptionElement(foodOptionName));
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public List<OptionElement> getOption() {
            return option;
        }

        public void setOption(List<OptionElement> option) {
            this.option = option;
        }

        private class OptionElement{
            private String name;

            OptionElement(String foodOptionName){
                name = foodOptionName;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
