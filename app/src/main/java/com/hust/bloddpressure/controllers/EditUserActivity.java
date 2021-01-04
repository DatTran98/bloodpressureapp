package com.hust.bloddpressure.controllers;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hust.bloddpressure.R;
import com.hust.bloddpressure.model.MyService;
import com.hust.bloddpressure.model.entities.UserInfor;
import com.hust.bloddpressure.util.Common;
import com.hust.bloddpressure.util.Constant;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Class thực hiện chức năng edit thông tin user
 */
public class EditUserActivity extends AppCompatActivity {
    Button btnSave;
    EditText textFullName, textUsername, textTel, textAge, textDisease;
    TextView textRoom, textUserId, textViewMessage;
    ArrayList<UserInfor> listUserInfor;
    ProgressDialog pDialog;
    private int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        findViewById();
        // Get data  set for view
        getAndSetDataToView();
        // when click button save
        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserInfor userInfor;
                // get data from form form
                userInfor = getDataFormAndValidate();
                String error = Common.validateUser(userInfor, Constant.MODE_EDIT);
                // Check exist error
                if (!Common.checkEmpty(error)) {
                    textViewMessage.setText(error);
                } else {
                    EditUser editUser = new EditUser(userInfor);
                    editUser.execute();
                    // THỰC HIỆN TRUY VẤN DB TẠI ĐÂY
                    // KIỂM TRA USER TỒN TẠI
                    // TRUY VẤN UPDATE BẢNG
                }
            }
        });
    }

    /**
     * Find view by id
     */
    private void findViewById() {
        textViewMessage= findViewById(R.id.message);
        textUserId = findViewById(R.id.user_id);
        textUsername = findViewById(R.id.username);
        textFullName = findViewById(R.id.full_name);
        textAge = findViewById(R.id.age);
        textDisease = findViewById(R.id.disease_name);
        textTel = findViewById(R.id.tel);
        textRoom = findViewById(R.id.room);
    }

    /**
     * get data from other activity sent
     */
    private void getAndSetDataToView() {
        // get data from detail activity
        Bundle bundle = getIntent().getExtras();
        // Check data sent have or not
        if (bundle == null) {
            textViewMessage.setText(Constant.MESAGE_NO_DATA);
            btnSave = findViewById(R.id.btn_save);
            btnSave.setEnabled(false);
        } else {
            String userId = bundle.getString(Constant.USER_ID);
            listUserInfor = new ArrayList<>();
            GetDetailUser getDetailUser = new GetDetailUser(userId);
            getDetailUser.execute();

        }
    }
    /**
     * Get data from form
     *
     * @return UserInfor object entities
     */
    private UserInfor getDataFormAndValidate() {
        UserInfor userInfor = new UserInfor();
        textFullName = findViewById(R.id.full_name);
        textAge = findViewById(R.id.age);
        textTel = findViewById(R.id.tel);
        textDisease = findViewById(R.id.disease_name);
        textAge = findViewById(R.id.age);
        String fullName = textFullName.getText().toString().trim();
        int age = Common.convertToInt(textAge.getText().toString().trim(), 0);
        String tel = textTel.getText().toString().trim().trim().trim();
        String disease = textDisease.getText().toString().trim();
        // Set value for user
        userInfor.setFullName(fullName);
        userInfor.setAge(age);
        userInfor.setDiseaseName(disease);
        userInfor.setTel(tel);
        return userInfor;
    }

    @SuppressLint("StaticFieldLeak")
    class GetDetailUser extends AsyncTask {
        private String userId;

        public GetDetailUser(String userId) {
            this.userId = userId;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            MyService jsonParser = new MyService();
            List<NameValuePair> args = new ArrayList<>();
            args.add(new BasicNameValuePair(Constant.USER_ID, userId));
            String json = jsonParser.callService(Constant.URL_LIST_USER, MyService.GET, args);
            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    JSONArray jsonArrayUser = jsonObj.getJSONArray(Constant.OBJECT_JSON_LIST_USER);
                    JSONArray pressure = jsonObj.getJSONArray(Constant.OBJECT_JSON_PRESSURE);
                    for (int i = 0; i < jsonArrayUser.length(); i++) {
                        JSONObject obj = (JSONObject) jsonArrayUser.get(i);
                        JSONObject obj1 = (JSONObject) pressure.get(i);
                        int roomId = obj.getInt(Constant.ROOM_ID);
                        int age = obj.getInt(Constant.AGE);
                        int rule = obj.getInt(Constant.RULE);
                        String fullName = obj.getString(Constant.FULL_NAME);
                        String tel = obj.getString(Constant.TEL);
                        String room = obj.getString(Constant.ROOM_NAME);
                        String diseaseName = obj.getString(Constant.DISEASE_NAME);
                        String username = obj.getString(Constant.USERNAME);
                        int pressureMax = obj1.getInt(Constant.PRESSURE_MAX);
                        int pressureMin = obj1.getInt(Constant.PRESSURE_MIN);
                        int predictType = obj.getInt(Constant.PREDICT_TYPE);
                        UserInfor userInfor = new UserInfor(userId, roomId, age, rule, pressureMin, pressureMax,predictType, fullName, tel, room, diseaseName, username);
                        listUserInfor.add(userInfor);
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
            pDialog = new ProgressDialog(EditUserActivity.this);
            pDialog.setMessage(Constant.MESSAGE_LOADING);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (pDialog.isShowing())
                pDialog.dismiss();
            getData();
        }

        private void getData() {
            UserInfor userInfor = listUserInfor.get(0);
            String userId = userInfor.getUserId();
            String username = userInfor.getUsername();
            String fullName = userInfor.getFullName();
            int age = userInfor.getAge();
            String tel = userInfor.getTel();
            String disease = userInfor.getDiseaseName();
            String roomName = userInfor.getRoom();
            // set value for view
            textUserId.setText(userId);
            textUsername.setText(username);
            textAge.setText(age + Constant.EMPTY);
            textFullName.setText(fullName);
            textRoom.setText(roomName);
            textDisease.setText(disease);
            textTel.setText(tel);
        }
    }
    @SuppressLint("StaticFieldLeak")
    class EditUser extends AsyncTask {
        UserInfor userInfor;

        public EditUser(UserInfor userInfor) {
            this.userInfor = userInfor;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            // Create list param to sent to sever
            List<NameValuePair> args = new ArrayList<>();
            args.add(new BasicNameValuePair("user_id", userInfor.getUserId()));
            args.add(new BasicNameValuePair("full_name", userInfor.getFullName()));
            args.add(new BasicNameValuePair("age", userInfor.getAge() + Constant.EMPTY));
            args.add(new BasicNameValuePair("disease_name", userInfor.getDiseaseName()));
            args.add(new BasicNameValuePair("tel", userInfor.getTel()));
            args.add(new BasicNameValuePair("action", Constant.ACTION_EDIT));
            MyService sh = new MyService();
            // Get JSON object
            String json = sh.callService(Constant.URL_EDIT_USER, MyService.POST, args);
            if (json != null) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    success = jsonObject.getInt(Constant.JSON_SUCCESS);
                } catch (JSONException e) {
                    Log.d(Constant.ERROR_TAG, e.toString());
                }
            }
            return null;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditUserActivity.this);
            pDialog.setMessage(Constant.CREATING);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (success == 1) {
                textViewMessage.setText(Constant.MESSAGE_EDIT_SUCCESS);
            } else {
                textViewMessage.setText(Constant.MESSAGE_EDIT_FAIL);
            }
        }
    }
}