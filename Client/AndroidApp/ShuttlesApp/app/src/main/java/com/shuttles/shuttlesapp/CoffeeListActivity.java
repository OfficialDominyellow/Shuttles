package com.shuttles.shuttlesapp;

import android.content.Context;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.shuttles.shuttlesapp.vo.DrinkListVO;

import java.util.ArrayList;

public class CoffeeListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coffee_list_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_coffee_list);
        toolbar.setNavigationIcon(R.drawable.ic_lock_white_18dp); // your drawable
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });
        TabLayout tlCoffeeList = (TabLayout)findViewById(R.id.tl_coffee_list);
        ViewPager vpCoffeeList = (ViewPager)findViewById(R.id.vp_coffee_list);
        CoffeeListPagerAdapter coffeeListPagerAdapter = new CoffeeListPagerAdapter(getSupportFragmentManager());
        vpCoffeeList.setAdapter(coffeeListPagerAdapter);
        tlCoffeeList.setTabsFromPagerAdapter(coffeeListPagerAdapter);

        tlCoffeeList.setupWithViewPager(vpCoffeeList);
        vpCoffeeList.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tlCoffeeList));

    }

    public static class CoffeeListFragment extends Fragment {

        public static final java.lang.String ARG_PAGE = "arg_page";

        public CoffeeListFragment(){

        }

        public static CoffeeListFragment newInstance(int pageNumber){
            CoffeeListFragment coffeeListFragment = new CoffeeListFragment();
            Bundle arguments = new Bundle();
            arguments.putInt(ARG_PAGE, pageNumber);
            coffeeListFragment.setArguments(arguments);

            //return fragment including drink list which is suitable with each drink type(ex. all, new, shake, frappuccino, etc...)
            return coffeeListFragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            Bundle arguments = getArguments();
            int pageNumber = arguments.getInt(ARG_PAGE);
            View view = inflater.inflate(R.layout.drink_list_in_tab_layout, container,false);

            ListViewCompat lvDrinkList;
            CoffeeListViewAdapter coffeeListViewAdapter;

            coffeeListViewAdapter = new CoffeeListViewAdapter();
            lvDrinkList = (ListViewCompat) view.findViewById(R.id.lv_drink_tab);

            //onItemClickListener gogo
            lvDrinkList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    DrinkListVO drinkListVO = (DrinkListVO) adapterView.getItemAtPosition(i);

                    String name = drinkListVO.getName();
                    int price = drinkListVO.getPrice();

                    Toast.makeText(getContext(), "name : " + name + ", price : " + price + ", pos : " + i, Toast.LENGTH_SHORT).show();
                }
            });
            lvDrinkList.setAdapter(coffeeListViewAdapter);

            //add dummy data
            for(int i=0; i<pageNumber; i++){
                coffeeListViewAdapter.addItem(ContextCompat.getDrawable(getContext(), R.drawable.img_coffee_example), "아메리카노", 1234);
            }

            return view;
        }
    }
}

class CoffeeListPagerAdapter extends FragmentStatePagerAdapter{

    public CoffeeListPagerAdapter(FragmentManager fm){
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        CoffeeListActivity.CoffeeListFragment coffeeListFragment = CoffeeListActivity.CoffeeListFragment.newInstance(position);
        return coffeeListFragment;
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0:
                return "All";
            case 1:
                return "New";
            case 2:
                return "Frappuccino";
            case 3:
                return "Shake";
            default:
                return "xbal";
        }
    }
}

class CoffeeListViewAdapter extends BaseAdapter{
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<DrinkListVO> listViewItemList = new ArrayList<DrinkListVO>() ;

    // CoffeeListViewAdapter 생성자
    public CoffeeListViewAdapter() {

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
            convertView = inflater.inflate(R.layout.drink_list_row, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView ivDrinkList = (ImageView) convertView.findViewById(R.id.iv_drink_list) ;
        TextView tvDrinkName = (TextView) convertView.findViewById(R.id.tv_drink_name) ;
        TextView tvDrinkPrice = (TextView) convertView.findViewById(R.id.tv_drink_price) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        DrinkListVO drinkListVO = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        ivDrinkList.setImageDrawable(drinkListVO.getImg());
        tvDrinkName.setText(drinkListVO.getName());
        tvDrinkPrice.setText(drinkListVO.getPrice() + "원");

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
    public void addItem(Drawable img, String name, int price) {
        DrinkListVO item = new DrinkListVO();

        item.setImg(img);
        item.setName(name);
        item.setPrice(price);

        listViewItemList.add(item);
    }
}

