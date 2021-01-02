package com.hust.bloddpressure.controllers;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hust.bloddpressure.R;
import com.hust.bloddpressure.model.MyService;
import com.hust.bloddpressure.model.entities.News;
import com.hust.bloddpressure.model.entities.Room;
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
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment# newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {
    EditText textUserId, textFullName, textUsername, textTel, textAge, textDisease, textPass;
    TextView selectRoomId, message;
    AppCompatSpinner spinnerRoom;
    Button btnRegister;
    ImageButton switchToLogin;
    SpinnerRoomAdapter spinnerRoomAdapter;
    List<Room> listRoom;
    ProgressDialog pDialog;
    private int success;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Find view by id
        findViewByIdForView(view);
        // Set data  dropdown for and set text when item selected
        setDataForDropdown();
        // Set listen event for button
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserInfor userInfor;
                userInfor = getUserInformationForm();
                String errorMessage = Common.validateUser(userInfor, Constant.MODE_ADD);
                if (!Common.checkEmpty(errorMessage)) {
                    message.setText(errorMessage);
                } else {
                    // THUC HIEN KET NOI DB
                    // THUC HIEN INSERT USER MOI VAO DB TAI DAY
                    AddUser addUser = new AddUser(userInfor);
                    addUser.execute();
                }
            }
        });
        switchToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSwitchLoginView();
            }
        });
    }

    private void findViewByIdForView(View view) {
        message = view.findViewById(R.id.message);
        textUserId = view.findViewById(R.id.user_id);
        btnRegister = view.findViewById(R.id.btn_register);
        switchToLogin = view.findViewById(R.id.btn_login);
        textUsername = view.findViewById(R.id.username);
        textPass = view.findViewById(R.id.password);
        textFullName = view.findViewById(R.id.full_name);
        textAge = view.findViewById(R.id.age);
        textDisease = view.findViewById(R.id.disease_name);
        textTel = view.findViewById(R.id.tel);
        selectRoomId = view.findViewById(R.id.room_id_add);
        spinnerRoom = view.findViewById(R.id.spinner_room);
    }

    private void doSwitchLoginView() {
        LoginFragment loginFragment = new LoginFragment();
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contentFrame, loginFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void setDataForDropdown() {
        listRoom = new ArrayList<>();
        final Room room = new Room();
        room.setRoomId(-1);
        room.setRoomName(Constant.CHOOSE_ROOM);
        listRoom.add(room);
        // Do this in back ground
        GetListRoom getListRoom = new GetListRoom();
        getListRoom.execute();
    }

    private UserInfor getUserInformationForm() {
        UserInfor userInfor = new UserInfor();
        String userId = textUserId.getText().toString().trim();
        String fullName = textFullName.getText().toString().trim();
        String username = textUsername.getText().toString().trim();
        String password = textPass.getText().toString().trim();
        int age = Common.convertToInt(textAge.getText().toString(), 0);
        String tel = textTel.getText().toString().trim().trim().trim();
        String disease = textDisease.getText().toString().trim();
        int roomId = Common.convertToInt(selectRoomId.getHint().toString().trim(), 0);
        String room = selectRoomId.getText().toString().trim();
        // Set value for user
        userInfor.setUserId(userId);
        userInfor.setFullName(fullName);
        userInfor.setUsername(username);
        userInfor.setPassword(password);
        userInfor.setAge(age);
        userInfor.setDiseaseName(disease);
        userInfor.setTel(tel);
        userInfor.setRoomId(roomId);
        userInfor.setRoom(room);
        return userInfor;
    }

    class AddUser extends AsyncTask {
        UserInfor userInfor;

        public AddUser(UserInfor userInfor) {
            this.userInfor = userInfor;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            // Tạo danh sách tham số gửi đến máy chủ
            List<NameValuePair> args = new ArrayList<NameValuePair>();
            args.add(new BasicNameValuePair("user_id", userInfor.getUserId()));
            args.add(new BasicNameValuePair("full_name", userInfor.getFullName()));
            args.add(new BasicNameValuePair("user_name", userInfor.getUsername()));
            args.add(new BasicNameValuePair("password", userInfor.getPassword()));
            args.add(new BasicNameValuePair("age", userInfor.getAge() + Constant.EMPTY));
            args.add(new BasicNameValuePair("disease_name", userInfor.getDiseaseName()));
            args.add(new BasicNameValuePair("tel", userInfor.getTel()));
            args.add(new BasicNameValuePair("room_id", userInfor.getRoomId() + Constant.EMPTY));
            args.add(new BasicNameValuePair("salt", Common.createSalt()));
            args.add(new BasicNameValuePair("action", Constant.ACTION_ADD));
            MyService sh = new MyService();
            // Lấy đối tượng JSON
            String json = sh.callService(Constant.URL_REGISTER_USER, MyService.POST, args);
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
            pDialog = new ProgressDialog(getActivity());
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
                message.setText(Constant.MESSAGE_ADD_SUCCESS);
            } else if (success == 2) {
                message.setText(Constant.MESSAGE_EXIST_USERNAME);
            } else {
                message.setText(Constant.MESSAGE_ADD_FAIL);
            }
        }
    }

    /**
     * Background Get List Room
     */
    class GetListRoom extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            MyService jsonParser = new MyService();
            String json = jsonParser.callService(Constant.URL_LIST_ROOM, MyService.GET);
            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray jsonArrayRoom = jsonObj.getJSONArray(Constant.OBJECT_JSON_LIST_ROOM);
                        for (int i = 0; i < jsonArrayRoom.length(); i++) {
                            JSONObject obj = (JSONObject) jsonArrayRoom.get(i);
                            Room room = new Room(obj.getInt(Constant.ROOM_ID), obj.getString(Constant.ROOM_NAME));
                            listRoom.add(room);
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

        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            getData();
        }

        private void getData() {
            if (listRoom.size() == 1) {
                listRoom.get(0).setRoomName(Constant.MESAGE_NO_DATA);
                spinnerRoom.setEnabled(false);
            }
            spinnerRoomAdapter = new SpinnerRoomAdapter(listRoom);
            spinnerRoom.setAdapter(spinnerRoomAdapter);
            spinnerRoom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    // Get room selected
                    Room roomItem = (Room) spinnerRoomAdapter.getItem(i);
                    int roomId = roomItem.getRoomId();
                    String roomName = roomItem.getRoomName();
                    // Get id room and check
                    if (roomId > 0) {
                        selectRoomId.setHint(roomId + "");
                        selectRoomId.setText(roomName);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    selectRoomId.setText("");
                }
            });
        }
    }

}