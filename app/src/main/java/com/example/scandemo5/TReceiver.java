package com.example.scandemo5;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class TReceiver extends BroadcastReceiver {
    static String str;

    //split_key 数组主要用于数据的分割和封装操作，若需要对数据项的增删改时，不仅要在RMap.java 中进行添加数据项，同时也需要对 split_key 数组中的数据项做修改
    static String[] split_key = {"goods_name", "goods_no", "goods_classify_no", "goods_spce", "unit", "origin"};

    static JMap<String, String> list;

    //商品名称，商品编码，商品类别编码，规格型号，单品单位，产地
    @Override
    public void onReceive(Context context, Intent intent) {
        //  for(int i=0; i<20; i++)
        //    System.out.println("调用了此函数！！！！！！！！！！！！！");
        //此处获取扫描结果信息
        String scanResult = intent.getStringExtra("EXTRA_SCAN_DATA");

        str = scanResult;

        String json_str = DealStr(str);
        //处理显示
        showUI(list, json_str.toString());

    }

    public static void DealHandleStr(){
        for(int i =0;i<list.size();i++){
            list.set(split_key[i],((EditText)MainActivity.tabltLayout.getChildAt(i).findViewById(R.id.handle_item_value)).getText().toString());
        }
        String json_str = JMapToJson(list);
        MainActivity.mainActivity.upload(json_str);
    }

    private static String JMapToJson(JMap<String,String> map){
        StringBuilder json_str = new StringBuilder();
        json_str.append("{");
        for (int i = 0; i < list.size(); i++) {


            if (i == list.size() - 1) {
                json_str.append("\"" + split_key[i] + "\":\"" + list.get(split_key[i]) + "\"\n");
            } else {
                json_str.append("\"" + split_key[i] + "\":\"" + list.get(split_key[i]) + "\",\n");
            }
        }
        json_str.append("}");

        return json_str.toString();
    }

    private String DealStr(String str) {
        list = new JMap<>();

        String[] Array = str.split("，");

        StringBuilder json_str = new StringBuilder();
        json_str.append("{");
        for (int i = 0; i < Array.length; i++) {
            list.add(split_key[i], Array[i]);


            if (i == Array.length - 1) {
                json_str.append("\"" + split_key[i] + "\":\"" + list.get(split_key[i]) + "\"\n");
            } else {
                json_str.append("\"" + split_key[i] + "\":\"" + list.get(split_key[i]) + "\",\n");
            }
        }
        json_str.append("}");

        return json_str.toString();
    }

    private void showUI(JMap<String, String> map, String str) {


        if (MainActivity.type == MainActivity.AUTO) {
            MainActivity.tabltLayout.removeAllViewsInLayout();
            for (int i = 0; i < map.size(); i++) {
                String key = RMap.getrMap().get(split_key[i]);
                String value = map.get(split_key[i]);

                TableRow row = (TableRow) LayoutInflater.from(MyApp.getContext()).inflate(R.layout.auto_item, null);

                TextView tKey = (TextView) row.findViewById(R.id.auto_item_key);
                TextView tValue = (TextView) row.findViewById(R.id.auto_item_value);

//                Log.d("11010", "showUI: " + key + "------" + value);
                tKey.setText(key);
                tValue.setText(value);

                MainActivity.tabltLayout.addView(row);


            }
            upload(str);
        }

        //
        if (MainActivity.type == MainActivity.HANDLE) {
            MainActivity.submit.setEnabled(true);
            MainActivity.tabltLayout.removeAllViewsInLayout();
            for (int i = 0; i < map.size(); i++) {
                String key = RMap.getrMap().get(split_key[i]);
                String value = map.get(split_key[i]);

                TableRow row = (TableRow) LayoutInflater.from(MyApp.getContext()).inflate(R.layout.handle_item, null);

                TextView tKey = (TextView) row.findViewById(R.id.handle_item_key);
                EditText tValue = (EditText) row.findViewById(R.id.handle_item_value);

//                Log.d("11010", "showUI: " + key + "------" + value);
                tKey.setText(key);
                tValue.setText(value);

                MainActivity.tabltLayout.addView(row);

            }
        }

        }

    private void upload(String str){
        Log.d("123456", "onReceive: 开始上传");
        //Http上传
        Map<String, String> map_upload = new HashMap<>();
        map_upload.put("key", str);
        Http.getInstance().Post(Http.getInstance().getUpload_url(), map_upload, new Http.Callback() {
            @Override
            public void done(String data) {
                Log.d("123456", "done: " + data);

                //到时候改成失败提示！！
                AlertDialog.Builder a = new AlertDialog.Builder(MyApp.getContext()).setTitle("恭喜你").setMessage("上传成功").setPositiveButton("确定", null);
                AlertDialog alert = a.create();
                alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                alert.show();
            }
        });
    }
}