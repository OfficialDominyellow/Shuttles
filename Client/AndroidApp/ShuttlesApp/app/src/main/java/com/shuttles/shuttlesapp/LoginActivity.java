package com.shuttles.shuttlesapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

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

        Button btnLogin = (Button)findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent dashboardIntent = new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(dashboardIntent);
            }
        });
    }
}
