package com.example.scandemo5.Adapter;

import android.app.Activity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.scandemo5.Activity.Storage.MainActivity;
import com.example.scandemo5.Data.UpLoad;
import com.example.scandemo5.R;
import com.example.scandemo5.Utils.DateDeal;
import com.example.scandemo5.Utils.Global;
import com.example.scandemo5.Utils.JMap;
import com.example.scandemo5.Utils.RMap;
import com.example.scandemo5.Utils.SQLite;

import org.feezu.liuli.timeselector.TimeSelector;


/**
 * Created by JC on 2017/10/28.
 */

public class MsgShowScanAdapter extends BaseAdapter {

    private SQLite.Goods goods;
    private Activity activity;
    private JMap<String,String> map;

    public MsgShowScanAdapter(Activity activity, SQLite.Goods goods){
        this.activity = activity;
//        this.map = map;
//        goods = SQLite.getInstance().getGoodsByGoodNo(map.get("goods_no"));
        this.goods = goods;

        map = new JMap<>();
        map.add("goods_no", goods.goods_no);
        map.add("goods_name", goods.goods_name);
        map.add("barcode", goods.barcode);
        map.add("goods_spce", goods.goods_spce);
        map.add("quantity", "");
        map.add("LOT", "");
        map.add("location_no", "010001");
        map.add("MFG", "");
        map.add("EXP", "");
    }

    public MsgShowScanAdapter(Activity activity, SQLite.Goods goods, UpLoad.ScanData scanData){
        this.activity = activity;
//        this.map = map;
//        goods = SQLite.getInstance().getGoodsByGoodNo(map.get("goods_no"));
        this.goods = goods;

        map = new JMap<>();
        map.add("goods_no", goods.goods_no);
        map.add("goods_name", goods.goods_name);
        map.add("barcode", goods.barcode);
        map.add("goods_spce", goods.goods_spce);
        map.add("quantity", scanData.quantity);
        map.add("LOT", scanData.LOT);
        map.add("location_no", scanData.location_no);
        map.add("MFG", scanData.MFG);
        map.add("EXP", scanData.EXP);
    }

    @Override
    public int getCount() {
        return map.size();
    }

    @Override
    public Object getItem(int position) {
        return map.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(activity).inflate(R.layout.handle_item, null);

        TextView tKey = (TextView) convertView.findViewById(R.id.handle_item_key);
        EditText tValue = (EditText) convertView.findViewById(R.id.handle_item_value);
        tValue.setInputType(InputType.TYPE_CLASS_NUMBER);
        tValue.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        if(position >= map.size() - 5) {
            tKey.setText(RMap.getrMap().get(map.get(position)));
            tValue.setText(map.get(map.get(position)));


            //,,对库位编号的点击添加事件(扫描结果填写)
            if (position - 4 == 2) {
                tValue.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        MainActivity.mainActivity.LocationNo_EditText = v;
                        Global.setTYPE_SCA(Global.ScanType.rk_LocationNo);
                        return false;
                    }
                });
            }

            //数量默认设置
            if (position - 4 == 0) {
                tValue.setText("1");
            }

            if ("0".equals(goods.batch)) { //是否进行批次管理 1-进行管理   0-不管理（入库时不需要录入生产日期、到日期、批次信息）

                tValue.setSingleLine();
                tValue.setImeOptions(EditorInfo.IME_ACTION_NEXT);

                switch (position - 4) {
                    case 0:
                        convertView.setId(R.id.ids_quantity);
                        break;
                    case 1:
                        convertView.setId(R.id.ids_LOT);
                        tValue.setText("XXXX");
                        tValue.setEnabled(false);
                        break;
                    case 2:
                        convertView.setId(R.id.ids_location_no);
                        break;
                    case 3:
                        convertView.setId(R.id.ids_MFG);
                        tValue.setText("XXXXXXXX");
                        tValue.setEnabled(false);
                        break;
                    case 4:
                        convertView.setId(R.id.ids_EXP);
                        tValue.setText("XXXXXXXX");
                        tValue.setEnabled(false);
                        break;
                }
            }else {
                if (position - 4 > 2) {
                    tValue.setFocusableInTouchMode(false);
                    tValue.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            TimeSelector timeSelector = new TimeSelector(activity, new TimeSelector.ResultHandler() {
                                @Override
                                public void handle(String time) {
                                    time = time.substring(0, 10);
                                    ((EditText) v).setText(time);
                                    if (position - 4 == 3) {
                                        //出厂日期
                                        ((EditText) (v.getRootView().findViewById(R.id.ids_EXP).findViewById(R.id.handle_item_value))).setText(DateDeal.add(time, Integer.parseInt(goods.ex_day)));
                                    } else if (position - 4 == 4) {
                                        //到期日期
                                        ((EditText) (v.getRootView().findViewById(R.id.ids_MFG).findViewById(R.id.handle_item_value))).setText(DateDeal.reduce(time, Integer.parseInt(goods.ex_day)));
                                    }
                                }
                            }, DateDeal.Format(DateDeal.reduce(DateDeal.Now(), Integer.parseInt(goods.ex_day))), DateDeal.Format(DateDeal.add(DateDeal.Now(), Integer.parseInt(goods.ex_day))));
                            timeSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
                            timeSelector.setIsLoop(true);
                            timeSelector.show();
                        }
                    });
                } else {
                    tValue.setSingleLine();
                    tValue.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                }
                switch (position - 4) {
                    case 0:
                        convertView.setId(R.id.ids_quantity);
                        break;
                    case 1:
                        convertView.setId(R.id.ids_LOT);
                        break;
                    case 2:
                        convertView.setId(R.id.ids_location_no);
                        break;
                    case 3:
                        convertView.setId(R.id.ids_MFG);
                        break;
                    case 4:
                        convertView.setId(R.id.ids_EXP);
                        break;
                }
            }
        } else {
            convertView.setId(position);
            tKey.setText(RMap.getrMap().get(map.get(position)));
            tValue.setText(map.get(map.get(position)));
        }

        return convertView;
    }
}
