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
import android.widget.ImageView;
import android.widget.Toast;

import com.shuttles.shuttlesapp.vo.AddressVO;
import com.shuttles.shuttlesapp.vo.OrderRequestVO;

import io.realm.Realm;
import io.realm.RealmResults;

public class DeliveryInfoActivity extends AppCompatActivity {

    private static String TAG = "DeliveryInfoActivity";
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
                Log.i(TAG, OrderRequestVO.getInstance().toString());
                //refresh activity
                //finish();
                //startActivity(getIntent());
            }
        });
        alertDialog.show();
    }
}

