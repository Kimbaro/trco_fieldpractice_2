package com.TR_co.Final;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kimbaro.plugin.domain.DB_Object_user;
import com.unity3d.player.UnityPlayer;

import java.util.HashMap;
import java.util.Map;

public class AddressActivity extends AppCompatActivity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        webView = findViewById(R.id.myWeb_address);
        settingWebview(webView);

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Set-Cookie", "ACookieAvailableCrossSite; SameSite=None; secure; httponly");

        //webView.loadUrl("http://192.168.0.108:8080/address_search", headers);
        webView.loadUrl(ServerConfig.IP + "/address_search", headers);
        webView.addJavascriptInterface(this, "Android");
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
        });

    }

    @JavascriptInterface
    public void request_address(final String postcode, final String address, final String detailAddress, final String extraAddress) {
        DB_Object_user.data.put("postcode", postcode);
        DB_Object_user.data.put("address", address);
        DB_Object_user.data.put("detailAddress", detailAddress);
        DB_Object_user.data.put("extraAddress", extraAddress);

        Log.e("data", postcode + address + detailAddress + extraAddress);
        webView.post(new Runnable() {
            @Override
            public void run() {
                UnityPlayer.UnitySendMessage("Main Camera", "setAddress", postcode + "," + address + "," + detailAddress + "," + extraAddress);
                onBackPressed();
            }
        });
    }
}
