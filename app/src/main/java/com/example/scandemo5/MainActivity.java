package com.example.scandemo5;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TReceiver mReceiver;  //接收广播信息
    private IntentFilter mFilter;
    public static TextView detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        findViewById(R.id.auto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_main);
                main_init();
            }
        });
        findViewById(R.id.handle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //人工识别界面
                setContentView(R.layout.handle);
                main_init();
                findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LinearLayout item = (LinearLayout) LayoutInflater.from(MainActivity.this).inflate(R.layout.item,null);
                        ((LinearLayout)findViewById(R.id.handle_item)).addView(item);
                    }
                });
            }
        });

    }



    private void main_init(){


        detail = (TextView) findViewById(R.id.detail);
//        //   final Context context = this;
//        mReceiver = new TReceiver();
//
//        mFilter = new IntentFilter("ACTION_BAR_SCAN");
//        //在用户自行获取数据时，将广播的优先级调到最高 1000，实际为int的最大值2147483647。  ***此处必须***
//        mFilter.setPriority(2147483647);
//        this.registerReceiver(mReceiver, mFilter);
//
//        //开启扫描， 这时可以不需要按钮，直接使用广播实现把编辑框的数据传递给文本标签。
//        //   Intent intent = new Intent("ACTION_BAR_TRIGSCAN");
//        //   intent.putExtra("timeout", 3);
//        //   sendBroadcast(intent);

    }

   @Override
    protected void onResume() {
        super.onResume();
        //注册广播来获取扫描结果
     //   this.registerReceiver(mReceiver, mFilter);
    }

    @Override
    protected void onPause() {
        //注销获取扫描结果的广播
      //  this.unregisterReceiver(mReceiver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mReceiver = null;
        mFilter = null;
        super.onDestroy();

    }

    /* */
}




