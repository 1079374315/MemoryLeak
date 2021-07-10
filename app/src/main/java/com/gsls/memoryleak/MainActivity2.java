package com.gsls.memoryleak;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.gsls.gt.GT;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity2.this, MainActivity3.class));
            }
        });

        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GT.toast(MainActivity2.this, "发送广播2");
                Intent intent = new Intent("LocationAction");
                intent.putExtra("locationMessage", "开始定位");
                sendOrderedBroadcast(intent, null);//发送样本界面的广播让它更新
            }
        });

    }
}