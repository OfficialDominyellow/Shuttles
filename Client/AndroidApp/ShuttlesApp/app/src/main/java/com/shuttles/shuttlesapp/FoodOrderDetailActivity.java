package com.shuttles.shuttlesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class FoodOrderDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_order_detail_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_food_order_detail);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_12dp); // your drawable
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });

        ImageView ivCart = (ImageView) findViewById(R.id.iv_cart_in_food_order_detail);
        ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "장바구니", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), CartActivityV2.class);
                startActivity(intent);
            }
        });
    }
}

