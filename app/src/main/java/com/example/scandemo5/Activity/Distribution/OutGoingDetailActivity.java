package com.example.scandemo5.Activity.Distribution;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.scandemo5.Activity.BaseActivity;
import com.example.scandemo5.Activity.ScanRActivity;
import com.example.scandemo5.Activity.Storage.MainActivity;
import com.example.scandemo5.Adapter.OrderDataAdapter;
import com.example.scandemo5.Adapter.ScanDataAdapter;
import com.example.scandemo5.Data.Order;
import com.example.scandemo5.Data.UpLoad;
import com.example.scandemo5.R;
import com.example.scandemo5.Utils.DJson;
import com.example.scandemo5.Utils.Global;
import com.example.scandemo5.Utils.HamButtonBuilderManager;
import com.example.scandemo5.Utils.Http;
import com.example.scandemo5.Utils.Msg;
import com.example.scandemo5.Utils.RMap;
import com.example.scandemo5.Utils.SQLite;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.OnItemClickListener;

import org.feezu.liuli.timeselector.TimeSelector;

import java.util.List;

import co.dift.ui.SwipeToAction;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

/**
 * Created by Sam on 2017/10/12.
 */

public class OutGoingDetailActivity extends BaseActivity{

    public static OutGoingDetailActivity activity;
    private TextView order_no;
    private TextView client_name;
    private TextView linkman;
    private TextView client_address;
    private TextView ord_date;
    private LinearLayout layout;
    private RecyclerView recyclerview;
    private int Postion;
    private ScanDataAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        HamButtonBuilderManager.setHamButtonText(HamButtonBuilderManager.outgoingdetailtextId);
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_outgoingdetail);
        setActionTitle(R.string.text_outside_circle_button_text_2);
        enableBackPress();//启用返回按键
        bindId();

        setHamButtonClick(new HamButtonClick() {
            @Override
            public void onClick(int index, BoomButton boomButton) {
                switch (index){
                    case 0:
                        Msg.showMsg(OutGoingDetailActivity.this,"提示","确定上传?",null);
                        break;
                    case 1:
                        Msg.showMsg(OutGoingDetailActivity.this, "警告", "确定重置当前数据?", new Msg.CallBack() {
                            @Override
                            public void confirm(DialogPlus dialog) {
                                finish();
                                startActivity(new Intent(OutGoingDetailActivity.this,OutGoingDetailActivity.class));
                            }
                        });
                        break;
                }
            }
        });

        Global.upLoad.list.clear();
        for(int i = 0;i<Global.outgoingdrtialorder.order_s.size();i++){
            Order.order_s goods = Global.outgoingdrtialorder.order_s.get(i);
            Global.upLoad.list.add(new UpLoad.ScanData(goods.barcode,goods.goods_no,goods.goods_name,null,null,null,null,goods.ord_quantity));
        }
        recyclerview = new RecyclerView(OutGoingDetailActivity.this);
        recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new ScanDataAdapter();
        recyclerview.setAdapter(adapter);
        layout.addView(recyclerview);

        bindSwipeAction();
    }

    @Override
    public void onBackPressed() {
        Msg.showMsg(OutGoingDetailActivity.this, "警告", "返回将会重置当前界面数据，是否继续？", new Msg.CallBack() {
            @Override
            public void confirm(DialogPlus dialog) {
                finish();
            }
        });
    }

    private void bindSwipeAction(){
        SwipeToAction swipeToAction = new SwipeToAction(recyclerview, new SwipeToAction.SwipeListener<UpLoad.ScanData>() {
            @Override
            public boolean swipeLeft(final UpLoad.ScanData itemData) {
                //do something
//                final int pos = removeScanData(itemData);
//                if(-1 != pos) {
//                    displaySnackbar(pos + "-移除" + itemData.barcode + "的商品", "撤销", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            addScanData(pos, itemData);
//                        }
//                    });
//                }
                return true; //true will move the front view to its starting position
            }

            @Override
            public boolean swipeRight(UpLoad.ScanData itemData) {
                //do something
                Postion = Global.upLoad.list.indexOf(itemData);

                Global.ShowUI_Scanmap = Global.ScanDataToJMap(Global.upLoad.list.get(Postion));

                alertUpdateScannData();
                return true;
            }

            @Override
            public void onClick(UpLoad.ScanData itemData) {
                //do something
                SQLite.Goods goods = SQLite.getInstance().getGoods(itemData.barcode);
                if (goods != null) {
                    Global.ShowUI_map = Global.GoodsToJMap(goods);
                    Global.ShowUI_Scanmap = Global.ScanDataToJMap(itemData);
                    int pos = removeScanData(itemData);
                    Intent intent1 = new Intent(OutGoingDetailActivity.this, ScanRActivity.class);
                    intent1.putExtra("postion",pos);
                    Global.ScanUpdateActivity = "OutGoingDetailActivity";
                    startActivity(intent1);
                }else {
                    Msg.showMsg(OutGoingDetailActivity.this,"提示","商品库未找到该商品信息,更新商品数据后重试",null);
                }


            }

            @Override
            public void onLongClick(UpLoad.ScanData itemData) {
                //do something
            }
        });
    }

    private void alertUpdateScannData(){
        DialogPlus dialog = DialogPlus.newDialog(this)
                .setAdapter(new BaseAdapter() {
                    @Override
                    public int getCount() {
                        return 5;
                    }

                    @Override
                    public Object getItem(int position) {
                        return Global.ShowUI_Scanmap.get(position);
                    }

                    @Override
                    public long getItemId(int position) {
                        return position;
                    }

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        convertView = LayoutInflater.from(OutGoingDetailActivity.this).inflate(R.layout.handle_item, null);

                        TextView tKey = (TextView) convertView.findViewById(R.id.handle_item_key);
                        EditText tValue = (EditText) convertView.findViewById(R.id.handle_item_value);

                        tKey.setText(RMap.getrMap().get(Global.ShowUI_Scanmap.get(position + 3)));  //从Global.ShowUI_Scanmap中第3项开始显示
                        tValue.setText(Global.ShowUI_Scanmap.get(Global.ShowUI_Scanmap.get(position+3)));

                        //,, switch 函数从末尾往前移动到了该位置
                        switch (position){
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

                        //,,对库位编号的点击添加事件(右滑快速填写)
                        if(position == 2){
                            tValue.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    MainActivity.mainActivity.LocationNo_EditText = v;
                                    Global.setTYPE_SCA(Global.ScanType.rk_LocationNo);
                                    return false;
                                }
                            });
                        }


                        if(position > 2) {  //到生产日期才开始启用日期选择组件
                            tValue.setFocusableInTouchMode(false);
                            tValue.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(final View v) {
                                    TimeSelector timeSelector = new TimeSelector(OutGoingDetailActivity.this, new TimeSelector.ResultHandler() {
                                        @Override
                                        public void handle(String time) {
                                            ((EditText)v).setText(time.substring(0,10));
                                        }
                                    }, "2015-01-01 00:00", "2030-12-31 24:00");
                                    timeSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
                                    timeSelector.setIsLoop(true);
                                    timeSelector.show();
                                }
                            });
                        }else {
                            tValue.setSingleLine();
                            tValue.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                        }

                        return convertView;
                    }
                })
                .setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogPlus dialog) {
                        Global.setTYPE_SCA(Global.ScanType.rk_GoodsNo);
                    }
                })
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                    }
                })
                .setExpanded(false)  // This will enable the expand feature, (similar to android L share dialog)
                .setFooter(R.layout.dialog_foot).setContentBackgroundResource(R.color.balck).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        switch (view.getId()){
                            case R.id.footer_close_button://点击关闭按钮
                                dialog.dismiss();
                                break;
                            case R.id.footer_confirm_button://点击保存按钮
                                String sl = ((EditText)(dialog.getHolderView().findViewById(R.id.ids_quantity).findViewById(R.id.handle_item_value))).getText().toString();
                                String pc = ((EditText)(dialog.getHolderView().findViewById(R.id.ids_LOT).findViewById(R.id.handle_item_value))).getText().toString();
                                String kw = ((EditText)(dialog.getHolderView().findViewById(R.id.ids_location_no).findViewById(R.id.handle_item_value))).getText().toString();
                                String sc = ((EditText)(dialog.getHolderView().findViewById(R.id.ids_MFG).findViewById(R.id.handle_item_value))).getText().toString();
                                String dq = ((EditText)(dialog.getHolderView().findViewById(R.id.ids_EXP).findViewById(R.id.handle_item_value))).getText().toString();
                                Global.upLoad.list.get(Postion).quantity = sl;
                                Global.upLoad.list.get(Postion).LOT = pc;
                                Global.upLoad.list.get(Postion).location_no = kw;
                                Global.upLoad.list.get(Postion).MFG = sc;
                                Global.upLoad.list.get(Postion).EXP = dq;
                                adapter.notifyItemChanged(Postion);
                                dialog.dismiss();
                                break;
                        }

                    }
                })
                .create();
        dialog.show();
    }

    private int removeScanData(UpLoad.ScanData data){
        try {
            int pos = Global.upLoad.list.indexOf(data);
            Global.upLoad.list.remove(pos);
            adapter.notifyItemRemoved(pos);
            return pos;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    public void addScanData(int pos, UpLoad.ScanData data){
        Global.upLoad.list.add(pos,data);
        adapter.notifyItemInserted(pos);
    }

    public void addScanDataEnd(UpLoad.ScanData data){
        Global.upLoad.list.add(data);
        int pos = Global.upLoad.list.indexOf(data);
        adapter.notifyItemInserted(pos);
    }

    @Override
    protected void onDestroy() {
        Global.outgoingdrtialorder = null;//清理本次数据
        super.onDestroy();
    }

    private void bindId() {
        layout = (LinearLayout) findViewById(R.id.outgoingdetail);
        order_no = (TextView) findViewById(R.id.outgoingdetailorder_no);
        client_name = (TextView) findViewById(R.id.outgoingdetailclient_name);
        linkman = (TextView) findViewById(R.id.outgoingdetaillinkman);
        client_address = (TextView) findViewById(R.id.outgoingdetailclient_address);
        ord_date = (TextView) findViewById(R.id.outgoingdetailord_date);

        order_no.setText("订单号：" + Global.outgoingdrtialorder.order_m.order_no);
        client_name.setText("客户名称：" + Global.outgoingdrtialorder.order_m.client_name);
        linkman.setText("联系人：" + Global.outgoingdrtialorder.order_m.linkman);
        client_address.setText("客户地址：" + Global.outgoingdrtialorder.order_m.client_address);
        ord_date.setText("订货日期：" + Global.outgoingdrtialorder.order_m.ord_date);
    }
}
