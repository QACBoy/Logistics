package com.example.scandemo5.Data;

import java.util.List;

/**
 * Created by JC on 2017/10/15.
 */

public class Order {

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

        public class order_s{
            public String id;
            public String order_no;
            public String goods_no;
            public String barcode;
            public String goods_name;
            public String ord_quantity;
        }


}
