package com.hust.bloddpressure.controllers;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hust.bloddpressure.R;
import com.hust.bloddpressure.util.Common;

import java.util.ArrayList;
import java.util.List;

/**
 * Class main activity
 */
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
        ManageMenuFragment manageMenuFragment = new ManageMenuFragment();
        Common.setFragmentByManagerFragment(R.id.constrain, manageMenuFragment, this);
        listViewId = getListViewId();
        // Set action for menu navigation when click
        setClickNavigationItem();
        // Set action for action bar
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        //enable button home
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
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
                Intent intent = new Intent(this,MenuManagerActivity.class);
                startActivity(intent);
                return true;
            case R.id.reset:
                Intent intent1 = new Intent(this,this.getClass());
                startActivity(intent1);
                return true;
            case R.id.about:
                // Create about activity
                Toast.makeText(this, "About button selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.help:
                // Create help activity
                Toast.makeText(this, "Help button selected", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Find view by id from id view for any navigation view
     */
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

    /**
     * Get list Id for each view
     *
     * @return list ID
     */
    private List<Integer> getListViewId() {
        List<Integer> listId = new ArrayList<>();
        listId.add(navBasic.getId());
        listId.add(navMess.getId());
        listId.add(navHome.getId());
        listId.add(navRoom.getId());
        listId.add(navNews.getId());
        listId.add(navAnalyst.getId());
        listId.add(navExport.getId());
        listId.add(navLogout.getId());
        return listId;
    }

    /**
     * Method do action when choose navigation menu
     */
    private void setClickNavigationItem() {
        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeBackGroundExceptView(view);
                Intent intent = getParentActivityIntent();
                startActivity(intent);
            }
        });

        navBasic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                changeBackGroundExceptView(v);
                return true;
            }
        });
        navMess.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                changeBackGroundExceptView(v);
                return true;
            }
        });
        navRoom.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                changeBackGroundExceptView(v);
                return true;
            }
        });
        navNews.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                changeBackGroundExceptView(v);
                return true;
            }
        });
        navAnalyst.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                changeBackGroundExceptView(v);
                return true;
            }
        });
        navExport.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                changeBackGroundExceptView(v);
                return true;
            }
        });
        navLogout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                changeBackGroundExceptView(v);
                return true;
            }
        });
    }

    /**
     * Set view background except selected view
     * @param v view selecting
     */
    private void changeBackGroundExceptView(View v) {
        int viewId = v.getId();
        for (int i : listViewId) {
            if (viewId == i) {
                v.setBackgroundResource(R.color.select_nav);
            } else {
                findViewById(i).setBackgroundResource(R.color.white);
            }
        }

    }
}