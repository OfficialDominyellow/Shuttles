package com.shuttles.shuttlesapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

/**
 * Created by domin on 2018-01-12.
 */

public class LoginActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        //call splash
        Intent splashIntent = new Intent(this, SplashActivity.class);
        startActivity(splashIntent);
    }
}
