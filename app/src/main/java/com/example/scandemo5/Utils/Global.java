package com.example.scandemo5.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.scandemo5.Data.UpLoad;
import com.example.scandemo5.MyApp;

import java.util.List;

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


}
