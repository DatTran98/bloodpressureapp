package com.hust.bloddpressure.controllers;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.hust.bloddpressure.R;
import com.hust.bloddpressure.model.entities.Room;
import com.hust.bloddpressure.util.Common;
import com.hust.bloddpressure.util.Constant;

import java.util.ArrayList;
import java.util.List;

public class MoveToAnotherRoom extends AppCompatActivity {
    private AppCompatSpinner spinnerRoom;
    private SpinnerRoomAdapter spinnerRoomAdapter;
    private TextView selectRoomId;
    private Button btnSave;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_to_another_room);
        new NavigationSetting(MoveToAnotherRoom.this);
        drawerLayout = findViewById(R.id.drawable);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        getSupportActionBar().setTitle(R.string.move_to_room_title);
        // Get user id from bundle

        // Find view by id
        findViewByIdForView();
        // Set data cho dropdown và and set text for dropdown
        setDataForDropdown();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int roomId = spinnerRoom.getSelectedItemPosition();
                if (roomId == 0) {
                    Common.showToast(MoveToAnotherRoom.this, Constant.ROOM_CHOOSE_MESS);
                    btnSave.setEnabled(false);
                } else {
                    Intent intent = getIntent();
//                int userId = intent.getExtras().getInt("userId");
                    // Do move user to another room
                    selectRoomId.setText("1");
                }
            }
        });
    }

    @SuppressLint("WrongViewCast")
    private void findViewByIdForView() {
        selectRoomId = findViewById(R.id.room_id_edit);
        spinnerRoom = findViewById(R.id.spinner_room);
        btnSave = findViewById(R.id.btn_save);
    }

    private void setDataForDropdown() {
        final List<Room> listRoom = new ArrayList<>();
        final Room room = new Room();
        room.setRoomId(-1);
        room.setRoomName(Constant.CHOOSE_ROOM);
        listRoom.add(room);
        // THỰC HIỆN TRUY VẤN DB ĐỂ LẤY RA PHÒNG

        for (int i = 1; i < 5; i++) {
            Room room1 = new Room();
            room1.setRoomId(i);
            room1.setRoomName("Phong " + i);
            listRoom.add(room1);
        }
        // Create adapter and set value for spiner
        spinnerRoomAdapter = new SpinnerRoomAdapter(listRoom);
        spinnerRoom.setAdapter(spinnerRoomAdapter);
        spinnerRoom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Get room item selected
                Room roomItem = (Room) spinnerRoomAdapter.getItem(i);
                int roomId = roomItem.getRoomId();
                String roomName = roomItem.getRoomName();
                // Get room id and check condition
                if (roomId > 0) {
                    selectRoomId.setText(roomName);
                    btnSave.setEnabled(true);
                } else {
                    btnSave.setEnabled(false);
                    selectRoomId.setText(Constant.CHOOSE_ROOM);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectRoomId.setText(Constant.EMPTY);
            }
        });
    }
}