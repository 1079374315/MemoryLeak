package com.gsls.memoryleak;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.gsls.gt.GT;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView tv_show;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;//高德定位广播
    private LocationReceiver locationReceiver;//自定义内部类广播 服务于 定位

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getPersimmions();//获取定位权限

        tv_show = findViewById(R.id.tv_show);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainActivity2.class));
            }
        });

        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    GT.logs("aMapLocation.getErrorCode():" + aMapLocation.getErrorCode());
                    if (aMapLocation.getErrorCode() == 0) {
                        //解析定位结果
                        runOnUiThread(() -> tv_show.setText(aMapLocation.getLongitude() + "---" + aMapLocation.getLatitude() + "\n" + aMapLocation.getAddress()));
                        GT.logs(aMapLocation.getAddress());
                        GT.logs(aMapLocation.getLongitude() + "---" + aMapLocation.getLatitude());

                        mLocationClient.stopLocation();
                    }
                }
            }
        });
        //启动定位
        mLocationClient.startLocation();


        //注册广播
        locationReceiver = new LocationReceiver();
        //实例化过滤器并设置要过滤的广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("LocationAction");
        registerReceiver(locationReceiver, intentFilter); //注册广播


    }

    /**
     * 定义一个接收到消息后刷新UI的内部类广播
     */
    private class LocationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String locationMessage = intent.getStringExtra("locationMessage");
                if ("开始定位".equals(locationMessage)) {
                    //启动定位
                    mLocationClient.startLocation();
                } else if ("停止定位".equals(locationMessage)) {
                    //停止定位
                    mLocationClient.stopLocation();
                }
            }

        }

    }


    private String permissionInfo;
    private final int SDK_PERMISSION_REQUEST = 127;

    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            /*
             * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
             */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            } else {
                permissionsList.add(permission);
                return false;
            }
        } else {
            return true;
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(locationReceiver);
    }
}