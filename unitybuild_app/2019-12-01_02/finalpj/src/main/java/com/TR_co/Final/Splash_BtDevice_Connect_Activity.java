package com.TR_co.Final;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class Splash_BtDevice_Connect_Activity extends AppCompatActivity {
    int PERMISSION = 1000;

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    private String mDeviceName;
    private String mDeviceAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__bt_device__connect_);
        Toast.makeText(getApplicationContext(), "블루투스 디바이스 연결작업 진행할것,", Toast.LENGTH_LONG).show();

        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

        Log.e("ASDKIM", mDeviceName + " : " + mDeviceAddress);

//        Intent i = new Intent(this, UnityPlayerActivity.class);
//        startActivity(i);
//        finish();
    }
}
