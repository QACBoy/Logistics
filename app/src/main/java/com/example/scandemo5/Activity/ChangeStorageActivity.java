package com.example.scandemo5.Activity;

import android.app.Activity;
import android.os.Bundle;

import com.example.scandemo5.R;
import com.example.scandemo5.Utils.Global;

public class ChangeStorageActivity extends BaseActivity {

    public static ChangeStorageActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        toStorageNo();
    }

    private void toStorageNo(){
        Global.setTYPE_SCA(Global.ScanType.kw_stroageno);
        setContentView(R.layout.activity_change_storage);
    }
}
