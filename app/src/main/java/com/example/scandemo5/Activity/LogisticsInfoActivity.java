package com.example.scandemo5.Activity;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.scandemo5.R;

/**
 * Created by Sam on 2017/10/12.
 */

public class LogisticsInfoActivity  extends BaseActivity{

    private EditText distribution_no;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logisticsinfo);
        distribution_no= (EditText) findViewById(R.id.distribution_logistics_no);
        findViewById(R.id.distribution_logistics_bt).setOnClickListener(new View.OnClickListener() {
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
