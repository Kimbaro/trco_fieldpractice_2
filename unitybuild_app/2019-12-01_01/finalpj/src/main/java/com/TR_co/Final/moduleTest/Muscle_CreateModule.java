package com.TR_co.Final.moduleTest;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.TR_co.Final.HistoryActivity;
import com.TR_co.Final.bt_module.Result_VO;
import com.TR_co.Final.unity_activity.Unity;
import com.google.gson.JsonObject;
import com.kimbaro.plugin.domain.DB_Object_user;
import com.kimbaro.plugin.domain.RetrofitService;
import com.kimbaro.plugin.util.ServerConfig;
import com.unity3d.player.UnityPlayer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Muscle_CreateModule {
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                UnityPlayer.UnitySendMessage("Main Camera", "SceneChangeToInhaleCheckScene_Android", "");
                Intent intent = new Intent(Unity.activity, HistoryActivity.class);
                Unity.activity.startActivity(intent);

            } else if (msg.what == 2) {
                UnityPlayer.UnitySendMessage("Main Camera", "SceneChangeToInhaleCheckScene_Android", "");
            }
        }
    };

    public void requestDataSave(int type) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(date.getTime());

        if (type == 1) {//호흡근력
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerConfig.IP).addConverterFactory(GsonConverterFactory.create()).build();
                    RetrofitService trco = retrofit.create(RetrofitService.class);

                    Map<String, String> data = new HashMap<>();
                    data.put("userId", DB_Object_user.data.get("id"));
                    data.put("time", date.getTime() + "");
                    data.put("type", type + "");

                    data.put("mep", String.format("%.2f", Result_VO.mep));
                    data.put("mip", String.format("%.2f", Result_VO.mip));


                    //폐기능 측정내역 전달
                    Call<JsonObject> call = trco.create_func(data);
                    call.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            if (t.getCause() == null) {
                                //rest 반환값이 없으므로 측정완료 시 null 값을 서버에서 전달함
                                mHandler.sendEmptyMessage(1);
                            } else {
                                //그외 통신 에러 발생 시
                                mHandler.sendEmptyMessage(2);
                                t.printStackTrace();
                            }
                        }
                    });
                }
            });
            thread.start();


        } else if (type == 0) {//폐기능
        }
    }
}
