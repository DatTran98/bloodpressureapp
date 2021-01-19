package com.hust.bloddpressure.controllers;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.hust.bloddpressure.R;
import com.hust.bloddpressure.model.MyService;
import com.hust.bloddpressure.model.entities.UserInfor;
import com.hust.bloddpressure.util.Constant;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Class main activity
 */
public class ExportActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ImageButton btnExport;
    private RadioGroup radioOptUser, radioOptPressure;
    private int optionUser = 0;
    private int optionPressure = 0;
    private ProgressDialog pDialog;
    private ArrayList<UserInfor> listUserInfo;
    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        // init navigation
        new NavigationSetting(ExportActivity.this);
        // Set action for action bar
        drawerLayout = findViewById(R.id.drawable);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.export);
        btnExport = findViewById(R.id.btn_export);
        radioOptUser = findViewById(R.id.radio_user);
        radioOptPressure = findViewById(R.id.radio_pressure);
        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedUser = radioOptUser.getCheckedRadioButtonId();
                int selectedPressure = radioOptPressure.getCheckedRadioButtonId();
                switch (selectedUser) {
                    case R.id.radio_50:
                        optionUser = 50;
                        break;
                    case R.id.radio_10:
                        optionUser = 10;
                        break;
                    default:
                        break;
                }
                switch (selectedPressure) {
                    case R.id.radio_a_10:
                        optionPressure = 10;
                        break;
                    case R.id.radio_a_7:
                        optionPressure = 7;
                        break;
                    case R.id.radio_a_3:
                        optionPressure = 3;
                        break;
                    default:
                        break;
                }
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
    class GetDetailUser extends AsyncTask {
        private String userId;

        public GetDetailUser(String userId) {
            this.userId = userId;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            MyService jsonParser = new MyService();
            List<NameValuePair> args = new ArrayList<NameValuePair>();
            args.add(new BasicNameValuePair(Constant.USER_ID, userId));
            String json = jsonParser.callService(Constant.URL_LIST_USER, MyService.GET, args);
            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray jsonArrayUser = jsonObj.getJSONArray(Constant.OBJECT_JSON_LIST_USER);
                        JSONArray pressure = jsonObj.getJSONArray(Constant.OBJECT_JSON_PRESSURE);
                        for (int i = 0; i < jsonArrayUser.length(); i++) {
                            JSONObject obj = (JSONObject) jsonArrayUser.get(i);
                            int pressureMax = Constant.INT_VALUE_DEFAULT;
                            int pressureMin = Constant.INT_VALUE_DEFAULT;
                            int heartBeat = Constant.INT_VALUE_DEFAULT;
                            if (pressure.length() != 0) {
                                JSONObject obj1 = (JSONObject) pressure.get(i);
                                pressureMax = obj1.getInt(Constant.PRESSURE_MAX);
                                pressureMin = obj1.getInt(Constant.PRESSURE_MIN);
                                heartBeat = obj1.getInt(Constant.HEART_BEAT);
                            }
                            int roomId = obj.getInt(Constant.ROOM_ID);
                            int age = obj.getInt(Constant.AGE);
                            int rule = obj.getInt(Constant.RULE);
                            String fullName = obj.getString(Constant.FULL_NAME);
                            String tel = obj.getString(Constant.TEL);
                            String room = obj.getString(Constant.ROOM_NAME);
                            String diseaseName = obj.getString(Constant.DISEASE_NAME);
                            String username = obj.getString(Constant.USERNAME);
                            int predictType = obj.getInt(Constant.PREDICT_TYPE);

                            UserInfor userInfor = new UserInfor(userId, roomId, age, rule, pressureMin, pressureMax, predictType, heartBeat, fullName, tel, room, diseaseName, username);
                            listUserInfo.add(userInfor);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e(Constant.LOG_JSON, Constant.MSG_JSON);
            }
            return null;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ExportActivity.this);
            pDialog.setMessage(Constant.MESSAGE_LOADING);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (pDialog.isShowing())
                pDialog.dismiss();
            sendMessage();
        }
    }

    /**
     * Send message to the screen
     */
    private void sendMessage() {
    }
}