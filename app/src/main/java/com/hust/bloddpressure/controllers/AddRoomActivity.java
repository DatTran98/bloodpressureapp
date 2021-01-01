package com.hust.bloddpressure.controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hust.bloddpressure.R;
import com.hust.bloddpressure.model.MyService;
import com.hust.bloddpressure.model.entities.Room;
import com.hust.bloddpressure.util.Constant;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddRoomActivity extends AppCompatActivity {
    private Button btnCreate;
    private EditText textRoomId, textRoomName;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);
        getViewById();
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int roomId = Integer.parseInt(textRoomId.getText().toString());
                String roomName = textRoomName.getText().toString();
                Room room = new Room(roomId, roomName);
                AddNewRoom addNewRoom = new AddNewRoom(room);
                addNewRoom.execute();
            }
        });
    }

    private void getViewById() {
        btnCreate = findViewById(R.id.btn_create_room);
        textRoomId = findViewById(R.id.room_id);
        textRoomName = findViewById(R.id.room_name);
    }

    class AddNewRoom extends AsyncTask {
        Room room;

        public AddNewRoom(Room room) {
            this.room = room;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            // Tạo danh sách tham số gửi đến máy chủ
            List<NameValuePair> args = new ArrayList<NameValuePair>();
            args.add(new BasicNameValuePair("room_id", room.getRoomId() + ""));
            args.add(new BasicNameValuePair("room_name", room.getRoomName()));
            args.add(new BasicNameValuePair("action", Constant.ACTION_ADD));
            MyService sh = new MyService();
            // Lấy đối tượng JSON
            String json = sh.callService(Constant.URL_ADD_ROOM, MyService.POST, args);
            if (json != null) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    int success = jsonObject.getInt("success");
                    if (success == 1) {
                    } else {
                    }
                } catch (JSONException e) {
                    Log.d("Error...", e.toString());
                }
            }
            return null;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddRoomActivity.this);
            pDialog.setMessage("Creating new room..");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (pDialog.isShowing())
                pDialog.dismiss();
            Intent intent = new Intent(AddRoomActivity.this, ListRoomActivity.class);
            startActivity(intent);
        }
    }
}