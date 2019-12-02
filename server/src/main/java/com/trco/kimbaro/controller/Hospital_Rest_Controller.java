package com.trco.kimbaro.controller;

import com.trco.kimbaro.db.domain.DB_Object_Hospital;
import com.trco.kimbaro.db.repo.DB_repo_hospital;
import com.trco.kimbaro.util.AreaNumber;
import com.trco.kimbaro.util.Distance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.HashMap;

@RestController
@CrossOrigin("*")
public class Hospital_Rest_Controller {

    @Autowired
    @Qualifier("hospital")
    DB_repo_hospital db_repo_hospital;

    @Autowired
    @Qualifier("distance")
    Distance distance;

    @Autowired
    AreaNumber areaNumber;


    //?area=30&latitude=36.339929&longitude=127.448820&distance=100.0
    /*
     * data param
     * area=30  #지역코드 util.AreaNumber.class 참고
     * latitude=36.339929    #자기위치 위도
     * longitude=127.448820  #자기위치 경도
     * distance=100.0    #거리
     * */
    @PostMapping("/hospital_search")
    public Flux<DB_Object_Hospital> hospital_search(@RequestParam("latitude") double latitude, @RequestParam("longitude") double longitude, @RequestParam("distance") double dist) {
        System.out.println("response of /hospital_search " + latitude + longitude + dist);

        //# 나중에 area(지역코드)로 병원위치 구분하여 받아올것
        // int area = Integer.valueOf(data.get("area"));
//        double lat = Double.valueOf(data.get("latitude"));
//        double lon = Double.valueOf(data.get("longitude"));
//        double dist = Double.valueOf(data.get("distance")); //검색반경 해당 값 이하의 데이터만 추출
        return db_repo_hospital.findAll().filter(db_object_hospital -> distance.distance(latitude, longitude, db_object_hospital.getLatitude(), db_object_hospital.getLongitude(), dist));
    }
}
