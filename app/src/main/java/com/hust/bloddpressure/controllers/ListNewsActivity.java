package com.hust.bloddpressure.controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.hust.bloddpressure.R;
import com.hust.bloddpressure.model.MyService;
import com.hust.bloddpressure.model.entities.InforStaticClass;
import com.hust.bloddpressure.model.entities.News;
import com.hust.bloddpressure.util.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListNewsActivity extends AppCompatActivity {
    ArrayList<News> listNews;
    ListViewNewsAdapter listViewNewAdapter;
    ListView listViewNews;
    private int rule;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_news);
        // Phải lấy ra id được truyền từ màn hình list từ intent
        Bundle bundle = getIntent().getExtras();
        rule = InforStaticClass.getRule();
        if (rule == Constant.USER_RULE) {
            findViewById(R.id.btn_add_news).setVisibility(View.INVISIBLE);
        }
        listNews = new ArrayList<>();
        GetListNews getListNews = new GetListNews();
        getListNews.execute();

        findViewById(R.id.btn_add_news).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListNewsActivity.this, AddNewsActivity.class);
                startActivity(intent);
            }
        });
    }
    class GetListNews extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            MyService jsonParser = new MyService();
            String json = jsonParser.callService(Constant.URL_LIST_NEWS, MyService.GET);
            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray jsonArrayRoom = jsonObj.getJSONArray("list_news");
                        for (int i = 0; i < jsonArrayRoom.length(); i++) {
                            JSONObject obj = (JSONObject) jsonArrayRoom.get(i);
                            News news = new News(obj.getInt("new_id"), obj.getString("new_title"), obj.getString("new_content"));
                            listNews.add(news);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("JSON Data", "Didn't receive any data from server!");
            }
            return null;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ListNewsActivity.this);
            pDialog.setMessage("Loading..");
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
                listNews.add(new News(1, "Không có bản tin nào", "Vui lòng quay lại sau"));
            }
            listViewNewAdapter = new ListViewNewsAdapter(listNews);
            listViewNews = findViewById(R.id.list_news);
            listViewNews.setAdapter(listViewNewAdapter);
            listViewNewAdapter.notifyDataSetChanged();
            listViewNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(ListNewsActivity.this, "Cám ơn bạn đã đọc tin", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}