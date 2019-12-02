package com.TR_co.Final.bt_module;

import com.kimbaro.plugin.domain.DB_Object_user;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

//평균값 계산
public class Morris_VO {
    //Todo Morris 아시아인 퀀저식
    //https://books.google.co.kr/books?id=ABl8BwAAQBAJ&pg=PA117&lpg=PA117&dq=-4.241+*+((0.148+*+Height)+%2B+(0.025+*+Age)&source=bl&ots=sjmLwKqjoQ&sig=ACfU3U1HBQ2jx-v-Z-3XZFKtB4CUOnpCCg&hl=ko&sa=X&ved=2ahUKEwiP6abvn5HmAhU5K6YKHb5oCeQQ6AEwCnoECAsQAQ#v=onepage&q=0.092&f=false
    //FVC(남자) : [(0.148 * height(inches)) - (0.025 * age)] - 4.241
    //FVC(여자) : [(0.115 * height(inches)) - (0.024 * age)] - 2.852
    //FEV1(남자) : [(-1.260)-(0.032*age)]+[(0.092*height(inches))+(0.179)]
    //FEV1(여자) : [(-1.932)-(0.025*age)]+[(0.089*height(inches))+(0.179)]

    public void set_Morris_Asian() {
        try {
            int age = Integer.valueOf(getAge(DB_Object_user.data.get("birth").replaceAll("\"", "")));
            float weight = Float.valueOf(DB_Object_user.data.get("weight").replaceAll("\"", ""));
            float height = getInch(Float.valueOf(DB_Object_user.data.get("height").replaceAll("\"", "")));
            String sex = DB_Object_user.data.get("sex").replaceAll("\"", "");

            float MORRIS_FVC = 0.0f;
            float MORRIS_FEV1 = 0.0f;
            float MORRIS_FEV1_FVC = 0.0f;

            if (sex.equals("남")) {
                MORRIS_FVC = set_FVC_Male(age, height);
                MORRIS_FEV1 = set_FEV1_Male(age, height);
                MORRIS_FEV1_FVC = (MORRIS_FEV1 / MORRIS_FVC) * 100;
            } else if (sex.equals("여")) {
                MORRIS_FVC = set_FVC_Female(age, height);
                MORRIS_FEV1 = set_FEV1_Female(age, height);
                MORRIS_FEV1_FVC = (MORRIS_FEV1 / MORRIS_FVC) * 100;
            }

//            Log.e("Morris_VO", "(Male) FVC_Morris_value :: " + MORRIS_FVC + " FEV1_Morris_value :: " + MORRIS_FEV1 + " FEV1FVC_Morris_value :: " + MORRIS_FEV1_FVC);
//            Log.e("Morris_VO", age + " : " + weight + " : " + height + " : " + sex);
//            Log.e("Morris_VO", DB_Object_user.data.toString());

            Result_VO.M_FVC = MORRIS_FVC;
            Result_VO.M_FEV1 = MORRIS_FEV1;
            Result_VO.M_FEV1_FVC = MORRIS_FEV1_FVC;

            Result_VO.U_M_FVC = (Result_VO.U_FVC / Result_VO.M_FVC) * 100;
            Result_VO.U_M_FEV1 = (Result_VO.U_FEV1 / Result_VO.M_FEV1) * 100;
            Result_VO.U_M_FEV1_FVC = (Result_VO.U_FEV1_FVC / Result_VO.M_FEV1_FVC) * 100;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //아시안 모리스 식 계산 반환 START
    public float set_FVC_Male(int age, float height_inch) {
        float param1 = 0.148f * height_inch;
        float param2 = 0.025f * age;
        float param3 = (param1 - param2) - 4.241f;

        return param3;
    }

    public float set_FEV1_Male(int age, float height_inch) {
        float param1 = -1.260f - (0.032f * age);
        float param2 = (0.092f * height_inch) + 0.179f;
        float param3 = param1 + param2;

        return param3;
    }

    public float set_FVC_Female(int age, float height_inch) {
        float param1 = 0.115f * height_inch;
        float param2 = 0.024f * age;
        float param3 = (param1 - param2) - 2.852f;

        return param3;
    }

    public float set_FEV1_Female(int age, float height_inch) {
        float param1 = -1.932f - (0.025f * age);
        float param2 = (0.089f * height_inch) + 0.179f;
        float param3 = param1 + param2;

        return param3;
    }
    //아시안 모리스 식 계산 반환 END


    //ex 19950000  8자리 생년월일로 나이 산출
    private int getAge(String birthStr) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.KOREAN);
        Date birthDay = sdf.parse(birthStr);

        GregorianCalendar today = new GregorianCalendar();
        GregorianCalendar birth = new GregorianCalendar();
        birth.setTime(birthDay);

        int factor = 0;
        if (today.get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)) {
            factor = -1;
        }
        return today.get(Calendar.YEAR) - birth.get(Calendar.YEAR) + factor;
    }

    private float getInch(float inch) {
        return inch / 2.54f;
    }
}
