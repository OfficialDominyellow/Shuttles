package com.shuttles.shuttlesapp.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shuttles.shuttlesapp.ConnectionController.ConnectionImpl;
import com.shuttles.shuttlesapp.ConnectionController.ConnectionResponse;
import com.shuttles.shuttlesapp.ConnectionController.RequestData;
import com.shuttles.shuttlesapp.ConnectionController.RequestHandler;
import com.shuttles.shuttlesapp.ConnectionController.RestAPI;
import com.shuttles.shuttlesapp.R;
import com.shuttles.shuttlesapp.Utils.LoadingDialog;
import com.shuttles.shuttlesapp.vo.DrinkElementVO;
import com.shuttles.shuttlesapp.vo.FoodElementVO;
import com.shuttles.shuttlesapp.vo.OptionElementVO;
import com.shuttles.shuttlesapp.vo.OrderRequestVO;

import java.util.ArrayList;

public class OrderReceiptActivity extends AppCompatActivity implements ConnectionImpl {
    private String TAG = "OrderReceiptActivity";
    private String mOrderHistoryDetailData;

    private LoadingDialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_receipt_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_order_receipt);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_12dp); // your drawable
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });

        loadOrderReceipt();
    }

    private void loadOrderReceipt(){
        Log.i(TAG, "loadOrderReceipt");
        int orderId = getIntent().getExtras().getInt("orderId");
        Log.i(TAG, "order ID : " + orderId);
        RequestData requestData = new RequestData(RestAPI.Method.GET, RestAPI.ORDER_DETAIL+"/"+orderId, RestAPI.REQUEST_TYPE.ORDER_DETAIL);
        sendRequestData(requestData);
    }

    @Override
    public void sendRequestData(RequestData requestData) {
        loadingDialog = new LoadingDialog(OrderReceiptActivity.this);
        loadingDialog.show();
        new RequestHandler(this).execute(requestData);
    }

    @Override
    public void requestCallback(ConnectionResponse connectionResponse) {
        loadingDialog.dismiss();

        Log.i(TAG, "response : " + connectionResponse.getResult());
        switch (connectionResponse.getRequestType()) {
            case FAILED:
                Log.i(TAG, "callback FAILED");
                break;
            case ORDER_DETAIL:
                Log.i(TAG, "callback ORDER_DETAIL" );
                Gson gson = new Gson();
                mOrderHistoryDetailData = connectionResponse.getResult();
                OrderRequestVO orderRequestVO = gson.fromJson(mOrderHistoryDetailData, new TypeToken<OrderRequestVO>(){}.getType());

                ListViewCompat lvOrderReceipt = (ListViewCompat) findViewById(R.id.lv_order_receipt);
                final CartListViewAdapterV2 cartListViewAdapter = new CartListViewAdapterV2();
                //add coffee
                for(DrinkElementVO e : orderRequestVO.getCoffee()){
                    cartListViewAdapter.addItem(e.getName(), e.getPrice() * e.getCount(), e.getPrice(), e.getCount(), (ArrayList<OptionElementVO>) e.getOption(), e.getOid());
                }

                //add drink
                for(FoodElementVO e : orderRequestVO.getFood()){
                    cartListViewAdapter.addItem(e.getName(), e.getPrice() * e.getCount(), e.getPrice(), e.getCount(), (ArrayList<OptionElementVO>) e.getOption(), e.getOid());
                }

                lvOrderReceipt.setAdapter(cartListViewAdapter);

                ((TextView)findViewById(R.id.tv_product_total_price_receipt)).setText(orderRequestVO.getOrderPrice() + "원");
                break;
        }
    }
}

