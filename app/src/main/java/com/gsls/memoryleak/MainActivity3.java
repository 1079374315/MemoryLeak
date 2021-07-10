package com.gsls.memoryleak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gsls.gt.GT;

public class MainActivity3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GT.toast(MainActivity3.this, "发送广播3");
                Intent intent = new Intent("LocationAction");
                intent.putExtra("locationMessage", "开始定位");
                sendOrderedBroadcast(intent, null);//发送样本界面的广播让它更新UI
            }
        });

    }
}