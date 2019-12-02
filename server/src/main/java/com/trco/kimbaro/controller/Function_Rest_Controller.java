package com.trco.kimbaro.controller;

import com.trco.kimbaro.db.domain.DB_Object_Pulmonary;
import com.trco.kimbaro.db.repo.DB_repo_pulmonary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@RestController
@CrossOrigin("*")
//기능 관련 반환
public class Function_Rest_Controller {
    @Autowired
    @Qualifier("pulmonary")
    DB_repo_pulmonary db_repo_pulmonary;

    //호흡근력내역생성
    @GetMapping("/create_muscle")
    public void create_muscle(@RequestParam HashMap<String, String> data) {
        System.out.println("response of /create_muscle");
        Mono.just(new DB_Object_Pulmonary(data, Integer.parseInt(data.get("type"))))
                .flatMap(db_repo_pulmonary::save).subscribe(System.out::println);
    }

    //폐기능측정내역생성
    @GetMapping("/create_func")
    public void create_func(@RequestParam HashMap<String, String> data) {
        // System.out.println("response of /create_func    values ::" + data.toString());
        Mono.just(new DB_Object_Pulmonary(data, Integer.parseInt(data.get("type"))))
                .flatMap(db_repo_pulmonary::save).subscribe(System.out::println);
    }

    //검사내역조회 id
    //테스트용 파라미터 값 : 4b866f08234ae01d21d89604
    @GetMapping("/find_history")
    public Flux<DB_Object_Pulmonary> find_pulmonary(@RequestParam("id") String userId) {
        System.out.println(userId);
//        Flux<>
//        return db_repo_pulmonary.findByUserId(userId).subscribe(db_object_pulmonary -> {
//
//        });
        return db_repo_pulmonary.findByUserId(userId);
    }

    //검사내역조회 id, type
    @GetMapping("/find_pulmonary")
    public Flux<DB_Object_Pulmonary> find_pulmonary(@RequestParam("id") String userId, @RequestParam("type") int type) {
//        Flux<>
//        return db_repo_pulmonary.findByUserId(userId).subscribe(db_object_pulmonary -> {
//
//        });
        return db_repo_pulmonary.findByUserIdAndType(userId, type);
    }
}
