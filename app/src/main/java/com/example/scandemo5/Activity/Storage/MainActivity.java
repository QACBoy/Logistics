package com.example.scandemo5.Activity.Storage;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scandemo5.Activity.BaseActivity;
import com.example.scandemo5.Activity.ScanRActivity;
import com.example.scandemo5.Adapter.MsgShowScanAdapter;
import com.example.scandemo5.Adapter.ScanDataAdapter;
import com.example.scandemo5.Data.ClintInfo;
import com.example.scandemo5.Data.UpLoad;
import com.example.scandemo5.MyApp;
import com.example.scandemo5.R;
import com.example.scandemo5.Recevier.TReceiver;
import com.example.scandemo5.Utils.DJson;
import com.example.scandemo5.Utils.DateDeal;
import com.example.scandemo5.Utils.HamButtonBuilderManager;
import com.example.scandemo5.Utils.Global;
import com.example.scandemo5.Utils.Http;
import com.example.scandemo5.Utils.InputTools;
import com.example.scandemo5.Utils.JMap;
import com.example.scandemo5.Utils.RMap;
import com.example.scandemo5.Utils.SQLite;
import com.example.scandemo5.Utils.Msg;
import com.example.scandemo5.Utils.User;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.OnItemClickListener;

import org.feezu.liuli.timeselector.TimeSelector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.dift.ui.SwipeToAction;

public class MainActivity extends BaseActivity {

