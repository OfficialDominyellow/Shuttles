package com.shuttles.shuttlesapp.vo;

import com.google.gson.Gson;

public class OrderVerifyVO {
    private String verify;
    private int order_id;

    public OrderVerifyVO(String verify, int order_id) {
        this.verify = verify;
        this.order_id = order_id;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String toString(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
