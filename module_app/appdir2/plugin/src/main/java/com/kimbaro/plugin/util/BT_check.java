package com.kimbaro.plugin.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BT_check {
    private BluetoothAdapter mBluetoothAdapter = null;
    private Activity activity = null;
    private TextView textView;
    private static final int REQUEST_ENABLE_BT = 3;
    private Handler splash_handler;

    private BT_check() {
    }

    public BT_check(Activity activity) {
        this.activity = activity;
    }
//
//    public BT_check(Activity activity, Handler splash_handler) {
//        this.activity = activity;
//        this.splash_handler = splash_handler;
//    }


    public void checkBluetooth(Handler splash_handler) {
        this.splash_handler = splash_handler;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // 장치가 블루투스 지원하지 않는 경우
            Toast.makeText(activity.getApplicationContext(), "해당 모바일은 블루투스를 지원하지 않음", Toast.LENGTH_SHORT).show();
        } else {
            // 장치가 블루투스 지원하는 경우
            if (!mBluetoothAdapter.isEnabled()) {
                // 블루투스를 지원하지만 비활성 상태인 경우
                // 블루투스를 활성 상태로 바꾸기 위해 사용자 동의 요첨
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else {
                // 블루투스를 지원하며 활성 상태인 경우
                // 페어링된 기기 목록을 보여주고 연결할 장치를 선택.
                selectDevice();
            }
        }
    }


    Set<BluetoothDevice> mDevices;
    int mPairedDeviceCount;

    private void selectDevice() {
        //페어링되었던 기기 목록 획득
        mDevices = mBluetoothAdapter.getBondedDevices();
        //페어링되었던 기기 갯수
        mPairedDeviceCount = mDevices.size();
        //Alertdialog 생성(activity에는 context입력)
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //AlertDialog 제목 설정
        builder.setTitle("등록된 디바이스");


        // 페어링 된 블루투스 장치의 이름 목록 작성
        final List<String> listItems = new ArrayList<String>();
        for (BluetoothDevice device : mDevices) {

            //임의의 디바이스만 조회하게 하려면 해당 코드사용
            //if (Device.DEVICE_NAME.equals(device.getName())) {
            //listItems.add(device.getName());
            //}

            //전체 다바이스 조회
            listItems.add(device.getName());
        }
        if (listItems.size() == 0) {
            //no bonded device => searching
            Toast.makeText(activity, "등록된 디바이스가 없습니다. 페어링이 필요합니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
            activity.startActivityForResult(intent, 0);
        } else {
            Toast.makeText(activity, "등록된 디바이스 목록을 불러왔습니다.", Toast.LENGTH_SHORT).show();
            // 취소 항목 추가
            listItems.add("# 블루투스 장치등록");
            listItems.add("# 앱 종료");


            final CharSequence[] items = listItems.toArray(new CharSequence[listItems.size()]);
            builder.setItems(items, new DialogInterface.OnClickListener() {
                // 리스트 아이템 클릭 이벤트
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Dialog dialog_ = (Dialog) dialog;
                    // 연결할 장치를 선택하지 않고 '닫기'를 누른 경우
                    if (which == listItems.size() - 1) {
                        Toast.makeText(dialog_.getContext(), "앱 종료", Toast.LENGTH_SHORT).show();
                        activity.finishAffinity();
                        System.runFinalization();
                        System.exit(0);
                    } else if (which == listItems.size() - 2) {
                        Toast.makeText(dialog_.getContext(), "블루투스 설정", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                        activity.startActivityForResult(intent, 0);
                    } else {
                        //취소가 아닌 디바이스를 선택한 경우 해당 영역 실행
                        Toast.makeText(dialog_.getContext(), "선택 장치명 : " + items[which].toString(), Toast.LENGTH_SHORT).show();
                        setDeviceName(items[which].toString());

                        //타겟 액티비티 ,블루투스 검색 목록 , 검증된 블루투스 정보, 받아온 값 입력대상
                        BT_connect bt_connect
                                = new BT_connect(activity, getMDevices(), getMBluetoothAdapter(), splash_handler);

                        //본인이 선택한 블루투스이름
                        bt_connect.connectToSelectedDevice(getDeviceName());
                    }
                }
            });

            builder.setCancelable(false);    // 뒤로 가기 버튼 사용 금지
            AlertDialog alert = builder.create();
            alert.show();   //alert 시작
        }
    }

    public Set<BluetoothDevice> getMDevices() {
        return mDevices;
    }

    public BluetoothAdapter getMBluetoothAdapter() {
        return mBluetoothAdapter;
    }

    private String deviceName = null;

    private void setDeviceName(String name) {
        deviceName = name;
    }

    public String getDeviceName() {
        return deviceName;
    }
}
