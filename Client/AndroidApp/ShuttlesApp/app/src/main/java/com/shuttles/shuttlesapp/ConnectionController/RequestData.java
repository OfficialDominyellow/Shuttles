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

    public RequestData(RestAPI.Method restMethod, String restURL, RestAPI.REQUEST_TYPE requestType){
        convertMethodToString(restMethod);
        this.restURL = restURL;
        this.requestType = requestType;
    }

    public RequestData(RestAPI.Method restMethod, String restURL, RestAPI.REQUEST_TYPE requestType, JSONObject uploadJsonObject){
        convertMethodToString(restMethod);
        this.restURL = restURL;
        this.requestType = requestType;
        this.postData = uploadJsonObject.toString();
    }

    public RequestData(RestAPI.Method  restMethod, String restURL, RestAPI.REQUEST_TYPE requestType, JSONArray uploadJsonArray){
        convertMethodToString(restMethod);
        this.restURL = restURL;
        this.requestType = requestType;
        this.postData = uploadJsonArray.toString();
    }

    private void convertMethodToString(RestAPI.Method eMethod){
        switch (eMethod){
            case GET:
                method = "GET";
                break;
            case POST:
                method = "POST";
                break;
            case UPDATE:
                method = "UPDATE";
                break;
            case DELETE:
                method = "DELETE";
                break;
        }

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
