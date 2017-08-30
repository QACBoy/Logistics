package com.example.scandemo5.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
import com.example.scandemo5.Utils.Http;
import com.example.scandemo5.Utils.SQLite;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrInterface;
import com.r0adkll.slidr.model.SlidrListener;
import com.r0adkll.slidr.model.SlidrPosition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.dift.ui.SwipeToAction;

import static com.r0adkll.slidr.model.SlidrPosition.BOTTOM;
import static com.r0adkll.slidr.model.SlidrPosition.HORIZONTAL;
import static com.r0adkll.slidr.model.SlidrPosition.RIGHT;
import static com.r0adkll.slidr.model.SlidrPosition.TOP;
import static com.r0adkll.slidr.model.SlidrPosition.VERTICAL;

public class MainActivity extends AppCompatActivity {

    TReceiver mReceiver;  //接收广播信息
    private IntentFilter mFilter;
    public static MainActivity mainActivity;
    public static TableLayout tabltLayout;
    private RecyclerView recyclerView;
    private ScanDataAdapter adapter;
    private SwipeToAction swipeToAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToMain();
        mainActivity = this;
    }



    private void ToScanner(){
        Global.setTYPE_SCA(Global.GoodsNo);
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
                    displaySnackbar("移除编码为" + itemData.barcode + "的商品", "撤销", new View.OnClickListener() {
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
                int pos = Global.upLoad.list.indexOf(itemData);

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

    private void ToMain() {
        Global.setTYPE_SCA(Global.Procure);
        setContentView(R.layout.procure);
        findViewById(R.id.handle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String no = ((EditText)findViewById(R.id.procure_no)).getText().toString();
                if(no != null && !"".equals(no.trim())) {
                    Global.PROCURENO = no;
                    Global.upLoad.procure_no = no;
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
            vh.imageView.setImageResource(R.mipmap.ic_book_black_24dp);
            vh.data = item;
        }

        @Override
        public int getItemCount() {
            return Global.upLoad.list.size();
        }

        public class ScanDataViewHolder extends SwipeToAction.ViewHolder<UpLoad.ScanData> {

            public TextView titleView;
            public TextView authorView;
            public ImageView imageView;

            public ScanDataViewHolder(View v) {
                super(v);

                titleView = (TextView) v.findViewById(R.id.title);
                authorView = (TextView) v.findViewById(R.id.author);
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

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ToSet();
        return super.onOptionsItemSelected(item);
    }

    private void ToSet() {
        setContentView(R.layout.settings);
        EditText url = (EditText) findViewById(R.id.Url);
        url.setText(Global.getSharedPreferences().getString("url",""));
        findViewById(R.id.Save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText url = (EditText) findViewById(R.id.Url);
                String str = url.getText().toString();
                if(!str.isEmpty()){
                    Global.getSharedPreferences().edit().putString("url",str).commit();
                    Global.getSharedPreferences().edit().putBoolean("isFirst", false).commit();
                    ToMain();
                }else {
                    Toast.makeText(MainActivity.this,"输入不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}




