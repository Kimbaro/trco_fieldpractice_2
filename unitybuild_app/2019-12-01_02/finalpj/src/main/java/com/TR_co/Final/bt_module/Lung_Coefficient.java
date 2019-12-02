package com.TR_co.Final.bt_module;

import android.util.Log;

import java.util.ArrayList;

public class Lung_Coefficient {
    ArrayList<Double> P_Array = new ArrayList<Double>();

    ArrayList<Double> P_Array2 = new ArrayList<Double>();

    double firstP = 0.0D;
    //-----------
    int T1_ADC_P;

    int T1_ADC_T;

    long T1_Pres = 0L;

    int T1_Temp = 0;

    int T2_ADC_P;

    int T2_ADC_T;

    long T2_Pres = 0L;

    int T2_Temp = 0;

    int T_ADC_P;

    int T_ADC_T;

    int before_P = 0;

    int before_T = 0;
    //사용자 측정 값 END

    private void dataReset() { //측정 완료 후 사용된 변수 데이터 초기화
        P_Array = new ArrayList<Double>();

        P_Array2 = new ArrayList<Double>();

        T1_ADC_P = 0;

        T1_ADC_T = 0;

        T1_Pres = 0L;

        T1_Temp = 0;

        T2_ADC_P = 0;

        T2_ADC_T = 0;

        T2_Pres = 0L;

        T2_Temp = 0;

        T_ADC_P = 0;

        T_ADC_T = 0;

        before_P = 0;

        before_T = 0;
    }


    //기기통신을 위한 변수 리스트 START
    //SU1 set ---- START
    int st1;
    int st2;
    int st3;
    int t1;
    int t2;
    int t3;
    //END

    //SU2 set ----START
    int p1;
    int p2;
    int p3;
    short sp1;
    short sp2;
    short sp3;
    //END

    //SU3 set ----START
    int p4;
    int p5;
    int p6;
    short sp4;
    short sp5;
    short sp6;
    //END

    //SU4 set ----START
    int p7;
    int p8;
    int p9;
    short sp7;
    short sp8;
    short sp9;
    //END

    boolean seconedTestCheck = false;

    boolean seconedTestStartCheck = false;

    boolean testEndCheck = false;

    boolean testStartCheck = false;

    void dataGet(int paramInt1, int paramInt2, int paramInt3) {
        int j = paramInt1 / 8;
        int k = this.st1;
        paramInt1 = (j - k * 2) * this.st2 / 2048 + (paramInt1 / 16 - k) * (paramInt1 / 16 - k) / 4096 * this.st3 / 16384;
        j = (paramInt1 * 5 + 128) / 256;
        if (paramInt3 == 1) {
            this.T1_Temp = j;
        } else if (paramInt3 == 2) {
            this.T2_Temp = j;
        }
        paramInt1 = paramInt1 / 2 - 64000;
        j = paramInt1 / 4 * paramInt1 / 4 / 2048 * this.sp6;
        j = (j + this.sp5 * paramInt1 * 2) / 4;
        k = this.sp4;
        paramInt1 = ((this.sp3 * paramInt1 / 4 * paramInt1 / 4 / 8192 / 8 + this.sp2 * paramInt1 / 2) / 262144 + 32768) * this.sp1 / 32768;
        if (paramInt1 == 0) {
            Log.e("mtj_var = 0", "====stop====");
            return;
        }
        long l = (((1048576 - paramInt2) & 0xFFFFFFFFL) - ((j + k * 65536) / 4096)) * 3125L;
        if (l < 2147483648L) {
            l = 2L * l / (paramInt1 * -1);
        } else {
            l = 2L * l / (paramInt1 * -1);
        }
        paramInt1 = this.sp9 * (int) (l / 8L * l / 8L / 8192L) / 4096;
        paramInt2 = (int) (l / 4L) * this.sp8 / 8192;

        l = ((int) l + (paramInt1 + paramInt2 + this.sp7) / 16);
        if (paramInt3 == 1) {
            this.T1_Pres = l;
            return;
        }
        if (paramInt3 == 2) {
            this.T2_Pres = l;
            return;
        }
    } //--

