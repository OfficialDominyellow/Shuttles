package com.shuttles.shuttlesapp.ConnectionController;

/**
 * Created by daeyonglee on 2018. 1. 24..
 */

public interface ConnectionImpl {
    void sendRequestData(RequestData requestData);
    void requestCallback(ConnectionResponse connectionResponse);
}
