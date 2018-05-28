package com.shuttles.shuttlesapp.ConnectionController;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by daeyonglee on 2018. 1. 29..
 */

public class RequestData {
    private String method;
    private String restURL;
    RestAPI.REQUEST_TYPE requestType;
    private String postData;
    private JSONArray uploadJsonArray;

    public RequestData(String method, String restURL, RestAPI.REQUEST_TYPE requestType){
        this.method = method;
        this.restURL = restURL;
        this.requestType = requestType;
    }

    public RequestData(String method, String restURL, RestAPI.REQUEST_TYPE requestType, JSONObject uploadJsonObject){
        this.method = method;
        this.restURL = restURL;
        this.requestType = requestType;
        this.postData = uploadJsonObject.toString();
    }

    public RequestData(String method, String restURL, RestAPI.REQUEST_TYPE requestType, JSONArray uploadJsonArray){
        this.method = method;
        this.restURL = restURL;
        this.requestType = requestType;
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

    public RestAPI.REQUEST_TYPE getRequestType() {
        return requestType;
    }

    public void setRequestType(RestAPI.REQUEST_TYPE request_type) {
        this.requestType = request_type;
    }

    public String getPostData() {
        return postData;
    }

    public void setPostData(String postData) {
        this.postData = postData;
    }
}
