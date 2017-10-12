package com.example.scandemo5.Activity;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

import com.example.scandemo5.R;

import static com.example.scandemo5.R.layout.activity_distribution;
import static com.example.scandemo5.R.layout.logisticsinfo;

/**
 * Created by Sam on 2017/10/12.
 */

public class Distribution extends BaseActivity{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_distribution);
        findViewById(R.id.distribution_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogisticsInfo mVerticalStepViewReverseFragment;
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                mVerticalStepViewReverseFragment = new LogisticsInfo();
                setContentView(logisticsinfo);
                fragmentTransaction.replace(R.id.container, mVerticalStepViewReverseFragment).commit();
                
            }
        });
    }
}
