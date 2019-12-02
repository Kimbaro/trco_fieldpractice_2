package com.TR_co.Final;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.Toast;

import com.TR_co.Final.bt_module.BluetoothLeService;
import com.TR_co.Final.bt_module.Lung_Coefficient;
import com.TR_co.Final.bt_module.Morris_VO;
import com.TR_co.Final.bt_module.Result_VO;
import com.TR_co.Final.moduleTest.Lung_CreateModule;
import com.TR_co.Final.moduleTest.Muscle_CreateModule;
import com.TR_co.Final.unity_activity.Unity;
import com.unity3d.player.UnityPlayer;

public class UnityPlayerActivity extends AppCompatActivity {
    protected UnityPlayer mUnityPlayer; // don't change the name of this variable; referenced from native code

    // Override this in your custom UnityPlayerActivity to tweak the command line arguments passed to the Unity Android Player
    // The command line arguments are passed as a string, separated by spaces
    // UnityPlayerActivity calls this from 'onCreate'
    // Supported: -force-gles20, -force-gles30, -force-gles31, -force-gles31aep, -force-gles32, -force-gles, -force-vulkan
    // See https://docs.unity3d.com/Manual/CommandLineArguments.html
    // @param cmdLine the current command line arguments, may be null
    // @return the modified command line string or null
    protected String updateUnityCommandLineArguments(String cmdLine) {
        return cmdLine;
    }

    public void setActivity(Activity activity) {
        Log.e("ASDKIM", "setActivity :: 접근");
        Unity.activity = activity;
    }

    // Setup activity layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        String cmdLine = updateUnityCommandLineArguments(getIntent().getStringExtra("unity"));
        getIntent().putExtra("unity", cmdLine);

        mUnityPlayer = new UnityPlayer(this);
        setContentView(mUnityPlayer);
        mUnityPlayer.requestFocus();

        //해당 액티비티 접근 시 초기설정때 유니티 프로젝트에 Activity를 요청한다.    // receive method ->  setActivity(Activity activity)
        UnityPlayer.UnitySendMessage("Main Camera", "requestActivity", "");

        //Todo DeviceControlActivity setting
        this.mProgress = ProgressDialog.show(this, "초기값 로딩", "잠시만 기다려주세요.");

        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

        // Sets up UI references.
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        registerReceiver(this.mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // To support deep linking, we need to make sure that the client can get access to
        // the last sent intent. The clients access this through a JNI api that allows them
        // to get the intent set on launch. To update that after launch we have to manually
        // replace the intent with the one caught here.
        setIntent(intent);
        mUnityPlayer.newIntent(intent);
    }

    // Quit Unity
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnityPlayer.destroy();
        unregisterReceiver(mGattUpdateReceiver);
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    // Pause Unity
    @Override
    protected void onPause() {
        super.onPause();
        mUnityPlayer.pause();
    }

    // Resume Unity
    @Override
    protected void onResume() {
        super.onResume();
        mUnityPlayer.resume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mUnityPlayer.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mUnityPlayer.stop();
    }

