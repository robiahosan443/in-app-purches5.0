package com.alphabetlore3d.simsoundboard.w;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alphabetlore3d.simsoundboard.R;
import com.alphabetlore3d.simsoundboard.b.n;


public class w extends AppCompatActivity {
    private WebView webView;
    private TextView toolBarTitle;
    ImageView a;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wv);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.a = (ImageView) findViewById(R.id.menu);

        webView = findViewById(R.id.webView);
        toolBarTitle = findViewById(R.id.tvTitle);

        String title = getIntent().getStringExtra("title");
        String url = getIntent().getStringExtra("url");
        toolBarTitle.setText("Loading...");
        webView.loadUrl(url);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                toolBarTitle.setText(title);
            }
        });



        this.a.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext() , n.class));

            }
        });


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}