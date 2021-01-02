package com.hust.bloddpressure.controllers;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.hust.bloddpressure.R;
import com.hust.bloddpressure.model.entities.Room;

import java.util.List;

public class SpinnerRoomAdapter implements SpinnerAdapter {
    private List<Room> listRoom;

    public SpinnerRoomAdapter(List<Room> listRoom) {
        this.listRoom = listRoom;
    }

    @Override
    public View getDropDownView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.room_item_view, viewGroup, false);
        TextView textViewRoomId, textViewRoomName;
        textViewRoomId = view.findViewById(R.id.room_id);
        textViewRoomName = view.findViewById(R.id.room_name);
        if (listRoom.get(i).getRoomId() > 0) {
            textViewRoomId.setText(String.format("Mã phòng: %d", listRoom.get(i).getRoomId()));
        }
        textViewRoomName.setText(listRoom.get(i).getRoomName());
        return view;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

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
        return listRoom.get(i).getRoomId();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.room_item_view, viewGroup, false);
        TextView textViewRoomId, textViewRoomName;
        textViewRoomId = view.findViewById(R.id.room_id);
        textViewRoomName = view.findViewById(R.id.room_name);
        if (listRoom.get(i).getRoomId() > 0) {
            textViewRoomId.setText(String.format("Mã phòng: %d", listRoom.get(i).getRoomId()));

        }
        textViewRoomName.setText(listRoom.get(i).getRoomName());

        return view;
    }


}
