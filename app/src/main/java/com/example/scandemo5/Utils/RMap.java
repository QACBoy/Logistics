package com.example.scandemo5.Utils;


import com.example.scandemo5.MyApp;

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

//        map.add("t_goods_goods_no","商品编码");//
//        map.add("t_goods_goods_classify_no","商品类编码");
//        map.add("t_goods_brand_no","品牌编码");
//        map.add("t_goods_brief_code","简码");
//        map.add("t_goods_factory_goods_no","厂家编码");
//        map.add("t_goods_barcode","条码");//
//        map.add("t_goods_goods_name","商品名称");
//        map.add("t_goods_ename","商品英文名");
//        map.add("t_goods_brief_name","商品简码");
//        map.add("t_goods_goods_spce","规格型号");
//        map.add("t_goods_s_spec","最小销售规格");
//        map.add("t_goods_unit","单品单位");
//        map.add("t_goods_box_barcode","外箱条码");
//        map.add("t_goods_pack_quantity","箱装数量");//
//        map.add("t_goods_pack_unit","包装单位");
//        map.add("t_goods_in_price","进货价格");
//        map.add("t_goods_in_tax","进项税率");
//        map.add("t_goods_in_taxprice","含税进项");
//        map.add("t_goods_s_price","销售价格");
//        map.add("t_goods_bulk_discount","批发折价");
//        map.add("t_goods_bulk_price","批发价格");
//        map.add("t_goods_retail_price","建议零售价");
//        map.add("t_goods_single_weight","单品重量");
//        map.add("t_goods_pack_weight","包装重量");
//        map.add("t_goods_single_vol","单品体积");
//        map.add("t_goods_pack_vol","包装体积");
//        map.add("t_goods_ex_day","保质期");//
//        map.add("t_goods_store_type","存储类型");
//        map.add("t_goods_base_box","标准箱");
//        map.add("t_goods_origin","产地");
//        map.add("t_goods_store_place","存放库位");
//        map.add("t_goods_batch","批次管理");
//        map.add("t_goods_group_no_goods","集团码_商品");
//        map.add("t_goods_tax_code","税控软件编码");
//        map.add("t_goods_remark","备注");
//        map.add("t_goods_create_no","商品表的创建人");
//        map.add("t_goods_create_date","商品表的创建日期");
//        map.add("t_goods_modifi_no","商品表的修改人");
//        map.add("t_goods_modifi_time","商品表的修改日期");
//        map.add("t_goods_group_node_id","机构编码");
//        map.add("t_goods_flag","锁定标志（1锁定，0正常）");


        map.add("goods_no","商品编码");
        map.add("goods_name","商品名称");
        map.add("barcode","条码");
        map.add("box_barcode","外箱条码");
        map.add("goods_spce","最小销售规格");
        map.add("unit","包装单位");
        map.add("pack_quantity","箱装数量");
        map.add("ex_day","保质期");
        map.add("single_weight","单品重量");
        map.add("pack_weight","包装重量");
        map.add("single_vol","单品体积");
        map.add("pack_vol","包装体积");
        //工人添加的数据项
        map.add("procure_no","入库单号");
        map.add("quantity","数量");
        map.add("LOT","批次号");
        map.add("location_no","库位编号");
        map.add("MFG","生产日期");
        map.add("EXP","到期日期");
    }

    public String get(String key){
        return map.get(key);
    }

}
