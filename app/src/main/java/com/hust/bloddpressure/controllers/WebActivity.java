package com.hust.bloddpressure.controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.hust.bloddpressure.R;
import com.hust.bloddpressure.model.MyWebViewClient;

import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class WebActivity extends AppCompatActivity {
    private WebView webView;
    private Button buttonGo;
    private EditText addressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        webView =  findViewById(R.id.webView);
        buttonGo =findViewById(R.id.button_go);
        addressBar =findViewById(R.id.editText_addressBar);
        webView.setWebViewClient(new MyWebViewClient(addressBar));
        buttonGo.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                goUrl();
            }
        });
        goUrl();
    }

    private void goUrl() {
        String url = addressBar.getText().toString().trim();
        if(url.isEmpty())  {
            url = "https://www.vinmec.com/";
        }
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(url);
    }
}