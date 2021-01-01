package com.hust.bloddpressure.controllers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hust.bloddpressure.R;
import com.hust.bloddpressure.model.entities.InforStaticClass;
import com.hust.bloddpressure.util.Common;
import com.hust.bloddpressure.util.Constant;

public class DetailUserActivity extends AppCompatActivity {
    TextView textViewIdUser, textViewUsername, textViewFullName, textViewAge, textViewDisease, textViewTel, textViewRoom, textViewMessage;
    private int tabMode;
    Button btnMoveRoom, btnEdit, btnHis, btnBasic;
    String userId;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);
        getIdElementView();
        // Phải lấy ra id được truyền từ màn hình list từ intent
        Bundle bundle = getIntent().getExtras();
        int rule = InforStaticClass.getRule();
        // Lấy ra giá trị userId được truyền từ list user
        if (Constant.USER_RULE == rule) {
            userId = InforStaticClass.getUserId();
        } else {
            if (bundle == null) {
                // PHẢI HIỆN THỊ ĐÚNG THÔNG BÁO
                textViewMessage.setText(R.string.no_data);
            } else {
                userId = bundle.getString(Constant.USER_ID);
            }
        }
        // Set id lên view chính để 2 tab basic và history sử dụng
        textViewIdUser.setText(userId);
        Bundle bundle1 = getIdUserMain();
        TabBasicDetailUserFragment tabBasicDetailUserFragment = new TabBasicDetailUserFragment();
        // Gửi bundle chứ id này qua fragment( không cần thiết vi đã set view)
        tabBasicDetailUserFragment.setArguments(bundle1);
        // Set fragment cho view, frame cần hiển thị fragment
        setFragmentByManagerFragment(R.id.content_detail, tabBasicDetailUserFragment);
        // Set mode tab
        tabMode = Constant.MODE_BASIC;
        // Set disible button khi là user không có quyên edit
        if (rule == Constant.USER_RULE) {
            btnEdit.setVisibility(View.INVISIBLE);
            btnMoveRoom.setVisibility(View.INVISIBLE);
        }

        btnHis.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // get id user truyền data sang tab history để get thông tin hiển thị
                Bundle bundle = getIdUserMain();
                TabHistoryPressureFragment tabHistoryPressureFragment = new TabHistoryPressureFragment();
                tabHistoryPressureFragment.setArguments(bundle);
                setFragmentByManagerFragment(R.id.content_detail, tabHistoryPressureFragment);
                tabMode = Constant.MODE_HISTORY;

            }
        });
        btnBasic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // truyền dữ liệu là id user
                Bundle bundle = getIdUserMain();
                TabBasicDetailUserFragment tabBasicDetailFragment = new TabBasicDetailUserFragment();
                tabBasicDetailFragment.setArguments(bundle);
                setFragmentByManagerFragment(R.id.content_detail, tabBasicDetailFragment);
                tabMode = Constant.MODE_BASIC;
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy id để truyền qua activity, thực hiện get thông tin user tại acitvity
                Intent intent = new Intent(DetailUserActivity.this, EditUserActivity.class);
                Bundle bundle = getIdUserMain();
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        btnMoveRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // truyền dữ liệu là id user
                Intent intent = new Intent(DetailUserActivity.this, MoveToAnotherRoom.class);
                Bundle bundle = getIdUserMain();
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        // truyền data khi vào màn hình: rule user đang đăng nhập, thông tin bệnh nhân
        // tạo mới tab thông tin bệnh nhân
        // Thực hiện check quyền user và manager
        // tạo mới tab button riêng cho quyền

    }

    private Bundle getIdUserMain() {
        Bundle bundle = new Bundle();
        textViewIdUser = findViewById(R.id.user_id_main);
        bundle.putString(Constant.USER_ID, textViewIdUser.getText().toString());
        return bundle;
    }

    /**
     * @param idFrameContent  id của frame cần set
     * @param fragmentRequire fragment cần set vào layout
     */
    private void setFragmentByManagerFragment(int idFrameContent, Fragment fragmentRequire) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(idFrameContent, fragmentRequire);
        fragmentTransaction.commit();
    }

    /**
     * Tìm id view trong layout viewgroup
     */
    private void getIdElementView() {
        btnHis = findViewById(R.id.btn_history_pressure);
        btnBasic = findViewById(R.id.btn_basic_detail);
        textViewMessage = findViewById(R.id.message);
        textViewIdUser = findViewById(R.id.user_id_main);
        textViewUsername = findViewById(R.id.username);
        textViewFullName = findViewById(R.id.full_name);
        textViewAge = findViewById(R.id.age);
        textViewDisease = findViewById(R.id.disease);
        textViewTel = findViewById(R.id.tel);
        textViewRoom = findViewById(R.id.room);
        btnEdit = findViewById(R.id.btn_edit_user);
        btnMoveRoom = findViewById(R.id.btn_move_to_another_room);
    }

    /**
     * Set cac gia tri lấy từ view vào bundle
     *
     * @param bundle
     */
    private void setValuesForBundle(Bundle bundle) {
        String userId = textViewIdUser.getText().toString();
        String username = textViewUsername.getText().toString();
        String fullName = textViewFullName.getText().toString();
        String age = textViewAge.getText().toString();
        String disease = textViewDisease.getText().toString();
        String tel = textViewTel.getText().toString();
        String room = textViewRoom.getText().toString();

        bundle.putString("userId", userId);
        bundle.putString("username", username);
        bundle.putString("fullName", fullName);
        bundle.putString("age", age);
        bundle.putString("tel", tel);
        bundle.putString("room", room);
        bundle.putString("disease", disease);
    }
}