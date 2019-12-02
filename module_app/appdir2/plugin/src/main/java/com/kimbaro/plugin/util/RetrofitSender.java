package com.kimbaro.plugin.util;

import com.kimbaro.plugin.domain.RetrofitService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitSender {
    public static final String BASE_URL = ServerConfig.IP;
    private static Retrofit retrofit = null;
    private static RetrofitService endPoints = null;


    public static RetrofitService getEndPoint() {
        if (endPoints == null) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                    .build();
            endPoints = retrofit.create(RetrofitService.class);
        }
        return endPoints;
    }
}
