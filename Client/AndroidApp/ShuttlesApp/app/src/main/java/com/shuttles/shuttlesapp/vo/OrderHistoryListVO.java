package com.shuttles.shuttlesapp.vo;

import android.graphics.drawable.Drawable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by domin on 2018-01-28.
 */

public class OrderHistoryListVO {
    public static final int ORDER_PEND = 0; //주문 대기
    public static final int ORDER_COMPETE = 1; // 주문 완료
    public static final int DELIVERY_BEGIN = 2; //배달중
    public static final int DELIVERY_COMPLETE = 3; //배달 완료
    public static final int ORDER_CANCEL = 99; //주문 취소

    @SerializedName("order_id")
    private int orderId;
    @SerializedName("order_price")
    private int orderPrice;
    @SerializedName("order_state")
    private int orderState;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(int orderPrice) {
        this.orderPrice = orderPrice;
    }

    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }

    public String getStatusStatement(){
        switch(orderState){
            case ORDER_PEND :
                return "주문대기";
            case ORDER_COMPETE:
                return "주문완료";
            case DELIVERY_BEGIN:
                return "배달중";
            case DELIVERY_COMPLETE:
                return "배달완료";
            case ORDER_CANCEL:
                return "주문취소";
        }
        return "알수없음";
    }
}
