package com.shuttles.shuttlesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.shuttles.shuttlesapp.ConnectionController.UserInfo;

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
        TextView tvDashboardName = (TextView)findViewById(R.id.tv_dashboard_name);
        TextView tvDashBoardEmail = (TextView)findViewById(R.id.tv_dashboard_email);
        tvDashboardName.setText(UserInfo.getInstance().getProfile().getNickname());
        tvDashBoardEmail.setText(UserInfo.getInstance().getProfile().getEmail());
        initNavigationDrawer();
        setCardView();
    }

    public void setCardView(){
        CardView cvModifyPersonal = (CardView) findViewById(R.id.cv_modify_personal);
        cvModifyPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "주소록", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), AddressListActivity.class);
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
                Intent intent = new Intent(getApplicationContext(), DrinkListVtcActivity.class);
                startActivity(intent);
            }
        });

    }
    private boolean isOwner(){
        if(UserInfo.getInstance().getUserType() == UserInfo.Type.owner)
            return true;
        else
            return false;
    }
    public void initNavigationDrawer() {

        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);

        /*Owner 권한이 있는 경우만 활성화*/
        if(!isOwner()) {
            Menu ownerPage = navigationView.getMenu();
            MenuItem menuItem = ownerPage.findItem(R.id.itm_order_manage);
            menuItem.setEnabled(false);
            menuItem.setVisible(false);
        }

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
                    case R.id.itm_address:
                        Toast.makeText(getApplicationContext(),"주소록",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        intent = new Intent(getApplicationContext(), AddressListActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.itm_notice:
                        Toast.makeText(getApplicationContext(),"공지사항",Toast.LENGTH_SHORT).show();
                        intent = new Intent(getApplicationContext(), NoticeActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.itm_introduce:
                        Toast.makeText(getApplicationContext(),"소개",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.itm_order_history:
                        Toast.makeText(getApplicationContext(),"주문내역조회",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        intent = new Intent(getApplicationContext(), OrderHistoryActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
        View header = navigationView.getHeaderView(0);
        TextView tvNavUserName = (TextView)header.findViewById(R.id.tv_nav_user_name);
        tvNavUserName.setText(UserInfo.getInstance().getProfile().getNickname() + "");

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