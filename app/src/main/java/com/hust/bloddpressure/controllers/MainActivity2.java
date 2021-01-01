package com.hust.bloddpressure.controllers;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hust.bloddpressure.R;

import java.util.List;

public class MainActivity2 extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private View navBasic, navMess, navHome, navRoom, navNews, navAnalyst, navExport, navLogout;
    ConstraintLayout constraintLayout;
    List<Integer> listViewId;

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        findViewByIdView();
        setClickNavigationItem();
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
    private void findViewByIdView() {
        constraintLayout = findViewById(R.id.constrain);
        drawerLayout = findViewById(R.id.activity2_main_drawer);
        navBasic = findViewById(R.id.nav_basic);
        navMess = findViewById(R.id.nav_message);
        navHome = findViewById(R.id.nav_home);
        navRoom = findViewById(R.id.nav_room);
        navNews = findViewById(R.id.nav_news);
        navAnalyst = findViewById(R.id.nav_analyst);
        navExport = findViewById(R.id.nav_export);
        navLogout = findViewById(R.id.nav_logout);
    }

    private void setClickNavigationItem() {
        navBasic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setBackgroundResource(R.color.select_nav);

//                Intent intent = new Intent(MainActivity2.this, MenuManagerActivity.class);
//                startActivity(intent);
                return false;
            }
        });
        navMess.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setBackgroundResource(R.color.select_nav);
                ManageMenuFragment manageMenuFragment = new ManageMenuFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.constrain, manageMenuFragment);
                fragmentTransaction.commit();
                return true;
            }
        });

        navHome.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setBackgroundResource(R.color.select_nav);
                return true;
            }
        });
        navRoom.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setBackgroundResource(R.color.select_nav);
                return true;
            }
        });
        navNews.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setBackgroundResource(R.color.select_nav);
                return true;
            }
        });
        navAnalyst.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setBackgroundResource(R.color.select_nav);
                return true;
            }
        });
        navExport.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setBackgroundResource(R.color.select_nav);
                return true;
            }
        });
        navLogout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setBackgroundResource(R.color.select_nav);
                return true;
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.home:
                Toast.makeText(this, "Home button selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.reset:
                Toast.makeText(this, "Reset button selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.about:
                Toast.makeText(this, "About button selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.help:
                Toast.makeText(this, "Help button selected", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

}