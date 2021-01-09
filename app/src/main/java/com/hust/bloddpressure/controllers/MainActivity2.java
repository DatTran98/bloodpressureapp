package com.hust.bloddpressure.controllers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.hust.bloddpressure.R;
import com.hust.bloddpressure.model.entities.InforStaticClass;
import com.hust.bloddpressure.util.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Class main activity
 */
public class MainActivity2 extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private View navBasic, navMess, navHome, navRoom, navNews, navAnalyst, navExport, navLogout;
    List<Integer> listViewId;
    int rule = 1;

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        InforStaticClass.setRule(0);
        findViewByIdView();
        listViewId = getListViewId();
        // Set action for menu navigation when click
        setClickNavigationItem();
        // set Display with rule user
        setDisplayRule();
        // Set action for action bar
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        //enable button home
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    /**
     * Set display navigation by rule
     */
    private void setDisplayRule() {
        if (InforStaticClass.getRule() == Constant.USER_RULE) {
            navExport.setVisibility(View.GONE);
            navRoom.setVisibility(View.GONE);
        } else {
            navBasic.setVisibility(View.GONE);
        }
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
                Intent intent = new Intent(this, MenuManagerActivity.class);
                startActivity(intent);
                return true;
            case R.id.reset:
                Intent intent1 = new Intent(this, this.getClass());
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
        drawerLayout = findViewById(R.id.export_activity);
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
                Intent intent = new Intent(MainActivity2.this, MenuManagerActivity.class);
                startActivity(intent);
            }
        });

        navBasic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                changeBackGroundExceptView(v);
                Intent intent = new Intent(MainActivity2.this, DetailUserActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constant.FLOW, Constant.FROM_NAV);
                intent.putExtras(bundle);
                startActivity(intent);
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
                Intent intent = new Intent(MainActivity2.this, ListRoomActivity.class);
                startActivity(intent);
                return true;
            }
        });
        navNews.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                changeBackGroundExceptView(v);
                Intent intent = new Intent(MainActivity2.this, ListNewsActivity.class);
                startActivity(intent);
                return true;
            }
        });
        navAnalyst.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                changeBackGroundExceptView(v);
                Intent intent = new Intent(MainActivity2.this, AnalysisActivity.class);
                startActivity(intent);
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
                setDefaultInfoLogoutSession();
                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                ActivityCompat.finishAffinity(MainActivity2.this);
                return true;
            }
        });
    }

    /**
     * When log out, set default information
     */
    private void setDefaultInfoLogoutSession() {
        InforStaticClass.setRule(1);
        InforStaticClass.setUserId(null);
    }

    /**
     * Set view background except selected view
     *
     * @param v view selecting
     */
    private void changeBackGroundExceptView(View v) {
        drawerLayout.closeDrawer(Gravity.LEFT);
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