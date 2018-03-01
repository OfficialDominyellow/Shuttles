package com.shuttles.shuttlesapp.ConnectionController;

/**
 * Created by daeyonglee on 2018. 1. 30..
 */

public class RestAPI {
    public static final String SERVER_IP = "http://13.125.114.13:3000";
    public static final String TEST = SERVER_IP+"/test";
    public static final String USER = SERVER_IP+"/user";
    public static final String COFFE_LIST = SERVER_IP + "/drink/list";
    public static final String ORDER = SERVER_IP +"/order";

    public static final int REQUEST_TYPE_FAILED = -1;
    public static final int REQUEST_TYPE_USER = 0;
    public static final int REQUEST_TYPE_COFFE_LIST = 1;
    public static final int REQUEST_TYPE_ORDER = 2;
    public static final int REQUEST_TYPE_IMAGE_LOAD = 3;

}
