package com.hust.bloddpressure.controllers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.hust.bloddpressure.R;
import com.hust.bloddpressure.model.entities.InforStaticClass;
import com.hust.bloddpressure.util.Common;
import com.hust.bloddpressure.util.Constant;

import java.util.ArrayList;
import java.util.List;

public class NavigationSetting {
    private DrawerLayout drawerLayout;
    private final Activity activityNav;
    private List<Integer> listViewId;
    private View navAbout, navMess, navRoom, navExport, navLogout, navHelp, navSetting;
    private TextView navName;

    public NavigationSetting(Activity activity) {
        this.activityNav = activity;
        initNav();
    }

    /**
     * Init Navigation
     */
    private void initNav() {
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
        navName = activityNav.findViewById(R.id.activity_main_tv_user_name);
        drawerLayout = activityNav.findViewById(R.id.drawable);
        navMess = activityNav.findViewById(R.id.nav_message);
        navRoom = activityNav.findViewById(R.id.nav_room);
        navAbout = activityNav.findViewById(R.id.nav_about);
        navHelp = activityNav.findViewById(R.id.nav_help);
        navExport = activityNav.findViewById(R.id.nav_export);
        navLogout = activityNav.findViewById(R.id.nav_logout);
        navSetting = activityNav.findViewById(R.id.nav_setting);

    }

    /**
     * Get list Id for each view
     *
     * @return list ID
     */
    private List<Integer> getListViewId() {
        List<Integer> listId = new ArrayList<>();
        listId.add(navAbout.getId());
        listId.add(navMess.getId());
        listId.add(navRoom.getId());
        listId.add(navHelp.getId());
        listId.add(navExport.getId());
        listId.add(navLogout.getId());
        listId.add(navSetting.getId());
        return listId;
    }

    /**
     * Method do action when choose navigation menu
     */
    private void setClickNavigationItem() {
        navAbout.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                changeBackGroundExceptView(v);
                Intent intent = new Intent(activityNav, AboutActivity.class);
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
        navSetting.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                changeBackGroundExceptView(v);
                Intent intent = new Intent(activityNav, SettingStandardPressureActivity.class);
                activityNav.startActivity(intent);
                return true;
            }
        });
        navHelp.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                changeBackGroundExceptView(v);
                //Create interface dialog
                AlertDialog.Builder confirm = new AlertDialog.Builder(activityNav);
                // Set information for dialog
                confirm.setTitle(Constant.CONTACT);
                confirm.setMessage(Constant.MESSAGE_CONTACT);
                confirm.setPositiveButton(Constant.YES, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                //Create dialog
                AlertDialog dialogConfirm = confirm.create();
                dialogConfirm.show();
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
            navName.setText(Constant.HELLO + InforStaticClass.getFullName());
            navExport.setVisibility(View.GONE);
            navRoom.setVisibility(View.GONE);
        } else {
            navSetting.setVisibility(View.GONE);
        }
    }

}
