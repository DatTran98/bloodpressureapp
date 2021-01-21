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
        View pressureView;
        if (view == null) {
            pressureView = View.inflate(viewGroup.getContext(), R.layout.pressure_item_view, null);
        } else pressureView = view;
        BloodPressureInfor bloodPressureInfor = (BloodPressureInfor) getItem(i);
        int pressMin = bloodPressureInfor.getPressureMin();
        int pressMax = bloodPressureInfor.getPressureMax();
        int heartBeat = bloodPressureInfor.getHeartBeat();
        int standardMax = bloodPressureInfor.getStandardMax();
        int standardMin = bloodPressureInfor.getStandardMin();
        ((TextView) pressureView.findViewById(R.id.heart_beat)).setText(String.format("  Nhịp tim:   %d nhịp/phút", heartBeat));
        ((TextView) pressureView.findViewById(R.id.min_pressure)).setText(String.format("  Huyết áp tâm trương:   %d mmHg", pressMin));
        ((TextView) pressureView.findViewById(R.id.max_pressure)).setText(String.format("  Huyết áp tâm thu:   %d mmHg", pressMax));
        ((TextView) pressureView.findViewById(R.id.date)).setText(String.format("   Ngày đo:    %s", new SimpleDateFormat("yyyy-MM-dd").format(bloodPressureInfor.getTime())));
        ((TextView) pressureView.findViewById(R.id.id_pressure)).setText(String.format("Mã đo: %d   ", bloodPressureInfor.getBloodPressureId()));
        if ((pressMax >= standardMax) || (pressMin <= standardMin) || (pressMax <= 100)) {
            pressureView.findViewById(R.id.pressure_item).setBackgroundResource(R.drawable.view_item_custom_warring);
            pressureView.findViewById(R.id.take_care).setVisibility(View.VISIBLE);
        }
        return pressureView;
    }
}
