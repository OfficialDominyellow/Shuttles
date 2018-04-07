package com.shuttles.shuttlesapp.ConnectionController;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by daeyonglee on 2018. 1. 29..
 */

public class RequestData {
    private String method;
    private String restURL;
    private int request_type;
    private String postData;
    private JSONArray uploadJsonArray;

    public RequestData(String method, String restURL, int request_type){
        this.method = method;
        this.restURL = restURL;
        this.request_type = request_type;
    }

    public RequestData(String method, String restURL, int request_type, JSONObject uploadJsonObject){
        this.method = method;
        this.restURL = restURL;
        this.request_type = request_type;
        this.postData = uploadJsonObject.toString();
    }

    public RequestData(String method, String restURL, int request_type, JSONArray uploadJsonArray){
        this.method = method;
        this.restURL = restURL;
        this.request_type = request_type;
        this.postData = uploadJsonArray.toString();
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRestURL() {
        return restURL;
    }

    public void setRestURL(String restURL) {
        this.restURL = restURL;
    }

    public JSONArray getUploadJsonArray() {
        return uploadJsonArray;
    }

    public void setUploadJsonArray(JSONArray uploadJsonArray) {
        this.uploadJsonArray = uploadJsonArray;
    }

    public int getRequest_type() {
        return request_type;
    }

    public void setRequest_type(int request_type) {
        this.request_type = request_type;
    }

    public String getPostData() {
        return postData;
    }

    public void setPostData(String postData) {
        this.postData = postData;
    }
}
