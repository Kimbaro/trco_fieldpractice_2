package com.TR_co.Final;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kimbaro.plugin.domain.DB_Object_user;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.myTool);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        WebView webView = findViewById(R.id.myWeb);
        settingWebview(webView);

        String name = DB_Object_user.data.get("name");
        String birth = DB_Object_user.data.get("birth");
        String sex = DB_Object_user.data.get("sex");
        String height = DB_Object_user.data.get("height");
        String weight = DB_Object_user.data.get("weight");
        String age = DB_Object_user.data.get("age");

        JSONObject object = new JSONObject();
        try {
            object.put("name", name);
            object.put("birth", birth);
            object.put("sex", sex);
            object.put("height", height);
            object.put("weight", weight);
            object.put("age", age);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Set-Cookie", "ACookieAvailableCrossSite; SameSite=None; secure; httponly");

        String id = DB_Object_user.data.get("id");

        webView.loadUrl("file:///android_asset/www/목록/index.html?id=" + id + "&info=" + object.toString(), headers);
    }

    private void settingWebview(WebView webView) {
        WebViewClient client = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        };

        webView.setWebViewClient(client);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
