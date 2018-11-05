package com.shuttles.shuttlesapp.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.shuttles.shuttlesapp.R;
import com.shuttles.shuttlesapp.vo.DrinkElementVO;
import com.shuttles.shuttlesapp.vo.FoodElementVO;
import com.shuttles.shuttlesapp.vo.OptionElementVO;
import com.shuttles.shuttlesapp.vo.OrderHistoryListVO;
import com.shuttles.shuttlesapp.vo.OrderProductListVO;
import com.shuttles.shuttlesapp.vo.OrderRequestVO;

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

    }

    @Override
    protected void onResume() {
        super.onResume();

        ListViewCompat lvCart = (ListViewCompat) findViewById(R.id.lv_cart_v2);
        final CartListViewAdapterV2 cartListViewAdapter = new CartListViewAdapterV2();

        lvCart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                OrderProductListVO orderProductListVO = (OrderProductListVO) adapterView.getItemAtPosition(i);

                String name = orderProductListVO.getProductName();
                int price = orderProductListVO.getPrice();
                int type = orderProductListVO.getType();
                int oid = orderProductListVO.getOid();

                //Toast.makeText(getApplicationContext(), "name : " + name + ", price : " + price + ", pos : " + i + ", type : " + type + ", oid : " + oid, Toast.LENGTH_SHORT).show();
            }
        });

        lvCart.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), "Long click. 삭제 pos : " + position, Toast.LENGTH_SHORT).show();
                OrderProductListVO orderProductListVO = (OrderProductListVO) cartListViewAdapter.getItem(position);
                int type = orderProductListVO.getType();
                int oid = orderProductListVO.getOid();
                cartListViewAdapter.removeItemByPos(position, type, oid);
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                return false;
            }
        });

        //add coffee
        for(DrinkElementVO e : OrderRequestVO.getInstance().getCoffee()){
            cartListViewAdapter.addItem(e.getName(), e.getPrice() * e.getCount(), e.getPrice(), e.getCount(), (ArrayList<OptionElementVO>) e.getOption(), OrderProductListVO.COFFEE, e.getOid());
        }

        //add drink
        for(FoodElementVO e : OrderRequestVO.getInstance().getFood()){
            cartListViewAdapter.addItem(e.getName(), e.getPrice() * e.getCount(), e.getPrice(), e.getCount(), (ArrayList<OptionElementVO>) e.getOption(), OrderProductListVO.SPECIAL_FOOD, e.getOid());
        }

        lvCart.setAdapter(cartListViewAdapter);

        Button btnOrderAll = (Button)findViewById(R.id.btn_order_all);
        btnOrderAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "모두 주문하기", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), DeliveryInfoActivity.class);
                if(OrderRequestVO.getInstance().isValidCart()) {
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "커피를 한개이상 주문해야합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView tvProductTotalPrice = (TextView) findViewById(R.id.tv_product_total_price);
        tvProductTotalPrice.setText(OrderRequestVO.getInstance().getOrderPrice() + "");
    }
}


class CartListViewAdapterV2 extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<OrderProductListVO> listViewItemList = new ArrayList<OrderProductListVO>() ;

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
    public View getView(int position, View convertView, final ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        //"listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.cart_list_row_v2, parent, false);
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


        //button action
        Button btnDecreaseCnt = (Button)convertView.findViewById(R.id.btn_decrease_cnt);
        btnDecreaseCnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cnt = Integer.parseInt(tvProductCnt.getText() + "");
                //Toast.makeText(context, "- " + orderProductListVO.getType() + " name : " + orderProductListVO.getProductName() + ", oid : "  + orderProductListVO.getOid() ,Toast.LENGTH_SHORT).show();
                if(cnt <= 1){
                    Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    //그 pos의 oid를 찾아서 OrderRequestVO에서도 삭제
                    int type = orderProductListVO.getType();
                    int oid = orderProductListVO.getOid();
                    if(type == OrderProductListVO.COFFEE){
                        OrderRequestVO.getInstance().removeDrinkByOid(oid);
                    }
                    else if(type == OrderProductListVO.SPECIAL_FOOD){
                        OrderRequestVO.getInstance().removeFoodByOid(oid);
                    }
                    ((CartActivityV2)context).onResume();
                    return;
                }
                OrderRequestVO.getInstance().decreaseProductByTypeAndOid(orderProductListVO.getType(), orderProductListVO.getOid());
                ((CartActivityV2)context).onResume();
            }
        });
        Button btnIncreaseCnt = (Button)convertView.findViewById(R.id.btn_increase_cnt);
        btnIncreaseCnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cnt = Integer.parseInt(tvProductCnt.getText() + "");
                //Toast.makeText(context, "+ " + orderProductListVO.getType() + " name : " + orderProductListVO.getProductName() + ", oid : "  + orderProductListVO.getOid() ,Toast.LENGTH_SHORT).show();
                if(cnt >= 99){
                    Toast.makeText(context, "Can not add", Toast.LENGTH_SHORT).show();
                    return;
                }

                OrderRequestVO.getInstance().increaseProductByTypeAndOid(orderProductListVO.getType(), orderProductListVO.getOid());
                ((CartActivityV2)context).onResume();
            }
        });

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

    //아이템 삭제 해야하는 일이 있다.
        public void removeItemByPos(int pos, int type, int oid){
        listViewItemList.remove(pos);

        //그 pos의 oid를 찾아서 OrderRequestVO에서도 삭제
        if(type == OrderProductListVO.COFFEE){
            OrderRequestVO.getInstance().removeDrinkByOid(oid);
        }
        else if(type == OrderProductListVO.SPECIAL_FOOD){
            OrderRequestVO.getInstance().removeFoodByOid(oid);
        }
        notifyDataSetChanged();
    }

    //개수 올리기
    public void increaseCntofItemByPos(int pos, int type, int oid){

    }

    //개수 내리기
    public void decreaseCntofItemByPos(int pos, int type, int oid){

    }
}

