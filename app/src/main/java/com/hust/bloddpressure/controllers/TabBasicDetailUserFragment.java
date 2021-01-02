package com.hust.bloddpressure.controllers;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
 * A simple {@link Fragment} subclass.
 * Use the {@link TabBasicDetailUserFragment# newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabBasicDetailUserFragment extends Fragment {
    TextView textViewFullName, textViewUserId, textViewAge, textViewUserName, textViewTel, textViewDisease, textViewRoom, textViewPressureMax, textViewPressureMin;
    ArrayList<UserInfor> listUserInfor;
    private int rule;
    ProgressDialog pDialog;
    TextView txtMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab_basic_detail_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViewById(view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String userId;
            userId = bundle.getString(Constant.USER_ID);
            listUserInfor = new ArrayList<>();
            GetDetailtUser getListUser = new GetDetailtUser(userId);
            getListUser.execute();
        } else {
            setMessageNoData();
        }
    }

    private void findViewById(View view) {
        // Get view layout by id
        txtMessage = getActivity().findViewById(R.id.message);
        textViewUserId = view.findViewById(R.id.user_id);
        textViewFullName = view.findViewById(R.id.full_name);
        textViewAge = view.findViewById(R.id.age);
        textViewUserName = view.findViewById(R.id.username);
        textViewTel = view.findViewById(R.id.tel);
        textViewDisease = view.findViewById(R.id.disease);
        textViewRoom = view.findViewById(R.id.room);
        textViewPressureMax = view.findViewById(R.id.max_pressure);
        textViewPressureMin = view.findViewById(R.id.min_pressure);
    }

    class GetDetailtUser extends AsyncTask {
        private String userId;

        public GetDetailtUser(String userId) {
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
                            UserInfor userInfor = new UserInfor(userId, roomId, age, rule, pressureMin, pressureMax, fullName, tel, room, diseaseName, username);
                            listUserInfor.add(userInfor);
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
            pDialog = new ProgressDialog(getActivity());
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

        @SuppressLint("SetTextI18n")
        private void getData() {
            if (listUserInfor.size() != 0) {
                txtMessage.setText(R.string.basic_detail);
                UserInfor userInfor = listUserInfor.get(0);
                // Set value for text view display
                textViewUserId.setText(userId);
                textViewFullName.setText(userInfor.getFullName());
                textViewAge.setText(userInfor.getAge() + "");
                textViewUserName.setText(userInfor.getUsername());
                textViewTel.setText(userInfor.getTel() + "");
                textViewDisease.setText(userInfor.getDiseaseName());
                textViewRoom.setText(userInfor.getRoom());
                textViewPressureMax.setText(userInfor.getPressureMax() + " mmHg");
                textViewPressureMin.setText(userInfor.getPressureMin() + " mmHg");
            } else {
                setMessageNoData();
            }
        }
    }

    private void setMessageNoData() {
        txtMessage.setTextColor(ContextCompat.getColor(getContext(), R.color.no_data_color));
        txtMessage.setText(R.string.no_data);
        getActivity().findViewById(R.id.content_detail).setVisibility(View.INVISIBLE);
    }
}