    TReceiver mReceiver;  //接收广播信息
    private IntentFilter mFilter;
    public static MainActivity mainActivity;
    public static TableLayout tabltLayout;
    private RecyclerView recyclerView;
    private ScanDataAdapter adapter;
    private SwipeToAction swipeToAction;
    private LinearLayoutManager layoutManager;
    private int Postion;
    public View LocationNo_EditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        HamButtonBuilderManager.setHamButtonText(HamButtonBuilderManager.mainTextId);//设置右bambutton文字
        super.onCreate(savedInstanceState);
        mainActivity = this;
        setActionTitle(R.string.text_outside_circle_button_text_0);
        if(!Global.isSuccessUpdataHttpdata) Toast.makeText(MyApp.getContext(),"供货商数据更新失败",Toast.LENGTH_SHORT).show();
        initHanButton();
        ToMain();
    }

    private boolean check(){
        int size = Global.upLoad.list.size();
        if(size == 0){
            Msg.showMsg(MainActivity.this,"警告","请扫描数据后再次提交",null);
            return false;
        }
        for(int i = 0;i<size;i++){
            if(!Global.isNullorEmpty(Global.upLoad.list.get(i).quantity) && !Global.isNullorEmpty(Global.upLoad.list.get(i).EXP) && !Global.isNullorEmpty(Global.upLoad.list.get(i).MFG) && !Global.isNullorEmpty(Global.upLoad.list.get(i).LOT)){
                //通过
            }else {
                //不通过
                MoveToPosition(layoutManager,recyclerView,i);
                Msg.showMsg(MainActivity.this,"警告","请填写完整后再次提交\n错误发生在" + i + "条数据",null);
                return false;
            }
        }
        return true;
    }

    /**
     * RecyclerView 移动到当前位置，
     *
     * @param manager   设置RecyclerView对应的manager
     * @param mRecyclerView  当前的RecyclerView
     * @param n  要跳转的位置
     */
    public static void MoveToPosition(LinearLayoutManager manager, RecyclerView mRecyclerView, int n) {


        int firstItem = manager.findFirstVisibleItemPosition();
        int lastItem = manager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            mRecyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        } else {
            mRecyclerView.scrollToPosition(n);
        }

    }

    private void initHanButton() { //初始化右菜单
        setHamButtonClick(new HamButtonClick() {
            @Override
            public void onClick(int index, BoomButton boomButton) {
                switch (index){
                    case 0:
                        if(check()) {
                            Msg.showMsg(MainActivity.this, "确定", "确定上传吗？", new Msg.CallBack() {
                                @Override
                                public void confirm(DialogPlus dialog) {
                                    Msg.wait(MainActivity.this,"提示","上传中,请稍等（此操作需要1-20秒左右）");
                                    Http.getInstance().Get_rk_detail(Global.upLoad.procure_no, Global.upLoad.come_goods_no, DJson.ObjectToJson(Global.upLoad.list), new Http.Callback() {
                                        @Override
                                        public void done(String data) {
                                            Msg.stopwait();
                                            if ("提交成功".equals(data.substring(0, 4))) {
                                                Global.upLoad = new UpLoad();
                                                ToMain();
                                                Msg.showMsg(MainActivity.this, "提示", data, null);
                                            } else {
                                                Msg.showMsg(MainActivity.this, "警告", data, null);
                                            }
                                        }
                                    });
                                }
                            });
                        }
                        break;
                    case 1:
                        ToMain();
                        break;
                    case 2:
                        Msg.showMsg(MainActivity.this,"警告", "此举将清空所有已扫描数据 您确定吗？", new Msg.CallBack() {
                            @Override
                            public void confirm(DialogPlus dialog) {
                                Global.upLoad = new UpLoad();
                                ToMain();
                            }
                        });
                        break;
                    default:
                }
            }
        });
    }

    private void ToScanner(){
        Msg.showMsg(MainActivity.this,"提示","请扫描商品条码",null,null);
        Global.setTYPE_SCA(Global.ScanType.rk_GoodsNo);
        setContentView(R.layout.handle);
        tabltLayout = (TableLayout) findViewById(R.id.table);
        BindRectclerView();
    }

    private void BindRectclerView(){

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new ScanDataAdapter();
        recyclerView.setAdapter(adapter);
        swipeToAction = new SwipeToAction(recyclerView, new SwipeToAction.SwipeListener<UpLoad.ScanData>() {
            @Override
            public boolean swipeLeft(final UpLoad.ScanData itemData) {
                //do something
                Msg.showMsg(MainActivity.this, "警告", "是否确定删除该条目？", new Msg.CallBack() {
                    @Override
                    public void confirm(DialogPlus dialog) {
                        dialog.dismiss();
                        final int pos = removeScanData(itemData);
                        if(-1 != pos) {
                            displaySnackbar(pos + "-移除" + itemData.barcode + "的商品", "撤销", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    addScanData(pos, itemData);
                                }
                            });
                        }
                    }
                });
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
                SQLite.Goods goods = SQLite.getInstance().getGoodsByGoodNo(itemData.goods_no);
                if (goods != null) {
                    Global.ShowUI_map = Global.GoodsToJMap(goods);
                    Global.ShowUI_Scanmap = Global.ScanDataToJMap(itemData);
                    int pos = removeScanData(itemData);
                    Intent intent1 = new Intent(MainActivity.this, ScanRActivity.class);
                    intent1.putExtra("postion",pos);
                    Global.ScanUpdateActivity = "MainActivity";
                    startActivity(intent1);
                }
                else {
                    Msg.showMsg(MainActivity.this,"提示","商品库未找到该商品信息,更新商品数据后重试",null);
                }

            }

            @Override
            public void onLongClick(UpLoad.ScanData itemData) {
                //do something
            }
        });
    }

    private void alertUpdateScannData(){
        SQLite.Goods goods = SQLite.getInstance().getGoodsByGoodNo(Global.upLoad.list.get(Postion).goods_no);
        final DialogPlus dialog = DialogPlus.newDialog(this)
                .setAdapter(new MsgShowScanAdapter(MainActivity.this,goods,Global.upLoad.list.get(Postion)))
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

    private void ToMain() {
        Global.setTYPE_SCA(Global.ScanType.rk_ComeGoodsNo);
        setContentView(R.layout.procure);
        EditText procure_no = (EditText) findViewById(R.id.procure_no);
        final EditText procure_key = (EditText) findViewById(R.id.procure_key);
        EditText come_goods_no = (EditText) findViewById(R.id.come_goods_no);
        procure_no.setText(Global.upLoad.procure_no);
        come_goods_no.setText(Global.upLoad.come_goods_no);
        procure_no.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Global.setTYPE_SCA(Global.ScanType.rk_Procure);
                return false;
            }
        });
        come_goods_no.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Global.setTYPE_SCA(Global.ScanType.rk_ComeGoodsNo);
                return false;
            }
        });
        findViewById(R.id.get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(InputTools.KeyBoard(procure_key)){
                    InputTools.HideKeyboard(procure_key);
                }
                String key = procure_key.getText().toString();
                if(!Global.isNullorEmpty(key)){
                    Msg.wait(MainActivity.this,"提示","查询中...");
                    Http.getInstance().Get_client_info(key, new Http.OBJCallback() {
                        @Override
                        public void done(String isSuccess, List data) {
                            Log.d("", "done: " + isSuccess );
                            Msg.stopwait();
                            if(Http.getInstance().Success.equals(isSuccess)){
                                procuretoshow(data);
                            }
                        }
                    });
                }else {
                    Toast.makeText(MainActivity.this,"关键字不能为空",Toast.LENGTH_LONG).show();
                }
            }
        });
        findViewById(R.id.handle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String no = ((EditText)findViewById(R.id.procure_no)).getText().toString();
                String com_no = ((EditText)findViewById(R.id.come_goods_no)).getText().toString();
                if(!Global.isNullorEmpty(no)) {
                    Global.PROCURENO = no;
                    Global.upLoad.procure_no = no;
                    Global.upLoad.come_goods_no = com_no;
                    ToScanner();
                }else {
                    Toast.makeText(MyApp.getContext(),"请选择供货商",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void procuretoshow(List data){
//        RecyclerView recyclerview_list = new RecyclerView(MainActivity.this);
//        recyclerview_list.setLayoutManager(new LinearLayoutManager(MainActivity.this));
//        recyclerview_list.setAdapter(new ClintInfoDataAdapter(data));
//        LinearLayout content = ((LinearLayout)Global.dialog.getHolderView().findViewById(R.id.show_list_content));
////        content.removeAllViews();
//        content.addView(recyclerview_list,50,50);
        Msg.showFunciton(MainActivity.this, "选择供货商", data, new OnItemClickListener() {
            @Override
            public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                dialog.dismiss();
                ClintInfo info = (ClintInfo) item;
                ((EditText) findViewById(R.id.procure_key)).setText(info.client_name);
                ((EditText)findViewById(R.id.procure_no)).setText(info.client_no);
            }
        });

    }

    private void displaySnackbar(String text, String actionName, View.OnClickListener action) {
        Snackbar snack = Snackbar.make(findViewById(android.R.id.content), text, Snackbar.LENGTH_LONG)
                .setAction(actionName, action);

        View v = snack.getView();
        v.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        ((TextView) v.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(Color.WHITE);
        ((TextView) v.findViewById(android.support.design.R.id.snackbar_action)).setTextColor(Color.WHITE);

        snack.show();
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
    protected void onResume() {
        super.onResume();
        Global.ifCloseInput(MainActivity.this);
        if(!Global.isSuccessUpdataHttpdata){
            Msg.showMsg(this, "警告", "未能成功更新数据,请检查网络后重试", new Msg.CallBack() {
                @Override
                public void confirm(DialogPlus dialog) {
                    dialog.dismiss();
                }
            });
        }
        //注册广播来获取扫描结果
     //   this.registerReceiver(mReceiver, mFilter);
    }

    @Override
    protected void onPause() {
        //注销获取扫描结果的广播
      //  this.unregisterReceiver(mReceiver);
        Msg.stopwait();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mReceiver = null;
        mFilter = null;
        super.onDestroy();

    }


    //扫描结果处理
    public void dealScanData(String data){

        if(Global.dialog != null){  //看看是否有弹窗存在
            Global.dialog.dismiss();
        }

        List<SQLite.Goods> goods = SQLite.getInstance().getGoods(data);

        if (goods != null) {
            if(goods.size() == 1) {
                SQLite.Goods good = goods.get(0);
                Msg.showSacn(this, good);
            }else {
                //同一条码不同编号弹窗处理
                Msg.showFunciton(MainActivity.this, "请选择商品编号", goods, new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                        dialog.dismiss();
                        SQLite.Goods good = (SQLite.Goods) item;
                        Msg.showSacn(MainActivity.this, good);
                    }
                });
            }

        } else {
            Toast.makeText(MyApp.getContext(), "未找到商品" + data, Toast.LENGTH_SHORT).show();
        }
    }
}




