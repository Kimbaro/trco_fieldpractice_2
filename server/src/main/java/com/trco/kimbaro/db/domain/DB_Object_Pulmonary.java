package com.trco.kimbaro.db.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashMap;

@Data
@Document(collection = "pulmonary_function")
public class DB_Object_Pulmonary {

    @Id
    String id;

    @Indexed
    String userId;
    @Indexed
    Date time;
    @Indexed
    Integer type;

    @Indexed
    Double fvc_meas;
    @Indexed
    Double fvc_pred;
    @Indexed
    Double fvc_pred_avg;

    @Indexed
    Double fev_meas;
    @Indexed
    Double fev_pred;
    @Indexed
    Double fev_pred_avg;

    @Indexed
    Double fevfvc_meas;
    @Indexed
    Double fevfvc_pred;
    @Indexed
    Double fevfvc_pred_avg;

    @Indexed
    Double fef25_meas;
//    @Indexed
//    Double fef25_pred;
//    @Indexed
//    Double fef25_pred_avg;

    @Indexed
    Double fef50_meas;
//    @Indexed
//    Double fef50_pred;
//    @Indexed
//    Double fef50_pred_avg;

    @Indexed
    Double fef75_meas;
//    @Indexed
//    Double fef75_pred;
//    @Indexed
//    Double fef75_pred_avg;

    @Indexed
    Double mip;
    Double mep;

    public DB_Object_Pulmonary() {
    }

    public DB_Object_Pulmonary(HashMap<String, String> data, int type) {

        setUserId(data.get("userId"));
        setTime(new Date(Long.parseLong(data.get("time"))));
        setType(Integer.parseInt(data.get("type")));

        if (type == 0) {
            setFvc_meas(Double.parseDouble(data.get("fvc_meas")));
            setFvc_pred(Double.parseDouble(data.get("fvc_pred")));
            setFvc_pred_avg(Double.parseDouble(data.get("fvc_pred_avg")));

            setFev_meas(Double.parseDouble(data.get("fev_meas")));
            setFev_pred(Double.parseDouble(data.get("fev_pred")));
            setFev_pred_avg(Double.parseDouble(data.get("fev_pred_avg")));

            setFevfvc_meas(Double.parseDouble(data.get("fevfvc_meas")));
            setFevfvc_pred(Double.parseDouble(data.get("fevfvc_pred")));
            setFevfvc_pred_avg(Double.parseDouble(data.get("fevfvc_pred_avg")));

            setFef25_meas(Double.parseDouble(data.get("fef25_meas")));
//            setFef25_pred(Double.parseDouble(data.get("fef25_pred")));
//            setFef25_pred_avg(Double.parseDouble(data.get("fef25_pred_avg")));

            setFef50_meas(Double.parseDouble(data.get("fef50_meas")));
//            setFef50_pred(Double.parseDouble(data.get("fef50_pred")));
//            setFef50_pred_avg(Double.parseDouble(data.get("fef50_pred_avg")));

            setFef75_meas(Double.parseDouble(data.get("fef75_meas")));
//            setFef75_pred(Double.parseDouble(data.get("fef75_pred")));
//            setFef75_pred_avg(Double.parseDouble(data.get("fef75_pred_avg")));
        } else if (type == 1) {
            setMep(Double.parseDouble(data.get("mep")));
            setMip(Double.parseDouble(data.get("mip")));
        }
    }
}
