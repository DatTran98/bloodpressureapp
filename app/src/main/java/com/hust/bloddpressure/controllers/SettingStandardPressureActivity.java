package com.hust.bloddpressure.controllers;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hust.bloddpressure.R;
import com.hust.bloddpressure.model.MyService;
import com.hust.bloddpressure.model.entities.BloodPressureInfor;
import com.hust.bloddpressure.model.entities.InforStaticClass;
import com.hust.bloddpressure.model.entities.Room;
import com.hust.bloddpressure.util.Common;
import com.hust.bloddpressure.util.Constant;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SettingStandardPressureActivity extends AppCompatActivity {
    private Button btnUpdate;
    private EditText standardMax, standardMin;
    private ProgressDialog pDialog;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private int standardMaxValueDB, standardMinValueDB;
    private TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_standard_pressure);
        getViewById();
        new NavigationSetting(SettingStandardPressureActivity.this);
        drawerLayout = findViewById(R.id.drawable);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(Constant.EMPTY);
        GetStandardPressure getStandardPressure = new GetStandardPressure(InforStaticClass.getUserId());
        getStandardPressure.execute();
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int standardMaxValue = Common.convertToInt(standardMax.getText().toString(), Constant.INT_VALUE_DEFAULT);
                int standardMinValue = Common.convertToInt(standardMin.getText().toString(), Constant.INT_VALUE_DEFAULT);
                SetStandard setStandard = new SetStandard(standardMinValue, standardMaxValue);
                setStandard.execute();

            }
        });
    }

    private void getViewById() {
        btnUpdate = findViewById(R.id.btn_update);
        standardMax = findViewById(R.id.max_standard);
        standardMin = findViewById(R.id.min_standard);
        message = findViewById(R.id.message);
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
            case R.id.reset:
                GetStandardPressure getStandardPressure = new GetStandardPressure(InforStaticClass.getUserId());
                getStandardPressure.execute();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class GetStandardPressure extends AsyncTask {
        private String userId;

        public GetStandardPressure(String userId) {
            this.userId = userId;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            MyService jsonParser = new MyService();
            List<NameValuePair> args = new ArrayList<>();
            args.add(new BasicNameValuePair(Constant.USER_ID, userId));
            String json = jsonParser.callService(Constant.URL_UPDATE_STANDARD_PRESSURE, MyService.GET, args);
            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    int success = jsonObj.getInt(Constant.JSON_SUCCESS);
                    if (Constant.SERVER_ERROR == success) {
                        message.setText(Constant.EDIT_USER_DELETED);
                    } else {
                        JSONArray jsonArrayStandard = jsonObj.getJSONArray(Constant.OBJECT_JSON_STANDARD);
                        for (int i = 0; i < jsonArrayStandard.length(); i++) {
                            JSONObject obj = (JSONObject) jsonArrayStandard.get(i);
                            standardMaxValueDB = obj.getInt(Constant.STANDARD_MAX);
                            standardMinValueDB = obj.getInt(Constant.STANDARD_MIN);
                        }
                    }
                } catch (JSONException e) {
                    message.setText(Constant.MESSAGE_SERVER_FAILED);
                }
            } else {
                Log.e(Constant.LOG_JSON, Constant.MSG_JSON);
                message.setText(Constant.MESSAGE_SERVER_FAILED);
            }
            return null;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SettingStandardPressureActivity.this);
            pDialog.setMessage(Constant.MESSAGE_LOADING);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (pDialog.isShowing())
                pDialog.dismiss();
            standardMax.setText(standardMaxValueDB + Constant.EMPTY);
            standardMin.setText(standardMinValueDB + Constant.EMPTY);
        }
    }

    class SetStandard extends AsyncTask {
        private int max;
        private int min;

        public SetStandard(int min, int max) {
            this.min = min;
            this.max = max;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            // Tạo danh sách tham số gửi đến máy chủ
            List<NameValuePair> args = new ArrayList<NameValuePair>();
            args.add(new BasicNameValuePair("user_id", InforStaticClass.getUserId()));
            args.add(new BasicNameValuePair("standard_pressure_max", max + Constant.EMPTY));
            args.add(new BasicNameValuePair("standard_pressure_min", min + Constant.EMPTY));
            MyService sh = new MyService();
            // Lấy đối tượng JSON
            String json = sh.callService(Constant.URL_UPDATE_STANDARD_PRESSURE, MyService.POST, args);
            if (json != null) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    int success = jsonObject.getInt(Constant.JSON_SUCCESS);
                    if (success == 1) {
                        message.setText(Constant.MESSAGE_UPDATE_SUCCESS);
                    } else {
                        message.setText(Constant.MESSAGE_UPDATE_FAIL);
                    }
                } catch (JSONException e) {
                    Log.d(Constant.ERROR_TAG, e.toString());
                    message.setText(Constant.MESSAGE_UPDATE_FAIL);
                }
            } else {
                message.setText(Constant.MESSAGE_UPDATE_FAIL);
            }
            return null;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SettingStandardPressureActivity.this);
            pDialog.setMessage(Constant.UPDATING);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (pDialog.isShowing())
                pDialog.dismiss();
            GetStandardPressure getStandardPressure = new GetStandardPressure(InforStaticClass.getUserId());
            getStandardPressure.execute();
        }
    }
}