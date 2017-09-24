package com.example.scandemo5.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scandemo5.Data.UpLoad;
import com.example.scandemo5.R;
import com.example.scandemo5.Utils.BuilderManager;
import com.example.scandemo5.Utils.Global;
import com.example.scandemo5.Utils.Msg;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.OnBoomListener;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;
import com.orhanobut.dialogplus.DialogPlus;

import info.hoang8f.widget.FButton;

import static com.example.scandemo5.Utils.Global.dialog;

public class BaseActivity extends AppCompatActivity {
    BoomMenuButton leftBmb,rightBmb;
    TextView mTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init_actionbar();
    }

    private void init_actionbar(){
        /*ActionBar actionBar =getSupportActionBar();
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.menu);*/
        ActionBar mActionBar = getSupportActionBar();
        assert mActionBar != null;

        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View actionBar = mInflater.inflate(R.layout.actionbar, null);
        mTitleTextView = (TextView) actionBar.findViewById(R.id.title_text);
        mTitleTextView.setText("DMS System");
        mActionBar.setCustomView(actionBar);
        mActionBar.setDisplayShowCustomEnabled(true);
        ((Toolbar) actionBar.getParent()).setContentInsetsAbsolute(0,0);

        leftBmb = (BoomMenuButton) actionBar.findViewById(R.id.action_bar_left_bmb);
        rightBmb = (BoomMenuButton) actionBar.findViewById(R.id.action_bar_right_bmb);

        leftBmb.setButtonEnum(ButtonEnum.TextOutsideCircle);
        leftBmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_9_1);
        leftBmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_9_1);
        leftBmb.setDuration(300);
        leftBmb.setDelay(25);
        for (int i = 0; i < leftBmb.getPiecePlaceEnum().pieceNumber(); i++)
            leftBmb.addBuilder(BuilderManager.getTextOutsideCircleButtonBuilderWithDifferentPieceColor(i));

        leftBmb.setOnBoomListener(new OnBoomListener() {

            @Override
            public void onClicked(int index, BoomButton
                    boomButton) {

                switch (index){
                    case 0:
//                        Toast.makeText(BaseActivity.this,"0ooooo",Toast.LENGTH_SHORT).show();
                        hintMsg("警告","切换模式将清空所有已扫描数据 您确定继续吗？",0,MainActivity.class);
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                        break;
                    case 1:
//                        Toast.makeText(BaseActivity.this,"1ooooo",Toast.LENGTH_SHORT).show();
                        hintMsg("警告","切换模式将清空所有已扫描数据 您确定继续吗？",0,MainActivity.class);
//                        startActivity(new Intent(getApplicationContext(),ChangeStorageActivity.class));
                        break;
                    case 2:
                        Toast.makeText(BaseActivity.this,"2ooooo",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(BaseActivity.this,"3ooooo",Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(BaseActivity.this,"4ooooo",Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        Toast.makeText(BaseActivity.this,"5ooooo",Toast.LENGTH_SHORT).show();
                        break;
                    case 6:
                        Toast.makeText(BaseActivity.this,"6ooooo",Toast.LENGTH_SHORT).show();
                        break;
                    case 7:
                        Toast.makeText(BaseActivity.this,"7ooooo",Toast.LENGTH_SHORT).show();
                        break;
                    case 8:
                        Toast.makeText(BaseActivity.this,"8ooooo",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                }



            }

            @Override
            public void onBackgroundClick() {

            }

            @Override
            public void onBoomWillHide() {

            }

            @Override
            public void onBoomDidHide() {

            }

            @Override
            public void onBoomWillShow() {

            }

            @Override
            public void onBoomDidShow() {

            }
        });

        rightBmb.setButtonEnum(ButtonEnum.Ham);
        rightBmb.setPiecePlaceEnum(PiecePlaceEnum.HAM_4);
        rightBmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_4);
        rightBmb.setDuration(400);
        rightBmb.setDelay(35);
        for (int i = 0; i < rightBmb.getPiecePlaceEnum().pieceNumber(); i++)
            rightBmb.addBuilder(BuilderManager.getHamButtonBuilderWithDifferentPieceColor(i));
        rightBmb.setOnBoomListener(new OnBoomListener() {
            @Override
            public void onClicked(int index, BoomButton boomButton) {
                switch (index){
                    case 0:
                        Toast.makeText(BaseActivity.this,"ooooo",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(BaseActivity.this,"1ooooo",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(BaseActivity.this,"2ooooo",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(BaseActivity.this,"3ooooo",Toast.LENGTH_SHORT).show();
                        break;
                    /*
                    case 4:
                        Toast.makeText(BaseActivity.this,"4ooooo",Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        Toast.makeText(BaseActivity.this,"5ooooo",Toast.LENGTH_SHORT).show();
                        break;
                    case 6:
                        Toast.makeText(BaseActivity.this,"6ooooo",Toast.LENGTH_SHORT).show();
                        break;
                    case 7:
                        Toast.makeText(BaseActivity.this,"7ooooo",Toast.LENGTH_SHORT).show();
                        break;
                    case 8:
                        Toast.makeText(BaseActivity.this,"8ooooo",Toast.LENGTH_SHORT).show();
                        break;*/
                    default:
                }
            }

            @Override
            public void onBackgroundClick() {

            }

            @Override
            public void onBoomWillHide() {

            }

            @Override
            public void onBoomDidHide() {

            }

            @Override
            public void onBoomWillShow() {

            }

            @Override
            public void onBoomDidShow() {

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        Global.ifCloseInput(BaseActivity.this);//输入法控制
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();   //屏蔽返回按键
    }

    // 待修改功能还补不全
/*
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
                Global.upLoad = new UpLoad();//切换模式清空数据
                switch (id){
                    case R.id.function_storage:
                        startActivity(new Intent(getApplicationContext(),BaseActivity.class));
                        break;
                    case R.id.function_changestorage:
                        startActivity(new Intent(getApplicationContext(),ChangeStorageActivity.class));
                        break;
                }
                dialog.dismiss();
                finish();
            }
        });
    }*/

    private  void  hintMsg(String str1, String str2, final int n, final Class<?> activity){
        Msg.showMsg(BaseActivity.this, str1, str2, new Msg.CallBack() {
            @Override
            public void confirm(DialogPlus dialog) {
                if(n == 1)
                    toUpload();
                else
                    startActivity(new Intent(getApplicationContext(), activity));
                dialog.dismiss();
            }
        });
    }
    private void toUpload(){
        setContentView(R.layout.activity_change_storage);
        ((EditText)findViewById(R.id.changestorage_stroageno)).setHint("请扫描 移至库位");
        ((FButton)findViewById(R.id.changestorage_ok)).setText("确定移动商品");
    }
}




