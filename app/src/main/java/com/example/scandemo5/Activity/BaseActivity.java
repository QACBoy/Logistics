package com.example.scandemo5.Activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.example.scandemo5.Utils.JMap;
import com.example.scandemo5.Utils.Msg;
import com.example.scandemo5.Utils.RMap;
import com.example.scandemo5.Utils.SQLite;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnItemClickListener;

import org.feezu.liuli.timeselector.TimeSelector;

import co.dift.ui.SwipeToAction;

public class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init_actionbar();
    }

    private void init_actionbar(){
        ActionBar actionBar =getSupportActionBar();
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.action_settings);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Global.ifCloseInput(BaseActivity.this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {  //写有home导航的判断
        switch (item.getItemId()){
            case android.R.id.home: //点击导航按钮事件
//                Toast.makeText(MainActivity.this,"-------",Toast.LENGTH_SHORT).show();
                Msg.showFunciton(BaseActivity.this, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toFunction(v.getId());
                    }
                });
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toFunction(final int id){
        Msg.showMsg(this,"警告", "切换模式将清空所有已扫描数据 您确定继续吗？", new Msg.CallBack() {
            @Override
            public void confirm(DialogPlus dialog) {
                switch (id){
                    case R.id.function_storage:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        break;
                    case R.id.function_changestorage:
                        startActivity(new Intent(getApplicationContext(),ChangeStorageActivity.class));
                        break;
                }
                dialog.dismiss();
            }
        });
    }
}




