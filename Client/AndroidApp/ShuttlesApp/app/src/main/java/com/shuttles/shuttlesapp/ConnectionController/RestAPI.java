package com.shuttles.shuttlesapp.ConnectionController;

/**
 * Created by daeyonglee on 2018. 1. 30..
 */

public class RestAPI {
    public static final String SERVER_IP = "http://13.125.114.13:3000";
    public static final String TEST = SERVER_IP+"/test";
    public static final String USER = SERVER_IP+"/user";
    public static final String DRINK_LIST = SERVER_IP + "/drink/list";
    public static final String FOOD_LIST = SERVER_IP + "/food/list";
    public static final String ORDER = SERVER_IP +"/order";
    public static final String ORDER_DETAIL = ORDER+"/detail";
    public static final String DRINK_OPTION = SERVER_IP +"/drink/detail"; //show coffee option detail
    public static final String FOOD_OPTION = SERVER_IP +"/food/detail"; //show food option detail
    public static final String NOTICE = SERVER_IP + "/notice";
    public static final String ADMIN_ORDERS = SERVER_IP + "/admin/orders";
    public static final String ADMIN_ORDERS_DETAIL =  ADMIN_ORDERS+"/detail";
    public static final String ADMIN_ORDERS_VERIFY = ADMIN_ORDERS + "/verify";

    public enum REQUEST_TYPE {
        FAILED,

        USER,
        DRINK_LIST,
        FOOD_LIST,
        DRINK_LIST_OPTION,
        FOOD_OPTION,
        ORDER,
        ORDER_DETAIL,
        IMAGE_LOAD,
        NOTICE,
        ADMIN_ORDERS,
        ADMIN_ORDERS_DETAIL,
        ADMIN_ORDERS_VERIFY
    }

    public enum Method {
        GET,
        POST,
        UPDATE,
        DELETE,
    }

    public enum Verify {
        ACCEPT,
        CANCEL,
        READY
    }

}
