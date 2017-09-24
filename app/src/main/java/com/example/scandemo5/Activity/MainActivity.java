package com.example.scandemo5.Activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scandemo5.Data.UpLoad;
import com.example.scandemo5.MyApp;
import com.example.scandemo5.R;
import com.example.scandemo5.Recevier.TReceiver;
import com.example.scandemo5.Utils.Global;
import com.example.scandemo5.Utils.JMap;
import com.example.scandemo5.Utils.RMap;
import com.example.scandemo5.Utils.SQLite;
import com.example.scandemo5.Utils.Msg;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.OnItemClickListener;

import org.feezu.liuli.timeselector.TimeSelector;

import co.dift.ui.SwipeToAction;

public class MainActivity extends BaseActivity {

    TReceiver mReceiver;  //接收广播信息
    private IntentFilter mFilter;
    public static MainActivity mainActivity;
    public static TableLayout tabltLayout;
    private RecyclerView recyclerView;
    private ScanDataAdapter adapter;
    private SwipeToAction swipeToAction;
    private int Postion;
    public View LocationNo_EditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!Global.isSuccessUpdataHttpdata) Toast.makeText(MyApp.getContext(),"供货商数据更新失败",Toast.LENGTH_SHORT).show();
        ToMain();
        mainActivity = this;
    }

    private void ToScanner(){
        Global.setTYPE_SCA(Global.ScanType.rk_GoodsNo);
        setContentView(R.layout.handle);
        tabltLayout = (TableLayout) findViewById(R.id.table);
        BindRectclerView();
    }

    private void BindRectclerView(){

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new ScanDataAdapter();
        recyclerView.setAdapter(adapter);
        swipeToAction = new SwipeToAction(recyclerView, new SwipeToAction.SwipeListener<UpLoad.ScanData>() {
            @Override
            public boolean swipeLeft(final UpLoad.ScanData itemData) {
                //do something
                final int pos = removeScanData(itemData);
                if(-1 != pos) {
                    displaySnackbar(pos + "-移除" + itemData.barcode + "的商品", "撤销", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addScanData(pos, itemData);
                        }
                    });
                }
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
                    Intent intent1 = new Intent(MainActivity.mainActivity, ScanRActivity.class);
                    intent1.putExtra("postion",pos);
                    MainActivity.mainActivity.startActivity(intent1);
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
                        convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.handle_item, null);

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
                                    TimeSelector timeSelector = new TimeSelector(MainActivity.this, new TimeSelector.ResultHandler() {
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

    private void ToMain() {
        Global.setTYPE_SCA(Global.ScanType.rk_ComeGoodsNo);
        setContentView(R.layout.procure);
        EditText procure_no = (EditText) findViewById(R.id.procure_no);
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
        findViewById(R.id.handle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String no = ((EditText)findViewById(R.id.procure_no)).getText().toString();
                String com_no = ((EditText)findViewById(R.id.come_goods_no)).getText().toString();
                if(!Global.isNullorEmpty(no) && !Global.isNullorEmpty(com_no)) {
                    Global.PROCURENO = no;
                    Global.upLoad.procure_no = no;
                    Global.upLoad.come_goods_no = com_no;
                    ToScanner();
                }else {
                    Toast.makeText(MyApp.getContext(),"请扫描或者输入入库单号",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public class ScanDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_view, parent, false);

            return new ScanDataViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            UpLoad.ScanData item = Global.upLoad.list.get(position);
            ScanDataViewHolder vh = (ScanDataViewHolder) holder;
            vh.titleView.setText(item.barcode);
            vh.authorView.setText(item.goods_name);
            vh.lot.setText(item.LOT);
            vh.quantity.setText(item.quantity);
            if(!Global.isNullorEmpty(Global.upLoad.list.get(position).quantity) && !Global.isNullorEmpty(Global.upLoad.list.get(position).EXP) && !Global.isNullorEmpty(Global.upLoad.list.get(position).MFG) && !Global.isNullorEmpty(Global.upLoad.list.get(position).LOT)){
                vh.imageView.setImageResource(R.mipmap.accept);
            }else {
                vh.imageView.setImageResource(R.mipmap.warning);
            }
            vh.data = item;
        }

        @Override
        public int getItemCount() {
            return Global.upLoad.list.size();
        }

        public class ScanDataViewHolder extends SwipeToAction.ViewHolder<UpLoad.ScanData> {

            public TextView titleView;
            public TextView authorView;
            public TextView lot;
            public TextView quantity;
            public ImageView imageView;

            public ScanDataViewHolder(View v) {
                super(v);

                titleView = (TextView) v.findViewById(R.id.title);
                authorView = (TextView) v.findViewById(R.id.author);
                lot = (TextView) v.findViewById(R.id.lot);
                quantity = (TextView) v.findViewById(R.id.quantity);
                imageView = (ImageView) v.findViewById(R.id.image);
            }
        }
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
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mReceiver = null;
        mFilter = null;
        super.onDestroy();

    }

    private MenuItem count;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar, menu);
        count = menu.findItem(R.id.action_count);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isFinishing()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            count.setTitle(String.valueOf(Global.upLoad.list.size()));
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_upload:
                Msg.showMsg(this,"确定", "确定上传吗？", new Msg.CallBack() {
                    @Override
                    public void confirm(DialogPlus dialog) {
                        dialog.dismiss();
                    }
                });
                break;
            case R.id.action_update_no:
                ToMain();
                break;
            case R.id.action_reset:
                Msg.showMsg(this,"警告", "此举将清空所有已扫描数据 您确定吗？", new Msg.CallBack() {
                    @Override
                    public void confirm(DialogPlus dialog) {
                        Global.upLoad = new UpLoad();
                        ToMain();
                    }
                });
                break;
            case R.id.action_set:
                startActivity(new Intent(MainActivity.this,SetActivity.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    //扫描结果处理
    public void dealScanData(String data){

        if(Global.dialog != null){  //看看是否有弹窗存在
            Global.dialog.dismiss();
        }

        SQLite.Goods goods = SQLite.getInstance().getGoods(data);

        if (goods != null) {
//            Global.ShowUI_map = Global.GoodsToJMap(goods);
//            Global.ShowUI_Scanmap = Global.ScanDataToJMap(new UpLoad.ScanData());
//            Intent intent1 = new Intent(MainActivity.mainActivity, ScanRActivity.class);
//            startActivity(intent1);

            JMap<String, String> map = new JMap<>();
            map.add("goods_no", goods.goods_no);
            map.add("goods_name", goods.goods_name);
            map.add("barcode", goods.barcode);
            map.add("goods_spce", goods.goods_spce);
            map.add("quantity", "");
            map.add("LOT", "");
            map.add("location_no", "010001");
            map.add("MFG", "");
            map.add("EXP", "");

            Msg.showSacn(this,map);

        } else {
            Toast.makeText(MyApp.getContext(), "未找到商品" + data, Toast.LENGTH_SHORT).show();
        }
    }
}




