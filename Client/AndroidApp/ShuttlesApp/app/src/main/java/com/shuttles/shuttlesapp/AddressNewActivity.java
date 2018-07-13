package com.shuttles.shuttlesapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.shuttles.shuttlesapp.vo.AddressVO;

import io.realm.Realm;

/**
 * Created by domin on 2018-03-22.
 */

public class AddressNewActivity extends AppCompatActivity {
    private final String TAG = "AddressNewActivity";
    private final int REQUEST_NEW = 1;

    private EditText etZipcode;
    private EditText etFullAddress;
    private EditText etAddressExtra;
    private EditText etOrderComment;

    private String mZipcode;
    private String mAddress1;
    private String mAddress2;
    private String mAddressExtra;
    private String mOrderComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_new_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_address_new);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_12dp); // your drawable
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });

        etZipcode = (EditText)findViewById(R.id.et_zipcode);
        etFullAddress = (EditText)findViewById(R.id.et_full_address);
        etAddressExtra = (EditText)findViewById(R.id.et_address_extra);
        etOrderComment = (EditText)findViewById(R.id.et_order_comment);

        Button btnSearchAddress = (Button)findViewById(R.id.btn_search_address);
        btnSearchAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddressSearchActivity.class);
                startActivityForResult(intent, REQUEST_NEW);
            }
        });

        Button btnAddAddress = (Button)findViewById(R.id.btn_add_address);
        btnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Add address
                mAddressExtra = etAddressExtra.getText() + "";
                mOrderComment = etOrderComment.getText() + "";

                if(mZipcode.equals("") || mAddress1.equals("")  || mAddressExtra.equals("") || mOrderComment.equals("")){
                    Toast.makeText(getApplicationContext(), "주소 입력이 부족합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Realm.init(getApplicationContext());
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Number currentIdNum = realm.where(AddressVO.class).max("id");
                        int nextId;
                        if(currentIdNum == null) {
                            nextId = 1;
                        } else {
                            nextId = currentIdNum.intValue() + 1;
                        }
                        AddressVO addressVO = new AddressVO(nextId, mZipcode, mAddress1, mAddress2, mAddressExtra, mOrderComment);
                        realm.insert(addressVO);
                    }
                });
                realm.commitTransaction();

                Toast.makeText(getApplicationContext(), "zipcode : " + mZipcode + ", add1 : " + mAddress1 + ", add2 : " + mAddress2 + ", addressExtra : " + mAddressExtra + ", orderComment : " + mOrderComment, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult");
        switch(requestCode) {
            case REQUEST_NEW:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        String zipcode, address1, address2;
                        try {
                            zipcode = data.getExtras().getString("zipcode");
                            address1 = data.getExtras().getString("address1");
                            address2 = data.getExtras().getString("address2");
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            zipcode = address1 = address2 = "";
                        }
                        //Toast.makeText(getApplicationContext(), "zipcode : " + zipcode + ", add1 : " + address1 + ", add2 : " + address2, Toast.LENGTH_SHORT).show();
                        mZipcode = zipcode;
                        mAddress1 = address1;
                        mAddress2 = address2;

                        etZipcode.setText(zipcode);
                        etFullAddress.setText(mAddress1 + " " + mAddress2);
                        break;

                    default:
                        break;
                }
            break;
            default:
                break;
        }

    }
}
