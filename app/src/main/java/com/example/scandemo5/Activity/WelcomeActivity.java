package com.example.scandemo5.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scandemo5.Activity.Storage.MainActivity;
import com.example.scandemo5.Data.UserInfo;
import com.example.scandemo5.MyApp;
import com.example.scandemo5.R;
import com.example.scandemo5.Utils.DJson;
import com.example.scandemo5.Utils.Encryption;
import com.example.scandemo5.Utils.Global;
import com.example.scandemo5.Utils.Http;
import com.example.scandemo5.Utils.HttpData;
import com.example.scandemo5.Utils.InputTools;
import com.example.scandemo5.Utils.Msg;
import com.example.scandemo5.Utils.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable;
import info.hoang8f.widget.FButton;

public class WelcomeActivity extends AppCompatActivity {

    private int SET_RequestCode = 10101;
    private FButton login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        Global.firstIn=isFirstin();
        if(Global.firstIn){
            startActivityForResult(new Intent(WelcomeActivity.this,SetActivity.class),SET_RequestCode);
        }else {
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
        ((TextView)findViewById(R.id.login_title)).setText(getResources().getString(R.string.app_name));
        login_btn = (FButton) findViewById(R.id.btn_login);
        ((EditText)findViewById(R.id.username)).setText(User.getUser().getUsername());
        findViewById(R.id.password_field).performClick();
        findViewById(R.id.username_field).performClick();
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputTools.HideKeyboard(((EditText)findViewById(R.id.password)));
                String username = ((EditText)findViewById(R.id.username)).getText().toString();
                String password = ((EditText)findViewById(R.id.password)).getText().toString();
                if(Global.isNullorEmpty(username) || Global.isNullorEmpty(password)){
                    Toast.makeText(MyApp.getContext(), "请输入用户名或密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                Msg.wait(WelcomeActivity.this,"提示","登录中..");
                Http.getInstance().access(username, password, new Http.Callback() {
                    @Override
                    public void done(String data) {
                        Msg.stopwait();
                        if ("-1".equals(data)) {
                            Toast.makeText(MyApp.getContext(), "用户名或密码错误", Toast.LENGTH_SHORT).show();
                        }else if("0".equals(data)){
                            //网络错误这里不做提示
                        }
                        else {
                            toLoad();
                        }
                    }
                });
            }
        });
    }

    public void toLoad(){
        setContentView(R.layout.activity_welcome);
        ((TextView) findViewById(R.id.welcome_tv)).setText(getResources().getString(R.string.app_name));
        SmoothProgressBar mPocketBar = (SmoothProgressBar)findViewById(R.id.welcome_bar);
        mPocketBar.setIndeterminateDrawable(new SmoothProgressDrawable.Builder(WelcomeActivity.this).interpolator(new AccelerateInterpolator()).build());
        mPocketBar.setSmoothProgressDrawableColors(getResources().getIntArray(R.array.wait_colors));
        GetHttpData();
    }

    private void GetHttpData(){

        try {
            HttpData.getInstance().GetHttpData(new HttpData.CallBack() {
                @Override
                public void done(boolean isSuccess) {
                    Global.isSuccessUpdataHttpdata = isSuccess;
                    startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
                    finish();
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
