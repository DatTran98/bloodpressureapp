package com.hust.bloddpressure.controllers;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.hust.bloddpressure.R;
import com.hust.bloddpressure.model.MyService;
import com.hust.bloddpressure.model.entities.InforStaticClass;
import com.hust.bloddpressure.model.entities.UserInfor;
import com.hust.bloddpressure.util.Common;
import com.hust.bloddpressure.util.Constant;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class main activity
 */
public class ExportActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private ImageButton btnExport;
    private RadioGroup radioOptUser, radioOptPressure;
    private int optionUser = 0;
    private int optionPressure = 0;
    private ProgressDialog pDialog;
    private ArrayList<UserInfor> listUserInfoExport;
    private String simpleFileName = Constant.EMPTY;
    // Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final String FILE_NAME_EXTENSION = ".csv";

    // CSV file header
    private static final String FILE_HEADER = "STT,Mã bệnh nhân,Tên bệnh nhân,Tuổi,Bệnh,Số điện thoại,Phòng," +
            "Chuẩn đoán,Chuẩn tâm trương,Chuẩn tâm thu,Lần đo,Tâm trương,Tâm thu,Nhịp tim,Ngày kiểm tra";

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
//        toolbar = findViewById(R.id.tool_bar5);
//        toolbar.setTitle(Constant.EMPTY);
//        setSupportActionBar(toolbar);
        // init navigation
        new NavigationSetting(ExportActivity.this);
        // Set action for action bar
        drawerLayout = findViewById(R.id.drawable);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setTitle(R.string.export);
        getSupportActionBar().setTitle(Constant.EMPTY);
        btnExport = findViewById(R.id.btn_export);
        radioOptUser = findViewById(R.id.radio_user);
//        radioOptPressure = findViewById(R.id.radio_pressure);
        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedUser = radioOptUser.getCheckedRadioButtonId();
//                int selectedPressure = radioOptPressure.getCheckedRadioButtonId();
                String optUser = Constant.EMPTY;
//                String optPressure = Constant.EMPTY;
                Date date = new Date();
                String optTime = new SimpleDateFormat("yyyy-MM-dd").format(date);
                switch (selectedUser) {
                    case R.id.radio_50:
                        optionUser = 50;
                        optUser += 50;
                        break;
                    case R.id.radio_10:
                        optionUser = 10;
                        optUser += 10;
                        break;
                    default:
                        optUser += "all";
                        break;
                }
//                switch (selectedPressure) {
//                    case R.id.radio_a_10:
//                        optionPressure = 10;
//                        optPressure += 10;
//                        break;
//                    case R.id.radio_a_7:
//                        optionPressure = 7;
//                        optPressure += 7;
//                        break;
//                    case R.id.radio_a_3:
//                        optionPressure = 3;
//                        optPressure += 3;
//                        break;
//                    default:
//                        optPressure += "all";
//                        break;
//                }
                String fileName = optUser + "_" + "user_" + optTime + FILE_NAME_EXTENSION;
                simpleFileName = fileName;
                listUserInfoExport = new ArrayList<>();
                GetAllUserExport getAllUserExport = new GetAllUserExport(optionUser);
                getAllUserExport.execute();
