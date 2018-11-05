package com.shuttles.shuttlesapp.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import com.shuttles.shuttlesapp.vo.DrinkElementVO;
import com.shuttles.shuttlesapp.vo.FoodElementVO;
import com.shuttles.shuttlesapp.vo.OptionElementVO;
import com.shuttles.shuttlesapp.vo.OrderProductListVO;
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
                final OrderRequestVO orderRequestVO = gson.fromJson(mOrderHistoryDetailData, new TypeToken<OrderRequestVO>(){}.getType());

                ListViewCompat lvOrderReceipt = (ListViewCompat) findViewById(R.id.lv_order_receipt);
                final ReceiptListViewAdapter receiptListViewAdapter = new ReceiptListViewAdapter();
                //add coffee
                for(DrinkElementVO e : orderRequestVO.getCoffee()){
                    receiptListViewAdapter.addItem(e.getName(), e.getPrice() * e.getCount(), e.getPrice(), e.getCount(), (ArrayList<OptionElementVO>) e.getOption(), OrderProductListVO.COFFEE, e.getOid());
                }

                //add drink
                for(FoodElementVO e : orderRequestVO.getFood()){
                    receiptListViewAdapter.addItem(e.getName(), e.getPrice() * e.getCount(), e.getPrice(), e.getCount(), (ArrayList<OptionElementVO>) e.getOption(), OrderProductListVO.SPECIAL_FOOD, e.getOid());
                }

                lvOrderReceipt.setAdapter(receiptListViewAdapter);

                ((TextView)findViewById(R.id.tv_product_total_price_receipt)).setText(orderRequestVO.getOrderPrice() + "원");

                Button btnOrderAgain = (Button)findViewById(R.id.btn_order_again);
                btnOrderAgain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "To Cart ", Toast.LENGTH_SHORT).show();

                        OrderRequestVO.getInstance().clearOrderRequestVO();
                        OrderRequestVO.getInstance().setInstance(orderRequestVO);

                        Intent intent = new Intent(getApplicationContext(), CartActivityV2.class);
                        startActivity(intent);
                        finish();
                    }
                });
                break;
        }
    }
}

class ReceiptListViewAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<OrderProductListVO> listViewItemList = new ArrayList<OrderProductListVO>() ;

    // CartListViewAdapter 생성자
    public ReceiptListViewAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        //"listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.receipt_list_row_v2, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView tvProductName = (TextView)convertView.findViewById(R.id.tv_product_name);
        TextView tvProductPrice = (TextView)convertView.findViewById(R.id.tv_product_price);
        TextView tvProductUnitPrice = (TextView)convertView.findViewById(R.id.tv_product_unit_price);
        final TextView tvProductCnt = (TextView)convertView.findViewById(R.id.tv_product_cnt);
        TextView tvOptions = (TextView)convertView.findViewById(R.id.tv_options);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        final OrderProductListVO orderProductListVO = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        tvProductName.setText(orderProductListVO.getProductName());
        tvProductPrice.setText(orderProductListVO.getPrice()+"");
        tvProductUnitPrice.setText(orderProductListVO.getUnitPrice()+"");
        tvProductCnt.setText(orderProductListVO.getCount()+"");
        tvOptions.setText(orderProductListVO.getOptionString());

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String productName, int price, int unitPrice, int count, ArrayList<OptionElementVO> optionList, int type, int oid) {
        OrderProductListVO item = new OrderProductListVO(productName, price, unitPrice, count, optionList, type, oid);

        listViewItemList.add(item);
    }
}