package com.example.scandemo5.Data;


import java.util.List;

/**
 * Created by JC on 2017/10/12.
 */

public class DistributionInfo {

    public Distribution distribution;
    public List<goods> goods;
    public List<info> info;

    public class Distribution{
        public String id;
        public String name;
        public String address;
        public String distribution_no;
        public String phone;
    }
    public class goods{
        public String id;
        public String distribution_no;
        public String barcode;
        public String quantity;
    }
    public class info{
        public String id;
        public String distribution_no;
        public String distribution_address;
        public String time;
    }
}
