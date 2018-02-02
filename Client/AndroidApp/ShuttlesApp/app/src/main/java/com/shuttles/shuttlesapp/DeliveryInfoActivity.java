package com.shuttles.shuttlesapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class DeliveryInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery_info_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_delivery_info);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_12dp); // your drawable
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });

        Button btnFinalSubmit = (Button)findViewById(R.id.btn_final_submit);
        btnFinalSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "존나 완료되었습니다. 주문내역 메뉴 및 카톡보세요 시발", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

