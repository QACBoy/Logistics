package com.example.scandemo5;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class TReceiver extends BroadcastReceiver {
    String str;
    @Override
    public void onReceive(Context context, Intent intent) {
    //  for(int i=0; i<20; i++)
    //    System.out.println("调用了此函数！！！！！！！！！！！！！");
        //此处获取扫描结果信息
        String scanResult = intent.getStringExtra("EXTRA_SCAN_DATA");

        str = scanResult;

        String[] key = {"goods_name", "goods_no", "goods_classify_no", "goods_spce", "unit", "origin"};
        //商品名称，商品编码，商品类别编码，规格型号，单品单位，产地


        JMap<String, String> list = new JMap<>();

        String[] Array = str.split("，");

        StringBuilder show_str = new StringBuilder();
        StringBuilder json_str = new StringBuilder();
        json_str.append("{");
        for(int i = 0; i < Array.length; i++)
        {
            list.add(key[i], Array[i]);

            show_str.append(RMap.getrMap().get(key[i])+":"+list.get(key[i]) + "\n");
            if(i == Array.length - 1){
                json_str.append("\""+ key[i]+"\":\""+list.get(key[i]) + "\"\n");
            }else {
                json_str.append("\"" + key[i] + "\":\"" + list.get(key[i]) + "\",\n");
            }
        }
        json_str.append("}");
        MainActivity.detail.setText(show_str);

        Log.d("123456", "onReceive: 开始上传");
        //Http上传
        Map<String,String> map = new HashMap<>();
        map.put("key",json_str.toString());
        Http.getInstance().Post(Http.getInstance().upload_url, map, new Http.Callback() {
            @Override
            public void done(String data) {
                Log.d("123456", "done: "+data);

                //到时候改成失败提示！！
                AlertDialog.Builder a = new  AlertDialog.Builder(MyApp.getContext()).setTitle("恭喜你" ).setMessage("上传成功").setPositiveButton("确定" ,  null );
                AlertDialog alert = a.create();
                alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                alert.show();
            }
        });

//        MainActivity.txt2.setText(str);//将广播的数据显示到文本标签，以后此处改为函数，分割为数据项。
//        MainActivity.edit1.setText(intent.getStringExtra("EXTRA_SCAN_STATE") + "， 扫码完毕！！！！");
//        MainActivity.edit2.setText(str);
//        MainActivity.txt2.invalidate();
//        MainActivity.edit2.invalidate();

    }

}