package com.shuttles.shuttlesapp.ConnectionController;

import android.util.Log;

import com.kakao.usermgmt.response.model.UserProfile;
import com.shuttles.shuttlesapp.Utils.Constants;

public class UserInfo {
    private static UserInfo userInfoInstance = new UserInfo();

    private Type userType;
    private String fcmToken;
    private UserProfile profile;

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public enum Type{
        customer,owner;
    }

    private UserInfo()
    {

    }

    public static UserInfo getInstance() {
        return userInfoInstance;
    }


    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

    public Type getUserType() {
        return userType;
    }

    private void convertUserType(Type userType) {
        this.userType = userType;
        Log.i(Constants.LOG_TAG,"convertUserType usertype "+userType);
    }

    public void setUserType(String responseUserType){
        if(responseUserType.equals("owner")){
            convertUserType(Type.owner);
        } else {
            convertUserType(Type.owner);
        }
    }
}
