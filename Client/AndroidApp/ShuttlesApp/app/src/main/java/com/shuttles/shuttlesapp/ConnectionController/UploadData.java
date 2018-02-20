package com.shuttles.shuttlesapp.ConnectionController;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by daeyonglee on 2018. 1. 29..
 */

public class UploadData {
    private String method;
    private String restURL;
    private String data;//json format string only use for post and update

    private JSONArray uploadJsonArray;

    public UploadData(String method, String restURL, String data){
        this.method = method;
        this.restURL = restURL;
        this.data = data;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public JSONArray getUploadJsonArray() {
        return uploadJsonArray;
    }

    public void setUploadJsonArray(JSONArray uploadJsonArray) {
        this.uploadJsonArray = uploadJsonArray;
    }
}
