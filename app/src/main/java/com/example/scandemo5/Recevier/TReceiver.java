package com.example.scandemo5.Recevier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.scandemo5.Activity.Distribution.DistributionActivity;
import com.example.scandemo5.Activity.Storage.ChangeStorageActivity;
import com.example.scandemo5.Activity.Storage.MainActivity;
import com.example.scandemo5.MyApp;
import com.example.scandemo5.R;
import com.example.scandemo5.Utils.Global;

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
        //此处获取扫描结果信息
        String scanResult1 = intent.getStringExtra("SCAN_BARCODE1");
//        String scanResult2 = intent.getStringExtra("SCAN_BARCODE2");
        String scanStatus = intent.getStringExtra("SCAN_STATE");
//        String scanResult = intent.getStringExtra("EXTRA_SCAN_DATA");

//        Log.d("12344", "onReceive: "+ scanResult1 + "--" + scanResult2);
        if("ok".equals(scanStatus)){
            //成功
            str = scanResult1;
            Log.d("12344", "onReceive: 扫描结果：" + str);

            //入库管理区
            if (Global.ScanType.rk_Procure.equals(Global.getTYPE_SCA())) {// 扫描入库单/采购单
                ((EditText) MainActivity.mainActivity.findViewById(R.id.procure_no)).setText(str);
            }
            if (Global.ScanType.rk_ComeGoodsNo.equals(Global.getTYPE_SCA())) {// 扫描入库单
                ((EditText) MainActivity.mainActivity.findViewById(R.id.come_goods_no)).setText(str);
                Global.setTYPE_SCA(Global.ScanType.rk_Procure);
                ((EditText) MainActivity.mainActivity.findViewById(R.id.procure_no)).requestFocus();
            }
            if (Global.ScanType.rk_GoodsNo.equals(Global.getTYPE_SCA())) {// 扫描商品条码
                MainActivity.mainActivity.dealScanData(str);
            }

            if ( Global.ScanType.rk_LocationNo.equals(Global.getTYPE_SCA())){//,, 扫描库位编码
                ((EditText) MainActivity.mainActivity.LocationNo_EditText).setText(str);
            }

            if ( Global.ScanType.ps_stroageno.equals(Global.getTYPE_SCA())){//,, 配送员扫描出库单号编码
                Global.dialog.dismiss();
                DistributionActivity.activity.getStorage(str);
            }

            //移库区
            if(Global.getTYPE_SCA().equals(Global.ScanType.kw_stroageno) || Global.ScanType.kw_stroageno_traget.equals(Global.getTYPE_SCA())){
                ((EditText)ChangeStorageActivity.activity.findViewById(R.id.changestorage_stroageno)).setText(str);
            }
        }else{
            Toast.makeText(MyApp.getContext(), "扫描失败", Toast.LENGTH_SHORT).show();
        }

    }

}