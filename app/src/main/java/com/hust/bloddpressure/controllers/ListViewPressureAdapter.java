package com.hust.bloddpressure.controllers;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hust.bloddpressure.R;
import com.hust.bloddpressure.model.entities.BloodPressureInfor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ListViewPressureAdapter extends BaseAdapter {
    final ArrayList<BloodPressureInfor> listBloodPressureInfor;

    public ListViewPressureAdapter(ArrayList<BloodPressureInfor> listBloodPressureInfor) {
        this.listBloodPressureInfor = listBloodPressureInfor;
    }

    @Override
    public int getCount() {
        return listBloodPressureInfor.size();
    }

    @Override
    public Object getItem(int i) {
        return listBloodPressureInfor.get(i);
    }

    @Override
    public long getItemId(int i) {
        return listBloodPressureInfor.get(i).getBloodPressureId();
    }

    @SuppressLint({"DefaultLocale", "SimpleDateFormat"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View newView;
        if (view == null) {
            newView = View.inflate(viewGroup.getContext(), R.layout.pressure_item_view, null);
        } else newView = view;
        BloodPressureInfor bloodPressureInfor = (BloodPressureInfor) getItem(i);
        ((TextView) newView.findViewById(R.id.min_pressure)).setText(String.format("  Huyết áp tâm trương:   %d", bloodPressureInfor.getPressureMin()));
        ((TextView) newView.findViewById(R.id.max_pressure)).setText(String.format("  Huyết áp tâm thu:   %d", bloodPressureInfor.getPressureMax()));
        ((TextView) newView.findViewById(R.id.date)).setText(String.format("   Ngày đo:    %s", new SimpleDateFormat("yyyy-MM-dd").format(bloodPressureInfor.getTime())));
        ((TextView) newView.findViewById(R.id.id_pressure)).setText(String.format("Mã đo: %d   ", bloodPressureInfor.getBloodPressureId()));
        return newView;
    }
}
