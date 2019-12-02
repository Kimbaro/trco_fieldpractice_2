package com.kimbaro.plugin;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kimbaro.plugin.domain.Parameters_Of_Signup;
import com.kimbaro.plugin.domain.RetrofitService;
import com.kimbaro.plugin.util.Filtering;
import com.kimbaro.plugin.util.ServerConfig;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateModule {
    public boolean check = false;

    public boolean getCheck() {
        return check;
    }

    public void requestCreate(String jsonData) {
        Log.e("kim", jsonData);
        Gson gson = new Gson();
        Parameters_Of_Signup parameters = gson.fromJson(jsonData, Parameters_Of_Signup.class);

        final HashMap<String, String> option = new HashMap<>();
        option.put("trcoId", Filtering.data(parameters.getId()));
        option.put("trcoPw", Filtering.data(parameters.getPwd()));
        option.put("name", Filtering.data(parameters.getUsername()));
        option.put("birth", Filtering.data(parameters.getBirthday()));
        option.put("height", Filtering.data(parameters.getHeight()));
        option.put("weight", Filtering.data(parameters.getWeight()));
        option.put("tell", Filtering.data(parameters.getPhonenum()));
        option.put("address", Filtering.data(parameters.getAddress()));
        option.put("email", Filtering.data(parameters.getEmail()));
        option.put("sex", Filtering.data(parameters.getGender()));


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
                Call<JsonObject> call = trco.create_ac(option);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject jsonObject = response.body();
                        Log.e("kim", jsonObject.toString());
                        //계정생성 성공
                        //Toast.makeText(activity.getApplicationContext(), "이미 존재합니다.", Toast.LENGTH_LONG).show();
                        handler.sendEmptyMessage(1);
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        //계정생성 실패
                        //Toast.makeText(activity.getApplicationContext(), "사용가능한 아이디 입니다.", Toast.LENGTH_LONG).show();
                        handler.sendEmptyMessage(0);
                    }
                });
            }
        });
        thread.start();
    }
}
