package com.TR_co.Final;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class Splash_BtDevice_Connect_Activity extends AppCompatActivity {
    int PERMISSION = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__bt_device__connect_);
        Toast.makeText(getApplicationContext(), "블루투스 디바이스 연결작업 진행할것,", Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, UnityPlayerActivity.class);
        startActivity(i);
        finish();
    }
}