    void dataArrayGet(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        paramInt3 = paramInt1 / 8;
        paramInt4 = this.st1;
        paramInt1 = (paramInt3 - paramInt4 * 2) * this.st2 / 2048 + (paramInt1 / 16 - paramInt4) * (paramInt1 / 16 - paramInt4) / 4096 * this.st3 / 16384;
        paramInt3 = (paramInt1 * 5 + 128) / 256;
        paramInt4 = paramInt1 / 2 - 64000;
        paramInt1 = (paramInt4 / 4 * paramInt4 / 4 / 2048 * this.sp6 + this.sp5 * paramInt4 * 2) / 4;
        paramInt3 = this.sp4;
        paramInt4 = ((this.sp3 * paramInt4 / 4 * paramInt4 / 4 / 8192 / 8 + this.sp2 * paramInt4 / 2) / 262144 + 32768) * this.sp1 / 32768;
        if (paramInt4 == 0)
            return;
        long l = (((1048576 - paramInt2) & 0xFFFFFFFFL) - ((paramInt1 + paramInt3 * 65536) / 4096)) * 3125L;
        if (l < 2147483648L) {
            l = 2L * l / (paramInt4 * -1);
        } else {
            l = 2L * l / (paramInt4 * -1);
        }
        paramInt1 = this.sp9 * (int) (l / 8L * l / 8L / 8192L) / 4096;
        paramInt2 = (int) (l / 4L) * this.sp8 / 8192;
        double d = ((int) l + (paramInt1 + paramInt2 + this.sp7) / 16) / 100.0D;
        // Log.e("ASDKIM", d + "");
        this.P_Array.add(Double.valueOf(d));
    }

    //초기값 설정 SU1,SU2,SU3,SU4
    public void device_start(String str, byte[] arrayOfByte) {
        Log.e("ASDKIM", "start device_start");
        if (str.startsWith("SU1")) {
            Log.e("ASDKIM", "SU1");
            t1 = (arrayOfByte[4] & 0xFF) * 16 * 16 + (arrayOfByte[5] & 0xFF);
            t2 = (arrayOfByte[6] & 0xFF) * 16 * 16 + (arrayOfByte[7] & 0xFF);
            t3 = (arrayOfByte[8] & 0xFF) * 16 * 16 + (arrayOfByte[9] & 0xFF);
            st1 = (short) ((arrayOfByte[4] & 0xFF) << 8 | (arrayOfByte[5] & 0xFF) << 0);
            st2 = (short) ((arrayOfByte[6] & 0xFF) << 8 | (arrayOfByte[7] & 0xFF) << 0);
            st3 = (short) ((arrayOfByte[8] & 0xFF) << 8 | (arrayOfByte[9] & 0xFF) << 0);
        } else if (str.startsWith("SU2")) {
            Log.e("ASDKIM", "SU2");
            p1 = (arrayOfByte[4] & 0xFF) * 16 * 16 + (arrayOfByte[5] & 0xFF);
            p2 = (arrayOfByte[6] & 0xFF) * 16 * 16 + (arrayOfByte[7] & 0xFF);
            p3 = (arrayOfByte[8] & 0xFF) * 16 * 16 + (arrayOfByte[9] & 0xFF);
            sp1 = (short) ((arrayOfByte[4] & 0xFF) << 8 | (arrayOfByte[5] & 0xFF) << 0);
            sp2 = (short) ((arrayOfByte[6] & 0xFF) << 8 | (arrayOfByte[7] & 0xFF) << 0);
            sp3 = (short) ((arrayOfByte[8] & 0xFF) << 8 | (arrayOfByte[9] & 0xFF) << 0);
        } else if (str.startsWith("SU3")) {
            Log.e("ASDKIM", "SU3");
            p4 = (arrayOfByte[4] & 0xFF) * 16 * 16 + (arrayOfByte[5] & 0xFF);
            p5 = (arrayOfByte[6] & 0xFF) * 16 * 16 + (arrayOfByte[7] & 0xFF);
            p6 = (arrayOfByte[8] & 0xFF) * 16 * 16 + (arrayOfByte[9] & 0xFF);
            sp4 = (short) ((arrayOfByte[4] & 0xFF) << 8 | (arrayOfByte[5] & 0xFF) << 0);
            sp5 = (short) ((arrayOfByte[6] & 0xFF) << 8 | (arrayOfByte[7] & 0xFF) << 0);
            sp6 = (short) ((arrayOfByte[8] & 0xFF) << 8 | (arrayOfByte[9] & 0xFF) << 0);
        } else if (str.startsWith("SU4")) {
            Log.e("ASDKIM", "SU4");
            p7 = (arrayOfByte[4] & 0xFF) * 16 * 16 + (arrayOfByte[5] & 0xFF);
            p8 = (arrayOfByte[6] & 0xFF) * 16 * 16 + (arrayOfByte[7] & 0xFF);
            p9 = (arrayOfByte[8] & 0xFF) * 16 * 16 + (arrayOfByte[9] & 0xFF);
            sp7 = (short) ((arrayOfByte[4] & 0xFF) << 8 | (arrayOfByte[5] & 0xFF) << 0);
            sp8 = (short) ((arrayOfByte[6] & 0xFF) << 8 | (arrayOfByte[7] & 0xFF) << 0);
            sp9 = (short) ((arrayOfByte[8] & 0xFF) << 8 | (arrayOfByte[9] & 0xFF) << 0);
        }

    }

