package com.hust.bloddpressure.controllers;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
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

public class ListRoomActivity extends AppCompatActivity {
    private ArrayList<Room> listRoom;
    private ListViewRoomAdapter listViewRoomAdapter;
    private ListView listViewRoom;
    private int rule;
    private ProgressDialog pDialog;
    private TextView textView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_room);
//        toolbar = findViewById(R.id.tool_bar7);
//        toolbar.setTitle(Constant.EMPTY);
//        setSupportActionBar(toolbar);
        new NavigationSetting(ListRoomActivity.this);
        drawerLayout = findViewById(R.id.drawable);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.title_activity_rooms);
        rule = InforStaticClass.getRule();

        if (rule == Constant.USER_RULE) {
            findViewById(R.id.empty).setVisibility(View.INVISIBLE);
            findViewById(R.id.btn_add_room).setVisibility(View.INVISIBLE);
        }
        listRoom = new ArrayList<>();
        // Get list room from DB
        GetListRoom getListRoom = new GetListRoom();
        getListRoom.execute();
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
    /**
     * Set information for dialog
     *
     * @param confirm
     * @param position
     */
    private void setUpDialog(AlertDialog.Builder confirm, final int position) {
        confirm.setTitle(Constant.CONFIRM_TITLE);
        confirm.setMessage(Constant.CONFIRM_ROOM);
        confirm.setPositiveButton(Constant.YES, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String name = listRoom.get(position).getRoomName();
                int roomId = listRoom.get(position).getRoomId();
                DeleteRoom deleteRoom = new DeleteRoom(roomId);
                // Delete information in db
                deleteRoom.execute();
                listRoom.remove(position);
                listViewRoomAdapter.notifyDataSetChanged();
                Toast.makeText(ListRoomActivity.this, Constant.DELETED_ROOM + name, Toast.LENGTH_SHORT).show();
            }
        });
        confirm.setNegativeButton(Constant.NO, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                View view = listViewRoom.getChildAt(position);
                if (position % 2 == 0) {
                    view.setBackgroundResource(R.drawable.view_item_custom_odd);
                } else {
                    view.setBackgroundResource(R.drawable.view_item_custom_even);
                }
                dialogInterface.cancel();
            }
        });
    }

    /**
     * Background Get List Room
     */
    class GetListRoom extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            MyService jsonParser = new MyService();
            String json = jsonParser.callService(Constant.URL_LIST_ROOM, MyService.GET);
            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray jsonArrayRoom = jsonObj.getJSONArray(Constant.OBJECT_JSON_LIST_ROOM);
                        for (int i = 0; i < jsonArrayRoom.length(); i++) {
                            JSONObject obj = (JSONObject) jsonArrayRoom.get(i);
                            Room room = new Room(obj.getInt(Constant.ROOM_ID), obj.getString(Constant.ROOM_NAME));
                            listRoom.add(room);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e(Constant.LOG_JSON, Constant.MSG_JSON);
            }
            return null;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ListRoomActivity.this);
            pDialog.setMessage(Constant.MESSAGE_LOADING);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (pDialog.isShowing())
                pDialog.dismiss();
            getData();
        }
    }

    /**
     * Back ground to delete room information
     */
    class DeleteRoom extends AsyncTask {
        private int roomId;

        public DeleteRoom(int roomId) {
            this.roomId = roomId;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            // Create param list to send to server
            List<NameValuePair> args = new ArrayList<NameValuePair>();
            args.add(new BasicNameValuePair(Constant.ROOM_ID, roomId + ""));
            args.add(new BasicNameValuePair(Constant.ACTION, Constant.ACTION_DELETE));
            MyService sh = new MyService();
            // Get JSON object
            String json = sh.callService(Constant.URL_DELETE_ROOM, MyService.POST, args);
            if (json != null) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    int success = jsonObject.getInt(Constant.JSON_SUCCESS);
                    if (success == 1) {
                    } else {
                    }
                } catch (JSONException e) {
                    Log.d(Constant.ERROR_TAG, e.toString());
                }
            }
            return null;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ListRoomActivity.this);
            pDialog.setMessage(Constant.MSG_DELETING);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (pDialog.isShowing())
                pDialog.dismiss();
        }
    }

    /**
     * Get data and set to view
     */
    private void getData() {
        if (listRoom.size() == 0) {
            textView = findViewById(R.id.empty);
            textView.setText(Constant.MESAGE_NO_DATA);
        } else {
            listViewRoomAdapter = new ListViewRoomAdapter(listRoom);
            listViewRoom = findViewById(R.id.list_rooms);
            listViewRoom.setAdapter(listViewRoomAdapter);
            listViewRoom.setSelection(listViewRoomAdapter.getCount() - 1);
            listViewRoomAdapter.notifyDataSetChanged();
            if (rule == Constant.ADMIN_RULE) {
                listViewRoom.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int position, long l) {
                        view.setBackgroundResource(R.drawable.view_item_custom_warring);
                        //Create interface dialog
                        AlertDialog.Builder confirm = new AlertDialog.Builder(adapterView.getContext());
                        // Set information for dialog
                        setUpDialog(confirm, position);
                        //Create dialog
                        AlertDialog dialogConfirm = confirm.create();
                        dialogConfirm.show();
                        return true;
                    }
                });
            }
            findViewById(R.id.btn_add_room).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ListRoomActivity.this, AddRoomActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

}