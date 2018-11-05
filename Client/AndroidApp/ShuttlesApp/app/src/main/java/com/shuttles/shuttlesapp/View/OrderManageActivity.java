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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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
import com.shuttles.shuttlesapp.vo.OrderManageListVO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class OrderManageActivity extends AppCompatActivity implements ConnectionImpl{
    final private String TAG = "OrderManageActivity";
    private String mOrderManageData;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_manage_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_order_manage);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_12dp); // your drawable
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });

    }

    protected void onResume(){
        super.onResume();
        Log.i(TAG, "onResume loadAdminOrderList");
        loadAdminOrderList();
    }

    private void loadAdminOrderList(){
        RequestData requestData = new RequestData(RestAPI.Method.GET, RestAPI.ADMIN_ORDERS, RestAPI.REQUEST_TYPE.ADMIN_ORDERS);
        sendRequestData(requestData);
    }

    @Override
    public void sendRequestData(RequestData requestData) {
        loadingDialog = new LoadingDialog(OrderManageActivity.this);
        loadingDialog.show();
        new RequestHandler(this).execute(requestData);
    }

    @Override
    public void requestCallback(ConnectionResponse connectionResponse) {
        loadingDialog.dismiss();

        Log.i(TAG, "response : " + connectionResponse.getResult());
        switch (connectionResponse.getRequestType()) {
            case FAILED:
                //failed
                Log.i(TAG, "callback FAILED");
                break;
            case ADMIN_ORDERS:
                Log.i(TAG, "callback ADMIN_ORDERS");
                ListViewCompat lvOrderManage = (ListViewCompat) findViewById(R.id.lv_order_manage);
                lvOrderManage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        OrderManageListVO orderManageListVO = (OrderManageListVO) adapterView.getItemAtPosition(i);

                        int orderId = orderManageListVO.getOrderId();
                        int orderPrice = orderManageListVO.getOrderPrice();
                        int orderState = orderManageListVO.getOrderState();
                        String orderStateString = orderManageListVO.getStatusStatement();

                        //Toast.makeText(getApplicationContext(), "order id : " + orderId + ", orderPrice : " + orderPrice + ", status : " + orderState + ", statusStatement : " + orderStateString +", pos : " + i, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), OrderManageDetailActivity.class);
                        intent.putExtra("orderId", orderId);
                        startActivity(intent);
                    }
                });

                final OrderManageListViewAdapter orderManageListViewAdapter = new OrderManageListViewAdapter();
                Gson gson = new Gson();
                mOrderManageData = connectionResponse.getResult();
                List<OrderManageListVO> orderManageList =  gson.fromJson(mOrderManageData, new TypeToken<List<OrderManageListVO>>(){}.getType());

                orderManageList.sort(new Comparator<OrderManageListVO>() {
                    @Override
                    public int compare(OrderManageListVO o1, OrderManageListVO o2) {
                        return o2.getOrderId() - o1.getOrderId();
                    }
                });

                for(OrderManageListVO e : orderManageList) {
                    orderManageListViewAdapter.addItem(e.getOrderId(), e.getOrderPrice(), e.getOrderState(), e.getOrderDate(), e.getOrderUserId());
                }

                lvOrderManage.setAdapter(orderManageListViewAdapter);
                break;
        }
    }
}

class OrderManageListViewAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<OrderManageListVO> listViewItemList = new ArrayList<OrderManageListVO>() ;

    // OrderManageListViewAdapter 생성자
    public OrderManageListViewAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.order_manage_list_row, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView tvOrderManageTitle = (TextView) convertView.findViewById(R.id.tv_order_manage_title);
        TextView tvOrderManagePrice = (TextView) convertView.findViewById(R.id.tv_order_manage_price);
        TextView tvOrderManageStatus = (TextView) convertView.findViewById(R.id.tv_order_manage_status);
        TextView tvOrderManageDate = (TextView) convertView.findViewById(R.id.tv_order_manage_date);
        TextView tvOrderManageUserId = (TextView) convertView.findViewById(R.id.tv_order_manage_user_id);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        OrderManageListVO orderManageListVO = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        tvOrderManageTitle.setText(orderManageListVO.getOrderPrice() + "원");
        tvOrderManagePrice.setText(orderManageListVO.getOrderPrice() + "원");
        tvOrderManageStatus.setText(orderManageListVO.getStatusStatement());
        tvOrderManageDate.setText(orderManageListVO.getOrderDate());
        tvOrderManageUserId.setText(orderManageListVO.getOrderUserId());

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
    public void addItem(int orderId, int orderPrice, int orderState, String orderDate, String userId) {
        OrderManageListVO item = new OrderManageListVO();

        item.setOrderId(orderId);
        item.setOrderPrice(orderPrice);
        item.setOrderState(orderState);
        item.setOrderDate(orderDate);
        item.setOrderUserId(userId);

        listViewItemList.add(item);
    }
}