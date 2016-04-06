package com.example.innova6.cooperativa;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {

    public String lk;
    public static boolean flag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // recogerparametro();
       populateWebView();
    }
    private void populateWebView() {
        lk= "http://m.cooperativa.cl";
        Log.i("La url ", "es: " + lk);
        final WebView myWebView;
        myWebView = new WebView(this);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.loadUrl(lk);
        setContentView(myWebView);
        myWebView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                if (url.contains("#") && flag == false) {
                    myWebView.loadUrl(url);
                    flag = true;
                } else {
                    flag = false;
                }
            }
        });
    }

}
