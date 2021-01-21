package com.hust.bloddpressure.controllers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hust.bloddpressure.R;
import com.hust.bloddpressure.model.entities.InforStaticClass;
import com.hust.bloddpressure.util.Constant;

public class DetailUserActivity extends AppCompatActivity {
    private TextView textViewIdUser, textViewMessage;
    private int tabMode;
    private Button btnMoveRoom, btnEdit, btnHis, btnBasic;
    private String userId;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);
        findViewById();
//        toolbar = findViewById(R.id.tool_bar);
//        toolbar.setTitle(Constant.EMPTY);
//        setSupportActionBar(toolbar);
        new NavigationSetting(DetailUserActivity.this);
        drawerLayout = findViewById(R.id.drawable);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        // Get id from another activity
        Bundle bundle = getIntent().getExtras();
        int rule = InforStaticClass.getRule();

        if (Constant.USER_RULE == rule) {
            userId = InforStaticClass.getUserId();
        } else {
            if (bundle == null) {
                textViewMessage.setText(R.string.no_data);
            } else {
                String flow = bundle.getString(Constant.FLOW);
                if (flow != null) {
                    userId = InforStaticClass.getUserId();
                } else {
                    userId = bundle.getString(Constant.USER_ID);
                }
            }
        }
        textViewIdUser.setText(userId);
        Bundle bundle1 = getIdUserMain();
        TabBasicDetailUserFragment tabBasicDetailUserFragment = new TabBasicDetailUserFragment();
        tabBasicDetailUserFragment.setArguments(bundle1);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.content_detail, tabBasicDetailUserFragment);
        fragmentTransaction.commit();
        // Set mode tab
        tabMode = Constant.MODE_BASIC;
        // Set disable button when user user rule
        if (rule == Constant.USER_RULE) {
            btnEdit.setVisibility(View.INVISIBLE);
            btnMoveRoom.setVisibility(View.INVISIBLE);
        }

        btnHis.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Bundle bundle = getIdUserMain();
                TabHistoryPressureFragment tabHistoryPressureFragment = new TabHistoryPressureFragment();
                tabHistoryPressureFragment.setArguments(bundle);
                setFragmentByManagerFragment(R.id.content_detail, tabHistoryPressureFragment);
                tabMode = Constant.MODE_HISTORY;
                btnHis.setVisibility(View.GONE);
                btnBasic.setVisibility(View.VISIBLE);
            }
        });
        btnBasic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getIdUserMain();
                TabBasicDetailUserFragment tabBasicDetailFragment = new TabBasicDetailUserFragment();
                tabBasicDetailFragment.setArguments(bundle);
                setFragmentByManagerFragment(R.id.content_detail, tabBasicDetailFragment);
                tabMode = Constant.MODE_BASIC;
                btnBasic.setVisibility(View.GONE);
                btnHis.setVisibility(View.VISIBLE);
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailUserActivity.this, EditUserActivity.class);
                Bundle bundle = getIdUserMain();
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        btnMoveRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailUserActivity.this, MoveToAnotherRoom.class);
                Bundle bundle = getIdUserMain();
                intent.putExtras(bundle);
                startActivity(intent);
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
                Intent intent = new Intent(this, MenuManagerActivity.class);
                startActivity(intent);
                return true;
            case R.id.user:
                Intent intent1;
                if (Constant.USER_RULE == InforStaticClass.getRule()) {
                    intent1 = new Intent(this, DetailUserActivity.class);
                } else {
                    intent1 = new Intent(this, ListUserActivity.class);
                }
                startActivity(intent1);
                return true;
            case R.id.analyst:
                Intent intent2 = new Intent(this, AnalysisActivity.class);
                startActivity(intent2);
                return true;
            case R.id.news:
                Intent intent3 = new Intent(this, ListNewsActivity.class);
                startActivity(intent3);
                return true;
            case R.id.web:
                return true;
            case R.id.reset:
//                Intent intent1 = new Intent(this, this.getClass());
//                startActivity(intent1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Bundle getIdUserMain() {
        Bundle bundle = new Bundle();
        textViewIdUser = findViewById(R.id.user_id_main);
        bundle.putString(Constant.USER_ID, textViewIdUser.getText().toString());
        return bundle;
    }

    /**
     * @param idFrameContent  id frame need set
     * @param fragmentRequire fragment set to layout
     */
    private void setFragmentByManagerFragment(int idFrameContent, Fragment fragmentRequire) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(idFrameContent, fragmentRequire);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * find view view in layout view group
     */
    private void findViewById() {
        drawerLayout = findViewById(R.id.drawable);
        btnHis = findViewById(R.id.btn_history_pressure);
        btnBasic = findViewById(R.id.btn_basic_detail);
        textViewMessage = findViewById(R.id.message);
        textViewIdUser = findViewById(R.id.user_id_main);
        btnEdit = findViewById(R.id.btn_edit_user);
        btnMoveRoom = findViewById(R.id.btn_move_to_another_room);
    }

}