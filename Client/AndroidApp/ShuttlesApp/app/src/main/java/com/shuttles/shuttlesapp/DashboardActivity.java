package com.shuttles.shuttlesapp;

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
        CardView cvOrder;
        cvOrder = (CardView)findViewById(R.id.cv_order);
        cvOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();

                switch (id){
                    case R.id.cv_order:
                        Toast.makeText(getApplicationContext(), "주문하기", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    public void initNavigationDrawer() {

        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id){
                    case R.id.itm_home:
                        Toast.makeText(getApplicationContext(),"홈",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.itm_order:
                        Toast.makeText(getApplicationContext(),"주문하기",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.itm_event:
                        Toast.makeText(getApplicationContext(),"이벤트",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.itm_introduce:
                        Toast.makeText(getApplicationContext(),"소개",Toast.LENGTH_SHORT).show();

                }
                return true;
            }
        });
        View header = navigationView.getHeaderView(0);
        TextView tv_email = (TextView)header.findViewById(R.id.tv_email);
        tv_email.setText("dominyellow@gmail.com");
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