package com.shuttles.shuttlesapp.ConnectionController;

public class ConnectionResponse {
    private RestAPI.REQUEST_TYPE requestType;
    private String result; //result from server request

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public RestAPI.REQUEST_TYPE getRequestType() {
        return requestType;
    }

    public void setRequestType(RestAPI.REQUEST_TYPE requestType) {
        this.requestType = requestType;
    }
}
