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

import java.util.ArrayList;
import java.util.List;

public class DrinkListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drink_list_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_drink_list);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_12dp); // your drawable
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });
        TabLayout tlDrinkList = (TabLayout)findViewById(R.id.tl_drink_list);
        ViewPager vpDrinkList = (ViewPager)findViewById(R.id.vp_drink_list);
        DrinkListPagerAdapter drinkListPagerAdapter = new DrinkListPagerAdapter(getSupportFragmentManager());
        vpDrinkList.setAdapter(drinkListPagerAdapter);
        tlDrinkList.setTabsFromPagerAdapter(drinkListPagerAdapter);

        tlDrinkList.setupWithViewPager(vpDrinkList);
        vpDrinkList.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tlDrinkList));

        ImageView ivCart = (ImageView) findViewById(R.id.iv_cart_in_drink_list);
        ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "장바구니", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(intent);
            }
        });
    }

    public static class DrinkListFragment extends Fragment {

        public static final java.lang.String ARG_PAGE = "arg_page";

        public DrinkListFragment(){

        }

        public static DrinkListFragment newInstance(int pageNumber){
            DrinkListFragment drinkListFragment = new DrinkListFragment();
            Bundle arguments = new Bundle();
            arguments.putInt(ARG_PAGE, pageNumber);
            drinkListFragment.setArguments(arguments);

            //return fragment including drink list which is suitable with each drink type(ex. all, new, shake, frappuccino, etc...)
            return drinkListFragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            Bundle arguments = getArguments();
            int pageNumber = arguments.getInt(ARG_PAGE);
            View view = inflater.inflate(R.layout.drink_list_in_tab_layout, container,false);

            ListViewCompat lvDrinkList;
            DrinkListViewAdapter drinkListViewAdapter;

            drinkListViewAdapter = new DrinkListViewAdapter();
            lvDrinkList = (ListViewCompat) view.findViewById(R.id.lv_drink_tab);

            //onItemClickListener gogo
            lvDrinkList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    DrinkListVO drinkListVO = (DrinkListVO) adapterView.getItemAtPosition(i);

                    String name = drinkListVO.getName();
                    String price = drinkListVO.getPrice();

                    Toast.makeText(getContext(), "name : " + name + ", price : " + price + ", pos : " + i, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), DrinkOrderDetailActivity.class);
                    intent.putExtra("DRINK_VO",i);
                    startActivity(intent);
                }
            });
            lvDrinkList.setAdapter(drinkListViewAdapter);


            List<DrinkListVO> drinkList = GlobalApplication.drinkList;

            /*TODO 애초에 null이면 여기까지도 오면 안됨 null인경우는 데이터 못받아온경우임*/
            if(drinkList!=null) {
                for (DrinkListVO element : drinkList) {
                    drinkListViewAdapter.addItem(element.getImg(), element.getName(), element.getPrice());
                }
            }

            return view;
        }
    }
}

class DrinkListPagerAdapter extends FragmentStatePagerAdapter{

    public DrinkListPagerAdapter(FragmentManager fm){
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        DrinkListActivity.DrinkListFragment drinkListFragment = DrinkListActivity.DrinkListFragment.newInstance(position);
        return drinkListFragment;
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

class DrinkListViewAdapter extends BaseAdapter{
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<DrinkListVO> listViewItemList = new ArrayList<DrinkListVO>() ;

    // DrinkListViewAdapter 생성자
    public DrinkListViewAdapter() {

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
    public void addItem(Drawable img, String name, String price) {
        DrinkListVO item = new DrinkListVO();

        item.setImg(img);
        item.setName(name);
        item.setPrice(price);

        listViewItemList.add(item);
    }
}

