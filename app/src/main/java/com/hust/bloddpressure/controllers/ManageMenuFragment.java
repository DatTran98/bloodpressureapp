package com.hust.bloddpressure.controllers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hust.bloddpressure.R;
import com.hust.bloddpressure.model.entities.InforStaticClass;
import com.hust.bloddpressure.util.Constant;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManageMenuFragment# newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageMenuFragment extends Fragment {
    private TextView textViewLabelTile, textViewLabelListOrDetail, textViewLabelRoom, textViewStatic;
    private int ruleId;
    private ImageButton imgRoom;
    private RelativeLayout menu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manage_menu, container, false);
    }

    @SuppressLint("SetTextI18n")
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textViewLabelTile = view.findViewById(R.id.label_title);
        textViewLabelListOrDetail = view.findViewById(R.id.label_move_list_or_detail_user);
        textViewLabelRoom = view.findViewById(R.id.label_move_list_room);
        textViewStatic = view.findViewById(R.id.label_move_static);
        imgRoom = view.findViewById(R.id.btn_move_list_room);
        menu = view.findViewById(R.id.menu);
        // Get rule when login
        ruleId = InforStaticClass.getRule();
        if (ruleId == Constant.USER_RULE) {
            textViewLabelTile.setText(R.string.title_user_menu);
            textViewLabelListOrDetail.setText(R.string.btn_name_user_information);
            textViewLabelRoom.setText(R.string.label_setting_pressure);
            imgRoom.setBackgroundResource(R.mipmap.btn_setting);
        } else {
            textViewLabelTile.setText(R.string.blood_pressure);
            textViewStatic.setText(Constant.STATIC_NAME);
        }
        view.findViewById(R.id.btn_move_list_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.setVisibility(View.GONE);
                Intent intent;
                if (ruleId == Constant.USER_RULE) {
                    intent = new Intent(getActivity(), DetailUserActivity.class);
                } else {
                    intent = new Intent(getActivity(), ListUserActivity.class);
                }
                startActivity(intent);
                menu.setVisibility(View.VISIBLE);
            }
        });
        view.findViewById(R.id.btn_move_list_new).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ListNewsActivity.class);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.btn_move_list_room).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (ruleId == Constant.USER_RULE) {
                    intent = new Intent(getActivity(), SettingStandardPressureActivity.class);
                } else {
                    intent = new Intent(getActivity(), ListRoomActivity.class);
                }
                startActivity(intent);
            }
        });
        view.findViewById(R.id.btn_move_list_static).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AnalysisActivity.class);
                startActivity(intent);
            }
        });
    }
}