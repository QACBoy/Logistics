package com.example.scandemo5.Activity.Distribution;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.scandemo5.Activity.BaseActivity;
import com.example.scandemo5.R;
import com.example.scandemo5.Utils.HamButtonBuilderManager;
import com.example.scandemo5.Utils.Global;
import com.example.scandemo5.Utils.Http;


/**
 * Created by Sam on 2017/10/12.
 */

public class LogisticsInfoActivity  extends BaseActivity {

    private EditText distribution_no;
    private Button button;


    protected void onCreate(Bundle savedInstanceState) {
        HamButtonBuilderManager.setHamButtonText(HamButtonBuilderManager.distristartextId);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logisticsinfo);
        distribution_no = (EditText) findViewById(R.id.distribution_logistics_no);
        button = (Button) findViewById(R.id.distribution_logistics_bt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Global.isNullorEmpty(distribution_no.getText().toString())) {
                    Toast.makeText(LogisticsInfoActivity.this, "请扫描配送单号", Toast.LENGTH_SHORT).show();
                    return;
                }
                button.setEnabled(false);
                button.setText("获取数据中");
                Http.getInstance().Get(Http.getInstance().get_distribution + distribution_no.getText().toString(), new Http.Callback() {
                    @Override
                    public void done(String data) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                button.setEnabled(true);
                                button.setText("继续");
                            }
                        });
                        if ("NetError".equals(data)) {
                            Toast.makeText(LogisticsInfoActivity.this, "网络数据获取失败，请检查网络", Toast.LENGTH_SHORT).show();
                        }else if(Global.isNullorEmpty(data)){
                            Toast.makeText(LogisticsInfoActivity.this, "暂无该物流信息，请检查配单号", Toast.LENGTH_SHORT).show();
                        } else{
//                            Toast.makeText(LogisticsInfoActivity.this, distribution_no.getText().toString(), Toast.LENGTH_SHORT).show();
                            Global.logistics_jsonData = data;
                            LogisticsInfoShowActivity mlogisticsInfoShowActivity;
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            mlogisticsInfoShowActivity = new LogisticsInfoShowActivity();

                            setContentView(R.layout.logisticsinfo_show);
                            fragmentTransaction.replace(R.id.container, mlogisticsInfoShowActivity).commit();
                        }
                    }
                });
            }
        });
    }
}