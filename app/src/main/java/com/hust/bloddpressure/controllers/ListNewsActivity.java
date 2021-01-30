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
import android.widget.Toast;

import com.hust.bloddpressure.R;
import com.hust.bloddpressure.model.MyService;
import com.hust.bloddpressure.model.entities.InforStaticClass;
import com.hust.bloddpressure.model.entities.News;
import com.hust.bloddpressure.util.Constant;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListNewsActivity extends AppCompatActivity {
    ArrayList<News> listNews;
    ListViewNewsAdapter listViewNewAdapter;
    ListView listViewNews;
    private int rule;
    ProgressDialog pDialog;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_news);
//        toolbar = findViewById(R.id.tool_bar6);
//        toolbar.setTitle(Constant.EMPTY);
//        setSupportActionBar(toolbar);
        // check rule
        new NavigationSetting(ListNewsActivity.this);
        drawerLayout = findViewById(R.id.drawable);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setTitle(R.string.title_activity_news);
        getSupportActionBar().setTitle(Constant.EMPTY);
        rule = InforStaticClass.getRule();
        if (rule == Constant.USER_RULE) {
            findViewById(R.id.btn_add_news).setVisibility(View.INVISIBLE);
        }
        listNews = new ArrayList<>();
        GetListNews getListNews = new GetListNews();
        getListNews.execute();
        // button click listener
        findViewById(R.id.btn_add_news).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListNewsActivity.this, AddNewsActivity.class);
                startActivity(intent);
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
            case R.id.reset:
                GetListNews getListNews = new GetListNews();
                getListNews.execute();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Class do back ground get list news
     */
    class GetListNews extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            MyService jsonParser = new MyService();
            String json = jsonParser.callService(Constant.URL_LIST_NEWS, MyService.GET);
            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray jsonArrayRoom = jsonObj.getJSONArray(Constant.OBJECT_JSON_LIST_NEWS);
                        for (int i = 0; i < jsonArrayRoom.length(); i++) {
                            JSONObject obj = (JSONObject) jsonArrayRoom.get(i);
                            News news = new News(obj.getInt(Constant.NEWS_ID), obj.getString(Constant.NEWS_TITLE), obj.getString(Constant.NEWS_CONTENT));
                            listNews.add(news);
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
            pDialog = new ProgressDialog(ListNewsActivity.this);
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
            if (listNews.size() == 0) {
                listNews.add(new News(Constant.INT_VALUE_DEFAULT, Constant.NO_NEWS, Constant.COMBACK_LATER));
            }
            listViewNewAdapter = new ListViewNewsAdapter(listNews);
            listViewNews = findViewById(R.id.list_news);
            listViewNews.setAdapter(listViewNewAdapter);
            listViewNewAdapter.notifyDataSetChanged();
            listViewNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(ListNewsActivity.this, Constant.THANKS_READING, Toast.LENGTH_SHORT).show();
                }
            });
            if (rule == Constant.ADMIN_RULE) {
                listViewNews.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int position, long l) {
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

        }
    }

    /**
     * Set information for dialog
     *
     * @param confirm
     * @param position
     */
    private void setUpDialog(AlertDialog.Builder confirm, final int position) {
        confirm.setTitle(Constant.CONFIRM_TITLE);
        confirm.setMessage(Constant.CONFIRM_NEWS);
        confirm.setPositiveButton(Constant.YES, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int newId = listNews.get(position).getNewId();
                DeleteNews deleteNews = new DeleteNews(newId);
                // Delete information in db
                deleteNews.execute();
                listNews.remove(position);
                listViewNewAdapter.notifyDataSetChanged();

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
     * Back ground to delete room information
     */
    class DeleteNews extends AsyncTask {
        private int newsId;

        public DeleteNews(int newsId) {
            this.newsId = newsId;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            // Create param list to send to server
            List<NameValuePair> args = new ArrayList<NameValuePair>();
            args.add(new BasicNameValuePair(Constant.NEWS_ID, newsId + ""));
            args.add(new BasicNameValuePair(Constant.ACTION, Constant.ACTION_DELETE));
            MyService sh = new MyService();
            // Get JSON object
            String json = sh.callService(Constant.URL_DELETE_NEWS, MyService.POST, args);
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
            pDialog = new ProgressDialog(ListNewsActivity.this);
            pDialog.setMessage(Constant.MSG_DELETING);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (pDialog.isShowing())
                pDialog.dismiss();
            Toast.makeText(ListNewsActivity.this, Constant.DELETED_SUCCESS, Toast.LENGTH_SHORT).show();
        }
    }
}