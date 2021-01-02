package com.hust.bloddpressure.controllers;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.hust.bloddpressure.R;
import com.hust.bloddpressure.model.MyService;
import com.hust.bloddpressure.model.entities.UserInfor;
import com.hust.bloddpressure.util.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListUserFragment# newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListUserFragment extends Fragment {
    ArrayList<UserInfor> listUsers;
    ListViewUserAdapter listViewUserAdapter;
    ListView listViewUsers;
    private int rule;
    ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_user, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
        }
        listViewUsers = view.findViewById(R.id.list_users);
        listUsers = new ArrayList<>();
        GetListUser getListUser = new GetListUser();
        getListUser.execute();
    }


    /**
     * Background Get List User
     */
    class GetListUser extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            MyService jsonParser = new MyService();
            String json = jsonParser.callService(Constant.URL_LIST_USER, MyService.GET);
            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray jsonArrayUser = jsonObj.getJSONArray(Constant.OBJECT_JSON_LIST_USER);
                        for (int i = 0; i < jsonArrayUser.length(); i++) {
                            JSONObject obj = (JSONObject) jsonArrayUser.get(i);
                            int age = obj.getInt(Constant.AGE);
                            String fullName = obj.getString(Constant.FULL_NAME);
                            String diseaseName = obj.getString(Constant.DISEASE_NAME);
                            String userId = obj.getString(Constant.USER_ID);
                            UserInfor userInfor = new UserInfor(userId, fullName, age, diseaseName);
                            listUsers.add(userInfor);
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
            pDialog = new ProgressDialog(getActivity());
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

        private void getData() {
            if (listUsers.size() == 0) {
                UserInfor userInfor = new UserInfor(Constant.EMPTY, Constant.EMPTY, Constant.INT_VALUE_DEFAULT, Constant.EMPTY);
                listUsers.add(userInfor);
            }

            // Create adapter to sent the data on the view
            listViewUserAdapter = new ListViewUserAdapter(listUsers);
            listViewUsers.setAdapter(listViewUserAdapter);
            listViewUserAdapter.notifyDataSetChanged();
            listViewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // set status default when not selected
                    for (int i = 0; i < listUsers.size(); i++) {
                        View viewChoose1, userItem1;
                        View view1 = listViewUsers.getChildAt(i);
                        userItem1 = view1.findViewById(R.id.user_item);
                        viewChoose1 = view1.findViewById(R.id.btn_choose);
                        backNormal(i, userItem1, viewChoose1);
                    }
                    // Get user information that just have selected
                    UserInfor userInfor = (UserInfor) listViewUserAdapter.getItem(position);
                    // Get id of user and send to detail screen
                    String userId = userInfor.getUserId();
                    // Create detail activity
                    Intent intent = new Intent(getActivity(), DetailUserActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.USER_ID, userId);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            listViewUsers.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int position, long l) {
                    if (!listUsers.get(0).getUserId().isEmpty()) {
                        for (int i = 0; i < listUsers.size(); i++) {
                            if (i != position) {
                                View viewChoose1, userItem1;
                                View view1 = listViewUsers.getChildAt(i);
                                userItem1 = view1.findViewById(R.id.user_item);
                                viewChoose1 = view1.findViewById(R.id.btn_choose);
                                backNormal(i, userItem1, viewChoose1);
                            }
                        }
                        View viewChoose, userItem;
                        userItem = view.findViewById(R.id.user_item);
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
                                View view1 = listViewUsers.getChildAt(position);
                                View viewChoose1, userItem1;
                                userItem1 = view1.findViewById(R.id.user_item);
                                viewChoose1 = view1.findViewById(R.id.btn_choose);
                                backNormal(position, userItem1, viewChoose1);
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

        /**
         * Set information for dialog
         *
         * @param confirm
         * @param position
         */
        private void setUpDialog(AlertDialog.Builder confirm, final int position) {
            confirm.setTitle(Constant.CONFIRM_TITLE);
            confirm.setMessage(Constant.CONFIRM_USER);
            confirm.setPositiveButton(Constant.YES, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String name = listUsers.get(position).getFullName();
                    listUsers.remove(position);
                    // Do delete in DB
                    listViewUserAdapter.notifyDataSetChanged();
                    View view2 = listViewUsers.getChildAt(position);
                    View viewChoose, userItem;
                    userItem = view2.findViewById(R.id.user_item);
                    viewChoose = view2.findViewById(R.id.btn_choose);
                    backNormal(position, userItem, viewChoose);
                    Toast.makeText(getActivity(), Constant.DELETED_USER + name, Toast.LENGTH_SHORT).show();
                }
            });
            confirm.setNegativeButton(Constant.NO, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    View viewChoose, userItem;
                    View view3 = listViewUsers.getChildAt(position);
                    userItem = view3.findViewById(R.id.user_item);
                    viewChoose = view3.findViewById(R.id.btn_choose);
                    backNormal(position, userItem, viewChoose);
                    dialogInterface.cancel();
                }
            });
        }

        /**
         * Set status default when not selected
         *
         * @param position   position to set
         * @param userItem   view need set with position
         * @param viewChoose view need set with position
         */
        private void backNormal(int position, View userItem, View viewChoose) {
            if (position % 2 == 0) {
                userItem.setBackgroundResource(R.color.odd_item);
            } else {
                userItem.setBackgroundResource(R.color.even);
            }
            viewChoose.setVisibility(View.INVISIBLE);
        }
    }
}