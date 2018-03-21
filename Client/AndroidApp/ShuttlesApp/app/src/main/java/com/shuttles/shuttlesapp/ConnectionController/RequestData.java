package com.shuttles.shuttlesapp.ConnectionController;

import org.json.JSONArray;

/**
 * Created by daeyonglee on 2018. 1. 29..
 */

public class RequestData {
    private String method;
    private String restURL;
    private String result; //result from server request
    private int request_type;

    private JSONArray uploadJsonArray;

    public RequestData(String method, String restURL, int request_type){
        this.method = method;
        this.restURL = restURL;
        this.request_type = request_type;
    }

    public RequestData(String method, String restURL, int request_type, JSONArray uploadJsonArray){
        this.method = method;
        this.restURL = restURL;
        this.request_type = request_type;
        this.uploadJsonArray = uploadJsonArray;
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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getRequest_type() {
        return request_type;
    }

    public void setRequest_type(int request_type) {
        this.request_type = request_type;
    }
}
