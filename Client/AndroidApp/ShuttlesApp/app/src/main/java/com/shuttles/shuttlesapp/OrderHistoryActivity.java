package com.shuttles.shuttlesapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shuttles.shuttlesapp.vo.OrderHistoryListVO;

import java.util.ArrayList;

public class OrderHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_history_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_order_history);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_12dp); // your drawable
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });

        ListViewCompat lvOrderHistory = (ListViewCompat) findViewById(R.id.lv_order_history);
        final OrderHistoryListViewAdapter orderHistoryListViewAdapter = new OrderHistoryListViewAdapter();

        lvOrderHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                OrderHistoryListVO orderHistoryListVO = (OrderHistoryListVO) adapterView.getItemAtPosition(i);

                String title = orderHistoryListVO.getTitle();
                String orderSerial = orderHistoryListVO.getOrderSerial();
                int status = orderHistoryListVO.getStatus();
                String statusStatement = orderHistoryListVO.getStatusStatement();

                Toast.makeText(getApplicationContext(), "titlie : " + title + ", serial : " + orderSerial + ", status : " + status + ", statusStatement : " + statusStatement +", pos : " + i, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), OrderReceiptActivity.class);
                startActivity(intent);
            }
        });

        //add dummy data
        for(int i=0; i<15; i++){
            orderHistoryListViewAdapter.addItem(ContextCompat.getDrawable(getApplicationContext(), R.drawable.img_jeyuk), "제육 외 " + i + "건", "AA"+i+"A"+i+"BB", i%4) ;
            orderHistoryListViewAdapter.addItem(ContextCompat.getDrawable(getApplicationContext(), R.drawable.img_coffee_example), "커피 외 " + i + "건", "AA"+i+"A"+i+"BB", i%4) ;
        }

        lvOrderHistory.setAdapter(orderHistoryListViewAdapter);
    }
}

class OrderHistoryListViewAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<OrderHistoryListVO> listViewItemList = new ArrayList<OrderHistoryListVO>() ;

    // OrderHistoryListViewAdapter 생성자
    public OrderHistoryListViewAdapter() {

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
            convertView = inflater.inflate(R.layout.order_history_list_row, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView ivOrderHistoryList = (ImageView) convertView.findViewById(R.id.iv_order_history_list);
        TextView tvOrderHistoryTitle = (TextView) convertView.findViewById(R.id.tv_order_history_title);
        TextView tvOrderHistorySerial = (TextView) convertView.findViewById(R.id.tv_order_history_serial);
        TextView tvOrderHistoryStatus = (TextView) convertView.findViewById(R.id.tv_order_history_status);


        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        OrderHistoryListVO orderHistoryListVO = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        ivOrderHistoryList.setImageDrawable(orderHistoryListVO.getImg());
        tvOrderHistoryTitle.setText(orderHistoryListVO.getTitle());
        tvOrderHistorySerial.setText(orderHistoryListVO.getOrderSerial());
        tvOrderHistoryStatus.setText(orderHistoryListVO.getStatusStatement());


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
    public void addItem(Drawable img, String title, String orderSerial, int status) {
        OrderHistoryListVO item = new OrderHistoryListVO();

        item.setImg(img);
        item.setTitle(title);
        item.setOrderSerial(orderSerial);
        item.setStatus(status);

        listViewItemList.add(item);
    }
}