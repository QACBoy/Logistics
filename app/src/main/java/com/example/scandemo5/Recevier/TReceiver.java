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

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.example.scandemo5.Activity.MainActivity;
import com.example.scandemo5.Activity.ScanRActivity;
import com.example.scandemo5.MyApp;
import com.example.scandemo5.R;
import com.example.scandemo5.Utils.DJson;
import com.example.scandemo5.Utils.Encryption;
import com.example.scandemo5.Utils.Global;
import com.example.scandemo5.Utils.Http;
import com.example.scandemo5.Utils.JMap;
import com.example.scandemo5.Utils.RMap;
import com.example.scandemo5.Utils.SQLite;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
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

        Log.d(TAG, "onReceive: 扫描结果：" + str);
        if (Global.Procure.equals(Global.getTYPE_SAC())) {//扫描入库单
            ((EditText) MainActivity.mainActivity.findViewById(R.id.procure_no)).setText(str);
        }
        if (Global.GoodsNo.equals(Global.getTYPE_SAC())) {//扫描条码
            SQLite.Goods goods = SQLite.getInstance().getGoods(str);

            if (goods != null) {
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
                Global.ShowUI_map = map;
                Intent intent1 = new Intent(MainActivity.mainActivity, ScanRActivity.class);
                MainActivity.mainActivity.startActivity(intent1);

            } else {
                Toast.makeText(MyApp.getContext(), "未找到商品" + str, Toast.LENGTH_SHORT).show();
            }
        }
    }

}