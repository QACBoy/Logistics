package com.example.scandemo5.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scandemo5.Activity.Distribution.DistributionActivity;
import com.example.scandemo5.Activity.Distribution.OutGoingActivity;
import com.example.scandemo5.Activity.Distribution.OutGoingSureActivity;
import com.example.scandemo5.Activity.Storage.ChangeStorageActivity;
import com.example.scandemo5.Activity.Storage.MainActivity;
import com.example.scandemo5.Data.DistributionInfo;
import com.example.scandemo5.Data.UpLoad;
import com.example.scandemo5.R;
import com.example.scandemo5.Utils.HamButtonBuilderManager;
import com.example.scandemo5.Utils.Global;
import com.example.scandemo5.Utils.Msg;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.OnBoomListener;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;
import com.orhanobut.dialogplus.DialogPlus;

import java.util.ArrayList;

public class BaseActivity extends AppCompatActivity {
    public BoomMenuButton leftBmb,rightBmb;
    TextView mTitleTextView;
    private HamButtonClick hamButtonClick;
    private boolean useBackPress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init_actionbar();
    }

    public void setActionTitle(int textid){
        mTitleTextView.setText(textid);
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
        mTitleTextView.setText("DMS");
        mActionBar.setCustomView(actionBar);
        mActionBar.setDisplayShowCustomEnabled(true);
        ((Toolbar) actionBar.getParent()).setContentInsetsAbsolute(0,0);

        leftBmb = (BoomMenuButton) actionBar.findViewById(R.id.action_bar_left_bmb);
        rightBmb = (BoomMenuButton) actionBar.findViewById(R.id.action_bar_right_bmb);

        leftBmb.setButtonEnum(ButtonEnum.TextOutsideCircle);
        leftBmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_5_1);//设置左菜单功能个数
        leftBmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_5_1);
        leftBmb.setDuration(300);
        leftBmb.setDelay(25);
        for (int i = 0; i < leftBmb.getPiecePlaceEnum().pieceNumber(); i++)
            leftBmb.addBuilder(HamButtonBuilderManager.getTextOutsideCircleButtonBuilderWithDifferentPieceColor(i));

        leftBmb.setOnBoomListener(new OnBoomListener() {

            @Override
            public void onClicked(int index, BoomButton  boomButton) {

                switch (index){
                    case 0:
                        hintMsg(MainActivity.class);
                        break;
                    case 1:
                        hintMsg(ChangeStorageActivity.class);
                        break;
                    case 2:
//                        hintMsg(OutGoingActivity.class);
                        break;
                    case 3:
//                        hintMsg(OutGoingSureActivity.class);
                        break;
                    case 4:
//                        hintMsg(DistributionActivity.class);
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
        int count = HamButtonBuilderManager.getHamButtonText().length;
        switch (HamButtonBuilderManager.getHamButtonText().length){
            case 1:
                rightBmb.setPiecePlaceEnum(PiecePlaceEnum.HAM_1);
                rightBmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_1);
                break;
            case 2:
                rightBmb.setPiecePlaceEnum(PiecePlaceEnum.HAM_2);
                rightBmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_2);
                break;
            case 3:
                rightBmb.setPiecePlaceEnum(PiecePlaceEnum.HAM_3);
                rightBmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_3);
                break;
            case 4:
                rightBmb.setPiecePlaceEnum(PiecePlaceEnum.HAM_4);
                rightBmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_4);
                break;
            case 5:
                rightBmb.setPiecePlaceEnum(PiecePlaceEnum.HAM_5);
                rightBmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_5);
                break;
            case 6:
                rightBmb.setPiecePlaceEnum(PiecePlaceEnum.HAM_6);
                rightBmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_6);
                break;
        }
//        rightBmb.setPiecePlaceEnum(PiecePlaceEnum.HAM_4);
//        rightBmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_4);
        rightBmb.setDuration(400);
        rightBmb.setDelay(35);
        //注意要先设置文字
        for (int i = 0; i < rightBmb.getPiecePlaceEnum().pieceNumber(); i++)
            rightBmb.addBuilder(HamButtonBuilderManager.getHamButtonBuilderWithDifferentPieceColor(i));
        initHanButtonClick();
    }

    public interface HamButtonClick{ //菜单点击事件
        void onClick(int index, BoomButton boomButton);
    }

    public void setHamButtonClick(HamButtonClick click){
        hamButtonClick = click;
    }

    protected void initHanButtonClick() { //初始化右菜单
        rightBmb.setOnBoomListener(new OnBoomListener() {
            @Override
            public void onClicked(int index, BoomButton boomButton) {
                if(boomButton.getTextView().getText().toString().equals("设置")){
                    startActivity(new Intent(BaseActivity.this,SetActivity.class));
                    return;//不允许个体Activity订制“设置”功能
                }
                if(hamButtonClick != null)
                    hamButtonClick.onClick(index,boomButton);
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

    public void enableBackPress(){
        useBackPress = true;
    }
    public void disenableBackPress(){
        useBackPress = false;
    }

    @Override
    public void onBackPressed() {
        if(useBackPress)
            super.onBackPressed();   //屏蔽返回按键
    }


    private  void  hintMsg(final Class<?> activity){
        Msg.showMsg(BaseActivity.this, "警告","切换模式将清空所有已扫描数据 您确定继续吗？", new Msg.CallBack() {
            @Override
            public void confirm(DialogPlus dialog) {
                Global.upLoad = new UpLoad();
                finish();
                startActivity(new Intent(getApplicationContext(), activity));
                dialog.dismiss();
            }
        });
    }
}




