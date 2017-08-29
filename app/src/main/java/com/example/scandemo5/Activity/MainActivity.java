package com.example.scandemo5.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

import com.example.scandemo5.Data.UpLoad;
import com.example.scandemo5.MyApp;
import com.example.scandemo5.R;
import com.example.scandemo5.Recevier.TReceiver;
import com.example.scandemo5.Utils.Global;
import com.example.scandemo5.Utils.Http;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrInterface;
import com.r0adkll.slidr.model.SlidrListener;
import com.r0adkll.slidr.model.SlidrPosition;

import java.util.HashMap;
import java.util.Map;

import static com.r0adkll.slidr.model.SlidrPosition.BOTTOM;
import static com.r0adkll.slidr.model.SlidrPosition.HORIZONTAL;
import static com.r0adkll.slidr.model.SlidrPosition.RIGHT;
import static com.r0adkll.slidr.model.SlidrPosition.TOP;
import static com.r0adkll.slidr.model.SlidrPosition.VERTICAL;

public class MainActivity extends AppCompatActivity {

    TReceiver mReceiver;  //接收广播信息
    private IntentFilter mFilter;
    public SharedPreferences setting;
    public static MainActivity mainActivity;
    public static TableLayout tabltLayout;
    public static Button sure;
    public static Button history;

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
//        sure = (Button) findViewById(R.id.sure);
//        history = (Button) findViewById(R.id.history);
//        sure.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(validate()){
//                    String barcode = ((EditText)tabltLayout.getChildAt(3).findViewById(R.id.handle_item_value)).getText().toString();
//                    String goods_no = ((EditText)tabltLayout.getChildAt(1).findViewById(R.id.handle_item_value)).getText().toString();
//                    String goods_name = ((EditText)tabltLayout.getChildAt(2).findViewById(R.id.handle_item_value)).getText().toString();
//                    String MFG = ((EditText)tabltLayout.getChildAt(15).findViewById(R.id.handle_item_value)).getText().toString();
//                    String EXP = ((EditText)tabltLayout.getChildAt(16).findViewById(R.id.handle_item_value)).getText().toString();
//                    String LOT = ((EditText)tabltLayout.getChildAt(14).findViewById(R.id.handle_item_value)).getText().toString();
//                    String quantity = ((EditText)tabltLayout.getChildAt(13).findViewById(R.id.handle_item_value)).getText().toString();
//                    Global.upLoad.add(new UpLoad.ScanData(barcode,goods_no,goods_name,MFG,EXP,LOT,quantity));
//                    tabltLayout.removeAllViewsInLayout();
//                    tabltLayout.setBackgroundColor(Color.WHITE);
//                    tabltLayout.invalidate();
//                    sure.setEnabled(false);
//                    history.setEnabled(true);
//                }
//            }
//        });
//        history.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("123456",Global.upLoad.toJson());
//            }
//        });
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

    public void StartActivity(){
        startActivity(new Intent(MainActivity.mainActivity, ScanRActivity.class));
    }

    private boolean validate(){
        String str = ((EditText)tabltLayout.getChildAt(13).findViewById(R.id.handle_item_value)).getText().toString();
        if(!"".equals(str) && str != null){
            if(new Integer(str) > 0) {
                str = ((EditText) tabltLayout.getChildAt(14).findViewById(R.id.handle_item_value)).getText().toString();
                if(!"".equals(str) && str != null){
                    str = ((EditText) tabltLayout.getChildAt(15).findViewById(R.id.handle_item_value)).getText().toString();
                    if(!"".equals(str) && str != null){
                        str = ((EditText) tabltLayout.getChildAt(16).findViewById(R.id.handle_item_value)).getText().toString();
                        if(!"".equals(str) && str != null){
                            return true;
                        }else {
                            Toast.makeText(MainActivity.this,"请选择到期日期",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(MainActivity.this,"请选择生产日期",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(MainActivity.this,"请输入批次号",Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(MainActivity.this,"数量必需大于0",Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(MainActivity.this,"请输入数量",Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
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




