package com.kimbaro.plugin;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.kimbaro.plugin.domain.DB_Object_user;
import com.kimbaro.plugin.domain.RetrofitService;
import com.kimbaro.plugin.util.ServerConfig;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IdCheckModule {
    public boolean check = false;

    public boolean getCheck() {
        return check;
    }

    public void requestIdCheck(final String id, final Activity activity) {
        Log.e("kim", id);

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    check = false;
                } else if (msg.what == 1) {
                    check = true;
                }
            }
        };


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerConfig.IP).addConverterFactory(GsonConverterFactory.create()).build();
                RetrofitService trco = retrofit.create(RetrofitService.class);


                //아이디 중복여부
                Call<JsonObject> call = trco.find_id_user(id);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject jsonObject = response.body();
                        Log.e("kim", jsonObject.toString());
                        //이미 존재하는 아이디
                        Toast.makeText(activity.getApplicationContext(), "이미 존재합니다.", Toast.LENGTH_LONG).show();
                        handler.sendEmptyMessage(0);
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        //존재하지 않는 아이디
                        Toast.makeText(activity.getApplicationContext(), "사용가능한 아이디 입니다.", Toast.LENGTH_LONG).show();
                        handler.sendEmptyMessage(1);
                    }
                });
            }
        });
        thread.start();

    }
}
