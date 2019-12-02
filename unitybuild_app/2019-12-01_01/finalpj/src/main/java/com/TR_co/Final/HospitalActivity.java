package com.TR_co.Final;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.kimbaro.plugin.domain.DB_Object_Location;

import java.util.HashMap;
import java.util.Map;

public class HospitalActivity extends AppCompatActivity {
    DB_Object_Location db_object_location = null;

    WebView webView;
    public String check = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);

        db_object_location = new DB_Object_Location(this);
        db_object_location.getLocation();

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    check = "true";
                    webView = findViewById(R.id.myWeb_hospital);
                    settingWebview(webView);

                    int area = 30; //프로토타입 대전(30) 고정, 추후 개선 예정
                    double lat = db_object_location.getLatitude();
                    double lon = db_object_location.getLongitude();
                    double dist = 10.0; //검색반경 (개발요청사항 10km), 추후 사용자가 임의로 범위 조절하도록 개선 예정

                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Set-Cookie", "ACookieAvailableCrossSite; SameSite=None; secure; httponly");
                    // webView.loadUrl("http://192.168.0.3:8080/hospital_map/?area=" + area + "&latitude=" + lat + "&longitude=" + lon + "&distance=" + dist, headers);
                    webView.loadUrl("file:///android_asset/www/지도/location_detail_check.html?area=" + area + "&latitude=" + lat + "&longitude=" + lon + "&distance=" + dist, headers);
                    Log.e("kim", lat + " : " + lon);
                } else if (msg.what == 1) {
                    check = "false";
                    finish();
                }
            }
        };


        if (db_object_location.isGetLocation()) {
           // Toast.makeText(this, db_object_location.data.toString(), Toast.LENGTH_SHORT).show();
            // DB_Object_Location.data.put("start", new double[]{db_object_location.getLatitude(), db_object_location.getLongitude()});
            handler.sendEmptyMessage(0);
        }
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
        webView.getSettings().setSupportMultipleWindows(true);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
            }

            @Override
            public void onCloseWindow(WebView window) {
                super.onCloseWindow(window);

            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                super.onGeolocationPermissionsShowPrompt(origin, callback);
                callback.invoke(origin, true, true);
            }
        });
    }
}
