package com.hust.bloddpressure.controllers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.hust.bloddpressure.R;
import com.hust.bloddpressure.model.entities.InforStaticClass;
import com.hust.bloddpressure.util.Constant;

import java.util.ArrayList;
import java.util.List;

public class NavigationSetting {
    private DrawerLayout drawerLayout;
    private final Activity activityNav;
    private  List<Integer> listViewId;
    private View navBasic, navMess, navHome, navRoom, navNews, navAnalyst, navExport, navLogout;

    public NavigationSetting(Activity activity) {
        this.activityNav = activity;
        initNav();
    }

    /**
     * Init Navigation
     */
    private void initNav() {
        findViewByIdView();
        findViewByIdView();
        listViewId = getListViewId();
        // Set action for menu navigation when click
        setClickNavigationItem();
        // set Display with rule user
        setDisplayRule();
    }
    /**
     * Find view by id from id view for any navigation view
     */
    private void findViewByIdView() {
        drawerLayout = activityNav.findViewById(R.id.drawable);
        navBasic = activityNav.findViewById(R.id.nav_basic);
        navMess = activityNav.findViewById(R.id.nav_message);
        navHome = activityNav.findViewById(R.id.nav_home);
        navRoom = activityNav.findViewById(R.id.nav_room);
        navNews = activityNav.findViewById(R.id.nav_news);
        navAnalyst = activityNav.findViewById(R.id.nav_analyst);
        navExport = activityNav.findViewById(R.id.nav_export);
        navLogout = activityNav.findViewById(R.id.nav_logout);
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
                Intent intent = new Intent(activityNav, MenuManagerActivity.class);
                activityNav.startActivity(intent);
            }
        });

        navBasic.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                changeBackGroundExceptView(v);
                Intent intent = new Intent(activityNav, DetailUserActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constant.FLOW, Constant.FROM_NAV);
                intent.putExtras(bundle);
                activityNav.startActivity(intent);
                return true;
            }
        });
        navMess.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                changeBackGroundExceptView(v);
                Toast.makeText(activityNav, Constant.DEVERLOPING, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        navRoom.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                changeBackGroundExceptView(v);
                Intent intent = new Intent(activityNav, ListRoomActivity.class);
                activityNav.startActivity(intent);
                return true;
            }
        });
        navNews.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                changeBackGroundExceptView(v);
                Intent intent = new Intent(activityNav, ListNewsActivity.class);
                activityNav.startActivity(intent);
                return true;
            }
        });
        navAnalyst.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                changeBackGroundExceptView(v);
                Intent intent = new Intent(activityNav, AnalysisActivity.class);
                activityNav.startActivity(intent);
                return true;
            }
        });
        navExport.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                changeBackGroundExceptView(v);
                Intent intent = new Intent(activityNav, ExportActivity.class);
                activityNav.startActivity(intent);
                return true;
            }
        });
        navLogout.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                changeBackGroundExceptView(v);
                setDefaultInfoLogoutSession();
                Intent intent = new Intent(activityNav, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activityNav.startActivity(intent);
                ActivityCompat.finishAffinity(activityNav);
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
    @SuppressLint("RtlHardcoded")
    private void changeBackGroundExceptView(View v) {
        drawerLayout.closeDrawer(Gravity.LEFT);
        int viewId = v.getId();
        for (int i : listViewId) {
            if (viewId == i) {
                v.setBackgroundResource(R.color.select_nav);
            } else {
                activityNav.findViewById(i).setBackgroundResource(R.color.white);
            }
        }

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

}