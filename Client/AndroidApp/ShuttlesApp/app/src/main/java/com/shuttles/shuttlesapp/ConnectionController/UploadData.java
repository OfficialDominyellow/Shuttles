package com.shuttles.shuttlesapp.ConnectionController;

import org.json.JSONObject;

/**
 * Created by daeyonglee on 2018. 1. 29..
 */

public class UploadData {
    private String method;
    private String restURL;
    JSONObject uploadJson;

    public UploadData(){
        method = null;
        restURL = null;
        uploadJson = null;
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

    public void setUploadJson(JSONObject json){
        this.uploadJson = json;
    }

    public JSONObject getUploadJson(){
        return uploadJson;
    }
}
