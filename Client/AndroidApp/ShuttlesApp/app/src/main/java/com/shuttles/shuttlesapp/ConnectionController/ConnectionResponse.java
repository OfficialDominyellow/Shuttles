package com.shuttles.shuttlesapp.ConnectionController;

public class ConnectionResponse {
    private int responseType;
    private String result; //result from server request

    public int getResponseType() {
        return responseType;
    }

    public void setResponseType(int responseType) {
        this.responseType = responseType;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
