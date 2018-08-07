package com.shuttles.shuttlesapp.vo;

import java.util.List;

/**
 * Created by domin on 2018-05-27.
 */

public class DrinkElementVO  extends ProductElementVO{
    private int coffee_id;
    DrinkElementVO(String name, int id, int cnt, int orgPrice, int unitPrice, int oid, List<OptionElementVO> optionList) {
        super(name, cnt, orgPrice, unitPrice, oid, optionList);
        this.coffee_id = id;
    }

    public int getCoffee_id() {
        return coffee_id;
    }

    public void setCoffee_id(int coffee_id) {
        this.coffee_id = coffee_id;
    }
}
