package com.example.scandemo5.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scandemo5.R;
import com.example.scandemo5.Utils.Global;
import com.example.scandemo5.Utils.HttpData;
import com.example.scandemo5.Utils.Msg;
import com.orhanobut.dialogplus.DialogPlus;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

public class SetActivity extends AppCompatActivity {

    private EditText url;
    private Handler handler;

    private int SYCN = 1000110;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        handler = new Handler(){  //处理线程间通信
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what == SYCN){
                    findViewById(R.id.sycn).setEnabled(true);//恢复按钮可用
                    if(Global.isSuccessUpdataHttpdata){
                        Msg.showMsg(SetActivity.this, "提示", "数据更新成功", null);
                    }else {
                        Msg.showMsg(SetActivity.this, "警告", "数据更新失败，请检查网络", null);
                    }
                }
            }
        };

        int primary = getResources().getColor(R.color.colorPrimary);
        int secondary = getResources().getColor(R.color.colorPrimaryDark);
        SlidrInterface slidrInterface = Slidr.attach(this, primary, secondary);

        //URL设置
        url = (EditText) findViewById(R.id.Url);
        url.setText(Global.getSharedPreferences().getString("url",""));
        ((TextView)findViewById(R.id.user_set)).setText(Global.getSharedPreferences().getString("username","获取失败"));

        //软键盘设置
        ((Switch)findViewById(R.id.keyboard)).setChecked(Global.getSharedPreferences().getBoolean("ifOpenKeyboard",false));
        ((Switch)findViewById(R.id.keyboard)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Global.getSharedPreferences().edit().putBoolean("ifOpenKeyboard",isChecked).commit();
                if(!isChecked){
                    finish();
                    closeInput();
                    startActivity(new Intent(MainActivity.mainActivity ,SetActivity.class));
                }else {
                    Global.ifCloseInput(SetActivity.this);
                }
            }
        });

        //同步数据
        findViewById(R.id.sycn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.sycn).setEnabled(false);//点击后禁用以防多次点击
                try {
                    HttpData.getInstance().GetHttpData(new HttpData.CallBack() {
                        @Override
                        public void done(boolean isSuccess) {
                            Global.isSuccessUpdataHttpdata = isSuccess;
                            handler.sendEmptyMessage(SYCN);
                        }
                    });
                } catch (Exception e) {
                    Log.d("1122", "onClick: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        //注销账户
        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Msg.showMsg(SetActivity.this, "警告", "此操作将会注销当前账户，确定继续吗？", new Msg.CallBack() {
                    @Override
                    public void confirm(DialogPlus dialog) {
                        Global.getSharedPreferences().edit().putBoolean("isLogin",false).commit();
                        startActivity(new Intent(SetActivity.this,WelcomeActivity.class));
                        finish();
                    }
                });
            }
        });
    }

    private void closeInput(){
        InputMethodManager inputmanger = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        inputmanger.hideSoftInputFromWindow(getWindow().peekDecorView().getWindowToken(), 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        String str = url.getText().toString();
        if(!str.isEmpty()){
            Global.getSharedPreferences().edit().putString("url",str).commit();
            Global.getSharedPreferences().edit().putBoolean("isFirst", false).commit();
//                    toLogin();
            setResult(1);
//            finish();
        }else {
            Toast.makeText(SetActivity.this,"输入不能为空，保存失败",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Global.ifCloseInput(SetActivity.this);
    }
}
