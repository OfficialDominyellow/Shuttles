package com.shuttles.shuttlesapp;

        import android.app.Activity;
        import android.os.Bundle;
        import android.os.Handler;
        import android.support.annotation.Nullable;
        import android.support.v7.app.AppCompatActivity;

/**
 * Created by domin on 2018-01-12.
 */

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 1000);
    }
}
