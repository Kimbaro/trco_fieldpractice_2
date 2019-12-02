/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.TR_co.Final.bt_module;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.List;
import java.util.UUID;

/**
 * Service for managing connection and data communication with a GATT server hosted on a
 * given Bluetooth LE device.
 */
public class BluetoothLeService extends Service {
    private final static String TAG = BluetoothLeService.class.getSimpleName();

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private int mConnectionState = STATE_DISCONNECTED;

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";

    public final static UUID UUID_HEART_RATE_MEASUREMENT =
            UUID.fromString(SampleGattAttributes.lookup("UUID_HEART_RATE_MEASUREMENT"));


    // Implements callback methods for GATT events that the app cares about.  For example,
    // connection change and services discovered.
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = STATE_CONNECTED;
                broadcastUpdate(intentAction);
                Log.i(TAG, "Connected to GATT server.");
                // Attempts to discover services after successful connection.
                Log.i(TAG, "Attempting to start service discovery:" +
                        mBluetoothGatt.discoverServices());

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                intentAction = ACTION_GATT_DISCONNECTED;
                mConnectionState = STATE_DISCONNECTED;
                Log.i(TAG, "Disconnected from GATT server.");
                broadcastUpdate(intentAction);
            }
        }

        Handler mHandler = new Handler();

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.e("kim", "alert ::::::::::::: BluetoothGatt.GATT_SUCCESS");
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);

                for (BluetoothGattService bluetoothGattService : gatt.getServices()) {
                    if (bluetoothGattService.getUuid().equals(UUID.fromString(SampleGattAttributes.lookup("SERV_UUID")))) {
                        Log.e("kim", "alert ::::::::::::: BluetoothGatt SERV_UUID find : " + bluetoothGattService.getUuid());
                        //SERV_UUID가 제공하는 Characteristic(특성)을 불러옴
                        for (final BluetoothGattCharacteristic bluetoothGattCharacteristic : bluetoothGattService.getCharacteristics()) {
                            //SERV_UUID의 Characteristic 목록중 NOTI_UUID 항목을 찾음
                            if (bluetoothGattCharacteristic.getUuid().equals(UUID.fromString(SampleGattAttributes.lookup("NOTI_UUID")))) {
                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        StringBuilder stringBuilder2 = new StringBuilder();
                                        stringBuilder2.append("====gatt set noti====");
                                        stringBuilder2.append(mBluetoothGatt.setCharacteristicNotification(bluetoothGattCharacteristic, true));
                                        Log.e("mtj-noti", stringBuilder2.toString());

                                        BluetoothGattDescriptor bluetoothGattDescriptor = bluetoothGattCharacteristic.getDescriptor(UUID.fromString(SampleGattAttributes.lookup("CLIENT_CHAR_CONFIG")));
                                        StringBuilder stringBuilder3 = new StringBuilder();
                                        stringBuilder3.append("====descriptor on====");
                                        stringBuilder3.append(bluetoothGattDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE));
                                        Log.e("mtj-noti", stringBuilder3.toString());
                                        boolean bool = mBluetoothGatt.writeDescriptor(bluetoothGattDescriptor);
                                        StringBuilder stringBuilder1 = new StringBuilder();
                                        stringBuilder1.append("notiCheck-");
                                        stringBuilder1.append(bool);
                                        Log.e("mtj_service", stringBuilder1.toString());
                                        broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
                                    }
                                }, 1000L);
                            }
                            //SERV_UUID의 Characteristic 목록중 WRITE_UUID 항목을 찾음
                            if (bluetoothGattCharacteristic.getUuid().equals(UUID.fromString(SampleGattAttributes.lookup("WRITE_UUID")))) {
                                mHandler.postDelayed(new Runnable() {
                                    public void run() {
                                        //기기에 센서명 SA1E 전달 (값 반환 시 실행하는 거 같음)
                                        start("SA1E");
                                    }
                                }, 2000L);
                            }
                        }
                    }
                }

            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        byte[] bufferTemp;
        int countCheck = 0;
        boolean sendCheck = false;

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            Log.v("mtj-new", "notiRecive");
            StringBuilder stringBuilder1 = new StringBuilder();
            stringBuilder1.append("UUID - ");
            stringBuilder1.append(characteristic.getUuid());
            Log.e("mtj", stringBuilder1.toString() + " : " + characteristic.getValue());
            byte[] arrayOfByte = characteristic.getValue();
            String str = new String(arrayOfByte, 0, arrayOfByte.length);
            StringBuilder stringBuilder2 = new StringBuilder(arrayOfByte.length);
            int j = arrayOfByte.length;
            int i;
            for (i = 0; i < j; i++) {
                stringBuilder2.append(String.format("%02X", new Object[]{Byte.valueOf(arrayOfByte[i])}));
            }
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("characteristic.getValue() : ");
            stringBuilder3.append(stringBuilder2.toString());
            Log.v("mtj", stringBuilder3.toString());
            i = Integer.parseInt(String.valueOf(str.charAt(2)));
            if (i != 9) {
                switch (i) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        if (String.valueOf(str.charAt(3)).equals("D")) {
                            this.bufferTemp = arrayOfByte;
                            this.sendCheck = false;
                            break;
                        }
                        if (String.valueOf(str.charAt(3)).equals("I")) {
                            for (i = 4; i < arrayOfByte.length; i++) {
                                if (arrayOfByte[i] == 49)
                                    this.bufferTemp[i] = 13;
                            }
                            this.sendCheck = true;
                        }
                        break;
                }
            } else {
                this.bufferTemp = arrayOfByte;
                this.sendCheck = true;
            }
            if (this.sendCheck) {
                arrayOfByte = this.bufferTemp;
                if (arrayOfByte != null && arrayOfByte.length > 0) {
                    StringBuilder stringBuilder4 = new StringBuilder(arrayOfByte.length);
                    byte[] arrayOfByte2 = this.bufferTemp;
                    j = arrayOfByte2.length;
                    for (i = 0; i < j; i++) {
                        stringBuilder4.append(String.format("%02X", new Object[]{Byte.valueOf(arrayOfByte2[i])}));
                    }
                    StringBuilder stringBuilder5 = new StringBuilder();
                    stringBuilder5.append("send_data HEX : ");
                    stringBuilder5.append(stringBuilder4.toString());
                    Log.v("mtj", stringBuilder5.toString());
                    Intent intent = new Intent(ACTION_DATA_AVAILABLE);

                    byte[] arrayOfByte1 = this.bufferTemp;
                    intent.putExtra("DATA", new String(arrayOfByte1, 0, arrayOfByte1.length));
                    intent.putExtra("row_data", this.bufferTemp);
                    //커스텀 broadcastUpdate , 전용 기기의 데이터 검증 후 직접 update 하는 것 같음
                    sendBroadcast(intent);
                    this.sendCheck = false;
                    this.countCheck = 0;
                    return;
                }
                return;
            }
            //broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
        }
    };

    public void start(String paramString) {
        if (this.mBluetoothAdapter != null) {
            BluetoothGatt bluetoothGatt = this.mBluetoothGatt;
            if (bluetoothGatt != null) {
                BluetoothGattService bluetoothGattService = bluetoothGatt.getService(UUID.fromString(SampleGattAttributes.lookup("SERV_UUID")));
                if (bluetoothGattService == null) {
                    Log.w(TAG, "Custom BLE Service not found");
                    return;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("sensor - ");
                stringBuilder.append(paramString);
                Log.e("mtj", stringBuilder.toString());

                //기기에 WRITE_UUID 를 할용해 SA1E 전달
                BluetoothGattCharacteristic bluetoothGattCharacteristic = bluetoothGattService.getCharacteristic(UUID.fromString(SampleGattAttributes.lookup("WRITE_UUID")));
                stringBuilder = new StringBuilder();
                stringBuilder.append(paramString);
                stringBuilder.append("\n");
                bluetoothGattCharacteristic.setValue(stringBuilder.toString());
                if (!this.mBluetoothGatt.writeCharacteristic(bluetoothGattCharacteristic)) {
                    Log.w(TAG, "Failed to write characteristic");
                    return;
                }
                Log.w(TAG, "Success to write characteristic");
                return;
            }
        }
        Log.w(TAG, "BluetoothAdapter not initialized");
    }

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);
//
//        // This is special handling for the Heart Rate Measurement profile.  Data parsing is
//        // carried out as per profile specifications:
//        // http://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_measurement.xml
//        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
//            int flag = characteristic.getProperties();
//            int format = -1;
//            if ((flag & 0x01) != 0) {
//                format = BluetoothGattCharacteristic.FORMAT_UINT16;
//                Log.d(TAG, "Heart rate format UINT16.");
//            } else {
//                format = BluetoothGattCharacteristic.FORMAT_UINT8;
//                Log.d(TAG, "Heart rate format UINT8.");
//            }
//            final int heartRate = characteristic.getIntValue(format, 1);
//            Log.d(TAG, String.format("Received heart rate: %d", heartRate));
//            intent.putExtra(EXTRA_DATA, String.valueOf(heartRate));
//        } else {
//            // For all other profiles, writes the data formatted in HEX.
//            final byte[] data = characteristic.getValue();
//            if (data != null && data.length > 0) {
//                final StringBuilder stringBuilder = new StringBuilder(data.length);
//                for (byte byteChar : data)
//                    stringBuilder.append(String.format("%02X ", byteChar));
//                intent.putExtra(EXTRA_DATA, new String(data) + "\n" + stringBuilder.toString());
//            }
//        }
//        sendBroadcast(intent);
    }

    public class LocalBinder extends Binder {
        public BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // After using a given device, you should make sure that BluetoothGatt.close() is called
        // such that resources are cleaned up properly.  In this particular example, close() is
        // invoked when the UI is disconnected from the Service.
        close();
        return super.onUnbind(intent);
    }

    private final IBinder mBinder = new LocalBinder();

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        return true;
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     * @return Return true if the connection is initiated successfully. The connection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public boolean connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        // Previously connected device.  Try to reconnect.
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect()) {
                mConnectionState = STATE_CONNECTING;
                return true;
            } else {
                return false;
            }
        }

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        mConnectionState = STATE_CONNECTING;
        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled        If true, enable notification.  False otherwise.
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                              boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);

        //서비스 UUID인 UUID_HEART_RATE_MEASUREMENT가 일치할 경우 제공하는 기능인 CLIENT_CHAR_CONFIG 정보를 가져온다
        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                    UUID.fromString(SampleGattAttributes.lookup("CLIENT_CHAR_CONFIG")));
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }
    }

    /**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null) return null;

        return mBluetoothGatt.getServices();
    }
}
