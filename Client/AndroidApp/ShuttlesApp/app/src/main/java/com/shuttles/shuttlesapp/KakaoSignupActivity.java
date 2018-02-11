package com.shuttles.shuttlesapp;
/**
 * Created by daeyonglee on 2018. 1. 15..
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kakao.auth.ErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;

import com.shuttles.shuttlesapp.ConnectionController.ImageLoadHandler;
import com.shuttles.shuttlesapp.ConnectionController.RequestHandler;
import com.shuttles.shuttlesapp.ConnectionController.RestAPI;
import com.shuttles.shuttlesapp.ConnectionController.UploadData;
import com.shuttles.shuttlesapp.Utils.Constants;
import com.shuttles.shuttlesapp.vo.DrinkListVO;
import com.shuttles.shuttlesapp.vo.Product;

import java.util.List;

public class KakaoSignupActivity extends Activity implements AsyncCallback{
    private GlobalApplication globalApplication;
    /**
     * Main으로 넘길지 가입 페이지를 그릴지 판단하기 위해 me를 호출한다.
     * @param savedInstanceState 기존 session 정보가 저장된 객체
     */

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalApplication = (GlobalApplication)getApplicationContext();
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
                Log.d(Constants.LOG_TAG,message);

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
                Log.i(Constants.LOG_TAG,"User ID:"+userProfile.getId()+" userNickname: "+userProfile.getNickname()
                        +"UUID:"+userProfile.getUUID()+"email : "+userProfile.getEmail());
                redirectDashboardActivity(userProfile.getUUID()); // 로그인 성공시 MainActivity로
            }
        });
    }

    private void redirectDashboardActivity(String userUUID) {
        /*TODO
        UUID 바뀌는지 안바뀌는지 확인해봐야함
         */

        //Set All Data
        UploadData requestCoffeList = new UploadData("GET",RestAPI.COFFE_LIST,null);
        new RequestHandler(this).execute(requestCoffeList);

        /*TOTO
        request special food list
         */

        Intent intent = new Intent(this,DashboardActivity.class);
        Log.i(Constants.LOG_TAG,"UserProfile : " + userUUID);
        intent.putExtra("userID",userUUID);
        startActivity(intent);
        finish();
    }

    protected void redirectLoginActivity() {
        final Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    @Override
    public void onTaskFinish(String resultJsonArray) {
        //after request
        Gson gson = new Gson();
        List<DrinkListVO> drinkList = gson.fromJson(resultJsonArray, new TypeToken<List<DrinkListVO>>(){}.getType());

        globalApplication.setCoffeList(drinkList);

        new ImageLoadHandler(this).execute(drinkList);
    }
}
