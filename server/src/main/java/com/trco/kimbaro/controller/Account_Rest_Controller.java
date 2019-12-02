package com.trco.kimbaro.controller;

import com.trco.kimbaro.db.domain.DB_Object_user;
import com.trco.kimbaro.db.repo.DB_repo_user;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import reactor.core.CoreSubscriber;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import javax.xml.ws.soap.AddressingFeature;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

@RestController
@CrossOrigin("http://localhost:63342")
//계정 정보 관련 REST API
public class Account_Rest_Controller {
    @Autowired
    @Qualifier("user")
    DB_repo_user db_repo_user;

    //기존계정여부확인 => 중복여부는 안드로이드에서 반환값 처리
    @GetMapping("/find_id")
    public Mono<DB_Object_user> find_id_user(@RequestParam("id") String trcoId) {
        //System.out.println(Mono.just(null).single());

        return db_repo_user.findByTrcoId(trcoId);
        // return db_repo_user.findByTrcoId(trcoId);
    }

    // switchIfEmpty(async_find_id_check())
    Mono<String> async_find_id_check() {
        return Mono.fromFuture(CompletableFuture.supplyAsync(() -> {
            return "true";
        }));
    }

    //계정인증확인 => 인증여부는 안드로이드에서 반환값 처리
    @PostMapping("/trco_login")
    public Mono<DB_Object_user> trco_login(@RequestParam("id") String trcoId, @RequestParam("pw") String trcoPw) {
        System.out.println("trco_login 접근" + trcoId + trcoPw);
        db_repo_user.findByTrcoIdAndTrcoPw(trcoId, trcoPw).subscribe(db_object_user -> {
            System.out.println(db_object_user.getId());
            System.out.println(db_object_user.getTrcoId());
            System.out.println(db_object_user.getTrcoPw());
            System.out.println(db_object_user.getUser().toString());
            System.out.println(db_object_user.getInfo().toString());
        });
        return db_repo_user.findByTrcoIdAndTrcoPw(trcoId, trcoPw);
    }

    //계정생성
    @GetMapping("/trco_create")
    public Mono<DB_Object_user> create_ac(@RequestParam HashMap<String, String> data) {
        //System.out.println(Mono.just(null).single());
        System.out.println("response of /trco_create" + data.toString());
        return Mono.just(new DB_Object_user(data.get("trcoId"), data.get("trcoPw"),
                Arrays.asList(data.get("name"), data.get("birth"), data.get("tell"), data.get("address"), data.get("email")),
                Arrays.asList(data.get("height"), data.get("weight"), data.get("sex"))))
                .flatMap(db_repo_user::save);
    }

    @GetMapping("/trco_modify")
    public Mono<DB_Object_user> trco_modify(@RequestParam HashMap<String, String> data) {
        System.out.println("response of /trco_modify" + data.toString());

        db_repo_user.findById(data.get("id")).flatMap(db_object_user -> {
            String[] info = {data.get("name"), data.get("birth"), data.get("tell"), data.get("address"), data.get("email")};
            String[] user = {data.get("height"), data.get("weight"), data.get("sex")};
            db_object_user.setId(data.get("id"));
            db_object_user.setTrcoId(data.get("trcoId"));
            db_object_user.setTrcoPw(data.get("trcoPw"));
            db_object_user.setInfo(Arrays.asList(info));
            db_object_user.setUser(Arrays.asList(user));

            return db_repo_user.save(db_object_user);
        }).subscribe(System.out::println);

//        db_repo_user.findById(data.get("id")).subscribe(db_object_user -> {
//            String[] info = {data.get("name"), data.get("birth"), data.get("tell"), data.get("address"), data.get("email")};
//            String[] user = {data.get("height"), data.get("weight"), data.get("sex")};
//            db_object_user.setTrcoId(data.get("trcoId"));
//            db_object_user.setTrcoPw(data.get("trcoPw"));
//            db_object_user.setInfo(Arrays.asList(info));
//            db_object_user.setUser(Arrays.asList(user));
//        });

        return db_repo_user.findById(data.get("id"));
    }

//    @GetMapping("/trco_create_unity")
//    public String create_ac_unity(@RequestParam("sign") String json) {
//        System.out.println("response of /trco_create_unity");
//        System.out.println(json);
//        return "response of /trco_create_unity";
//    }
//
//    @GetMapping("/trco_check_unity")
//    public String check_id_unity(@RequestParam("idcheck") String json) {
//        System.out.println("response of /trco_check_unity");
//        System.out.println(json);
//        return "response of /trco_check_unity";
//    }
}
