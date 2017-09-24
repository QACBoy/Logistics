package com.example.scandemo5.Data;

import java.util.List;

/**
 * Created by JC on 2017/9/21.
 */

public class LocationInfo {

    public Location location;
    public List<Goods> goods;

    public class Location{
        public String id;
        public String location_no;
        public String desc;
    }
    public class Goods{
        public String goods_name;
        public String goods_no;
        public String barcode;
        public String quantity;
        public String MFG;
        public String EXP;
        public String LOT;
        public String location_no;
    }
}
