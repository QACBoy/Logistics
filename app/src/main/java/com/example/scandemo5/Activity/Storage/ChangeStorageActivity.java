package com.example.scandemo5.Activity.Storage;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.example.scandemo5.Adapter.ScanDataAdapter;
import com.example.scandemo5.Data.GoodsInfo;
import com.example.scandemo5.Data.LocationInfo;
import com.example.scandemo5.Data.UpLoad;
import com.example.scandemo5.R;
import com.example.scandemo5.Utils.HamButtonBuilderManager;
import com.example.scandemo5.Utils.DJson;
import com.example.scandemo5.Utils.Global;
import com.example.scandemo5.Utils.Http;
import com.example.scandemo5.Utils.InputTools;
import com.example.scandemo5.Utils.Msg;
import com.example.scandemo5.Utils.RMap;
import com.example.scandemo5.Utils.SQLite;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnItemClickListener;

import org.feezu.liuli.timeselector.TimeSelector;

import java.util.List;

import co.dift.ui.SwipeToAction;

public class ChangeStorageActivity extends BaseActivity {

    public String location_no_old;
    public String location_no_traget;

    public static ChangeStorageActivity activity;
    public static TableLayout tabltLayout;
    private RecyclerView recyclerView;
    private ScanDataAdapter adapter;
    private SwipeToAction swipeToAction;
    private EditText locationno;
    private Button button;
    private LocationInfo locationinfo;
    private int Postion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        HamButtonBuilderManager.setHamButtonText(HamButtonBuilderManager.changestoragetextId);//设置右bambutton文字
        super.onCreate(savedInstanceState);
        activity = this;
        setActionTitle(R.string.text_outside_circle_button_text_1);
        initHanButton();
        toStorageNo();
    }

    private void initHanButton() { //初始化右菜单
        setHamButtonClick(new HamButtonClick() {
            @Override
            public void onClick(int index, BoomButton boomButton) {
                switch (index){
                    case 0:
                        //上传移库数据
                        if(Global.isNullorEmpty(location_no_old)){
                            Toast.makeText(ChangeStorageActivity.this,"请扫描库位单号",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        toTraget();
                        break;
                    case 1:
                        Msg.showMsg(ChangeStorageActivity.this,"警告", "此举将清空所有已修改数据 您确定吗？", new Msg.CallBack() {
                            @Override
                            public void confirm(DialogPlus dialog) {
                                Global.upLoad = new UpLoad();
                                toStorageNo();
                            }
                        });
                        break;
                }
            }
        });
    }

    private void toTraget(){
        Global.setTYPE_SCA(Global.ScanType.kw_stroageno_traget);
        setContentView(R.layout.activity_change_storage);
        locationno = (EditText) findViewById(R.id.changestorage_stroageno);
        locationno.setHint("请扫描 目标库位");
        button = (Button) findViewById(R.id.changestorage_ok);
        button.setText("确认移动");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void toStorageNo(){
        Global.setTYPE_SCA(Global.ScanType.kw_stroageno);
        setContentView(R.layout.activity_change_storage);
        locationno = (EditText) findViewById(R.id.changestorage_stroageno);
        locationno.setInputType(InputType.TYPE_CLASS_NUMBER);
        button = (Button) findViewById(R.id.changestorage_ok);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location_no_old = locationno.getText().toString();
                if(Global.isNullorEmpty(location_no_old)){
                    Toast.makeText(ChangeStorageActivity.this,"请扫描库位单号",Toast.LENGTH_SHORT).show();
                    return;
                }
                InputTools.HideKeyboard(locationno);
                Msg.wait(ChangeStorageActivity.this,"提示","查询中商品数据...");
                Http.getInstance().Get_location_stock(locationno.getText().toString(), new Http.OBJCallback() {
                    @Override
                    public void done(String isSuccess, List data) {
                        Msg.stopwait();
                        if(Http.getInstance().Success.equals(isSuccess))
                            toShow(data);
                    }
                });
            }
        });
    }

    private void toShow(List data){
        Log.d("12222", "toShow: " + data.size());
        Global.setTYPE_SCA(Global.ScanType.Unknown);//扫描完切换到未知模式，防止崩溃
        //处理网络数据
        List<GoodsInfo> list = data;
        for(int i = 0;i<list.size();i++){
            Global.upLoad.list.add(new UpLoad.ScanData(
                    list.get(i).barcode,
                    list.get(i).goods_no,
                    list.get(i).goods_name,
                    list.get(i).mfg,
                    list.get(i).exp,
                    list.get(i).lot,
                    list.get(i).location_no,
                    Global.dealExtraZero(list.get(i).free_stock)
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
            public boolean swipeLeft(final UpLoad.ScanData itemData) {
                //do something
                Msg.showMsg(ChangeStorageActivity.this, "警告", "是否确定不移动删除该条目？", new Msg.CallBack() {
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
                    Global.ScanRisEditable = false;//设置成不可编辑
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

    private void displaySnackbar(String text, String actionName, View.OnClickListener action) {
        Snackbar snack = Snackbar.make(findViewById(android.R.id.content), text, Snackbar.LENGTH_LONG)
                .setAction(actionName, action);

        View v = snack.getView();
        v.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        ((TextView) v.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(Color.WHITE);
        ((TextView) v.findViewById(android.support.design.R.id.snackbar_action)).setTextColor(Color.WHITE);

        snack.show();
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

                        if(position > 0) {  //只有数量才让改
                            tValue.setEnabled(false);
                        }else {
                            tValue.setSingleLine();
                            tValue.setInputType(InputType.TYPE_CLASS_NUMBER);
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
                                if(Double.valueOf(sl) <=  Double.valueOf(Global.upLoad.list.get(Postion).quantity)) {
                                    if(Double.valueOf(sl) < 0){
                                        Toast.makeText(ChangeStorageActivity.this,"移动数量不能小于0",Toast.LENGTH_LONG).show();
                                    }else {
                                        Global.upLoad.list.get(Postion).quantity = sl;
                                        adapter.notifyItemChanged(Postion);
                                        dialog.dismiss();
                                    }
                                }else {
                                    Toast.makeText(ChangeStorageActivity.this,"移动数量不能大于库存数量",Toast.LENGTH_LONG).show();
                                }
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

}
