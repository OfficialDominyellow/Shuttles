package com.shuttles.shuttlesapp.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.shuttles.shuttlesapp.vo.OrderVerifyVO;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderManageDetailActivity extends AppCompatActivity implements ConnectionImpl{
    private String TAG = "OrderReceiptActivity";
    private String mOrderManageDetailData;

    private Button orderAcceptButton;
    private Button orderCancelButton;
    private Button orderReadyButton;

    private LoadingDialog loadingDialog;

    private int orderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_manage_detail_layout);

        setOrderButtons();

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_order_manage_detail);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_12dp); // your drawable
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });

        loadOrderManageDetail();
    }

    private void setOrderButtons() {
        orderAcceptButton = findViewById(R.id.btn_order_accept);
        orderAcceptButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick orderAcceptButton");
                setOrderOnclick(RestAPI.Verify.ACCEPT);
            }
        });

        orderCancelButton = findViewById(R.id.btn_order_cancel);
        orderCancelButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick orderCancelButton");
                setOrderOnclick(RestAPI.Verify.CANCEL);
            }
        });

        orderReadyButton = findViewById(R.id.btn_order_ready);
        orderReadyButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick orderReadyButton");
                setOrderOnclick(RestAPI.Verify.READY);
            }
        });

    }

    private void setOrderOnclick(RestAPI.Verify verifyType) {
        Log.i(TAG, "onClick orderAcceptButton");
        OrderVerifyVO orderVerifyVO = null;
        switch (verifyType){
            case ACCEPT:
                orderVerifyVO = new OrderVerifyVO("receive",orderId);
                break;
            case CANCEL:
                orderVerifyVO = new OrderVerifyVO("cancel",orderId);
                break;
            case READY:
                orderVerifyVO = new OrderVerifyVO("ready",orderId);
                break;
        }

        String jsonStr = orderVerifyVO.toString();
        JSONObject jsonObject= null;
        try {
            jsonObject = new JSONObject(jsonStr);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        Log.i(TAG, jsonStr);

        RequestData requestData = new RequestData(RestAPI.Method.POST, RestAPI.ADMIN_ORDERS_VERIFY, RestAPI.REQUEST_TYPE.ADMIN_ORDERS_VERIFY, jsonObject);
        sendRequestData(requestData);
    }

    private void loadOrderManageDetail(){
        Log.i(TAG, "loadOrderManageDetail");
        orderId = getIntent().getExtras().getInt("orderId");
        Log.i(TAG, "order ID : " + orderId);
        RequestData requestData = new RequestData(RestAPI.Method.GET, RestAPI.ADMIN_ORDERS_DETAIL+"/"+orderId, RestAPI.REQUEST_TYPE.ADMIN_ORDERS_DETAIL);
        sendRequestData(requestData);
    }

    @Override
    public void sendRequestData(RequestData requestData) {
        loadingDialog = new LoadingDialog(OrderManageDetailActivity.this);
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
            case ADMIN_ORDERS_DETAIL:
                Log.i(TAG, "callback ORDER_DETAIL" );
                Gson gson = new Gson();
                mOrderManageDetailData = connectionResponse.getResult();
                OrderRequestVO orderRequestVO = gson.fromJson(mOrderManageDetailData, new TypeToken<OrderRequestVO>(){}.getType());

                ListViewCompat lvOrderReceipt = (ListViewCompat) findViewById(R.id.lv_order_receipt_manage_detail);
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

                ((TextView)findViewById(R.id.tv_product_total_price_manage_detail)).setText(orderRequestVO.getOrderPrice() + "원");
                break;
            case ADMIN_ORDERS_VERIFY:
                Log.i(TAG, "callback ORDER_DETAIL" );

                break;
        }
    }
}

