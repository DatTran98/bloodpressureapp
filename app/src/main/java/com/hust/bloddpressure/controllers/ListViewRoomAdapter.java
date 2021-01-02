package com.hust.bloddpressure.controllers;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.hust.bloddpressure.R;
import com.hust.bloddpressure.model.entities.News;
import com.hust.bloddpressure.model.entities.Room;

import java.util.ArrayList;

public class ListViewRoomAdapter extends BaseAdapter {
    final ArrayList<Room> listRoom;

    public ListViewRoomAdapter(ArrayList<Room> listRoom) {
        this.listRoom = listRoom;
    }

    @Override
    public int getCount() {
        return listRoom.size();
    }

    @Override
    public Object getItem(int i) {
        return listRoom.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View newView;
        if (view == null) {
            newView = View.inflate(viewGroup.getContext(), R.layout.room_item_view, null);
        } else newView = view;
        Room room = (Room) getItem(i);
        ((TextView) newView.findViewById(R.id.room_id)).setText(String.format("Mã phòng\n %d", room.getRoomId()));
        ((TextView) newView.findViewById(R.id.room_name)).setText(String.format("Tên phòng\n %s", room.getRoomName()));
        newView.findViewById(R.id.btn_choose).setVisibility(View.INVISIBLE);

        if (i % 2 == 0) {
            newView.findViewById(R.id.room_item).setBackgroundResource(R.drawable.view_item_custom_odd);
        } else {
            newView.findViewById(R.id.room_item).setBackgroundResource(R.drawable.view_item_custom_even);
        }
        return newView;
    }
}
