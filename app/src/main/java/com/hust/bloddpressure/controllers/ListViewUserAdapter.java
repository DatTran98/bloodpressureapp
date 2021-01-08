package com.hust.bloddpressure.controllers;

import android.annotation.SuppressLint;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hust.bloddpressure.R;
import com.hust.bloddpressure.model.entities.UserInfor;
import com.hust.bloddpressure.util.Constant;

import java.util.ArrayList;


public class ListViewUserAdapter extends BaseAdapter {
    private ArrayList<UserInfor> listUsers;

    public ListViewUserAdapter(ArrayList<UserInfor> listUsers) {
        this.listUsers = listUsers;
    }

    @Override
    public int getCount() {
        return listUsers.size();
    }

    @Override
    public Object getItem(int i) {
        return listUsers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View accView;
        if (view == null) {
            accView = View.inflate(viewGroup.getContext(), R.layout.user_item_view, null);
        } else accView = view;

        UserInfor userInfor = (UserInfor) getItem(i);
        String userId = userInfor.getUserId();
        if (userId.isEmpty()) {
            TextView textView = accView.findViewById(R.id.user_id);
            textView.setText(Constant.MESAGE_NO_DATA);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(20);
            ((TextView) accView.findViewById(R.id.username)).setText(Constant.EMPTY);
            ((TextView) accView.findViewById(R.id.age)).setText(Constant.EMPTY);
        } else {
            ((TextView) accView.findViewById(R.id.user_id)).setText(String.format("Mã bệnh nhân: %s \t\t\t\t\tBệnh %s", userInfor.getUserId(), userInfor.getDiseaseName()));
            ((TextView) accView.findViewById(R.id.username)).setText(String.format("Tên bệnh nhân:   %s", userInfor.getFullName()));
            ((TextView) accView.findViewById(R.id.age)).setText(String.format("Tuổi: %s", userInfor.getAge()));
        }
        accView.findViewById(R.id.btn_choose).setVisibility(View.INVISIBLE);
        if (i % 2 == 0) {
            accView.findViewById(R.id.user_item).setBackgroundResource(R.drawable.view_item_custom_odd);
        } else {
            accView.findViewById(R.id.user_item).setBackgroundResource(R.drawable.view_item_custom_even);
        }
        return accView;
    }
}
