package com.hust.bloddpressure.controllers;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hust.bloddpressure.R;
import com.hust.bloddpressure.model.MyService;
import com.hust.bloddpressure.model.entities.InforStaticClass;
import com.hust.bloddpressure.model.entities.Room;
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
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment# newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    private int statusLogin;
    EditText editTextUserName, editTextPassword;
    Button btnLogin;
    ImageButton btnSwitchRegister;
    TextView message;
    ProgressDialog pDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Find view by id
        findViewByIdForView(view);
        // do action when click button login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get value from form
                String userName = editTextUserName.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String error = Common.validateLogin(userName, password);
                if (error.isEmpty()) {
                    Login login = new Login(userName, password);
                    login.execute();
                } else {
                    message.setText(error);
                }
            }
        });
        //Xử lý sự kiện cho button switchregiters
        btnSwitchRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSwitchRegisterView();
            }
        });

    }

    /**
     * Back ground to login
     */
    class Login extends AsyncTask {
        String username;
        String password;

        public Login(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            // Create param list to send to server
            List<NameValuePair> args = new ArrayList<NameValuePair>();
            args.add(new BasicNameValuePair(Constant.USERNAME, username));
            args.add(new BasicNameValuePair(Constant.PASSWORD, password));
            MyService sh = new MyService();
            // Get JSON object
            String json = sh.callService(Constant.URL_LOGIN, MyService.POST, args);
            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray jsonArrayRoom = jsonObj.getJSONArray(Constant.OBJECT_USER);
                        for (int i = 0; i < jsonArrayRoom.length(); i++) {
                            JSONObject obj = (JSONObject) jsonArrayRoom.get(i);
                            int rule = obj.getInt(Constant.RULE);
                            String fullName = obj.getString(Constant.FULL_NAME);
                            String userId = obj.getString(Constant.USER_ID);
                            InforStaticClass.setUserId(userId);
                            InforStaticClass.setRule(rule);
                            InforStaticClass.setFullName(fullName);
                            statusLogin = obj.getInt(Constant.JSON_SUCCESS);
                        }
                    }
                } catch (JSONException e) {
                    Log.d(Constant.ERROR_TAG, e.toString());
                }
            }
            return null;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage(Constant.MSG_LOGIN);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (statusLogin == 1) {
                message.setText(Constant.EMPTY);
                Intent intent = new Intent(getActivity(), MenuManagerActivity.class);
                startActivity(intent);
            } else {
                message.setText(Constant.MESSAGE_LOGIN_FAILED);
            }
        }
    }

    private void findViewByIdForView(View view) {
        message = view.findViewById(R.id.message);
        editTextUserName = view.findViewById(R.id.et_username);
        editTextPassword = view.findViewById(R.id.et_password);
        btnLogin = view.findViewById(R.id.btn_login);
        btnSwitchRegister = view.findViewById(R.id.btn_do_register);
    }

    private void doSwitchRegisterView() {
        // chuyển sang view đăng ký
        RegisterFragment registerFragment = new RegisterFragment();
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contentFrame, registerFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}