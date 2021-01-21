package com.hust.bloddpressure.controllers;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import com.hust.bloddpressure.R;
import com.hust.bloddpressure.model.entities.InforStaticClass;
import com.hust.bloddpressure.util.Constant;

public class ToolBarSetting {
    private Activity activityToolBar;
    private Toolbar toolbar;
    private MenuInflater menu;
    private MenuItem homeItem, analystItem, userItem, newsItem, webItem, resetItem;

    public ToolBarSetting(Activity activity, Toolbar toolbar) {
        this.activityToolBar = activity;
        this.toolbar = toolbar;
        initToolBar();
    }

    private void initToolBar() {
        findViewByIdView();
        menu = new MenuInflater(activityToolBar.getBaseContext());
        toolbar.setTitle(Constant.EMPTY);
        activityToolBar.getMenuInflater().inflate(R.menu.main_actions, (Menu)menu);
//        activityToolBar.onOptionsItemSelected(homeItem);
//
//        homeItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                Intent intent2 = new Intent(activityToolBar, MenuManagerActivity.class);
//                activityToolBar.startActivity(intent2);
//                return true;
//            }
//        });
//        analystItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                Intent intent1 = new Intent(activityToolBar, AnalysisActivity.class);
//                activityToolBar.startActivity(intent1);
//                return true;
//            }
//        });
//        userItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                Intent intent;
//                if (Constant.USER_RULE == InforStaticClass.getRule()) {
//                    intent = new Intent(activityToolBar, DetailUserActivity.class);
//                } else {
//                    intent = new Intent(activityToolBar, ListUserActivity.class);
//                }
//                return true;
//            }
//        });
//        newsItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                Intent intent3 = new Intent(activityToolBar, ListNewsActivity.class);
//                activityToolBar.startActivity(intent3);
//                return true;
//            }
//        });
//        webItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                return true;
//            }
//        });
//        resetItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
////                Intent intent4 = new Intent(activityToolBar, activityToolBar.getClass());
////                activityToolBar.startActivity(intent4);
//                return true;
//            }
//        });
    }

    private void findViewByIdView() {
        homeItem = toolbar.findViewById(R.id.home);
        analystItem = toolbar.findViewById(R.id.home);
        userItem = toolbar.findViewById(R.id.home);
        newsItem = toolbar.findViewById(R.id.home);
        resetItem = toolbar.findViewById(R.id.reset);
        webItem = toolbar.findViewById(R.id.home);
    }
}
