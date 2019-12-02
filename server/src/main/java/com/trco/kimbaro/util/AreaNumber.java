package com.trco.kimbaro.util;

import org.springframework.stereotype.Component;

@Component
public class AreaNumber {
    public String getAreaName(int areaNumber) {
        switch (areaNumber) {
            case 0:
                return "전국";
            case 10:
                return "서울";
            case 20:
                return "강원";
            case 30:
                return "대전";
            case 31:
                return "충남";
            case 33:
                return "세종";
            case 36:
                return "충북";
            case 40:
                return "인천";
            case 41:
                return "경기";
            case 50:
                return "광주";
            case 51:
                return "전남";
            case 56:
                return "전북";
            case 60:
                return "부산";
            case 62:
                return "경남";
            case 68:
                return "울산";
            case 69:
                return "제주";
            case 70:
                return "대구";
            case 71:
                return "경복";
            default:
                return "기타";
        }
    }
}
