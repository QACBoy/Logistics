package com.example.scandemo5.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.scandemo5.Activity.MainActivity;
import com.example.scandemo5.Activity.ScanRActivity;
import com.example.scandemo5.Data.UpLoad;
import com.example.scandemo5.MyApp;
import com.nightonke.boommenu.BoomMenuButton;
import com.orhanobut.dialogplus.DialogPlus;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Sam on 2017/8/28.
 */

public class Global {
    public static Boolean firstIn=true;
    private static SharedPreferences sharedPreferences = MyApp.getContext().getSharedPreferences("dms", Context.MODE_PRIVATE);

    public static SharedPreferences getSharedPreferences() {  //微数据存储区
        return sharedPreferences;
    }

    public static String DealXmlStr(String str){ //处理网络返回xml文件函数
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
        map.add("quantity", data.quantity);
        map.add("LOT", data.LOT);
        map.add("location_no", data.location_no);
        map.add("MFG", data.MFG);
        map.add("EXP", data.EXP);
        return map;
    }

    public static boolean isNullorEmpty(String str){  //自定数据判断函数
        if(str == null){
            return true;
        }
        if(str.isEmpty()){
            return true;
        }
        if(str.length() <= 0){
            return true;
        }
        if("".equals(str.trim())){
            return true;
        }
        return false;
    }

    public static void ifCloseInput(Activity activity){  //输入法控制端
        int flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
        if(!getSharedPreferences().getBoolean("ifOpenKeyboard",false)) {
            activity.getWindow().addFlags(flags);
        }else {
            activity.getWindow().clearFlags(flags);
        }
    }

    public static boolean isSuccessUpdataHttpdata = false;//网络数据是否加载成功


    public static String PROCURENO = null;

    public static UpLoad upLoad = new UpLoad();  //上传数据存储区

    public static ScanRActivity scanRActivity;

    public static JMap<String,String> ShowUI_map;
    public static JMap<String,String> ShowUI_Scanmap;


    public static DialogPlus dialog; //弹出窗口唯一控制


    //    扫描器工作模式
    private static String TYPE_SCA = "132564";
    public static void setTYPE_SCA(String TYPE_SAC) {
        TYPE_SCA = TYPE_SAC;
    }
    public static String getTYPE_SCA() {
        return TYPE_SCA;
    }
    public static class ScanType{
        ///入库管理模式
        public static String rk_Procure = "rk10001";//扫描供货商
        public static String rk_ComeGoodsNo = "rk10002";//扫描到货单
        public static String rk_GoodsNo = "rk10003";//扫描商品
        public static String rk_LocationNo = "rk10004";//,, 扫描库位编码
        ///库位调拨模式
        public static String kw_stroageno = "kw10001";//扫描商品
    }

}
