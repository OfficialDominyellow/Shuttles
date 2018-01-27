package com.shuttles.shuttlesapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
            return coffeeListFragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            Bundle arguments = getArguments();
            int pageNumber = arguments.getInt(ARG_PAGE);
            TextView tvTmp = new TextView(getActivity());
            tvTmp.setText("Hello coffee list fragment " + pageNumber);
            tvTmp.setGravity(Gravity.CENTER);
            return tvTmp;
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
        return "Tab " + position;
    }
}