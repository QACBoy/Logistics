package com.example.scandemo5.Activity.Distribution;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

import com.example.scandemo5.Activity.BaseActivity;
import com.example.scandemo5.Activity.ScanRActivity;
import com.example.scandemo5.Adapter.ScanDataAdapter;
import com.example.scandemo5.Data.DistributionInfo;
import com.example.scandemo5.Data.UpLoad;
import com.example.scandemo5.R;
import com.example.scandemo5.Utils.HamButtonBuilderManager;
import com.example.scandemo5.Utils.DJson;
import com.example.scandemo5.Utils.Global;
import com.example.scandemo5.Utils.Http;
import com.example.scandemo5.Utils.Msg;
import com.example.scandemo5.Utils.SQLite;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.OnBoomListener;
import com.orhanobut.dialogplus.DialogPlus;

import java.util.HashMap;
import java.util.Map;

import co.dift.ui.SwipeToAction;

/**
 * Created by Sam on 2017/10/12.
 */

public class DistributionActivity extends BaseActivity {

    public static DistributionActivity activity;
    public static TableLayout tabltLayout;
    private RecyclerView recyclerView;
    private ScanDataAdapter adapter;
    private SwipeToAction swipeToAction;
    private DistributionInfo info;

    protected void onCreate(Bundle savedInstanceState) {
        HamButtonBuilderManager.setHamButtonText(HamButtonBuilderManager.distristartextId);
        super.onCreate(savedInstanceState);
        initRigthHamButtonClick();
        activity = this;
        setContentView(R.layout.activity_distribution);
        findViewById(R.id.distribution_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DistributionActivity.this,LogisticsInfoActivity.class));
                finish();
            }
        });
        findViewById(R.id.distribution_star).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toStart();
            }
        });
        findViewById(R.id.distribution_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toStart();
            }
        });
    }

    private void initRigthHamButtonClick() {
        rightBmb.setOnBoomListener(new OnBoomListener() {
            @Override
            public void onClicked(int index, BoomButton boomButton) {
                switch (index) {
                    case 0:
                        Msg.showMsg(DistributionActivity.this, "警告", "确认已检货完成即将出库配送?", new Msg.CallBack() {
                            @Override
                            public void confirm(DialogPlus dialog) {
                                dialog.dismiss();
                                Map map = new HashMap();
                                map.put("address", "已出库，正前往" + info.distribution.address);
                                Http.getInstance().Post(Http.getInstance().address_distribution + info.distribution.distribution_no, map, new Http.Callback() {
                                    @Override
                                    public void done(String data) {
                                        if (data == null || "NetError".equals(data)) {
                                            Toast.makeText(DistributionActivity.this, "网络数据获取失败，请检查网络", Toast.LENGTH_SHORT).show();
                                        } else {
                                            if ("success".equals(data)) {
                                                Msg.showMsg(DistributionActivity.this, "提示", "同步数据成功，本单配送完成", new Msg.CallBack() {
                                                    @Override
                                                    public void confirm(DialogPlus dialog) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                            } else {
                                                Msg.showMsg(DistributionActivity.this, "警告", "同步数据失败，请检查网络", new Msg.CallBack() {
                                                    @Override
                                                    public void confirm(DialogPlus dialog) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                            }
                                        }
                                    }
                                });
                            }
                        });
                        break;
                    case 1:
                        Msg.showMsg(DistributionActivity.this, "提示", "请用户确定货物已经送达无误", new Msg.CallBack() {
                            @Override
                            public void confirm(DialogPlus dialog) {
                                dialog.dismiss();
                                toSure();
                                Msg.showMsg(DistributionActivity.this,"提示","请扫描配送单号确认",null);
                            }
                        });
                        break;
                }
            }

            @Override
            public void onBackgroundClick() {

            }

            @Override
            public void onBoomWillHide() {

            }

            @Override
            public void onBoomDidHide() {

            }

            @Override
            public void onBoomWillShow() {

            }

            @Override
            public void onBoomDidShow() {

            }
        });
    }
    private void toSure(){
        setContentView(R.layout.activity_change_storage);
        ((EditText)findViewById(R.id.changestorage_stroageno)).setHint("请扫描 配送单号");
        findViewById(R.id.changestorage_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(info.distribution.distribution_no.equals(((EditText)findViewById(R.id.changestorage_stroageno)).getText().toString())){
                    Map map = new HashMap();
                    map.put("address", "已签收，用户" + info.distribution.name + "已确认签收，欢迎您下次光临");
                    Http.getInstance().Post(Http.getInstance().address_distribution + info.distribution.distribution_no, map, new Http.Callback() {
                        @Override
                        public void done(String data) {
                            if (data == null || "NetError".equals(data)) {
                                Toast.makeText(DistributionActivity.this, "网络数据获取失败，请检查网络", Toast.LENGTH_SHORT).show();
                            } else {
                                if ("success".equals(data)) {
                                    Msg.showMsg(DistributionActivity.this, "提示", "同步数据成功，请及时配送", new Msg.CallBack() {
                                        @Override
                                        public void confirm(DialogPlus dialog) {
                                            dialog.dismiss();
                                        }
                                    });
                                } else {
                                    Msg.showMsg(DistributionActivity.this, "警告", "同步数据失败，请检查网络", new Msg.CallBack() {
                                        @Override
                                        public void confirm(DialogPlus dialog) {
                                            dialog.dismiss();
                                        }
                                    });
                                }
                            }
                        }
                    });
                }else {
                    Msg.showMsg(DistributionActivity.this,"提示","配送单不一致，不予确认",null);
                }
            }
        });
    }

    private void toStart(){
        setContentView(R.layout.activity_change_storage);
        ((EditText)findViewById(R.id.changestorage_stroageno)).setHint("请扫描 配送单号");
        findViewById(R.id.changestorage_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Http.getInstance().Get(Http.getInstance().get_distribution + ((EditText) findViewById(R.id.changestorage_stroageno)).getText().toString(), new Http.Callback() {
                    @Override
                    public void done(String data) {
                        Log.d("", "done:获取配送单商品信息 " + data);
                        if("NetError".equals(data)){
                            Toast.makeText(DistributionActivity.this,"网络数据获取失败，请检查网络",Toast.LENGTH_SHORT).show();
                        }else {
                            if (Global.isNullorEmpty(data)) {
                                Msg.showMsg(DistributionActivity.this, "警告", "未找到该配送单号", null);
                                return;
                            }
                            info = DJson.JsonToObject(data, DistributionInfo.class);
                            for (int i = 0; i < info.goods.size(); i++) {
                                Global.upLoad.list.add(new UpLoad.ScanData(
                                        info.goods.get(i).barcode,
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        info.goods.get(i).quantity
                                ));
                            }
                            setContentView(R.layout.handle);
                            tabltLayout = (TableLayout) findViewById(R.id.table);

                            recyclerView = (RecyclerView) findViewById(R.id.recycler);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(DistributionActivity.this);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setHasFixedSize(true);
                            adapter = new ScanDataAdapter();
                            recyclerView.setAdapter(adapter);
                            swipeToAction = new SwipeToAction(recyclerView, new SwipeToAction.SwipeListener<UpLoad.ScanData>() {
                                @Override
                                public boolean swipeLeft(UpLoad.ScanData itemData) {
                                    //do something
                                    return true; //true will move the front view to its starting position
                                }

                                @Override
                                public boolean swipeRight(UpLoad.ScanData itemData) {
                                    //do something
//                                Postion = Global.upLoad.list.indexOf(itemData);
//
//                                Global.ShowUI_Scanmap = Global.ScanDataToJMap(Global.upLoad.list.get(Postion));
//
//                                alertUpdateScannData();
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
                                        Intent intent1 = new Intent(DistributionActivity.this, ScanRActivity.class);
                                        intent1.putExtra("postion", pos);
                                        Global.ScanUpdateActivity = "DistributionActivity";
                                        startActivity(intent1);
                                    }


                                }

                                @Override
                                public void onLongClick(UpLoad.ScanData itemData) {
                                    //do something
                                }

                            });
                        }
                    }
                });
            }
        });
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
}
