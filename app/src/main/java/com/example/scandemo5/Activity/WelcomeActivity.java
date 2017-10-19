package com.example.scandemo5.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.scandemo5.Activity.Storage.MainActivity;
import com.example.scandemo5.Data.UserInfo;
import com.example.scandemo5.R;
import com.example.scandemo5.Utils.DJson;
import com.example.scandemo5.Utils.Encryption;
import com.example.scandemo5.Utils.Global;
import com.example.scandemo5.Utils.Http;
import com.example.scandemo5.Utils.HttpData;
import com.example.scandemo5.Utils.User;
import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;

import java.util.HashMap;
import java.util.List;
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
            //待定

            if (isLogin()) {
                toLoad();
            } else {
                toLogin();
            }
        }
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
        ((EditText)findViewById(R.id.username)).setText(User.getUser().getUsername());
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
//                Global.getSharedPreferences().edit().putString("username",username).commit();
                User.getUser().setUsername(username);
                User.getUser().setPassword(password);
                Http.getInstance().Post(Http.getInstance().access, map, new Http.Callback() {
                    @Override
                    public void done(String data) {
                            if ("-1".equals(data)) {
                                Toast.makeText(WelcomeActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                            }
                            else if("NetError".equals(data)){
                                Log.d("", "done: " + data);
                            }
                            else {
                                Global.getSharedPreferences().edit().putBoolean("isLogin", true).commit();
                                List<UserInfo> userInfo = DJson.JsonToList(data,UserInfo.class);
                                User.getUser().setAs_user(userInfo.get(0).as_user);
                                User.getUser().setGroup_node_id(userInfo.get(0).group_node_id);
//                                Global.getSharedPreferences().edit().putString("usercode", data).commit();
                                toLoad();
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


    }


    private boolean isFirstin(){
        return Global.getSharedPreferences().getBoolean("isFirst",true);
    }

    private boolean isLogin(){
        return Global.getSharedPreferences().getBoolean("isLogin",false);
    }
}
