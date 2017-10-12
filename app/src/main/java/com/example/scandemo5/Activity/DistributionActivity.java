package com.example.scandemo5.Activity;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

import com.example.scandemo5.R;

/**
 * Created by Sam on 2017/10/12.
 */

public class DistributionActivity extends BaseActivity{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distribution);
        findViewById(R.id.distribution_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LogisticsInfoShowActivity mVerticalStepViewReverseFragment;
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                mVerticalStepViewReverseFragment = new LogisticsInfoShowActivity();
                setContentView(R.layout.logisticsinfo_show);
                fragmentTransaction.replace(R.id.container, mVerticalStepViewReverseFragment).commit();

            }
        });
    }
}
