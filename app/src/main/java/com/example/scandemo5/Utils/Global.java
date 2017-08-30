package com.example.scandemo5.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.scandemo5.Activity.MainActivity;
import com.example.scandemo5.Activity.ScanRActivity;
import com.example.scandemo5.Data.UpLoad;
import com.example.scandemo5.MyApp;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Sam on 2017/8/28.
 */

public class Global {
    private static SharedPreferences sharedPreferences = MyApp.getContext().getSharedPreferences("dms", Context.MODE_PRIVATE);

    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public static String DealXmlStr(String str){
        int start = str.indexOf("\">") + 2;
        int end = str.lastIndexOf("</");
        return str.substring(start,end);
    }

    public static JMap<String,String> GoodsToJMap(SQLite.Goods goods){
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
        return map;
    }
    public static JMap<String,String> ScanDataToJMap(UpLoad.ScanData data){
        JMap<String, String> map = new JMap<>();
        map.add("goods_no", data.goods_no);
        map.add("goods_name", data.goods_name);
        map.add("barcode", data.barcode);
        map.add("MFG", data.MFG);
        map.add("quantity", data.quantity);
        map.add("EXP", data.EXP);
        map.add("LOT", data.LOT);
        return map;
    }

    private static String TYPE_SCA = "132564";
    public static String Procure = "100100";
    public static String GoodsNo = "100110";

    public static void setTYPE_SCA(String TYPE_SAC) {
        TYPE_SCA = TYPE_SAC;
    }

    public static String getTYPE_SAC() {
        return TYPE_SCA;
    }

    public static String PROCURENO = null;

    public static UpLoad upLoad = new UpLoad();

    public static ScanRActivity scanRActivity;

    public static JMap<String,String> ShowUI_map;
    public static JMap<String,String> ShowUI_Scanmap;

}
