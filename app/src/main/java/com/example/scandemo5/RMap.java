package com.example.scandemo5;


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

//主要用于存储数据项，若需要对于数据项的增删改，同时需要在 TReceiver.java 中对 split_key 数组中的数据项进行修改
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
