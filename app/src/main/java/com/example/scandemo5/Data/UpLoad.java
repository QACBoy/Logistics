package com.example.scandemo5.Data;

import com.example.scandemo5.Utils.DJson;
import com.example.scandemo5.Utils.Global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sam on 2017/8/28.
 */

public class UpLoad {
    public String come_goods_no;
    public String procure_no;
    public List<ScanData> list;

    public UpLoad(){
        list = new ArrayList<>();
    }

    public void add(ScanData data){
        list.add(data);
    }

    public String toJson(){
        return DJson.ObjectToJson(this);
    }

    public static class ScanData{
        public String barcode;
        public String goods_no;
        public String goods_name;
        public String MFG;
        public String EXP;
        public String LOT;
        public String quantity;
        public ScanData(){ }
        public ScanData(String barcode,String goods_no,String goods_name,String MFG,String EXP,String LOT,String quantity){
            this.barcode = barcode;this.goods_no = goods_no;this.goods_name = goods_name;this.MFG = MFG;this.EXP = EXP;this.LOT = LOT;this.quantity = quantity;
        }
    }
}
