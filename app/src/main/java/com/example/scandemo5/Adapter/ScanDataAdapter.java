package com.example.scandemo5.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.scandemo5.Data.UpLoad;
import com.example.scandemo5.R;
import com.example.scandemo5.Utils.Global;

import co.dift.ui.SwipeToAction;

/**
 * Created by JC on 2017/9/21.
 */

public class ScanDataAdapter extends   RecyclerView.Adapter<RecyclerView.ViewHolder>{

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view, parent, false);

        return new ScanDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        UpLoad.ScanData item = Global.upLoad.list.get(position);
        ScanDataViewHolder vh = (ScanDataViewHolder) holder;
        vh.titleView.setText(item.barcode);
        vh.authorView.setText(item.goods_name);
        vh.lot.setText(item.LOT);
        vh.quantity.setText(item.quantity);
        if(!Global.isNullorEmpty(Global.upLoad.list.get(position).quantity) && !Global.isNullorEmpty(Global.upLoad.list.get(position).EXP) && !Global.isNullorEmpty(Global.upLoad.list.get(position).MFG) && !Global.isNullorEmpty(Global.upLoad.list.get(position).LOT)){
            vh.imageView.setImageResource(R.mipmap.accept);
        }else {
            vh.imageView.setImageResource(R.mipmap.warning);
        }
        vh.data = item;
    }

    @Override
    public int getItemCount() {
        return Global.upLoad.list.size();
    }

    public class ScanDataViewHolder extends SwipeToAction.ViewHolder<UpLoad.ScanData> {

        public TextView titleView;
        public TextView authorView;
        public TextView lot;
        public TextView quantity;
        public ImageView imageView;

        public ScanDataViewHolder(View v) {
            super(v);

            titleView = (TextView) v.findViewById(R.id.title);
            authorView = (TextView) v.findViewById(R.id.author);
            lot = (TextView) v.findViewById(R.id.lot);
            quantity = (TextView) v.findViewById(R.id.quantity);
            imageView = (ImageView) v.findViewById(R.id.image);
        }
    }
}
