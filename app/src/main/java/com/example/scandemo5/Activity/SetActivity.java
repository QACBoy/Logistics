package com.example.scandemo5.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scandemo5.R;
import com.example.scandemo5.Utils.Global;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

public class SetActivity extends AppCompatActivity {

    private EditText url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        int primary = getResources().getColor(R.color.colorPrimary);
        int secondary = getResources().getColor(R.color.colorPrimaryDark);
        SlidrInterface slidrInterface = Slidr.attach(this, primary, secondary);
        url = (EditText) findViewById(R.id.Url);
        url.setText(Global.getSharedPreferences().getString("url",""));
        ((TextView)findViewById(R.id.user_set)).setText(Global.getSharedPreferences().getString("username","获取失败"));


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
            finish();
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
