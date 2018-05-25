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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shuttles.shuttlesapp.vo.CartListVO;

import java.util.ArrayList;

/**
 * Created by domin on 2018-05-26.
 */

public class CartActivityV2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_layout_v2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_cart_v2);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_12dp); // your drawable
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });

        ListViewCompat lvCart = (ListViewCompat) findViewById(R.id.lv_cart_v2);
        final CartListViewAdapterV2 cartListViewAdapter = new CartListViewAdapterV2();

        lvCart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CartListVO cartListVO = (CartListVO) adapterView.getItemAtPosition(i);

                String name = cartListVO.getName();
                int price = cartListVO.getPrice();
                int type = cartListVO.getType();

                Toast.makeText(getApplicationContext(), "name : " + name + ", price : " + price + ", pos : " + i + "type : " + type, Toast.LENGTH_SHORT).show();
            }
        });

        lvCart.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Long click. 삭제 pos : " + position, Toast.LENGTH_SHORT).show();
                cartListViewAdapter.removeItemByPos(position);
                return false;
            }
        });

        //add dummy data
        for(int i=0; i<15; i++){
            cartListViewAdapter.addItem(ContextCompat.getDrawable(getApplicationContext(), R.drawable.img_coffee_example), "장바구니음료", 1234, CartListVO.COFFEE);
            cartListViewAdapter.addItem(ContextCompat.getDrawable(getApplicationContext(), R.drawable.img_jeyuk), "장바구니음식", 4321, CartListVO.SPECIAL_FOOD);
        }
        lvCart.setAdapter(cartListViewAdapter);

        Button btnOrderAll = (Button)findViewById(R.id.btn_order_all);
        btnOrderAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "모두 주문하기", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), DeliveryInfoActivity.class);
                startActivity(intent);
            }
        });
    }
}

class CartListViewAdapterV2 extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<CartListVO> listViewItemList = new ArrayList<CartListVO>() ;

    // CartListViewAdapter 생성자
    public CartListViewAdapterV2() {

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
            convertView = inflater.inflate(R.layout.cart_list_row_v2, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
//        ImageView ivCartList = (ImageView) convertView.findViewById(R.id.iv_cart_list) ;
//        TextView tvCartName = (TextView) convertView.findViewById(R.id.tv_cart_name) ;
//        TextView tvCartPrice = (TextView) convertView.findViewById(R.id.tv_cart_price) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        CartListVO cartListVO = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
//        ivCartList.setImageDrawable(cartListVO.getImg());
//        tvCartName.setText(cartListVO.getName());
//        tvCartPrice.setText(cartListVO.getPrice() + "원");

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
    public void addItem(Drawable img, String name, int price, int type) {
        CartListVO item = new CartListVO();

        item.setImg(img);
        item.setName(name);
        item.setPrice(price);
        item.setType(type);

        listViewItemList.add(item);
    }

    //아이템 삭제 해야하는 일이 있다.
    public void removeItemByPos(int pos){
        listViewItemList.remove(pos);
        notifyDataSetChanged();
    }
}

