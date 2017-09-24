package com.example.scandemo5.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scandemo5.Adapter.ScanDataAdapter;
import com.example.scandemo5.Data.LocationInfo;
import com.example.scandemo5.Data.UpLoad;
import com.example.scandemo5.R;
import com.example.scandemo5.Utils.DJson;
import com.example.scandemo5.Utils.Global;
import com.example.scandemo5.Utils.Http;
import com.example.scandemo5.Utils.Msg;
import com.example.scandemo5.Utils.RMap;
import com.example.scandemo5.Utils.SQLite;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnItemClickListener;

import org.feezu.liuli.timeselector.TimeSelector;

import co.dift.ui.SwipeToAction;
import info.hoang8f.widget.FButton;

public class ChangeStorageActivity extends BaseActivity {

    public static ChangeStorageActivity activity;
    public static TableLayout tabltLayout;
    private RecyclerView recyclerView;
    private ScanDataAdapter adapter;
    private SwipeToAction swipeToAction;
    private EditText locationno;
    private FButton button;
    private LocationInfo locationinfo;
    private int Postion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        toStorageNo();
    }

    private void toStorageNo(){
        Global.setTYPE_SCA(Global.ScanType.kw_stroageno);
        setContentView(R.layout.activity_change_storage);
        locationno = (EditText) findViewById(R.id.changestorage_stroageno);
        button = (FButton) findViewById(R.id.changestorage_ok);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Global.isNullorEmpty(locationno.getText().toString())){
                    Toast.makeText(ChangeStorageActivity.this,"请扫描库位单号",Toast.LENGTH_SHORT).show();
                    return;
                }
                button.setEnabled(false);
                button.setText("获取数据中");
                Http.getInstance().Get(Http.getInstance().get_location + locationno.getText().toString(), new Http.Callback() {
                    @Override
                    public void done(String data) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                button.setEnabled(true);
                                button.setText("继续");
                            }
                        });
                        if(Global.isNullorEmpty(data) || "NetError".equals(data)){
                            Toast.makeText(ChangeStorageActivity.this,"网络数据获取失败，请检查网络",Toast.LENGTH_SHORT).show();
                        }else {
                            toShow(data);
                        }
                    }
                });
            }
        });
    }

    private void toShow(String data){
        Log.d("12222", "toShow: " + data);
        //处理网络数据
        locationinfo = DJson.JsonToObject(data,LocationInfo.class);
        if(Global.isNullorEmpty(locationinfo.location.location_no)){
            Msg.showMsg(ChangeStorageActivity.this,"警告","未找到该库位信息",null);
            return;
        }
        for(int i = 0;i<locationinfo.goods.size();i++){
            Global.upLoad.list.add(new UpLoad.ScanData(
                    locationinfo.goods.get(i).barcode,
                    locationinfo.goods.get(i).goods_no,
                    locationinfo.goods.get(i).goods_name,
                    locationinfo.goods.get(i).MFG,
                    locationinfo.goods.get(i).EXP,
                    locationinfo.goods.get(i).LOT,
                    locationinfo.goods.get(i).location_no,
                    locationinfo.goods.get(i).quantity
            ));
        }

        setContentView(R.layout.handle);
        tabltLayout = (TableLayout) findViewById(R.id.table);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
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
                    Intent intent1 = new Intent(ChangeStorageActivity.this, ScanRActivity.class);
                    intent1.putExtra("postion",pos);
                    Global.ScanUpdateActivity = "ChangeStorageActivity";
                    startActivity(intent1);
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
                        convertView = LayoutInflater.from(ChangeStorageActivity.this).inflate(R.layout.handle_item, null);

                        TextView tKey = (TextView) convertView.findViewById(R.id.handle_item_key);
                        EditText tValue = (EditText) convertView.findViewById(R.id.handle_item_value);

                        tKey.setText(RMap.getrMap().get(Global.ShowUI_Scanmap.get(position + 3)));  //从Global.ShowUI_Scanmap中第3项开始显示
                        tValue.setText(Global.ShowUI_Scanmap.get(Global.ShowUI_Scanmap.get(position+3)));

                        if(position > 2) {  //到生产日期才开始启用日期选择组件
                            tValue.setFocusableInTouchMode(false);
                            tValue.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(final View v) {
                                    TimeSelector timeSelector = new TimeSelector(ChangeStorageActivity.this, new TimeSelector.ResultHandler() {
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
                        return convertView;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_changestroage, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_upload:
                Msg.showMsg(this,"确定", "确定上传吗？", new Msg.CallBack() {
                    @Override
                    public void confirm(DialogPlus dialog) {
                        toUpload();
                        dialog.dismiss();
                    }
                });
                break;
            case R.id.action_reset:
                Msg.showMsg(this,"警告", "此举将清空所有已修改数据 您确定吗？", new Msg.CallBack() {
                    @Override
                    public void confirm(DialogPlus dialog) {
                        Global.upLoad = new UpLoad();
                        toStorageNo();
                    }
                });
                break;
            case R.id.action_set:
                startActivity(new Intent(ChangeStorageActivity.this,SetActivity.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void toUpload(){
        setContentView(R.layout.activity_change_storage);
        ((EditText)findViewById(R.id.changestorage_stroageno)).setHint("请扫描 移至库位");
        ((FButton)findViewById(R.id.changestorage_ok)).setText("确定移动商品");
    }
}
