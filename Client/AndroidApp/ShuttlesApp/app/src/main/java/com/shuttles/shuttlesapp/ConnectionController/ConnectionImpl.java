package com.shuttles.shuttlesapp.ConnectionController;

import com.shuttles.shuttlesapp.Utils.Constants;

/**
 * Created by daeyonglee on 2018. 1. 24..
 */

public interface ConnectionImpl {
    public void sendRequestData(RequestData requestData);
    public void requestCallback(int REQUEST_TYPE);
}