    //호흡근 SU5
    public void device_muscle_check(String str, byte[] arrayOfByte, int test_kind) {
        Log.e("ASDKIM", "HELLO device_muscle_check method || " + test_kind);
        Log.e("ASDKIM", "SU5");
        if (test_kind == 1) { //흡기압
            Log.e("ASDKIM", "inhale");
            byte b = arrayOfByte[4];
            T1_ADC_T = (arrayOfByte[5] & 0xFF) << 16 | (b & 0xFF) << 24 | (arrayOfByte[6] & 0xFF) << 8 | (arrayOfByte[7] & 0xFF) << 0;
            T1_ADC_P = (arrayOfByte[8] & 0xFF) << 24 | (arrayOfByte[9] & 0xFF) << 16 | (arrayOfByte[10] & 0xFF) << 8 | (arrayOfByte[11] & 0xFF) << 0;
            dataGet(T1_ADC_T, T1_ADC_P, test_kind);
            Result_VO.mip = Float.parseFloat(String.format("%.2f", Double.valueOf(((float) T1_Pres / 100.0F) / 9.8D)));
            Log.e("mtj_data_SU5_result", Result_VO.mip + "");
        } else if (test_kind == 2) {//호기압
            Log.e("ASDKIM", "exhale");
            T2_ADC_T = (arrayOfByte[4] & 0xFF) << 24 | (arrayOfByte[5] & 0xFF) << 16 | (arrayOfByte[6] & 0xFF) << 8 | (arrayOfByte[7] & 0xFF) << 0;
            T2_ADC_P = (arrayOfByte[8] & 0xFF) << 24 | (arrayOfByte[9] & 0xFF) << 16 | (arrayOfByte[10] & 0xFF) << 8 | (arrayOfByte[11] & 0xFF) << 0;
            dataGet(T2_ADC_T, T2_ADC_P, test_kind);
            Result_VO.mep = Float.parseFloat(String.format("%.2f", Double.valueOf(((float) T2_Pres / 100.0F) / 9.8D)));
            Log.e("mtj_data_SU5_result", Result_VO.mep + "");
        }
        dataReset();
    }

