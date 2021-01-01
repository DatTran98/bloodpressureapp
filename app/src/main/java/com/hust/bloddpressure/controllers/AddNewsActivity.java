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
import android.widget.Toast;

import com.hust.bloddpressure.R;
import com.hust.bloddpressure.model.MyService;
import com.hust.bloddpressure.model.entities.News;
import com.hust.bloddpressure.model.entities.Room;
import com.hust.bloddpressure.util.Constant;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddNewsActivity extends AppCompatActivity {
    private Button btnCreate;
    private EditText textNewsTittle, textNewsContent;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);
        getViewById();
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newsTitle = textNewsTittle.getText().toString();
                String newsContent = textNewsContent.getText().toString();
                News news = new News(newsTitle, newsContent);
                AddNews addNews = new AddNews(news);
                addNews.execute();
                Intent intent = new Intent(AddNewsActivity.this,ListNewsActivity.class);
                startActivity(intent);
            }
        });
    }
    private void getViewById() {
        btnCreate = findViewById(R.id.btn_create_news);
        textNewsTittle = findViewById(R.id.title_news);
        textNewsContent = findViewById(R.id.content_news);
    }
    class AddNews extends AsyncTask {
        News news;
        public AddNews(News news) {
            this.news = news;
        }
        @Override
        protected Object doInBackground(Object[] params) {
            // Tạo danh sách tham số gửi đến máy chủ
            List<NameValuePair> args = new ArrayList<NameValuePair>();
            args.add(new BasicNameValuePair("new_title", news.getTitleNew()));
            args.add(new BasicNameValuePair("new_content", news.getContentNew()));
            args.add(new BasicNameValuePair("action", Constant.ACTION_ADD));
            MyService sh = new MyService();
            // Lấy đối tượng JSON
            String json = sh.callService(Constant.URL_ADD_NEWS, MyService.POST, args);
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
            pDialog = new ProgressDialog(AddNewsActivity.this);
            pDialog.setMessage("Creating news..");
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
}