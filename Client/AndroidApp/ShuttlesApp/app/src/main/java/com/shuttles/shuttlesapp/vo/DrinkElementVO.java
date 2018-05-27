package com.shuttles.shuttlesapp.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by domin on 2018-05-27.
 */

public class DrinkElementVO  extends ProductElementVO{
    DrinkElementVO(String name, int id, int cnt, int orgPrice, int unitPrice, int oid, List<OptionElementVO> optionList) {
        super(name, id, cnt, orgPrice, unitPrice, oid, optionList);
    }
}
