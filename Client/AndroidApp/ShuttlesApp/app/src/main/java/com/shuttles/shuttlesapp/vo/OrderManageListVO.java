package com.shuttles.shuttlesapp.vo;

/**
 * Created by domin on 2018-01-28.
 */

public class OrderManageListVO {
    public static final int ORDER_PEND = 0; //주문 대기
    public static final int ORDER_COMPETE = 1; // 주문 완료
    public static final int DELIVERY_BEGIN = 2; //배달중
    public static final int DELIVERY_COMPLETE = 3; //배달 완료
    public static final int ORDER_CANCEL = 99; //주문 취소

    private String title;
    private String orderSerial;
    private int status;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOrderSerial() {
        return orderSerial;
    }

    public void setOrderSerial(String orderSerial) {
        this.orderSerial = orderSerial;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusStatement(){
        switch(status){
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
