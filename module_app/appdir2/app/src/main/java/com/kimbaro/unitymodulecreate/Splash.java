package com.kimbaro.unitymodulecreate;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.kimbaro.plugin.util.BT_check;

public class Splash extends AppCompatActivity {
    Activity activity = null;
    BT_check bt_check = null;
    Handler splash_handler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        activity = this;
        bt_check = new BT_check(activity);
        splash_handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    Toast.makeText(activity.getApplication(), "connectToSelectedDevice ==>> 연결성공", Toast.LENGTH_SHORT).show();

                    //본 개발 시 유니티액티비티로 이동할것
                    Intent intent = new Intent(activity.getApplication(), LoginActivity.class);
                    startActivity(intent);
                } else if (msg.what == -1) {
                    Toast.makeText(activity.getApplication(), "connectToSelectedDevice ==>> 연결실패", Toast.LENGTH_SHORT).show();
                    bt_check.checkBluetooth(this);
                }
//                if (msg.what == 1 && check) {
//                    Toast.makeText(activity.getApplication(), "connectToSelectedDevice ==>> 연결성공", Toast.LENGTH_SHORT).show();
//                    setCheck(false);
//                } else if (msg.what == -1) {
//                    Toast.makeText(activity.getApplication(), "connectToSelectedDevice ==>> 연결실패", Toast.LENGTH_SHORT).show();
//                    setCheck(true);
//                }
            }
        };
        bt_check.checkBluetooth(splash_handler);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            bt_check.checkBluetooth(splash_handler);
        }
    }
}
