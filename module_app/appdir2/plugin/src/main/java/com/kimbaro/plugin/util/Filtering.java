package com.kimbaro.plugin.util;

public class Filtering {

    //유니티 파트 json 문자열 생성 과정에서 유저데이터 string값에 " "이 같이 붙어옴.
    public static String data(String str) {

        String filter_str = "";
        for (int x = 0; x < str.length(); x++) {
            if (str.charAt(x) != '"') {
                filter_str += str.charAt(x);
            }
        }

        return filter_str;
    }
}
