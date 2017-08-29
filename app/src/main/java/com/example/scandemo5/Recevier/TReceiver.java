package com.example.scandemo5.Recevier;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scandemo5.Activity.MainActivity;
import com.example.scandemo5.MyApp;
import com.example.scandemo5.R;
import com.example.scandemo5.Utils.DJson;
import com.example.scandemo5.Utils.Encryption;
import com.example.scandemo5.Utils.Global;
import com.example.scandemo5.Utils.Http;
import com.example.scandemo5.Utils.JMap;
import com.example.scandemo5.Utils.RMap;
import com.example.scandemo5.Utils.SQLite;

import org.feezu.liuli.timeselector.TimeSelector;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class TReceiver extends BroadcastReceiver {
    private static String str;

    //split_key 数组主要用于数据的分割和封装操作，若需要对数据项的增删改时，不仅要在RMap.java 中进行添加数据项，同时也需要对 split_key 数组中的数据项做修改
//    static String[] split_key = {"t_goods_goods_no","t_goods_goods_classify_no","t_goods_brand_no","t_goods_brief_code","t_goods_factory_goods_no",
//            "t_goods_barcode","t_goods_goods_name","t_goods_ename","t_goods_brief_name","t_goods_goods_spce","t_goods_s_spec","t_goods_unit","t_goods_box_barcode",
//            "t_goods_pack_quantity","t_goods_pack_unit","t_goods_in_price","t_goods_in_tax","t_goods_in_taxprice","t_goods_s_price","t_goods_bulk_discount",
//            "t_goods_bulk_price","t_goods_retail_price","t_goods_single_weight","t_goods_pack_weight","t_goods_single_vol","t_goods_pack_vol","t_goods_ex_day",
//            "t_goods_store_type","t_goods_base_box","t_goods_origin","t_goods_store_place","t_goods_batch","t_goods_group_no_goods","t_goods_tax_code","t_goods_remark",
//            "t_goods_create_no","t_goods_create_date","t_goods_modifi_no","t_goods_modifi_time","t_goods_group_node_id","t_goods_flag","b_procure_m_procure_no","b_procure_s_quantity","b_procure_s_LOT","b_procure_s_MFG"};

//    static JMap<String, String> list;

    //商品名称，商品编码，商品类别编码，规格型号，单品单位，产地
    @Override
    public void onReceive(Context context, Intent intent) {
        //  for(int i=0; i<20; i++)
        //    System.out.println("调用了此函数！！！！！！！！！！！！！");
        //此处获取扫描结果信息
        String scanResult = intent.getStringExtra("EXTRA_SCAN_DATA");

        str = scanResult;

        Log.d(TAG, "onReceive: 扫描结果："+str);
        if(Global.Procure.equals(Global.getTYPE_SAC())){//扫描入库单
            ((EditText)MainActivity.mainActivity.findViewById(R.id.procure_no)).setText(str);
        }
        if(Global.GoodsNo.equals(Global.getTYPE_SAC())){//扫描条码
            SQLite.Goods goods = SQLite.getInstance().getGoods(str);

            if(goods!=null) {
                JMap<String, String> map = new JMap<>();
                map.add("goods_no", goods.goods_no);
                map.add("goods_name", goods.goods_name);
                map.add("barcode", goods.barcode);
                map.add("box_barcode", goods.box_barcode);
                map.add("goods_spce", goods.goods_spce);
                map.add("unit", goods.unit);
                map.add("pack_quantity", goods.pack_quantity);
                map.add("ex_day", goods.ex_day);
                map.add("single_weight", goods.single_weight);
                map.add("pack_weight", goods.pack_weight);
                map.add("single_vol", goods.single_vol);
                map.add("pack_vol", goods.pack_vol);
                Log.d(TAG, "1245onReceive: " + DJson.ObjectToJson(map));
                showUI(map);
                MainActivity.sure.setEnabled(true);
            }else {
                Toast.makeText(MyApp.getContext(),"未找到商品"+ str,Toast.LENGTH_SHORT).show();
            }
        }


//        //查询返回的数据结果
//        Map map = new HashMap();
//        try {
//            map.put("barcode", Encryption.enCode(str));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        Http.getInstance().Post(Http.getInstance().get_goods_info, map, new Http.Callback() {
//            @Override
//            public void done(String data) {
//                try {
//                    if(!"NetError".equals(data)) {
//                        int start = data.indexOf("\">") + 2;
//                        int end = data.lastIndexOf("<");
//                        data = data.substring(start,end);
//                        Log.d("111111", data);
//                        data = Encryption.deCode(data);
//                        Log.d("1111111", data);
//                        if ("0".equals(data.trim())) {
//                            AlertDialog.Builder a = new AlertDialog.Builder(MyApp.getContext()).setTitle("恭喜你").setMessage("未找到该商品").setPositiveButton("确定", null);
//                            AlertDialog alert = a.create();
//                            alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//                            alert.show();
//                        } else {
//                            String json_str = DealStr(data);
//                            //处理显示
//                            showUI(list, json_str.toString());
//                        }
//                    }else {
//                        Toast.makeText(MainActivity.mainActivity,"网络错误",Toast.LENGTH_SHORT).show();
//                    }
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//            }
//        });


    }
//
//    public static void DealHandleStr(){
//        for(int i =0;i<list.size();i++){
//            list.set(split_key[i],((EditText)MainActivity.tabltLayout.getChildAt(i).findViewById(R.id.handle_item_value)).getText().toString());
//        }
//        String json_str = JMapToJson(list);
//
//        MainActivity.mainActivity.upload(json_str);
//    }

//    private static String JMapToJson(JMap<String,String> map){
//        StringBuilder json_str = new StringBuilder();
//        json_str.append("{");
//        for (int i = 0; i < list.size(); i++) {
//
//            if (i == list.size() - 1) {
//                json_str.append("\"" + split_key[i] + "\":\"" + list.get(split_key[i]) + "\"\n");
//            } else {
//                json_str.append("\"" + split_key[i] + "\":\"" + list.get(split_key[i]) + "\",\n");
//            }
//        }
//        json_str.append("}");
//
//        Log.d("jianghujiuji ", "JMapToJson: "+json_str);
//        return json_str.toString();
//    }

//    private String DealStr(String str) {
//        list = new JMap<>();
//
//        String[] Array1 = str.split("，");
//        for (int i = 0; i < Array1.length; i++){
//            Log.d("2222222222", "DealStr: "+Array1[i]);
//        }
//        StringBuilder json_str = new StringBuilder();
//        json_str.append("{");
//        int size = Array1.length;
//        for (int i = 0; i < size; i++) {
//            String[] Array2 = Array1[i].split("：");
//
////                Log.d("3333333", "DealStr: "+Array2[1]);
//            list.add(Array2[0], Array2[1]);
//            json_str.append("\"" + split_key[i] + "\":\"" + list.get(split_key[i]) + "\"\n");
//
////            if (i == Array1.length - 1) {
////                json_str.append("\"" + split_key[i] + "\":\"" + list.get(split_key[i]) + "\"\n");
////            } else
////                json_str.append("\"" + split_key[i] + "\":\"" + list.get(split_key[i]) + "\",\n");
//        }
//
//        list.add("b_procure_m_procure_no",MainActivity.ProcureNo);
//        list.add("b_procure_s_quantity"," ");
//        list.add("b_procure_s_LOT"," ");
//        list.add("b_procure_s_MFG"," ");
//        json_str.append("\"" + split_key[size] + "\":\"" + list.get(split_key[size]) + "\"\n");
//        json_str.append("\"" + split_key[size+1] + "\":\"" + list.get(split_key[size+1]) + "\"\n");
//        json_str.append("\"" + split_key[size+2] + "\":\"" + list.get(split_key[size+2]) + "\"\n");
//        json_str.append("\"" + split_key[size+3] + "\":\"" + list.get(split_key[size+3]) + "\"\n");
//
//        json_str.append("}");
//        Log.d("jianghujiuji ", "JMapToJson: "+json_str);
//        return json_str.toString();
//    }

    private void addChildView(String key,String value,boolean Enable){
        TableRow row = (TableRow) LayoutInflater.from(MyApp.getContext()).inflate(R.layout.handle_item, null);

        TextView tKey = (TextView) row.findViewById(R.id.handle_item_key);
        EditText tValue = (EditText) row.findViewById(R.id.handle_item_value);
        tValue.setEnabled(Enable);

//              Log.d("11010", "showUI: " + key + "------" + value);
        tKey.setText(key);
        tValue.setText(value);

        MainActivity.tabltLayout.addView(row);
    }

    private void addChildTimeView(String key){
        TableRow row = (TableRow) LayoutInflater.from(MyApp.getContext()).inflate(R.layout.handle_item, null);

        TextView tKey = (TextView) row.findViewById(R.id.handle_item_key);
        EditText tValue = (EditText) row.findViewById(R.id.handle_item_value);
        tValue.setFocusableInTouchMode(false);

        tKey.setText(key);
        tValue.setText("");

        tValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                TimeSelector timeSelector = new TimeSelector(MainActivity.mainActivity, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        ((EditText)v).setText(time.substring(0,10));
                    }
                }, "2010-01-01 00:00", "2030-12-31 00:00");
                timeSelector.setMode(TimeSelector.MODE.YMD);
                timeSelector.setIsLoop(true);
                timeSelector.show();
            }
        });

        MainActivity.tabltLayout.addView(row);
    }

    private void showUI(JMap<String, String> map) {


//        if (MainActivity.type == MainActivity.AUTO) {
//            MainActivity.tabltLayout.removeAllViewsInLayout();
//            for (int i = 0; i < map.size(); i++) {
//                String key = RMap.getrMap().get(split_key[i]);
//                String value = map.get(split_key[i]);
//
//                TableRow row = (TableRow) LayoutInflater.from(MyApp.getContext()).inflate(R.layout.auto_item, null);
//
//                TextView tKey = (TextView) row.findViewById(R.id.auto_item_key);
//                TextView tValue = (TextView) row.findViewById(R.id.auto_item_value);
//
////                Log.d("11010", "showUI: " + key + "------" + value);
//                tKey.setText(key);
//                tValue.setText(value);
//
//                MainActivity.tabltLayout.addView(row);
//
//
//            }
//            upload(str);
//        }

        //
//        if (MainActivity.type == MainActivity.HANDLE) {
            MainActivity.tabltLayout.setBackgroundColor(Color.BLACK);
            addChildView(RMap.getrMap().get("procure_no"),Global.PROCURENO,false);
            for (int i = 0; i < map.size(); i++) {
                String key = RMap.getrMap().get(map.indexKey(i));
                String value = map.get(map.indexKey(i));

                addChildView(key,value,false);
            }
            addChildView(RMap.getrMap().get("quantity"),"",true);
            addChildView(RMap.getrMap().get("LOT"),"",true);
            //生产日期
           addChildTimeView(RMap.getrMap().get("MFG"));
            //到期日期

            addChildTimeView(RMap.getrMap().get("EXP"));

//        }

        }

//    private void upload(String str){
//        Log.d("123456", "onReceive: 开始上传");
//        //Http上传
//        Map<String, String> map_upload = new HashMap<>();
//        map_upload.put("key", str);
//        Http.getInstance().Post(Http.getInstance().get_goods_info, map_upload, new Http.Callback() {
//            @Override
//            public void done(String data) {
//                Log.d("123456", "done: " + data);
//
//                //到时候改成失败提示！！
//                AlertDialog.Builder a = new AlertDialog.Builder(MyApp.getContext()).setTitle("恭喜你").setMessage("上传成功").setPositiveButton("确定", null);
//                AlertDialog alert = a.create();
//                alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//                alert.show();
//            }
//        });
//    }
}