package com.hust.bloddpressure.controllers;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hust.bloddpressure.R;
import com.hust.bloddpressure.model.entities.InforStaticClass;
import com.hust.bloddpressure.util.Common;
import com.hust.bloddpressure.util.Constant;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment# newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    EditText editTextUserName, editTextPassword;
    Button btnLogin;
    ImageButton btnSwitchRegister;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Tìm các id cho view
        findViewByIdForView(view);
        // Xử lys sự kiện cho button login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy giá trị người dùng nhập vào từ form
                String userName = editTextUserName.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String error = Common.validateLogin(userName, password);
                if (error.isEmpty()) {
                    InforStaticClass.setUserId(10 + "");
                    InforStaticClass.setRule(Constant.ADMIN_RULE);
                    Intent intent = new Intent(getActivity(), MenuManagerActivity.class);
                    startActivity(intent);
//                    try {
//                     Tạo kết nối ở đây và thực hiện check login
//                    ConectSQL cnn = new ConectSQL();
//                    String rull = cnn.execute();
//                    editTextUserName.setText(rull);
//                        int userRule = Common.checkLogin(userName, password);
//                        if (userRule == 0) {
//                            Toast.makeText(getActivity(), "Sai ten dang nhap hoac mat khau", Toast.LENGTH_SHORT).show();
//                        } else if (userRule == 1) {
//                            Toast.makeText(getActivity(), "userrule" + userRule, Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(getActivity(), "userrule" + userRule, Toast.LENGTH_SHORT).show();
//                        }
//                    } catch (Exception e) {
//                        Intent intent = new Intent(getActivity(), MenuManagerActivity.class);
//                        startActivity(intent);
//                    }
////
                } else {
                    Common.showToast(getActivity(), error);
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

    private void findViewByIdForView(View view) {
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