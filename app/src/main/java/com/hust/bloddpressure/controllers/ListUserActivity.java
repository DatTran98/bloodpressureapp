package com.hust.bloddpressure.controllers;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.hust.bloddpressure.R;
import com.hust.bloddpressure.model.MyService;
import com.hust.bloddpressure.model.entities.InforStaticClass;
import com.hust.bloddpressure.model.entities.UserInfor;
import com.hust.bloddpressure.util.Constant;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListUserActivity extends AppCompatActivity {
    ArrayList<UserInfor> listUsers;
    ListViewUserAdapter listViewUserAdapter;
    ListView listViewUsers;
    private ArrayList<UserInfor> listUsersSource;
    private int rule;
    private ProgressDialog pDialog;
    private int selectedPosition = 0;
    private SearchView searchView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user);
        // get all user information from db
        listViewUsers = findViewById(R.id.list_users);
        searchView = findViewById(R.id.searchView);
        setQueryTextChange();
//        toolbar = findViewById(R.id.tool_bar);
//        toolbar.setTitle(Constant.EMPTY);
//        setSupportActionBar(toolbar);
        new NavigationSetting(ListUserActivity.this);
        drawerLayout = findViewById(R.id.drawable);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.list_user_title);
        listUsers = new ArrayList<>();
        listUsersSource = new ArrayList<>();
        GetListUser getListUser = new GetListUser();
        getListUser.execute();

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
    private void setQueryTextChange() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String text = newText.trim().toLowerCase();
                filterItem(text);
                return false;
            }
        });
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
                            listUsersSource.add(userInfor);
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
            pDialog = new ProgressDialog(ListUserActivity.this);
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
            listUsers.addAll(listUsersSource);
            if (listUsers.size() == 0) {
                UserInfor userInfor = new UserInfor(Constant.EMPTY, Constant.EMPTY, Constant.INT_VALUE_DEFAULT, Constant.EMPTY);
                listUsers.add(userInfor);
            }

            // Create adapter to sent the data on the view
            listViewUserAdapter = new ListViewUserAdapter(listUsers);
            listViewUsers.setSelection(listViewUserAdapter.getCount() - 1);
            listViewUsers.setAdapter(listViewUserAdapter);
            listViewUserAdapter.notifyDataSetChanged();
            listViewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // set status default when not selected

                    // Get user information that just have selected
                    UserInfor userInfor = (UserInfor) listViewUserAdapter.getItem(position);
                    // Get id of user and send to detail screen
                    String userId = userInfor.getUserId();
                    if (userId != null && !userId.isEmpty()) {
                        // Create detail activity
                        Intent intent = new Intent(ListUserActivity.this, DetailUserActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(Constant.USER_ID, userId);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ListUserActivity.this, Constant.MESAGE_NO_DATA, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            listViewUsers.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int position, long l) {
                    if (!listUsers.get(0).getUserId().isEmpty()) {
                        view.setBackgroundResource(R.drawable.view_item_custom_warring);
                        //Create interface dialog
                        AlertDialog.Builder confirm = new AlertDialog.Builder(adapterView.getContext());
                        // Set information for dialog
                        setUpDialog(confirm, position);
                        //Create dialog
                        AlertDialog dialogConfirm = confirm.create();
                        dialogConfirm.show();
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
                    String userId = listUsers.get(position).getUserId();
                    // Do delete in DB
                    DeleteUser deleteUser = new DeleteUser(userId);
                    deleteUser.execute();
                    listUsers.remove(position);
                    listViewUserAdapter.notifyDataSetChanged();
                    Toast.makeText(ListUserActivity.this, Constant.DELETED_USER + name, Toast.LENGTH_SHORT).show();
                }
            });
            confirm.setNegativeButton(Constant.NO, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    View view = listViewUsers.getChildAt(position);
                    if (position % 2 == 0) {
                        view.setBackgroundResource(R.drawable.view_item_custom_odd);
                    } else {
                        view.setBackgroundResource(R.drawable.view_item_custom_even);
                    }
                    dialogInterface.cancel();
                }
            });
        }
    }

    /**
     * Back ground to delete room information
     */
    class DeleteUser extends AsyncTask {
        private String userId;

        public DeleteUser(String userId) {
            this.userId = userId;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            // Create param list to send to server
            List<NameValuePair> args = new ArrayList<NameValuePair>();
            args.add(new BasicNameValuePair(Constant.USER_ID, userId));
            MyService sh = new MyService();
            // Get JSON object
            String json = sh.callService(Constant.URL_DELETE_USER, MyService.POST, args);
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
            pDialog = new ProgressDialog(ListUserActivity.this);
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
     * Do fill list view user when text search change
     * @param charText text got from text view
     */
    public void filterItem(String charText) {
        String text = charText;
        listUsers.clear();
        if (text.length() == 0) {
            listUsers.addAll(listUsersSource);
        } else {
            for (UserInfor userInfor : listUsersSource) {
                if (userInfor.getFullName().toLowerCase().contains(charText)) {
                    listUsers.add(userInfor);
                }
            }
        }
        listViewUserAdapter.notifyDataSetChanged();
    }
}