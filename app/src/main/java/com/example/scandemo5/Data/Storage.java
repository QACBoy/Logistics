package com.example.scandemo5.Data;

import java.util.List;

/**
 * Created by JC on 2017/10/16.
 */

public class Storage {

    public storage_m storage_m;
    public order_m order_m;
    public List<storage_s> storage_s;

    public class storage_m{
        public String storage_no;//S20171015001",
        public String order_no;//O20171015001",
        public String state;//0",
        public String warehouse_keeper;//小仓"
    }

    public class order_m{
        public String id;
        public String order_no;
        public String client_name;
        public String linkman;
        public String client_address;
        public String ord_date;
    }

    public class storage_s{
        public String storage_no;//S20171015001",
        public String order_no;//O20171015001",
        public String room_no;//R001",
        public String location_no;//L002",
        public String goods_no;//1010105003",
        public String ord_quantity;//1000.000000",
        public String out_quantity;//900.000000"
    }
}
