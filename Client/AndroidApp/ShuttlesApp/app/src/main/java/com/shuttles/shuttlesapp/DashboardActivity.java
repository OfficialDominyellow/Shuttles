package com.shuttles.shuttlesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class DashboardActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dashboard_layout);
        toolbar = (Toolbar) findViewById(R.id.dashboard_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        initNavigationDrawer();
        setCardView();
    }

    public void setCardView(){
        CardView cvModifyPersonal = (CardView) findViewById(R.id.cv_modify_personal);
        cvModifyPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "개인정보수정", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), ModifyPersonalActivity.class);
                startActivity(intent);
            }
        });

        CardView cvNotice = (CardView) findViewById(R.id.cv_notice);
        cvNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "공지사항", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), NoticeActivity.class);
                startActivity(intent);
            }
        });

        CardView cvOrder = (CardView)findViewById(R.id.cv_order);
        cvOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "주문하기", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), DrinkListActivity.class);
                startActivity(intent);
            }
        });

    }

    public void initNavigationDrawer() {

        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();
                Intent intent;
                switch (id){
                    case R.id.itm_home:
                        Toast.makeText(getApplicationContext(),"홈",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.itm_order:
                        Toast.makeText(getApplicationContext(),"주문하기",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        intent = new Intent(getApplicationContext(), DrinkListVtcActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.itm_event:
                        Toast.makeText(getApplicationContext(),"이벤트",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.itm_introduce:
                        Toast.makeText(getApplicationContext(),"소개",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.itm_order_history:
                        Toast.makeText(getApplicationContext(),"주문내역조회",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        intent = new Intent(getApplicationContext(), OrderHistoryActivity.class);
                        startActivity(intent);
                    case R.id.itm_order_manage:
                        Toast.makeText(getApplicationContext(),"주문내역관리(사장님)",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        intent = new Intent(getApplicationContext(), OrderManageActivity.class);
                        startActivity(intent);
                        break;

                }
                return true;
            }
        });
        View header = navigationView.getHeaderView(0);
        TextView tvUserName = (TextView)header.findViewById(R.id.tv_user_name);
        tvUserName.setText("러블리즈 미주");
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_dashboard);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerClosed(View v){
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

}