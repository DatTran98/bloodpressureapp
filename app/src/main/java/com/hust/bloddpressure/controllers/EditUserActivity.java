package com.hust.bloddpressure.controllers;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hust.bloddpressure.R;
import com.hust.bloddpressure.model.entities.UserInfor;
import com.hust.bloddpressure.util.Common;
import com.hust.bloddpressure.util.Constant;

/**
 * Class thực hiện chức năng edit thông tin user
 */
public class EditUserActivity extends AppCompatActivity {
    Button btnSave;
    EditText textFullName, textUsername, textTel, textAge, textDisease;
    TextView textRoom, textUserId, textViewMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        // Lấy data và set cho view
        getAndSetDataToView();
        // Tạo sự kiện click cho button save
        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserInfor userInfor;
                // lấy data người dùng nhập vào trên form
                userInfor = getDataFormAndValidate(view);
                String error = Common.validateUser(userInfor, Constant.MODE_EDIT);
                //Kiểm tra validate dữ liệu tại đây
                if (!Common.checkEmpty(error)) {
                    Common.showToast(EditUserActivity.this, error);
                } else {
                    // THỰC HIỆN TRUY VẤN DB TẠI ĐÂY
                    // KIỂM TRA USER TỒN TẠI
                    // TRUY VẤN UPDATE BẢNG
                    Toast.makeText(EditUserActivity.this, "Đang edit db", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Lấy data từ activity khác truyền vào dể get data cần thiết
     */
    private void getAndSetDataToView() {
        // Lấy dữ liệu được gửi từ detail
        Bundle bundle = getIntent().getExtras();
        // Kiểm tra xem có dữ liệu gửi tới không
        if (bundle == null) {
            textViewMessage = findViewById(R.id.message);
            textViewMessage.setText("Không có dữ liệu!");
            btnSave = findViewById(R.id.btn_save);
            btnSave.setEnabled(false);
        } else {
            String userId = (String) bundle.get("userId");
            String username = (String) bundle.get("username");
            String fullName = (String) bundle.get("fullName");
            String age = (String) bundle.get("age");
            String tel = (String) bundle.get("tel");
            String disease = (String) bundle.get("disease");
            String room = (String) bundle.get("room");

            // Tìm view trên màn hình edit
            textUserId = findViewById(R.id.user_id_main);
            textUsername = findViewById(R.id.username);
            textFullName = findViewById(R.id.full_name);
            textAge = findViewById(R.id.age);
            textDisease = findViewById(R.id.disease_name);
            textTel = findViewById(R.id.tel);
            textRoom = findViewById(R.id.room);
            // Set gia trị lên view
            textUserId.setText(userId);
            textUsername.setText(username);
            textAge.setText(age);
            textFullName.setText(fullName);
            textRoom.setText(room);
            textDisease.setText(disease);
            textTel.setText(tel);
        }
    }

    /**
     * Get các thông tin đã điền trên form
     *
     * @param view chứa thông tin
     * @return UserInfor đối tượng chứa thông tin
     */
    private UserInfor getDataFormAndValidate(View view) {
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
        // Set gia trị cho user
        userInfor.setFullName(fullName);
        userInfor.setAge(age);
        userInfor.setDiseaseName(disease);
        userInfor.setTel(tel);

        return userInfor;
    }
}