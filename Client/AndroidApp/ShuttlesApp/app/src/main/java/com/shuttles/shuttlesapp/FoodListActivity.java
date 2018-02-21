package com.shuttles.shuttlesapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
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

import com.shuttles.shuttlesapp.vo.DrinkListVO;
import com.shuttles.shuttlesapp.vo.FoodListVO;

import java.util.ArrayList;
import java.util.List;

public class FoodListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_list_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_food_list);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_12dp); // your drawable
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });

        TabLayout tlFoodList = (TabLayout)findViewById(R.id.tl_food_list);
        ViewPager vpFoodList = (ViewPager)findViewById(R.id.vp_food_list);
        FoodListPagerAdapter foodListPagerAdapter = new FoodListPagerAdapter(getSupportFragmentManager());
        vpFoodList.setAdapter(foodListPagerAdapter);
        tlFoodList.setTabsFromPagerAdapter(foodListPagerAdapter);

        tlFoodList.setupWithViewPager(vpFoodList);
        vpFoodList.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tlFoodList));

        ImageView ivCart = (ImageView) findViewById(R.id.iv_cart_in_food_list);
        ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "장바구니", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(intent);
            }
        });
    }

    public static class FoodListFragment extends Fragment {

        public static final java.lang.String ARG_PAGE = "arg_page";

        public FoodListFragment(){

        }

        public static FoodListActivity.FoodListFragment newInstance(int pageNumber){
            FoodListActivity.FoodListFragment foodListFragment = new FoodListActivity.FoodListFragment();
            Bundle arguments = new Bundle();
            arguments.putInt(ARG_PAGE, pageNumber);
            foodListFragment.setArguments(arguments);

            //return fragment including food list which is suitable with each food type(ex. all, new, shake, frappuccino, etc...)
            return foodListFragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            Bundle arguments = getArguments();
            int pageNumber = arguments.getInt(ARG_PAGE);
            View view = inflater.inflate(R.layout.food_list_in_tab_layout, container,false);

            ListViewCompat lvFoodList;
            FoodListViewAdapter foodListViewAdapter;

            foodListViewAdapter = new FoodListViewAdapter();
            lvFoodList = (ListViewCompat) view.findViewById(R.id.lv_food_tab);

            //onItemClickListener gogo
            lvFoodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    FoodListVO foodListVO = (FoodListVO) adapterView.getItemAtPosition(i);

                    String name = foodListVO.getName();
                    String price = foodListVO.getPrice();

                    Toast.makeText(getContext(), "name : " + name + ", price : " + price + ", pos : " + i, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), FoodOrderDetailActivity.class);
                    startActivity(intent);
                }
            });
            lvFoodList.setAdapter(foodListViewAdapter);

            List<FoodListVO> specialFoodList = GlobalApplication.specialFoodList;

            /*TODO 애초에 null이면 여기까지도 오면 안됨 null인경우는 데이터 못받아온경우임*/
            if(specialFoodList!=null) {
                for (FoodListVO element : specialFoodList) {
                    foodListViewAdapter.addItem(element.getImg(), element.getName(), element.getPrice());
                }
            }

            return view;
        }
    }
}

class FoodListPagerAdapter extends FragmentStatePagerAdapter {

    public FoodListPagerAdapter(FragmentManager fm){
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        FoodListActivity.FoodListFragment foodListFragment = FoodListActivity.FoodListFragment.newInstance(position);
        return foodListFragment;
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0:
                return "탕맛기픈";
            case 1:
                return "r4맛집";
            case 2:
                return "김밥천국";
            case 3:
                return "김천국밥";
            default:
                return "gral";
        }
    }
}

class FoodListViewAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<FoodListVO> listViewItemList = new ArrayList<FoodListVO>() ;

    // FoodListViewAdapter 생성자
    public FoodListViewAdapter() {

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
            convertView = inflater.inflate(R.layout.food_list_row, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView ivFoodList = (ImageView) convertView.findViewById(R.id.iv_food_list) ;
        TextView tvFoodName = (TextView) convertView.findViewById(R.id.tv_food_name) ;
        TextView tvFoodPrice = (TextView) convertView.findViewById(R.id.tv_food_price) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        FoodListVO foodListVO = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        ivFoodList.setImageDrawable(foodListVO.getImg());
        tvFoodName.setText(foodListVO.getName());
        tvFoodPrice.setText(foodListVO.getPrice() + "원");

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
    public void addItem(Drawable img, String name, String price) {
        FoodListVO item = new FoodListVO();

        item.setImg(img);
        item.setName(name);
        item.setPrice(price);

        listViewItemList.add(item);
    }
}