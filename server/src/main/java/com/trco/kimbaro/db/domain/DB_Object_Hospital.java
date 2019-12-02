package com.trco.kimbaro.db.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "hospital")
public class DB_Object_Hospital {

    @Id
    String id;

    @Indexed
    String area;
    @Indexed
    double latitude;
    @Indexed
    double longitude;
    @Indexed
    String division;
    @Indexed
    String name;
    @Indexed
    String address;
    @Indexed
    String tell;

    public DB_Object_Hospital() {
    }

    public DB_Object_Hospital(String area, double latitude, double longitude, String division, String name, String address, String tell) {
        this.area = area;
        this.latitude = latitude;
        this.longitude = longitude;
        this.division = division;
        this.name = name;
        this.address = address;
        this.tell = tell;
    }

}
