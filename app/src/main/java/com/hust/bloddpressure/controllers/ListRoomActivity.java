package com.hust.bloddpressure.controllers;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.hust.bloddpressure.R;
import com.hust.bloddpressure.model.MyService;
import com.hust.bloddpressure.model.entities.InforStaticClass;
import com.hust.bloddpressure.model.entities.Room;
import com.hust.bloddpressure.util.Common;
import com.hust.bloddpressure.util.Constant;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListRoomActivity extends AppCompatActivity {
    ArrayList<Room> listRoom;
    ListViewRoomAdapter listViewRoomAdapter;
    ListView listViewRoom;
    private int rule;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_room);
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
                deleteRoom.execute(roomId);
                listRoom.remove(position);
                listViewRoomAdapter.notifyDataSetChanged();
                Toast.makeText(ListRoomActivity.this, Constant.DELETED_ROOM + name, Toast.LENGTH_SHORT).show();
            }
        });
        confirm.setNegativeButton(Constant.NO, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
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
        listViewRoomAdapter = new ListViewRoomAdapter(listRoom);
        listViewRoom = findViewById(R.id.list_rooms);
        listViewRoom.setAdapter(listViewRoomAdapter);
        listViewRoom.setSelection(listViewRoomAdapter.getCount()-1);
        listViewRoomAdapter.notifyDataSetChanged();
        if (rule == Constant.ADMIN_RULE) {
            listViewRoom.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int position, long l) {
                    if (listRoom.get(0).getRoomId() != 0) {
                        for (int i = 0; i < listRoom.size() - 1; i++) {
                            if (i != position) {
                                int i1 = listViewRoom.getLastVisiblePosition();
                                View viewChoose1, roomItem1;
                                View view1 = listViewRoom.getChildAt(i);
                                if (view1 == null) continue;
                                roomItem1 = view1.findViewById(R.id.room_item);
                                viewChoose1 = view1.findViewById(R.id.btn_choose);
                                Common.backNormal(i, roomItem1, viewChoose1);
                            }
                        }
                        View viewChoose, userItem;
                        userItem = view.findViewById(R.id.room_item);
                        viewChoose = view.findViewById(R.id.btn_choose);
                        Button btnCancel = view.findViewById(R.id.btn_cancel);
                        Button btnDel = view.findViewById(R.id.btn_delete);
                        userItem.setBackgroundResource(R.color.choose_item);
                        int visibleStatus = viewChoose.getVisibility();
                        if (visibleStatus == View.INVISIBLE) {
                            viewChoose.setVisibility(View.VISIBLE);
                        } else {
                            viewChoose.setVisibility(View.INVISIBLE);
                        }
                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                View view1 = listViewRoom.getChildAt(position);
                                View viewChoose1, userItem1;
                                userItem1 = view1.findViewById(R.id.room_item);
                                viewChoose1 = view1.findViewById(R.id.btn_choose);
                                Common.backNormal(position, userItem1, viewChoose1);
                            }
                        });
                        btnDel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //Create interface dialog
                                AlertDialog.Builder confirm = new AlertDialog.Builder(adapterView.getContext());
                                // Set information for dialog
                                setUpDialog(confirm, position);
                                //Create dialog
                                AlertDialog dialogConfirm = confirm.create();
                                dialogConfirm.show();
                            }
                        });
                    }
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