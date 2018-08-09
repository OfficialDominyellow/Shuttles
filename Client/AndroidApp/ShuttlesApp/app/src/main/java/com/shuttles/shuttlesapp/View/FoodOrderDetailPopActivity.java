package com.shuttles.shuttlesapp.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shuttles.shuttlesapp.ConnectionController.ConnectionImpl;
import com.shuttles.shuttlesapp.ConnectionController.ConnectionResponse;
import com.shuttles.shuttlesapp.ConnectionController.RequestData;
import com.shuttles.shuttlesapp.ConnectionController.RequestHandler;
import com.shuttles.shuttlesapp.ConnectionController.RestAPI;
import com.shuttles.shuttlesapp.R;
import com.shuttles.shuttlesapp.Utils.LoadingDialog;
import com.shuttles.shuttlesapp.vo.OptionElementVO;
import com.shuttles.shuttlesapp.vo.OrderRequestVO;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FoodOrderDetailPopActivity extends AppCompatActivity implements ConnectionImpl{

    private final String TAG = "FoodOrderDetailPopActivity";

    private RequestData requestData;
    private RequestData orderRequestData;

    private String mFoodID;
    private String mFoodName;
    private int mFoodPrice;
    private String mFoodDescription;
    private int mUnitPrice; // mFoodPrice + options

    private int mOrderCount = 1;
    private int mTotalPrice;

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_order_detail_pop_layout);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8), (int)(height*.6));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        //params.gravity = Gravity.CENTER;

        mFoodID = getIntent().getExtras().getString("food_id");
        mFoodName = getIntent().getExtras().getString("name");
        mFoodPrice = Integer.parseInt(getIntent().getExtras().getString("price"));
        mFoodDescription = getIntent().getExtras().getString("description");

        //initialize
        TextView tvFoodTitle = (TextView)findViewById(R.id.tv_food_title);
        tvFoodTitle.setText(mFoodName);
        TextView tvFoodDescription = (TextView)findViewById(R.id.tv_food_description);
        tvFoodDescription.setText("des : " + mFoodDescription);

        mUnitPrice = mTotalPrice = mFoodPrice;

        EditText etFoodOrderCount= (EditText)findViewById(R.id.et_food_order_count);
        etFoodOrderCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0) {
                    Log.i(TAG, "Order count : " + s);
                    mOrderCount = Integer.parseInt(s + "");
                    renewTotalPrice();
                }
            }
        });

        Button btnAddToCart = (Button)findViewById(R.id.btn_add_to_cart);
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Click Add to cart", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Click Add to cart");
                addToCart();
                finish();
            }
        });

        Button btnOrderNow = (Button)findViewById(R.id.btn_order_now);
        btnOrderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Click Order Now", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Click Order Now");
                if(addToCart()){
                    //orderRequest(OrderRequestVO.getInstance());
                    Intent intent = new Intent(getApplicationContext(), CartActivityV2.class);
                    startActivity(intent);
                }
            }
        });
    }

    private boolean addToCart(){
        if(isValidForm()){
            Log.i(TAG, "Valid Form");
            Toast.makeText(getApplicationContext(), "hoho", Toast.LENGTH_SHORT).show();
            OrderRequestVO.getInstance().setUser_id("dominyellow@gmail.com");
            OrderRequestVO.getInstance().setOrder_address("너네집");

            ArrayList<OptionElementVO> selectedOptionList = new ArrayList<>();
            OrderRequestVO.getInstance().addFood(mFoodName, Integer.parseInt(mFoodID), mOrderCount, mFoodPrice, mUnitPrice, selectedOptionList);

            Log.i(TAG, OrderRequestVO.getInstance()+"");
        }
        return isValidForm();
    }

    private void orderRequest(OrderRequestVO e){
        String jsonStr = e.toString();
        JSONObject jsonObject= null;
        try {
            jsonObject = new JSONObject(jsonStr);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        Log.i(TAG, jsonStr);
        orderRequestData = new RequestData(RestAPI.Method.POST, RestAPI.ORDER, RestAPI.REQUEST_TYPE.ORDER, jsonObject);
        sendRequestData(orderRequestData);
    }

    @Override
    public void sendRequestData(RequestData requestData) {
        loadingDialog = new LoadingDialog(FoodOrderDetailPopActivity.this);
        loadingDialog.show();
        new RequestHandler(this).execute(requestData);
    }

    @Override
    public void requestCallback(ConnectionResponse connectionResponse) {
        loadingDialog.dismiss();

        switch(connectionResponse.getRequestType()){
            case ORDER:
                Log.e(TAG, "Order Request.");
                break;
            case FAILED:
                Log.e(TAG, "Request Fail.");
                break;
            default:
                Log.e(TAG, "There is no matched request type.");
                break;
        }
    }

    private void renewTotalPrice(){
        mUnitPrice = mFoodPrice;

        mTotalPrice = mUnitPrice * mOrderCount;

        TextView tvFoodTotalPrice = (TextView)findViewById(R.id.tv_food_total_price);
        tvFoodTotalPrice.setText(mTotalPrice + "원");
    }

    private boolean isValidForm(){
        if(mOrderCount < 1){
            showAlertDialog("수량이 잘못되었습니다.");
            return false;
        }
        return true;
    }

    private void showAlertDialog(String msg){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("주문정보오류");
        alertDialog.setMessage(msg);
        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }

}

