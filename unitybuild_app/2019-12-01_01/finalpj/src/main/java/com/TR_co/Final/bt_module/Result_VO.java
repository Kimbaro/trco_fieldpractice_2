package com.TR_co.Final.bt_module;

public class Result_VO {
    //측정값-호흡근
    public static float mep;
    public static float mip;

    //사용자측정값-폐기능
    public static float U_FVC;
    public static float U_FEV1;
    public static float U_FEV1_FVC;
    public static float U_PEF;
    public static float U_FEF25;
    public static float U_FEF50;
    public static float U_FEF75;

    //모리스계산값-폐기능
    public static float M_FVC;
    public static float M_FEV1;
    public static float M_FEV1_FVC;

    //사용자-모리스 퍼센트값
    public static float U_M_FVC;
    public static float U_M_FEV1;
    public static float U_M_FEV1_FVC;

    //계수값
    public static final float S_FVC = 0.0003f;
    public static final float S_FEV = 0.0003f;
    public static final float S_FEV1 = 0.00112f;
    public static final float S_FEV1_FVC = 15.0f;
    public static final float S_PEF = 0.0082f;

}
