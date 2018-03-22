package com.shuttles.shuttlesapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shuttles.shuttlesapp.ConnectionController.ConnectionImpl;
import com.shuttles.shuttlesapp.ConnectionController.ImageLoadHandler;
import com.shuttles.shuttlesapp.ConnectionController.RequestData;
import com.shuttles.shuttlesapp.ConnectionController.RequestHandler;
import com.shuttles.shuttlesapp.ConnectionController.RestAPI;
import com.shuttles.shuttlesapp.Utils.Constants;
import com.shuttles.shuttlesapp.vo.DrinkListVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DrinkListVtcActivity extends AppCompatActivity implements ConnectionImpl {

    private RequestData requestData = null;
    private List<DrinkListVO> drinkList;

    private ExpandableListView elvDrinkList;
    private ExpandableListAdapter listAdapter;
    private List<String> listHeader;
    private HashMap<String, List<DrinkListVO>> listHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestData = new RequestData("GET", RestAPI.DRINK_LIST, RestAPI.REQUEST_TYPE_DRINK_LIST);
        sendRequestData(requestData);
    }

    public void setCardView(){
        CardView cvMenu1 = (CardView) findViewById(R.id.cv_menu1);
        cvMenu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "11111", Toast.LENGTH_SHORT).show();
            }
        });

        CardView cvMenu2 = (CardView) findViewById(R.id.cv_menu2);
        cvMenu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "22222", Toast.LENGTH_SHORT).show();
            }
        });

        CardView cvMenu3 = (CardView) findViewById(R.id.cv_menu3);
        cvMenu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "33333", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void sendRequestData(RequestData requestData) {
        new RequestHandler(this).execute(requestData);
    }

    @Override
    public void requestCallback(int REQUEST_TYPE) {
        switch (REQUEST_TYPE) {
            case RestAPI.REQUEST_TYPE_FAILED:
                //failed
                break;
            case RestAPI.REQUEST_TYPE_DRINK_LIST:
                //set VO class
                Gson gson = new Gson();

                drinkList = gson.fromJson(requestData.getResult(), new TypeToken<List<DrinkListVO>>() {
                }.getType());

                for (DrinkListVO element : drinkList) {
                    Log.i(Constants.LOG_TAG, element.getCoffee_id());
                    element.convertURLtoFileName();
                }

                new ImageLoadHandler(this).execute(drinkList);
                break;
            case RestAPI.REQUEST_TYPE_IMAGE_LOAD:
                setContentView(R.layout.drink_list_vtc_layout);

                Toolbar toolbar = (Toolbar) findViewById(R.id.tb_drink_list_vtc);
                toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_12dp); // your drawable
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed(); // Implemented by activity
                    }
                });
                setCardView();

                elvDrinkList = (ExpandableListView)findViewById(R.id.elv_drink_list_vtc);

                elvDrinkList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                        DrinkListVO drinkListVO = (DrinkListVO) listAdapter.getChild(groupPosition, childPosition);

                        String name = drinkListVO.getName();
                        String url = drinkListVO.getPicture_url();
                        String coffeeID = drinkListVO.getCoffee_id();
                        String price = drinkListVO.getPrice();

                        Toast.makeText(getApplicationContext(), groupPosition + " " + childPosition + " " + name + " " + url, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), DrinkOrderDetailPopActivity.class);
                        intent.putExtra("coffee_id", coffeeID);
                        intent.putExtra("name", name);
                        intent.putExtra("price", price);
                        startActivity(intent);
                        return false;
                    }
                });

                listHeader = new ArrayList<String>();
                listHashMap = new HashMap<>();
                for(DrinkListVO element : drinkList){
                    String header = element.getState();
                    if(!listHeader.contains(header)){
                        listHeader.add(header);
                        listHashMap.put(header, new ArrayList<DrinkListVO>());
                    }
                    listHashMap.get(header).add(element);
                }

                listAdapter = new ExpandableListAdapter(this, listHeader, listHashMap);
                elvDrinkList.setAdapter(listAdapter);

                break;
        }
    }
}

class ExpandableListAdapter extends BaseExpandableListAdapter{
    private Context ctx;
    private List<String> listHeader;
    private HashMap<String, List<DrinkListVO>> listHashMap;

    public ExpandableListAdapter(Context ctx, List<String> listHeader, HashMap<String, List<DrinkListVO>> listHashMap) {
        this.ctx = ctx;
        this.listHeader = listHeader;
        this.listHashMap = listHashMap;
    }

    @Override
    public int getGroupCount() {
        return listHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listHashMap.get(listHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listHashMap.get(listHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerStr = (String) getGroup(groupPosition);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.drink_list_vtc_group, null);
        }
        TextView tvHeader = (TextView)convertView.findViewById(R.id.tv_drink_list_vtc_header);
        tvHeader.setTypeface(null, Typeface.BOLD);
        tvHeader.setText(headerStr);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final DrinkListVO drinkListVO = (DrinkListVO) getChild(groupPosition, childPosition);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.drink_list_row, null);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView ivDrinkList = (ImageView) convertView.findViewById(R.id.iv_drink_list) ;
        TextView tvDrinkName = (TextView) convertView.findViewById(R.id.tv_drink_name) ;
        TextView tvDrinkPrice = (TextView) convertView.findViewById(R.id.tv_drink_price) ;

        // 아이템 내 각 위젯에 데이터 반영
        ivDrinkList.setImageDrawable(drinkListVO.getImg());
        tvDrinkName.setText(drinkListVO.getName());
        tvDrinkPrice.setText(drinkListVO.getPrice() + "원");

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}