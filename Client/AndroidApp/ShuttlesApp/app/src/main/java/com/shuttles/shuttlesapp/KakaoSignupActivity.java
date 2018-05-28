package com.shuttles.shuttlesapp;
/**
 * Created by daeyonglee on 2018. 1. 15..
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kakao.auth.ErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;

import com.shuttles.shuttlesapp.ConnectionController.RequestData;
import com.shuttles.shuttlesapp.ConnectionController.RequestHandler;
import com.shuttles.shuttlesapp.ConnectionController.ConnectionResponse;
import com.shuttles.shuttlesapp.ConnectionController.RestAPI;
import com.shuttles.shuttlesapp.ConnectionController.ConnectionImpl;
import com.shuttles.shuttlesapp.ConnectionController.UserInfo;
import com.shuttles.shuttlesapp.Utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class KakaoSignupActivity extends Activity implements ConnectionImpl {
    private GlobalApplication globalApplication;
    private ProgressDialog dialog;
    private UserInfo userInfo;

    /**
     * Main으로 넘길지 가입 페이지를 그릴지 판단하기 위해 me를 호출한다.
     *
     * @param savedInstanceState 기존 session 정보가 저장된 객체
     */

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfo = UserInfo.getInstance();
        dialog = new ProgressDialog(KakaoSignupActivity.this);
        globalApplication = (GlobalApplication) getApplicationContext();
        requestMe();
    }

    /**
     * 사용자의 상태를 알아 보기 위해 me API 호출을 한다.
     */
    protected void requestMe() { //유저의 정보를 받아오는 함수
        UserManagement.getInstance().requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Log.d(Constants.LOG_TAG, message);

                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                if (result == ErrorCode.CLIENT_ERROR_CODE) {
                    finish();
                } else {
                    redirectLoginActivity();
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                redirectLoginActivity();
            }

            @Override
            public void onNotSignedUp() {
                /* TODO
                카카오톡 회원이 아닐 시 showSignup(); 호출해야함
                 */
            }

            @Override
            public void onSuccess(UserProfile userProfile) {  //성공 시 userProfile 형태로 반환
                //user의 유니크한 정보를 여기서 추출
                Log.i(Constants.LOG_TAG, "User ID:" + userProfile.getId() + " userNickname: " + userProfile.getNickname()
                        + " UUID:" + userProfile.getUUID() + " email : " + userProfile.getEmail());

                String fcmToken = FirebaseInstanceId.getInstance().getToken();
                Log.i(Constants.LOG_TAG,"fcmToken : " + fcmToken);

                userInfo.setFcmToken(fcmToken);
                userInfo.setProfile(userProfile);

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("user_id", userInfo.getProfile().getEmail());
                    jsonObject.put("pushId", userInfo.getFcmToken());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONArray jsonArray = new JSONArray();
                jsonArray.put(jsonObject);

                RequestData postUserData = new RequestData("POST", RestAPI.USER, RestAPI.REQUEST_TYPE.USER ,jsonObject);
                sendRequestData(postUserData);
            }
        });
    }

    private void redirectDashboardActivity() {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.putExtra("userData : ", userInfo.getProfile().getEmail());
        startActivity(intent);
        finish();

    }

    protected void redirectLoginActivity() {
        final Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    public void sendRequestData(RequestData requestData) {
        dialog.show();
        new RequestHandler(this).execute(requestData);
    }

    @Override
    public void requestCallback(ConnectionResponse connectionResponse) {
        Log.i(Constants.LOG_TAG,"requstCallback");
        dialog.dismiss();

        switch (connectionResponse.getRequestType()) {
            case FAILED:
                Log.e(Constants.LOG_TAG, "request failed!");
                redirectLoginActivity();
                break;

            case USER:
                JsonObject rootObject = new JsonParser().parse(connectionResponse.getResult()).getAsJsonObject();
                String userTypeResponse = rootObject.get("result").toString();
                Log.i(Constants.LOG_TAG, "User Type : "+userTypeResponse);
                userInfo.setUserType(userTypeResponse);

                redirectDashboardActivity();
                break;
            default:
                Log.e(Constants.LOG_TAG,"Unknown Response");
                break;
        }

    }
}
