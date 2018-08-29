package com.shuttles.shuttlesapp.FCM;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.shuttles.shuttlesapp.ConnectionController.ConnectionImpl;
import com.shuttles.shuttlesapp.ConnectionController.ConnectionResponse;
import com.shuttles.shuttlesapp.ConnectionController.RequestData;
import com.shuttles.shuttlesapp.ConnectionController.RequestHandler;
import com.shuttles.shuttlesapp.ConnectionController.RestAPI;
import com.shuttles.shuttlesapp.ConnectionController.UserInfo;
import com.shuttles.shuttlesapp.Utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ShuttlesFirebaseInstanceIDService extends FirebaseInstanceIdService implements ConnectionImpl{

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        sendRegistrationToServer(refreshedToken);
        Log.i(Constants.LOG_TAG,"Refreshed Token : " + refreshedToken);
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        //TODO:FCM 토큰 갱신 아마존 서버한테 새로 보내야함
        UserInfo userInfo = UserInfo.getInstance();
        Log.i(Constants.LOG_TAG,"refresh token " + token);

        if(userInfo.getProfile() == null)
            return;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", userInfo.getProfile().getEmail());
            jsonObject.put("pushId", userInfo.getFcmToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(jsonObject);

        RequestData postRefreshToken = new RequestData(RestAPI.Method.POST, RestAPI.USER, RestAPI.REQUEST_TYPE.USER ,jsonObject);
        sendRequestData(postRefreshToken);
    }

    @Override
    public void sendRequestData(RequestData requestData) {
        new RequestHandler(this).execute(requestData);
    }

    @Override
    public void requestCallback(ConnectionResponse connectionResponse) {
        switch (connectionResponse.getRequestType()){
            case FAILED:
                /*TODO : fail handling*/
                Log.e(Constants.LOG_TAG, "request failed!");
                break;
            case USER:
                Log.i(Constants.LOG_TAG, "request success!");
                break;

            default:
                break;
        }
    }
}