//                exportCSV();
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
            case R.id.reset:
                Common.showToast(ExportActivity.this, Constant.NOT_THING_TO_LOAD);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    class GetAllUserExport extends AsyncTask {
        private int optionUser;

        public GetAllUserExport(int optionUser) {
            this.optionUser = optionUser;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            MyService jsonParser = new MyService();
            List<NameValuePair> args = new ArrayList<NameValuePair>();
            args.add(new BasicNameValuePair(Constant.OPT_USER, optionUser + Constant.EMPTY));
            String json = jsonParser.callService(Constant.URL_LIST_USER_EXPORT, MyService.GET, args);
            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray jsonArrayUser = jsonObj.getJSONArray(Constant.OBJECT_JSON_LIST_USER_EXPORT);
                        for (int i = 0; i < jsonArrayUser.length(); i++) {
                            UserInfor userInfor;
                            JSONObject obj = (JSONObject) jsonArrayUser.get(i);
                            String userId = obj.getString(Constant.USER_ID);
                            int roomId = obj.getInt(Constant.ROOM_ID);
                            int age = obj.getInt(Constant.AGE);

                            int predictType = obj.getInt(Constant.PREDICT_TYPE);
                            int systolicMax = obj.getInt(Constant.STANDARD_MAX);
//                            int systolicMin = obj.getInt(Constant.SYSTOLIC_MIN);
//                            int diastolicMax = obj.getInt(Constant.DIASTOLIC_MAX);
                            int diastolicMin = obj.getInt(Constant.STANDARD_MIN);
                            int pressureId = obj.getInt(Constant.PRESSURE_ID);
                            int pressureMax = obj.getInt(Constant.PRESSURE_MAX);
                            int pressureMin = obj.getInt(Constant.PRESSURE_MIN);
                            int heartBeat = obj.getInt(Constant.HEART_BEAT);

                            String fullName = obj.getString(Constant.FULL_NAME);
                            String tel = obj.getString(Constant.TEL);
                            String room = obj.getString(Constant.ROOM_NAME);
                            String diseaseName = obj.getString(Constant.DISEASE_NAME);
                            Date date = new SimpleDateFormat(Constant.DATE_FORMAT).parse(obj.getString(Constant.TIME));

                            userInfor = new UserInfor(userId, roomId, age, predictType, systolicMax,
                                    diastolicMin, pressureId, pressureMax, pressureMin, heartBeat, fullName,
                                    tel, room, diseaseName, date);
                            listUserInfoExport.add(userInfor);
                        }
                    }
                } catch (JSONException | ParseException e) {
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

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (pDialog.isShowing())
                pDialog.dismiss();
            exportCSV();
        }
    }

    /**
     * Do export CSV file
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void exportCSV() {
        StringBuilder data = new StringBuilder();
        try {

//            dumyData();
            data.append(FILE_HEADER);
            data.append(NEW_LINE_SEPARATOR);
            int index = 0;
            for (UserInfor userInfor : listUserInfoExport) {
                data.append(index);
                data.append(COMMA_DELIMITER);
                data.append(userInfor.getUserId());
                data.append(COMMA_DELIMITER);
                data.append(userInfor.getFullName());
                data.append(COMMA_DELIMITER);
                data.append(userInfor.getAge());
                data.append(COMMA_DELIMITER);
                data.append(userInfor.getDiseaseName());
                data.append(COMMA_DELIMITER);
                data.append(userInfor.getTel());
                data.append(COMMA_DELIMITER);
                data.append(userInfor.getRoom());
                data.append(COMMA_DELIMITER);
                int predict = userInfor.getPredictType();
                if (Constant.VALUE_NORMAL_PREDICT == predict) {
                    data.append(Constant.PREDICT_NORMAL_NAME);
                } else if (Constant.VALUE_MAX_PREDICT == predict) {
                    data.append(Constant.PREDICT_MAX_NAME);
                } else {
                    data.append(Constant.PREDICT_MIN_NAME);
                }
                data.append(COMMA_DELIMITER);
                data.append(userInfor.getDiastolicMin());
                data.append(COMMA_DELIMITER);
                data.append(userInfor.getSystolicMin());
                data.append(COMMA_DELIMITER);
                data.append(userInfor.getPressureId());
                data.append(COMMA_DELIMITER);
                data.append(userInfor.getPressureMin());
                data.append(COMMA_DELIMITER);
                data.append(userInfor.getPressureMax());
                data.append(COMMA_DELIMITER);
                data.append(userInfor.getHeartBeat());
                data.append(COMMA_DELIMITER);
                data.append(new SimpleDateFormat("yyyy-MM-dd").format(userInfor.getDate()));
                data.append(NEW_LINE_SEPARATOR);
                index++;
            }
            String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
            // Open Stream to write file.
            FileOutputStream out = this.openFileOutput(sdcard+simpleFileName, MODE_PRIVATE);
            // Write.
            out.write(data.toString().getBytes(StandardCharsets.UTF_8));
            out.close();
            Toast.makeText(ExportActivity.this, Constant.SAVED, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(ExportActivity.this, Constant.ERROR_SAVE + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //Don't user this method
    private void dumyData() {
//        for (int i = 0; i < 100; i++) {
//            listUserInfoExport.add(new UserInfor("X12" + i, 0, 23, 1, 75, 56, 139,
//                    89, i + 1, 125, 86, 79, "Trần Bá Đạt",
//                    "09090909", "Không thuộc phòng nào", "Không có bệnh", new Date()));
//        }
    }
}