package com.example.scandemo5.Data;

import java.util.List;

/**
 * Created by JC on 2017/10/15.
 */

public class Distribution {

        public distribution_m distribution_m;
        public order_m order_m;
        public List<order_s> order_s;

        public class order_m{
            public String id;
            public String order_no;
            public String client_name;
            public String linkman;
            public String client_address;
            public String ord_date;
        }

        public class distribution_m{
            public String id;
            public String distribution_no;
            public String LOT;
            public String storage_no;
            public String order_no;
            public String deliveryman_no;
            public String distribution_date;
            public String state;
        }

        public class order_s{
            public String id;
            public String distribution_no;
            public String LOT;
            public String goods_no;
            public String goods_name;
            public String barcode;
            public String goods_spce;
            public String unit;
            public String ord_quantity;
            public String distribution_quantity;
        }


}