    public void device_lung_check(String str, byte[] arrayOfByte) {
        Log.e("ASDKIM", "HELLO device_lung_check method");
        if (str.startsWith("SU6")) {
            Log.e("ASDKIM", "SU6");
            T_ADC_T = (arrayOfByte[4] & 0xFF) << 24 | (arrayOfByte[5] & 0xFF) << 16 | (arrayOfByte[6] & 0xFF) << 8 | (arrayOfByte[7] & 0xFF) << 0;
            T_ADC_P = (arrayOfByte[8] & 0xFF) << 24 | (arrayOfByte[9] & 0xFF) << 16 | (arrayOfByte[10] & 0xFF) << 8 | (arrayOfByte[11] & 0xFF) << 0;
            dataArrayGet(T_ADC_T, T_ADC_P, before_T, before_P);
            before_T = T_ADC_T;
            before_P = T_ADC_P;
        } else if (str.startsWith("SU9")) {
            Log.e("ASDKIM", "SU9");
            double d4 = 0.0D;
            double d5 = 0.0D;
            if (P_Array.size() == 0) {
                testStartCheck = false;
                seconedTestCheck = false;
                seconedTestStartCheck = false;
                testEndCheck = false;
                firstP = 0.0D;
                return;
            }
            boolean bool2 = false;
            boolean bool3 = false;
            boolean bool1 = false;
            int j = 0;
            boolean bool4 = false;
            int i = 0;
            int k = 1;
            double d3 = -9999.0D;
            double d2 = 0.0D;
            while (k < P_Array.size()) {
                double d = ((Double) P_Array.get(0)).doubleValue() - ((Double) P_Array.get(k)).doubleValue();
                if (!bool4) {
                    if (d >= 0.1D) {
                        if (d3 == -9999.0D) {
                            j++;
                        } else {
                            d2 = d2 + Math.abs(d3) + Math.abs(d);
                            bool4 = true;
                            d = -9999.0D;
                        }
                    } else {
                        d = -9999.0D;
                        j = 0;
                    }
                } else if (!bool2) {
                    d2 += ((Double) P_Array.get(k)).doubleValue();
                    if (d < 0.1D) {
                        if (d3 != -9999.0D) {
                            d = -9999.0D;
                            bool2 = true;
                        }
                        i++;
                    } else {
                        d = -9999.0D;
                    }
                } else if (!bool3) {
                    if (d <= -0.1D) {
                        if (d3 == -9999.0D) {
                            i++;
                        } else {
                            P_Array2.add(Double.valueOf(d3));
                            P_Array2.add(Double.valueOf(d));
                            d = -9999.0D;
                            bool3 = true;
                        }
                    } else {
                        d = -9999.0D;
                        i = 0;
                    }
                } else if (!bool1) {
                    P_Array2.add(P_Array.get(k));
                    if (d > -0.1D) {
                        if (d3 != -9999.0D) {
                            d = -9999.0D;
                            bool1 = true;
                        }
                        i++;
                    } else {
                        d = -9999.0D;
                    }
                } else {
                    d = d3;
                }
                k++;
                d3 = d;
            }
            i = 0;
            d3 = d5;
            double d1 = d4;
            while (i < P_Array2.size()) {
                if (i < 5) {
                    d3 += Math.abs(((Double) P_Array2.get(i)).doubleValue());
                    StringBuilder stringBuilder1 = new StringBuilder();
                    stringBuilder1.append("B10-");
                    stringBuilder1.append(d3);
                    Log.e("mtj_rp", stringBuilder1.toString());
                }
                d1 += Math.abs(((Double) P_Array2.get(i)).doubleValue());
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("totalB-");
                stringBuilder.append(d1);
                Log.e("mtj_rp", stringBuilder.toString());
                i++;
            }
            d4 = Result_VO.S_FVC;
            Result_VO.U_FVC = Float.parseFloat(String.format("%.2f", new Object[]{Double.valueOf((d2 + d1) * d4)}));

            //  d4 = Result_VO.S_FEV;
            //   Result_VO.U_FEV = Float.parseFloat(String.format("%.2f", new Object[]{Double.valueOf(d4 * d1)}));

            d4 = Result_VO.S_FEV1;
            Result_VO.U_FEV1 = Float.parseFloat(String.format("%.2f", new Object[]{Double.valueOf(d4 * d3)}));

            d2 = d1 / P_Array2.size() / (d2 + d1);
            d3 = Result_VO.S_FEV1_FVC;

            Result_VO.U_FEV1_FVC = Float.parseFloat(String.format("%.2f", (Result_VO.U_FEV1 / Result_VO.U_FVC) * 100));

            d1 /= P_Array2.size();
            d2 = Result_VO.S_PEF;
            Result_VO.U_PEF = Float.parseFloat(String.format("%.2f", new Object[]{Double.valueOf(d1 * d2)}));
            Result_VO.U_FEF25 = (Result_VO.U_PEF / 100) * 75;
            Result_VO.U_FEF50 = (Result_VO.U_PEF / 100) * 50;
            Result_VO.U_FEF75 = (Result_VO.U_PEF / 100) * 25;

            testStartCheck = false;
            seconedTestCheck = false;
            seconedTestStartCheck = false;
            testEndCheck = false;
            firstP = 0.0D;
            dataReset();
            return;
        }
    }

    public void setPred() {

    }

}