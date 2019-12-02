package com.kimbaro.plugin.util;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Set;
import java.util.UUID;

public class BT_connect {

    BluetoothSocket mSocket;
    InputStream mInputStream;
    OutputStream mOutputStream;
    Activity activity;
    BluetoothDevice mRemoteDevice;
    Set<BluetoothDevice> mDevices;
    BluetoothAdapter mBluetoothAdapter;
    Handler splash_handler;

    byte[] readBuffer;   //수신 데이터
    int readBufferPositon;  //버퍼 내 수신 문자 저장 위치
    Thread mWorkerThread;
    byte mDelimiter = 10;

    private BT_connect() {
    }

    //데이터 출력 테스트용
    TextView textView;

    public BT_connect(Activity activity, Set<BluetoothDevice> mDevices, BluetoothAdapter mBluetoothAdapter) {
        this.activity = activity;
        this.mDevices = mDevices;
        this.mBluetoothAdapter = mBluetoothAdapter;
    }

    public BT_connect(Activity activity, Set<BluetoothDevice> mDevices, BluetoothAdapter mBluetoothAdapter, Handler splash_handler) {
        this.activity = activity;
        this.mDevices = mDevices;
        this.mBluetoothAdapter = mBluetoothAdapter;
        this.splash_handler = splash_handler;

    }

    Handler mHandler;

    public void connectToSelectedDevice(final String selectedDeviceName) {

        //handler는 thread에서 던지는 메세지를 보고 다음 동작을 수행시킨다.
        mHandler = new Handler() {
            public void handleMessage(Message msg) {


                if (msg.what == 1) // 연결 성공
                {
                    try {
                        //블루투스 기기와의 소켓통신을 위한 커넥션 실행
                        mOutputStream = mSocket.getOutputStream();
                        mInputStream = mSocket.getInputStream();

                        // 데이터 수신 리스너
                        beginListenForData();

                        //===#
                        //연결 성공 시 액티비티 이동
                        splash_handler.sendEmptyMessage(1);
                        //===#
                    } catch (IOException e) {
                        Log.e("kim","=============================");
                        e.printStackTrace();
                    }
                } else {    //연결 실패
                    Toast.makeText(activity, "연결실패! 연결상태를 확인해주세요", Toast.LENGTH_SHORT).show();
                    //===#
                    splash_handler.sendEmptyMessage(-1);
                    //===#
                    try {
                        mSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        //연결과정을 수행할 thread 생성
        Thread thread = new Thread(new Runnable() {
            public void run() {
                //선택된 기기의 이름을 갖는 bluetooth device의 object
                mRemoteDevice = getDeviceFromBondedList(selectedDeviceName);
                UUID uuid = Device.DEVICE_UUID;
                Log.e("kim", mRemoteDevice.getBondState() + ":" + mRemoteDevice.getAddress() + ":" + mRemoteDevice.getName());
                try {
                    // 소켓 생성
                    // mSocket = mRemoteDevice.createRfcommSocketToServiceRecord(uuid);
                    mSocket = mRemoteDevice.createInsecureRfcommSocketToServiceRecord(uuid);

                    // RFCOMM 채널을 통한 연결, socket에 connect하는데 시간이 걸린다. 따라서 ui에 영향을 주지 않기 위해서는
                    // Thread로 연결 과정을 수행해야 한다.
                    mSocket.connect();
                    mHandler.sendEmptyMessage(1);
                } catch (Exception e) {
                    // 블루투스 연결 중 오류 발생
                    mHandler.sendEmptyMessage(-1);
                }
            }
        });
        //연결 thread를 수행한다
        thread.start();
    }
    //기기에 저장되어 있는 해당 이름을 갖는 블루투스 디바이스의 bluetoothdevice 객채를 출력하는 함수
//bluetoothdevice객채는 기기의 맥주소뿐만 아니라 다양한 정보를 저장하고 있다.

    BluetoothDevice getDeviceFromBondedList(String name) {
        BluetoothDevice selectedDevice = null;
        mDevices = mBluetoothAdapter.getBondedDevices();
        //pair 목록에서 해당 이름을 갖는 기기 검색, 찾으면 해당 device 출력
        for (BluetoothDevice device : mDevices) {
            if (name.equals(device.getName())) {
                selectedDevice = device;
                break;
            }
        }
        return selectedDevice;
    }


    //블루투스 수신 리스너
    protected void beginListenForData() {
        final Handler handler = new Handler();
        readBuffer = new byte[1024];
        readBufferPositon = 0;

        mWorkerThread = new Thread(new Runnable() {
            @Override
            public void run() {

                while (!Thread.currentThread().isInterrupted()) {
                    try {

                        int bytesAvailable = mInputStream.available();
                        if (bytesAvailable > 0) { //데이터가 수신된 경우
                            byte[] packetBytes = new byte[bytesAvailable];
                            mInputStream.read(packetBytes);
                            for (int i = 0; i < bytesAvailable; i++) {
                                byte b = packetBytes[i];
                                if (b == mDelimiter) {
                                    byte[] encodedBytes = new byte[readBufferPositon];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPositon = 0;
                                    handler.post(new Runnable() {
                                        public void run() {
                                            //===== 데이터 정상 수신 영역 =====
                                            Log.e("수신데이터 : ", data);
                                            Device.data = data;
                                        }
                                    });
                                } else {
                                    readBuffer[readBufferPositon++] = b;
                                }
                            }
                        }
                    } catch (UnsupportedEncodingException e) {
                        Log.e("2 주의!!! : ", "데이터 IO 도중 연결오류 발생!!");
                        e.printStackTrace();
                    } catch (IOException e) {
                        Log.e("3 주의!!! : ", "데이터 IO 도중 연결오류 발생!!");
                        e.printStackTrace();
                    }
                }
            }
        });
        //데이터 수신 thread 시작
        mWorkerThread.start();
    }
}
