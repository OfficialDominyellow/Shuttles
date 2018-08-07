package com.shuttles.shuttlesapp.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

import com.shuttles.shuttlesapp.GlobalApplication;

/**
 * Created by daeyonglee on 2018. 3. 18..
 */

public class Utils {
    public static boolean checkNetworkState(){
        ConnectivityManager connectivityManager = (ConnectivityManager) GlobalApplication.getGlobalApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager.getActiveNetworkInfo() == null) {
            Log.e(Constants.LOG_TAG,"Network Connection Failed");
            Toast.makeText(GlobalApplication.getGlobalApplicationContext(), "네트워크가 연결되어 있지 않습니다.", Toast.LENGTH_LONG).show();
            return false;
        }
        else
            return true;
    }
}
