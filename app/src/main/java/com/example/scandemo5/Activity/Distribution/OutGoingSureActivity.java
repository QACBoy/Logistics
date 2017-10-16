package com.example.scandemo5.Activity.Distribution;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.scandemo5.Activity.BaseActivity;
import com.example.scandemo5.Adapter.DistributionDataAdapter;
import com.example.scandemo5.Adapter.StorageDataAdapter;
import com.example.scandemo5.Data.Distribution;
import com.example.scandemo5.Data.Storage;
import com.example.scandemo5.R;
import com.example.scandemo5.Utils.DJson;
import com.example.scandemo5.Utils.Global;
import com.example.scandemo5.Utils.HamButtonBuilderManager;
import com.example.scandemo5.Utils.Http;

import java.util.List;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

/**
 * Created by Sam on 2017/10/12.
 */

public class OutGoingSureActivity extends BaseActivity implements MaterialTabListener {

    public static OutGoingSureActivity activity;
    private MaterialTabHost tabHost;
    private ViewPager pager;
    private ViewPagerAdapter pagerAdapter;
    private static StorageDataAdapter orderDataAdapter;
    private static List<Storage> storages;

    protected void onCreate(Bundle savedInstanceState) {
        HamButtonBuilderManager.setHamButtonText(HamButtonBuilderManager.setTextId);
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_outgoing);
        setActionTitle(R.string.text_outside_circle_button_text_3);
        getHttpData();
        tabHost = (MaterialTabHost) this.findViewById(R.id.materialTabHost);
        pager = (ViewPager) this.findViewById(R.id.viewpager);


        // init view pager
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        // insert all tabs from pagerAdapter data
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            tabHost.addTab(
                    tabHost.newTab()
                            .setText(pagerAdapter.getPageTitle(i))
//                            .setIcon(getIcon(i))
                            .setTabListener(this)
            );
        }
//        initadapter();
    }

    private void initadapter(){

        pager.setAdapter(pagerAdapter);
        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // when user do a swipe the selected tab change
                tabHost.setSelectedNavigationItem(position);
            }
        });

    }

    private void getHttpData(){
        Http.getInstance().Get("http://119.29.223.148/dms/public/srorage/all", new Http.Callback() {
            @Override
            public void done(String data) {
                Log.d("", "done: " + data);
                if (data == null || "NetError".equals(data)) {
                    Toast.makeText(OutGoingSureActivity.this, "网络数据获取失败，请检查网络", Toast.LENGTH_SHORT).show();
                } else {
                    storages = DJson.JsonToList(data, Storage.class);
                    orderDataAdapter = new StorageDataAdapter(storages);
                    orderDataAdapter.setOnClickListener(new StorageDataAdapter.OnClick() {
                        @Override
                        public void onClick(int postion, Storage order) {
                            Log.d("12321", "onClick: " + postion + " " + order.order_m.client_address);
                            Global.outgongsurestorage = order;
                            startActivity(new Intent(OutGoingSureActivity.this, OutGoingSureDetailActivity.class));
                        }
                    });
                    initadapter();
//                RecyclerView recyclerview = new RecyclerView(activity.getApplication());
//                recyclerview.setLayoutManager(new LinearLayoutManager(activity.getApplication()));
//                recyclerview.setAdapter(new OrderDataAdapter(orders));
//                setContentView(recyclerview);
                }
            }
        });
    }

    public static class OutFragment extends Fragment { //订单视图

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            RecyclerView recyclerview = new RecyclerView(this.getContext());
            recyclerview.setLayoutManager(new LinearLayoutManager(activity.getApplication()));
            recyclerview.setAdapter(orderDataAdapter);
            return recyclerview;
//            TextView text = new TextView(container.getContext());
//            text.setText("Fragment content");
//            text.setGravity(Gravity.CENTER);
//            return text;
        }
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int position) {
            return new OutFragment();
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(0 == position){
                return "全部出库单";
            }
            else if(1 == position){
                return "未完成";
            }else if(2 == position){
                return "已完成";
            }
            return "Section " + position;
        }

    }

    @Override
    public void onTabSelected(MaterialTab tab) {
        pager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab tab) {
        pager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(MaterialTab tab) {

    }

}
