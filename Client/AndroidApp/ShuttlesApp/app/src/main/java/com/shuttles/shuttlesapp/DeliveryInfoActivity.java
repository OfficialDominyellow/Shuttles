package com.shuttles.shuttlesapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
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
import com.shuttles.shuttlesapp.ConnectionController.UserInfo;
import com.shuttles.shuttlesapp.vo.AddressVO;
import com.shuttles.shuttlesapp.vo.OptionElementVO;
import com.shuttles.shuttlesapp.vo.OrderRequestVO;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class DeliveryInfoActivity extends AppCompatActivity implements ConnectionImpl {

    private static String TAG = "DeliveryInfoActivity";

    private RequestData requestData = null;
    private RequestData orderRequestData = null;

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
        //init Realm
        Realm.init(getApplicationContext());

        ImageView ivCart = (ImageView) findViewById(R.id.iv_add_address_in_delivery);
        ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "주소록추가", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), AddressNewActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        final AddressListViewAdapter addressListViewAdapter = new AddressListViewAdapter();
        ListViewCompat lvAddressList = (ListViewCompat) findViewById(R.id.lv_address_list_in_delivery);

        lvAddressList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AddressVO addressVO = (AddressVO) adapterView.getItemAtPosition(i);

                int id = addressVO.getId();
                String fullAddress = addressVO.getZipcode() + " " + addressVO.getAddress1() + " " + addressVO.getAddress2() + " " + addressVO.getAddressExtra();

                Toast.makeText(getApplicationContext(), "id : " + id + " pos : " + i + ", full address : " + fullAddress, Toast.LENGTH_SHORT).show();

                showAlertDialog(fullAddress);
            }
        });

        Log.i(TAG, "Select adddress book");
        Realm realm = Realm.getDefaultInstance();
        RealmResults<AddressVO> results = realm.where(AddressVO.class).findAll();

        for(AddressVO e : results){
            addressListViewAdapter.addItem(e.getId(), e.getAddressName(), e.getZipcode(), e.getAddress1(), e.getAddress2(), e.getAddressExtra()) ;
        }

        lvAddressList.setAdapter(addressListViewAdapter);
    }

    private void showAlertDialog(final String fullAddress){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("주소 선택");
        alertDialog.setMessage("해당 주소로 배달하시겠습니까?\n\n" + fullAddress);
        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                OrderRequestVO.getInstance().setOrder_address(fullAddress);
                OrderRequestVO.getInstance().setUser_id(UserInfo.getInstance().getProfile().getEmail());
                Log.i(TAG, OrderRequestVO.getInstance().toString());
                orderRequest(OrderRequestVO.getInstance());
                //refresh activity
                //finish();
                //startActivity(getIntent());
            }
        });
        alertDialog.show();
    }

    private void orderRequest(OrderRequestVO e){
        Gson gson = new Gson();
        String jsonStr = e.toString();
        JSONObject jsonObject= null;
        try {
            jsonObject = new JSONObject(jsonStr);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        Log.i(TAG, jsonStr);
        orderRequestData = new RequestData("POST", RestAPI.ORDER, RestAPI.REQUEST_TYPE_ORDER, jsonObject);
        sendRequestData(orderRequestData);
    }

    @Override
    public void sendRequestData(RequestData requestData) {
        new RequestHandler(this).execute(requestData);
    }

    @Override
    public void requestCallback(ConnectionResponse connectionResponse) {
        switch(connectionResponse.getResponseType()){
            case RestAPI.REQUEST_TYPE_ORDER:
                Log.e(TAG, "Order Request.");
                break;
            case RestAPI.REQUEST_TYPE_FAILED:
                Log.e(TAG, "Request Fail.");
                break;
            default:
                Log.e(TAG, "There is no matched request type.");
                break;
        }
    }
}

