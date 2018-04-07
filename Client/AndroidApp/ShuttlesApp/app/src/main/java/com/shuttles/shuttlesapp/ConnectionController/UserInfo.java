package com.shuttles.shuttlesapp.ConnectionController;

import android.util.Log;

import com.kakao.usermgmt.response.model.UserProfile;
import com.shuttles.shuttlesapp.Utils.Constants;

public class UserInfo {
    private static UserInfo userInfoInstance = new UserInfo();
    private Type userType;

    private UserProfile profile;

    public enum Type{
        customer,owner;
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

    public void setUserType(Type userType) {
        this.userType = userType;
        Log.i(Constants.LOG_TAG,"set usertype "+userType);
    }

    public void setUserType(String responseUserType){

        switch (responseUserType) {
            case "customer":
                setUserType(Type.customer);
                break;
            case "owner":
                setUserType(Type.owner);
                break;
        }
    }
}