    // Low Memory Unity
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mUnityPlayer.lowMemory();
    }

    // Trim Memory Unity
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_RUNNING_CRITICAL) {
            mUnityPlayer.lowMemory();
        }
    }

    // This ensures the layout will be correct.
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mUnityPlayer.configurationChanged(newConfig);
    }

    // Notify Unity of the focus change.
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mUnityPlayer.windowFocusChanged(hasFocus);
    }

    // For some reason the multiple keyevent type is not supported by the ndk.
    // Force event injection by overriding dispatchKeyEvent().
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_MULTIPLE)
            return mUnityPlayer.injectEvent(event);
        return super.dispatchKeyEvent(event);
    }

    // Pass any events not handled by (unfocused) views straight to UnityPlayer
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return mUnityPlayer.injectEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mUnityPlayer.injectEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mUnityPlayer.injectEvent(event);
    }

    /*API12*/
    public boolean onGenericMotionEvent(MotionEvent event) {
        return mUnityPlayer.injectEvent(event);
    }

    //UnityBridge Method =======
    public void ScenceChangeToHistory() {
        //측정기록
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Unity.activity, HistoryActivity.class);
                Unity.activity.startActivity(intent);
            }
        });
        thread.start();
    }

    public void ScenceChangeToSearch(int type) {

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (type == 0) { //회원가입, 회원정보변경 주소 입력
                    Intent intent = new Intent(Unity.activity, AddressActivity.class);
                    Unity.activity.startActivity(intent);
                } else if (type == 1) { //병원검색
                    //주변 병원
                    Toast.makeText(Unity.activity, "병원", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Unity.activity, HospitalActivity.class);
                    Unity.activity.startActivity(intent);
                } else {
                    Toast.makeText(Unity.activity, "매개변수 type 의 값이 잘못됨.", Toast.LENGTH_SHORT).show();
                }
                handler.removeMessages(0);
            }
        });
    }


    //Todo DeviceControlACtivity.class
    //블루투스 커넥션 및 데이터 처리 ===========================

    Lung_Coefficient lung_coefficient = new Lung_Coefficient();
    Morris_VO morris_vo = new Morris_VO();

    static ProgressDialog mProgress;

    //호흡근, 호흡기압여부 구분
    int test_kind = 0;

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    private String mDeviceName;
    private String mDeviceAddress;
    private BluetoothLeService mBluetoothLeService;
    private boolean mConnected = false;

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e("ASDKIM", "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("ASDKIM", "BroadcastReceiver 접근");
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                invalidateOptionsMenu();
                mHandler.sendEmptyMessage(1);
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                // displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                //기기에서 전송받은 데이터 처리 로직
                String str = intent.getStringExtra("DATA");
                byte[] arrayOfByte = intent.getByteArrayExtra("row_data");
                Log.e("kim", "receive DATA:::::" + str);
                Log.e("kim", "receive row_data:::::" + new String(arrayOfByte, 0, arrayOfByte.length));

                /*==================== 초기값 로딩 시작 ====================*/
                if (str.startsWith("SU1") || str.startsWith("SU2") || str.startsWith("SU3") || str.startsWith("SU4")) {
                    lung_coefficient.device_start(str, arrayOfByte);
                    if (mProgress != null && mProgress.isShowing())
                        mProgress.cancel();
                }

                /*==================== 호흡근력 ====================*/
                if (str.startsWith("SU5")) {
                    // 1 흡기압 후 , 2 호기압 종료 시 서버에 mip,mep 데이터 보내 측정내역저장
                    lung_coefficient.device_muscle_check(str, arrayOfByte, test_kind);
                    if (mProgress != null && mProgress.isShowing())
                        mProgress.cancel();
                    if (test_kind == 2) {
                        UnityPlayer.UnitySendMessage("Main Camera", "SceneChangeToExhaleCheckScene_Android", "");
                    } else if (test_kind == 1) {
                        Muscle_CreateModule muscle_createModule = new Muscle_CreateModule();
                        muscle_createModule.requestDataSave(1);
                    }
                }

                /*==================== 폐기능 ====================*/
                if (str.startsWith("SU6")) {
                    lung_coefficient.device_lung_check(str, arrayOfByte);
                }
                if (str.startsWith("SU9")) {
                    if (mProgress != null && mProgress.isShowing())
                        mProgress.cancel();
                    lung_coefficient.device_lung_check(str, arrayOfByte);
//                    Log.e("mtj_data_SU9_result", Result_VO.U_FVC + " : " + Result_VO.U_FEV1 + " : " + Result_VO.U_PEF + " : " + Result_VO.U_FEV1_FVC);
//                    Log.e("mtj_data_SU9_result", Result_VO.U_FEF25 + " : " + Result_VO.U_FEF50 + " : " + Result_VO.U_FEF75);

                    if (Result_VO.U_FVC == 0.0f || Result_VO.U_FEV1 == 0.0f) {
                        Toast.makeText(Unity.activity, "정확하지 않는 측정입니다", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        morris_vo.set_Morris_Asian();
                        // Morris 식 구한 후 서버에 fvc,fev1 등등 측정값 구한 후 데이터 보내 측정내역 저장할것
                        // here
                        Lung_CreateModule lung_createModule = new Lung_CreateModule();
                        lung_createModule.requestDataSave(0);
                    }
                    UnityPlayer.UnitySendMessage("Main Camera", "SceneChangeToLungCheckScene_Android", "");
                }
                return;
            } else if ((BluetoothLeService.EXTRA_DATA.equals(action))) {
                Log.v("mtj_data_str_2", "=====PUT_LIST=====");
                if (mBluetoothLeService != null) {
                    mBluetoothLeService.start(intent.getStringExtra("start"));
                    StringBuilder object = new StringBuilder();
                    object.append("put_data - ");
                    object.append(intent.getStringExtra("start"));
                    Log.e("mtj_ble_list", object.toString());
                    if (intent.getStringExtra("start").equals("SA5E")) {
                        test_kind = intent.getIntExtra("testkind", 0);
                    }
                    return;
                }
                return;
            }
        }
    };

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothLeService.EXTRA_DATA);

        return intentFilter;
    }

    public void deviceController(String type) {
        Log.e("ASDKIM", "line : 321 : " + type);
        Intent intent;
        switch (type) {
            default:
                return;
            case "0":
//                this.lin_input.setVisibility(0);
                return;
            case "SA0E":
                this.mProgress = ProgressDialog.show(Unity.activity, "작동중", "숨을 크게 들이마셨다가 내쉬어 주세요");
                Toast.makeText(Unity.activity, "숨을 크게 들이마셨다가 내쉬어 주세요", Toast.LENGTH_LONG).show();
                intent = new Intent(BluetoothLeService.EXTRA_DATA);
                // this.test_kind = 3;
                intent.putExtra("testkind", 3);
                intent.putExtra("start", "SA0E");
                Unity.activity.sendBroadcast(intent);
                return;
            case "SAFE":
                intent = new Intent(BluetoothLeService.EXTRA_DATA);
                intent.putExtra("start", "SAFE");
                Unity.activity.sendBroadcast(intent);
                return;
            case "SA5E_1":
                this.mProgress = ProgressDialog.show(Unity.activity, "작동중", "숨을 크게 들이마셔주세요");
                Toast.makeText(Unity.activity, "작동중 숨을 크게 들이마셔주세요", Toast.LENGTH_LONG).show();
                // this.test_kind = 1;
                intent = new Intent(BluetoothLeService.EXTRA_DATA);
                intent.putExtra("start", "SA5E");
                intent.putExtra("testkind", 1);
                Unity.activity.sendBroadcast(intent);
                return;
            case "SA5E_2":
                this.mProgress = ProgressDialog.show(Unity.activity, "작동중", "숨을 크게 불어주세요");
                Toast.makeText(Unity.activity, "작동중 숨을 크게 불어주세요", Toast.LENGTH_LONG).show();
                // this.test_kind = 2;
                intent = new Intent(BluetoothLeService.EXTRA_DATA);
                intent.putExtra("start", "SA5E");
                intent.putExtra("testkind", 2);
                Unity.activity.sendBroadcast(intent);
                return;
            case "1":
                break;
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Toast.makeText(getApplicationContext(), "기기와의 연결이 끊어졌습니다", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), DeviceScanActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };
}
