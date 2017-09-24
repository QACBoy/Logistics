package com.example.scandemo5.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.scandemo5.MyApp;
import com.example.scandemo5.R;
import com.example.scandemo5.Utils.Encryption;
import com.example.scandemo5.Utils.Global;
import com.example.scandemo5.Utils.Http;
import com.example.scandemo5.Utils.HttpData;
import com.example.scandemo5.Utils.SQLite;
import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;

import java.util.HashMap;
import java.util.Map;

import info.hoang8f.widget.FButton;

public class WelcomeActivity extends AppCompatActivity {

    private AnimatedCircleLoadingView loadingview;
    private int SET_RequestCode = 10101;
    private FButton login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Global.firstIn=isFirstin();
        if(Global.firstIn){
            startActivityForResult(new Intent(WelcomeActivity.this,SetActivity.class),SET_RequestCode);
        }else {
            if (isLogin()) {
                toLoad();
            } else {
//                toLoad();
                toLogin();
            }
        }
//        GetHttpData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SET_RequestCode){
            if(resultCode == 1){
                toLogin();
            }
        }
    }

    private void toLogin(){
        setContentView(R.layout.login);
        login_btn = (FButton) findViewById(R.id.btn_login);
        ((EditText)findViewById(R.id.username)).setText(Global.getSharedPreferences().getString("username",""));
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_btn.setEnabled(false);
                login_btn.setText("登录中...");
                String username = ((EditText)findViewById(R.id.username)).getText().toString();
                String password = ((EditText)findViewById(R.id.password)).getText().toString();
                Map map = new HashMap();
                map.put("as_user",username);
                map.put("as_password", Encryption.md5(password));
                Global.getSharedPreferences().edit().putString("username",username).commit();
                Http.getInstance().Post(Http.getInstance().access, map, new Http.Callback() {
                    @Override
                    public void done(String data) {
                        if(data != null && !"NetError".equals(data)) {
                            data = Global.DealXmlStr(data);
                            if ("-1".equals(data)) {
                                Toast.makeText(WelcomeActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                            } else {
                                Global.getSharedPreferences().edit().putBoolean("isLogin", true).commit();
                                Global.getSharedPreferences().edit().putString("usercode", data).commit();
                                toLoad();
                            }
                        }else {
                            Toast.makeText(WelcomeActivity.this, "连接失败，请检查网络配置", Toast.LENGTH_SHORT).show();

                        }
                        login_btn.setEnabled(true);
                        login_btn.setText("登录");
                    }
                });
            }
        });
    }

    public void toLoad(){
        setContentView(R.layout.activity_welcome);
        loadingview = (AnimatedCircleLoadingView) findViewById(R.id.circle_loading_view);
        loadingview.startDeterminate();
        GetHttpData();
    }

    private void GetHttpData(){

        try {
            HttpData.getInstance().GetHttpData(new HttpData.CallBack() {
                @Override
                public void done(boolean isSuccess) {
                    Global.isSuccessUpdataHttpdata = isSuccess;
                    if(isSuccess){
                        loadingview.stopOk();
                    }else {
                        loadingview.stopFailure();
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
//                            try {
//                                Thread.sleep(3000);
                                startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
                                finish();
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
                        }
                    }).start();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        //加载商品信息
//        Http.getInstance().Post(Http.getInstance().get_goods_info,null, new Http.Callback() {
//            @Override
//            public void done(String data) {
//                if(data != null && !"NetError".equals(data)) {
//                    data = Global.DealXmlStr(data);
//                    loadingview.setPercent(0);
//                    Log.d("1235", "done: 开始插入" + data);
//                    final String finalData = data;
//                    loadingview.setPercent(10);
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            SQLite.getInstance().InsertGoodsAll(finalData);
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    loadingview.setPercent(50);
//                                    Log.d("1235", "done: 商品更新完成");
////                                  Log.d("1235", "done: " + SQLite.getInstance().getGoods("0601402011").goods_name);
//                                    //加载供货商信息
//                                    Http.getInstance().Post(Http.getInstance().get_procure_list, null, new Http.Callback() {
//                                        @Override
//                                        public void done(String data) {
//                                            if(data != null && !"NetError".equals(data)) {
//                                                loadingview.setPercent(70);
//                                                data = Global.DealXmlStr(data);
//                                                loadingview.setPercent(80);
//                                                Log.d("1235", "done: 开始插入" + data);
//                                                SQLite.getInstance().InsertProcureAll(data);
//                                                loadingview.setPercent(100);
//                                                Log.d("1235", "done: 供货商更新完成");
//                                                loadingview.stopOk();
//                                                Toast.makeText(MyApp.getContext(),"数据更新完成",Toast.LENGTH_SHORT).show();
//                                                new Thread(new Runnable() {
//                                                    @Override
//                                                    public void run() {
//                                                        try {
//                                                            Thread.sleep(3000);
//                                                            startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
//                                                            finish();
//                                                        } catch (InterruptedException e) {
//                                                            e.printStackTrace();
//                                                        }
//                                                    }
//                                                }).start();
//                                            }else {
//                                                loadingview.stopFailure();
//                                                Log.d("1235", "done: 获取供货商网络数据失败");
//                                                Toast.makeText(MyApp.getContext(),"供货商数据更新失败",Toast.LENGTH_SHORT).show();
//                                                new Thread(new Runnable() {
//                                                    @Override
//                                                    public void run() {
//                                                        try {
//                                                            Thread.sleep(3000);
//                                                            startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
//                                                            finish();
//                                                        } catch (InterruptedException e) {
//                                                            e.printStackTrace();
//                                                        }
//                                                    }
//                                                }).start();
//                                            }
//                                        }
//                                    });
//                                }
//                            });
//                        }
//                    }).start();
//                }else {
//                    loadingview.stopFailure();
//                    Toast.makeText(MyApp.getContext(),"商品数据更新失败",Toast.LENGTH_SHORT).show();
//                    Log.d("1235", "done: 获取商品网络数据失败");
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                Thread.sleep(3000);
//                                startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
//                                finish();
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }).start();
//                }
//            }
//        });
    }

//    private void toSet(){
//        setContentView(R.layout.settings);
//        EditText url = (EditText) findViewById(R.id.Url);
//        url.setText(Global.getSharedPreferences().getString("url",""));
//        findViewById(R.id.Save).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EditText url = (EditText) findViewById(R.id.Url);
//                String str = url.getText().toString();
//                if(!str.isEmpty()){
//                    Global.getSharedPreferences().edit().putString("url",str).commit();
//                    Global.getSharedPreferences().edit().putBoolean("isFirst", false).commit();
//                    toLogin();
//                }else {
//                    Toast.makeText(WelcomeActivity.this,"输入不能为空",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }

    private boolean isFirstin(){
        return Global.getSharedPreferences().getBoolean("isFirst",true);
    }

    private boolean isLogin(){
        return Global.getSharedPreferences().getBoolean("isLogin",false);
    }
}
