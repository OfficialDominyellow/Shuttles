package com.shuttles.shuttlesapp.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
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
import com.shuttles.shuttlesapp.ConnectionController.RequestData;
import com.shuttles.shuttlesapp.ConnectionController.RequestHandler;
import com.shuttles.shuttlesapp.ConnectionController.ConnectionResponse;
import com.shuttles.shuttlesapp.ConnectionController.RestAPI;
import com.shuttles.shuttlesapp.R;
import com.shuttles.shuttlesapp.Utils.LoadingDialog;
import com.shuttles.shuttlesapp.Utils.MinMaxFilter;
import com.shuttles.shuttlesapp.vo.OptionElementVO;
import com.shuttles.shuttlesapp.vo.OrderRequestVO;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DrinkOrderDetailPopActivity extends AppCompatActivity implements ConnectionImpl{

    private final String TAG = "DrinkOrderDetailPopActivity";

    private RequestData requestData;
    private RequestData orderRequestData;

    private String mCoffeeID;
    private String mCoffeeName;
    private int mCoffeePrice;
    private String mCoffeeDescription;
    private int mUnitPrice; // mCoffeePrice + options

    private int mOrderCount = 1;
    private int mTotalPrice;
    private List<OptionElementVO> mDrinkOptionList;

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drink_order_detail_pop_layout);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8), (int)(height*.6));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        //params.gravity = Gravity.CENTER;

        mCoffeeID = getIntent().getExtras().getString("coffee_id");
        mCoffeeName = getIntent().getExtras().getString("name");
        mCoffeePrice = Integer.parseInt(getIntent().getExtras().getString("price"));
        mCoffeeDescription = getIntent().getExtras().getString("description");

        //initialize
        TextView tvDrinkTitle = (TextView)findViewById(R.id.tv_drink_title);
        tvDrinkTitle.setText(mCoffeeName);
        TextView tvDrinkDescription = (TextView)findViewById(R.id.tv_drink_description);
        tvDrinkDescription.setText("des : " + mCoffeeDescription);

        mUnitPrice = mTotalPrice = mCoffeePrice;

        EditText etDrinkOrderCount= (EditText)findViewById(R.id.et_drink_order_count);
        etDrinkOrderCount.setFilters(new InputFilter[]{ new MinMaxFilter("1", "999")});
        etDrinkOrderCount.addTextChangedListener(new TextWatcher() {
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
                //Toast.makeText(getApplicationContext(), "Click Add to cart", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Click Add to cart");
                addToCart();
                finish();
            }
        });

        Button btnOrderNow = (Button)findViewById(R.id.btn_order_now);
        btnOrderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Click Order Now", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Click Order Now");
                if(addToCart()){
                    //orderRequest(OrderRequestVO.getInstance());
                    Intent intent = new Intent(getApplicationContext(), CartActivityV2.class);
                    startActivity(intent);
                }
            }
        });
        Log.i(TAG, "get coffee option URL : " + RestAPI.DRINK_OPTION + "/" + mCoffeeID);
        requestData = new RequestData(RestAPI.Method.GET, RestAPI.DRINK_OPTION + "/" + mCoffeeID, RestAPI.REQUEST_TYPE.DRINK_LIST_OPTION);
        sendRequestData(requestData);
    }

    private boolean addToCart(){
        if(isValidForm()){
            Log.i(TAG, "Valid Form");
            //Toast.makeText(getApplicationContext(), "add to cart", Toast.LENGTH_SHORT).show();

            ArrayList<OptionElementVO> selectedOptionList = new ArrayList<>();
            for(OptionElementVO e : mDrinkOptionList){
                if(e.isAddition()){
                    selectedOptionList.add(e);
                }
            }
            OrderRequestVO.getInstance().addCoffee(mCoffeeName, Integer.parseInt(mCoffeeID), mOrderCount, mCoffeePrice, mUnitPrice, selectedOptionList);

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
        loadingDialog = new LoadingDialog(DrinkOrderDetailPopActivity.this);
        loadingDialog.show();
        new RequestHandler(this).execute(requestData);
    }

    @Override
    public void requestCallback(ConnectionResponse connectionResponse) {
        loadingDialog.dismiss();

        switch(connectionResponse.getRequestType()){
            case DRINK_LIST_OPTION:
                Log.i(TAG, "data : " + connectionResponse.getResult());

                Gson gson = new Gson();
                mDrinkOptionList = gson.fromJson(connectionResponse.getResult(), new TypeToken< List<OptionElementVO> >(){}.getType());
                mDrinkOptionList.sort(new Comparator<OptionElementVO>() {
                    @Override
                    public int compare(OptionElementVO o1, OptionElementVO o2) {
                        return o1.getOption_name().compareTo(o2.getOption_name());
                    }
                });
                TableLayout tlDrinkOptionTable = (TableLayout)findViewById(R.id.tl_drink_option_table);
                TableRow template = (TableRow)findViewById(R.id.tr_drink_option);
                TextView templateTv1 = (TextView)findViewById(R.id.tv_drink_option_name);
                TextView templateTv2 = (TextView)findViewById(R.id.tv_drink_option_price);
                Switch templateSw = (Switch)findViewById(R.id.sw_drink_option_addition);
                TableRow tr;
                for(int i=0; i<mDrinkOptionList.size(); i++){
                    OptionElementVO e = mDrinkOptionList.get(i);
                    Log.i(TAG, e.getOption_name() + " " + e.getOption_id() + " " + e.getOption_price());
                    tr = new TableRow(this);
                    TextView tv1 = new TextView(this);
                    TextView tv2 = new TextView(this);
                    Switch sw = new Switch(this);

                    tv1.setText(e.getOption_name());
                    tv1.setLayoutParams(templateTv1.getLayoutParams());
                    tv1.setTextAlignment(templateTv1.getTextAlignment());
                    tv1.setTextColor(templateTv1.getTextColors());

                    tv2.setText(e.getOption_price() + "원");
                    tv2.setLayoutParams(templateTv2.getLayoutParams());
                    tv2.setTextAlignment(templateTv2.getTextAlignment());
                    tv2.setTextColor(templateTv2.getTextColors());

                    sw.setLayoutParams(templateSw.getLayoutParams());
                    //int resId = getResources().getIdentifier("sw_drink_option_" + i, "id", getApplicationContext().getPackageName());
                    int resId = View.generateViewId();
                    sw.setId(resId);
                    if(e.getOption_name().toLowerCase().equals("hot")){
                        sw.setChecked(true);
                        mDrinkOptionList.get(i).setAddition(true);
                    }
                    mDrinkOptionList.get(i).setResId(resId);

                    sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            for(int i=0; i<mDrinkOptionList.size(); i++){
                                OptionElementVO e = mDrinkOptionList.get(i);
                                if(buttonView.getId() == e.getResId()){
                                    Log.i(TAG, e.toString());

                                    mDrinkOptionList.get(i).setAddition(isChecked);
                                }
                            }
                            renewTotalPrice();
                        }
                    });

                    tr.addView(tv1);
                    tr.addView(tv2);
                    tr.addView(sw);

                    tr.setLayoutParams(template.getLayoutParams());
                    tr.setPadding(template.getPaddingLeft(), template.getPaddingTop(), template.getPaddingRight(), template.getPaddingBottom());
                    tlDrinkOptionTable.addView(tr);
                }

                template.removeAllViews();
                renewTotalPrice();

                break;
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
        mUnitPrice = mCoffeePrice;
        for(OptionElementVO e : mDrinkOptionList){
            if(e.isAddition()){
                mUnitPrice += e.getOption_price();
            }
        }
        mTotalPrice = mUnitPrice * mOrderCount;

        TextView tvDrinkTotalPrice = (TextView)findViewById(R.id.tv_drink_total_price);
        tvDrinkTotalPrice.setText(mTotalPrice + "원");
    }

    private boolean isValidForm(){
        if(mOrderCount < 1){
            showAlertDialog("수량이 잘못되었습니다.");
            return false;
        }
        boolean hotCheck = false;
        boolean iceCheck = false;
        boolean selectalbeHotIce = false; //차고 뜨거운 음료 구분 없으면 false
        for(OptionElementVO e : mDrinkOptionList){
            if(e.getOption_name().toLowerCase().equals("hot")){
                selectalbeHotIce = true;
                if(e.isAddition()) {
                    hotCheck = true;
                }
            }
            if(e.getOption_name().toLowerCase().equals("ice")){
                selectalbeHotIce = true;
                if(e.isAddition()) {
                    iceCheck = true;
                }
            }
        }
        if(selectalbeHotIce && !(hotCheck ^ iceCheck)){
            showAlertDialog("hot, ice 중 한가지를 선택해주세요.");
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

