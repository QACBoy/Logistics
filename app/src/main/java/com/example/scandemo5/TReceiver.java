package com.example.scandemo5;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TReceiver extends BroadcastReceiver {
    String str;
    @Override
    public void onReceive(Context context, Intent intent) {
    //  for(int i=0; i<20; i++)
    //    System.out.println("调用了此函数！！！！！！！！！！！！！");
        //此处获取扫描结果信息
        String scanResult = intent.getStringExtra("EXTRA_SCAN_DATA");
        str = scanResult;
//        MainActivity.txt2.setText(str);//将广播的数据显示到文本标签，以后此处改为函数，分割为数据项。
//        MainActivity.edit1.setText(intent.getStringExtra("EXTRA_SCAN_STATE") + "， 扫码完毕！！！！");
//        MainActivity.edit2.setText(str);
//        MainActivity.txt2.invalidate();
//        MainActivity.edit2.invalidate();

    }

}