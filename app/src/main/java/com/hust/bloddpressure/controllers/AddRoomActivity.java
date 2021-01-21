package com.hust.bloddpressure.controllers;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hust.bloddpressure.R;
import com.hust.bloddpressure.model.MyService;
import com.hust.bloddpressure.model.entities.InforStaticClass;
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
    private ProgressDialog pDialog;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);
        getViewById();
//        toolbar = findViewById(R.id.tool_bar2);
//        toolbar.setTitle(Constant.EMPTY);
//        setSupportActionBar(toolbar);
        new NavigationSetting(AddRoomActivity.this);
        drawerLayout = findViewById(R.id.drawable);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.title_add_room);
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
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.home:
                Intent intent = new Intent(this, MenuManagerActivity.class);
                startActivity(intent);
                return true;
            case R.id.user:
                Intent intent1;
                if (Constant.USER_RULE == InforStaticClass.getRule()) {
                    intent1 = new Intent(this, DetailUserActivity.class);
                } else {
                    intent1 = new Intent(this, ListUserActivity.class);
                }
                startActivity(intent1);
                return true;
            case R.id.analyst:
                Intent intent2 = new Intent(this, AnalysisActivity.class);
                startActivity(intent2);
                return true;
            case R.id.news:
                Intent intent3 = new Intent(this, ListNewsActivity.class);
                startActivity(intent3);
                return true;
            case R.id.web:
                return true;
            case R.id.reset:
//                Intent intent1 = new Intent(this, this.getClass());
//                startActivity(intent1);
                return true;
        }
        return super.onOptionsItemSelected(item);
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