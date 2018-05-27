package com.shuttles.shuttlesapp.FCM;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.shuttles.shuttlesapp.Utils.Constants;

public class ShuttlesFirebaseInstanceIDService extends FirebaseInstanceIdService {

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
        //FCM 토큰 갱신
    }
}
