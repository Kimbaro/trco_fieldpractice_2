package com.kimbaro.plugin.domain;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DB_Object_user implements Serializable {
    public String id = new String();

    public String trcoId = new String();

    public String trcoPw = new String();

    public List info;

    public List user;

    public static HashMap<String, String> data = new HashMap<String, String>();

    public String getData(String key) {
     return data.get(key).toString();
    }
}
