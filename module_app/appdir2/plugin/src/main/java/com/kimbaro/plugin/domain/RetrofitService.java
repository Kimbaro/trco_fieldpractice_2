package com.kimbaro.plugin.domain;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface RetrofitService {
    @POST("/trco_login")
    Call<JsonObject> trco_login(@QueryMap Map<String, String> option);

    @GET("/find_id")
    Call<JsonObject> find_id_user(@Query("id") String trcoId);

    @GET("/trco_create")
    Call<JsonObject> create_ac(@QueryMap Map<String, String> option);

    @GET("/trco_modify")
    Call<JsonObject> trco_modify(@QueryMap Map<String, String> option);

    @POST("/hospital_search")
    Call<JsonArray> hospital_search(@Query("latitude") double latitude, @Query("longitude") double longitude, @Query("distance") double dist);

    @GET("/create_func")
    Call<JsonObject> create_func(@QueryMap Map<String, String> option);

    @GET("/create_muscle")
    Call<JsonObject> create_muscle(@QueryMap Map<String, String> option);

}
