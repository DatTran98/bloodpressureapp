package com.hust.bloddpressure.controllers;

import android.content.Context;
import android.os.Bundle;
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
import com.hust.bloddpressure.model.entities.Room;
import com.hust.bloddpressure.model.entities.UserInfor;
import com.hust.bloddpressure.util.Common;
import com.hust.bloddpressure.util.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment# newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {
    EditText textFullName, textUsername, textTel, textAge, textDisease, textPass;
    TextView selectRoomId;
    AppCompatSpinner spinnerRoom;
    Button btnRegister;
    ImageButton switchToLogin;
    SpinnerRoomAdapter spinnerRoomAdapter;

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
        // Tìm id cho các view
        findViewByIdForView(view);
        // Set data cho dropdown và set text khi chọn item
        setDataForDropdown();
        // Set các sự kiện lắng nghe cho các button
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserInfor userInfor;
                userInfor = getUserInformationForm();
                String errorMessage = Common.validateUser(userInfor, Constant.MODE_ADD);
                if (!Common.checkEmpty(errorMessage)) {
                    Common.showToast(getActivity(), errorMessage);
                } else {
                    // THUC HIEN KET NOI DB
                    // THUC HIEN INSERT USER MOI VAO DB TAI DAY
                    Toast.makeText(getActivity(), "Do login", Toast.LENGTH_SHORT).show();
                    // XONG THI SWITCH SANG MAN HINH LOGIN
                    doSwitchLoginView();
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
        final List<Room> listRoom = new ArrayList<>();
        final Room room = new Room();
        room.setRoomId(-1);
        room.setRoomName("Chọn phòng");
        listRoom.add(room);
        // THỰC HIỆN TRUY VẤN DB ĐỂ LẤY RA PHÒNG
        for (int i = 1; i < 5; i++) {
            Room room1 = new Room();
            room1.setRoomId(i);
            room1.setRoomName("Phong " + i);
            listRoom.add(room1);
        }
        // Tạo mơi adapter và set giá trị cho spiner
        spinnerRoomAdapter = new SpinnerRoomAdapter(listRoom);
        spinnerRoom.setAdapter(spinnerRoomAdapter);
        spinnerRoom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Lấy ra phòng đã chọn
                Room roomItem = (Room) spinnerRoomAdapter.getItem(i);
                int roomId = roomItem.getRoomId();
                String roomName = roomItem.getRoomName();
                // Lấy id phòng và kiểm tra hợp lệ
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

    private UserInfor getUserInformationForm() {
        UserInfor userInfor = new UserInfor();
        String fullName = textFullName.getText().toString().trim();
        String username = textUsername.getText().toString().trim();
        String password = textPass.getText().toString().trim();
        int age = Common.convertToInt(textAge.getText().toString(), 0);
        String tel = textTel.getText().toString().trim().trim().trim();
        String disease = textDisease.getText().toString().trim();
        int roomId = Common.convertToInt(selectRoomId.getHint().toString().trim(), 0);
        String room = selectRoomId.getText().toString().trim();
        // Set gia trị cho user
        userInfor.setFullname(fullName);
        userInfor.setUsername(username);
        userInfor.setPassword(password);
        userInfor.setAge(age);
        userInfor.setDiseaseName(disease);
        userInfor.setTel(tel);
        userInfor.setRoomId(roomId);
        userInfor.setRoom(room);
        return userInfor;
    }

}