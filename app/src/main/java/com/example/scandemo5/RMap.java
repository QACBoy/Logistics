package com.example.scandemo5;

import java.util.ArrayList;

/**
 * Created by Sam on 2017/8/22.
 */

public class RMap {


    //    String[] key = {"goods_name", "goods_no", "goods_classify_no", "goods_spce", "unit", "origin"};
    //                      商品名称，商品编码，商品类别编码，规格型号，单品单位，产地
    private static JMap<String,String> map;

    private static RMap rMap;

    public static RMap getrMap() {
        if(rMap == null){
            synchronized (MyApp.getContext()){
                rMap = new RMap();
            }
        }
        return rMap;
    }
    private RMap(){
        map = new JMap<>();
        map.add("goods_name","商品名称");
        map.add("goods_no","商品编码");
        map.add("goods_classify_no","商品类别编码");
        map.add("goods_spce","规格型号");
        map.add("unit","单品单位");
        map.add("origin","产地");
    }

    public String get(String key){
        return map.get(key);
    }

}
