package com.trco.kimbaro.db.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "user")
public class DB_Object_user {
    @Id
    String id;

    @Indexed
    String trcoId;
    @Indexed
    String trcoPw;
    @Indexed
    List info;
    @Indexed
    List user;

// info
//    @Indexed
//    String name;
//    @Indexed
//    String birth;
//    @Indexed
//    String tell;
//    @Indexed
//    String address;
//    @Indexed
//    String email;

    // user
//    @Indexed
//    String height;
//    @Indexed
//    String weight;
//    @Indexed
//    String sex;
    public DB_Object_user() {
    }

    public DB_Object_user(String trcoId, String trcoPw, List info_arr, List user_arr) {
        this.trcoId = trcoId;
        this.trcoPw = trcoPw;
        this.info = info_arr;
        this.user = user_arr;
    }

    public DB_Object_user(String id, String trcoId, String trcoPw, List info_arr, List user_arr) {
        this.id = id;
        this.trcoId = trcoId;
        this.trcoPw = trcoPw;
        this.info = info_arr;
        this.user = user_arr;
    }
}
