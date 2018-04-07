package com.shuttles.shuttlesapp.ConnectionController;

/**
 * Created by daeyonglee on 2018. 1. 24..
 */

public interface ConnectionImpl {
    public void sendRequestData(RequestData requestData);
    public void requestCallback(ConnectionResponse connectionResponse);
}
