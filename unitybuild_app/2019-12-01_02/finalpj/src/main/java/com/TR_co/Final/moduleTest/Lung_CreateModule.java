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

public class Lung_CreateModule {
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                //측정 완료 시 유니티플러그인은 메인으로 , 안드로이드 액티비티는 HistoryActivity이동
                UnityPlayer.UnitySendMessage("Main Camera", "SceneChangeToLungCheckScene_Android", "");
                Intent intent = new Intent(Unity.activity, HistoryActivity.class);
                Unity.activity.startActivity(intent);

            } else if (msg.what == 2) {
                UnityPlayer.UnitySendMessage("Main Camera", "SceneChangeToLungCheckScene_Android", "");
            }
        }
    };

    public void requestDataSave(int type) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(date.getTime());

        if (type == 0) {//폐기능,
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerConfig.IP).addConverterFactory(GsonConverterFactory.create()).build();
                    RetrofitService trco = retrofit.create(RetrofitService.class);

                    Map<String, String> data = new HashMap<>();
                    data.put("userId", DB_Object_user.data.get("id"));
                    data.put("time", date.getTime() + "");
                    data.put("type", type + "");

                    data.put("fvc_meas", String.format("%.2f", Result_VO.U_FVC));
                    data.put("fvc_pred", String.format("%.2f", Result_VO.M_FVC));
                    data.put("fvc_pred_avg", String.format("%.2f", Result_VO.U_M_FVC));

                    data.put("fev_meas", String.format("%.2f", Result_VO.U_FEV1));
                    data.put("fev_pred", String.format("%.2f", Result_VO.M_FEV1));
                    data.put("fev_pred_avg", String.format("%.2f", Result_VO.U_M_FEV1));

                    data.put("fevfvc_meas", String.format("%.2f", Result_VO.U_FEV1_FVC));
                    data.put("fevfvc_pred", String.format("%.2f", Result_VO.M_FEV1_FVC));
                    data.put("fevfvc_pred_avg", String.format("%.2f", Result_VO.U_M_FEV1_FVC));

                    data.put("fef25_meas", String.format("%.2f", Result_VO.U_FEF25));
                    data.put("fef50_meas", String.format("%.2f", Result_VO.U_FEF50));
                    data.put("fef75_meas", String.format("%.2f", Result_VO.U_FEF75));

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


        } else if (type == 1) {//호흡근력
        }
    }
}
