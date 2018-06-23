package com.shuttles.shuttlesapp.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by domin on 2018-05-27.
 */

public class FoodElementVO extends ProductElementVO {
    private int food_id;
    FoodElementVO(String name, int id, int cnt, int orgPrice, int unitPrice, int oid, List<OptionElementVO> optionList) {
        super(name, cnt, orgPrice, unitPrice, oid, optionList);
        food_id = id;
    }

    public int getFood_id() {
        return food_id;
    }

    public void setFood_id(int food_id) {
        this.food_id = food_id;
    }
}
