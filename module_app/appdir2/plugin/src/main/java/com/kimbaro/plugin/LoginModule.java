package com.kimbaro.plugin;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.kimbaro.plugin.domain.DB_Object_user;
import com.kimbaro.plugin.domain.RetrofitService;
import com.kimbaro.plugin.util.Filtering;
import com.kimbaro.plugin.util.ServerConfig;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginModule {

    public String check = null;

    public String getCheck() {
        return check;
    }

    public void requestLogin(final String id, final String pw, final Activity activity) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    check = "true";
                } else if (msg.what == 1) {
                    check = "false";
                }
                DB_Object_user.data.get("weight");
            }
        };


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerConfig.IP).addConverterFactory(GsonConverterFactory.create()).build();
                RetrofitService trco = retrofit.create(RetrofitService.class);

                Map<String, String> data = new HashMap<>();
                data.put("id", id);
                data.put("pw", pw);
                //1차 회원 로그인. 및 성공 시 회원정보 불러오기.
                Call<JsonObject> call = trco.trco_login(data);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject jsonObject = response.body();

                        //로그인성공, 데이터 파싱
                        DB_Object_user.data.put("id", Filtering.data(jsonObject.get("id").toString()));
                        DB_Object_user.data.put("trcoId", Filtering.data(jsonObject.get("trcoId").toString()));
                        DB_Object_user.data.put("trcoPw", Filtering.data(jsonObject.get("trcoPw").toString()));

                        //각 정보는 MongoDB 참고할것
                        //info
                        DB_Object_user.data.put("name", Filtering.data(jsonObject.getAsJsonArray("info").get(0).toString()));
                        DB_Object_user.data.put("birth", Filtering.data(jsonObject.getAsJsonArray("info").get(1).toString()));
                        DB_Object_user.data.put("tell", Filtering.data(jsonObject.getAsJsonArray("info").get(2).toString()));
                        DB_Object_user.data.put("address", Filtering.data(jsonObject.getAsJsonArray("info").get(3).toString()));
                        DB_Object_user.data.put("email", Filtering.data(jsonObject.getAsJsonArray("info").get(4).toString()));

                        //user
                        DB_Object_user.data.put("height", Filtering.data(jsonObject.getAsJsonArray("user").get(0).toString()));
                        DB_Object_user.data.put("weight", Filtering.data(jsonObject.getAsJsonArray("user").get(1).toString()));
                        DB_Object_user.data.put("sex", Filtering.data(jsonObject.getAsJsonArray("user").get(2).toString()));

                        String age = Filtering.data(DB_Object_user.data.get("birth"));
                        String age_result = null;
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.KOREAN);
                            Date birthDay = null;
                            birthDay = sdf.parse(age);

                            GregorianCalendar today = new GregorianCalendar();
                            GregorianCalendar birth = new GregorianCalendar();
                            birth.setTime(birthDay);

                            int factor = 0;
                            if (today.get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)) {
                                factor = -1;
                            }
                            age_result = (today.get(Calendar.YEAR) - birth.get(Calendar.YEAR) + factor) + "";
                            Toast.makeText(activity.getApplicationContext(), "#" + age_result, Toast.LENGTH_SHORT).show();
                            DB_Object_user.data.put("age", age_result);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Log.e("kim", DB_Object_user.data.toString());
                        Toast.makeText(activity.getApplicationContext(), "환영합니다!", Toast.LENGTH_SHORT).show();
                        handler.sendEmptyMessage(0);
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        //데이터가 디비로 부터 반환되지 않았거나 에러인경우
                        Toast.makeText(activity.getApplicationContext(), "잘못된 회원정보 입니다.", Toast.LENGTH_LONG).show();
                        Log.e("Kim", "에러입니다.");
                        handler.sendEmptyMessage(1);
                    }
                });
            }
        });
        thread.start();

    }
}


