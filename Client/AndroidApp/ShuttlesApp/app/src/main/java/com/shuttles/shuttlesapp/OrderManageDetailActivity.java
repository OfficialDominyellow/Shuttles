package com.shuttles.shuttlesapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.shuttles.shuttlesapp.ConnectionController.ConnectionImpl;
import com.shuttles.shuttlesapp.ConnectionController.ConnectionResponse;
import com.shuttles.shuttlesapp.ConnectionController.RequestData;
import com.shuttles.shuttlesapp.ConnectionController.RestAPI;

public class OrderManageDetailActivity extends AppCompatActivity implements ConnectionImpl{
    private Button orderAcceptButton;
    private RequestData requestData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_manage_detail_layout);

        orderAcceptButton = (Button) findViewById(R.id.btn_order_accept);

        orderAcceptButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestData = new RequestData(RestAPI.Method.POST, RestAPI.DRINK_LIST, RestAPI.REQUEST_TYPE.DRINK_LIST);
                sendRequestData(requestData);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_order_manage_detail);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_12dp); // your drawable
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });

    }

    @Override
    public void sendRequestData(RequestData requestData) {

    }

    @Override
    public void requestCallback(ConnectionResponse connectionResponse) {

    }
}

