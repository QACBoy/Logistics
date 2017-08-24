package com.example.scandemo5;

import android.app.AlertDialog;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TReceiver mReceiver;  //接收广播信息
    private IntentFilter mFilter;
    public SharedPreferences setting;
    public static final int NOCHOOSE = 100001;
    public static final int AUTO = 100010;
    public static final int HANDLE = 100020;
    public static int type;
    public static MainActivity mainActivity;
    public static TableLayout tabltLayout;
    public static int isSubmit = 0;
    public static Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        //定义一个setting记录APP是几次启动！！！
        setting = getSharedPreferences("com.example.scandemo5", MODE_PRIVATE);

        Boolean user_first = setting.getBoolean("FIRST", true);
        if (user_first) {// 第一次则跳转到设置页面
            ToSet();
        } else {//如果是第二次启动则直接跳转到主页面
            ToMain();
        }

        mainActivity = this;
    }

    //跳转到主页面
    private void ToMain(){
        type = NOCHOOSE;
        setContentView(R.layout.welcome);
        findViewById(R.id.auto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_main);
                main_init(AUTO);
            }
        });
        findViewById(R.id.handle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //人工识别界面
                setContentView(R.layout.handle);
                main_init(HANDLE);
                submit = (Button) findViewById(R.id.submit);
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        submit.setEnabled(false);
                        TReceiver.DealHandleStr();
//                        LinearLayout item = (LinearLayout) LayoutInflater.from(MainActivity.this).inflate(R.layout.handle_item,null);
//                        ((LinearLayout)findViewById(R.id.handle_item)).addView(item);
                    }
                });
            }
        });
    }

    //跳转到设置页面
    private void ToSet(){
        setContentView(R.layout.settings);
        EditText url = (EditText) findViewById(R.id.Url);
        url.setText(setting.getString("url",""));
        findViewById(R.id.Save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText url = (EditText) findViewById(R.id.Url);
                String str = url.getText().toString();
                if(!str.isEmpty()){
                    setting.edit().putString("url",str).commit();
                    setting.edit().putBoolean("FIRST", false).commit();
                    Http.getInstance().setUpload_url(str);
                    ToMain();
                }else {
                    Toast.makeText(MainActivity.this,"输入不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void main_init(int type){

        this.type = type;
        if(AUTO == type){
            tabltLayout = (TableLayout) findViewById(R.id.auto);
        }
        if(HANDLE == type){
            tabltLayout = (TableLayout) findViewById(R.id.handle);//
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

    public void upload(String str){
        Log.d("123456", "onReceiveHandle: 开始上传");
        //Http上传
        Map<String, String> map_upload = new HashMap<>();
        map_upload.put("key", str);
        Http.getInstance().Post(Http.getInstance().getUpload_url(), map_upload, new Http.Callback() {
            @Override
            public void done(String data) {
                Log.d("123456", "done: " + data);
                tabltLayout.removeAllViews();
                tabltLayout.invalidate();
                //到时候改成失败提示！！
                AlertDialog.Builder a = new AlertDialog.Builder(MyApp.getContext()).setTitle("恭喜你").setMessage("上传成功").setPositiveButton("确定", null);
                AlertDialog alert = a.create();
                alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                alert.show();
            }
        });
    }
    /* */
}




