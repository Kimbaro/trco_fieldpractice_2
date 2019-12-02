package com.trco.kimbaro.util;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("distance")
public class Distance {

    /**
     * 두 지점간의 거리 계산
     *
     * @param lat1     지점 1 위도
     * @param lon1     지점 1 경도
     * @param lat2     지점 2 위도
     * @param lon2     지점 2 경도
     * @param unit     거리 표출단위
     * @param distance 두 점간 거리 km 단위
     */

    private double theta;
    private double dist;
    public boolean distance(double lat1, double lon1, double lat2, double lon2, double distance) {

        theta = lon1 - lon2;
        dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;
//        if (unit == "kilometer") {
//            dist = dist * 1.609344;
//        } else if (unit == "meter") {
//            dist = dist * 1609.344;
//        }

        if (dist <= distance) {
            return true;
        }else{
            return false;
        }
    }

    // This function converts decimal degrees to radians
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    private double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}
