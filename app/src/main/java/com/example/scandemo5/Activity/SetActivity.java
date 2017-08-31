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
import android.widget.Toast;

import com.example.scandemo5.R;
import com.example.scandemo5.Utils.Global;

public class SetActivity extends AppCompatActivity {

    private EditText url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        url = (EditText) findViewById(R.id.Url);
        url.setText(Global.getSharedPreferences().getString("url",""));
        findViewById(R.id.Save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = url.getText().toString();
                if(!str.isEmpty()){
                    Global.getSharedPreferences().edit().putString("url",str).commit();
                    Global.getSharedPreferences().edit().putBoolean("isFirst", false).commit();
//                    toLogin();
                    finish();
                }else {
                    Toast.makeText(SetActivity.this,"输入不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });

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
    protected void onResume() {
        super.onResume();
        Global.ifCloseInput(SetActivity.this);
    }
}
