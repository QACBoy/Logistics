package com.example.scandemo5.Adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.scandemo5.Data.Order;
import com.example.scandemo5.Data.UpLoad;
import com.example.scandemo5.R;
import com.example.scandemo5.Utils.Global;

import java.util.List;

import co.dift.ui.SwipeToAction;

/**
 * Created by JC on 2017/9/21.
 */

public class OrderDataAdapter extends   RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<Order> orders;

    public OrderDataAdapter(List<Order> orders){
        this.orders = orders;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view, parent, false);

        return new OrderDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Order item = orders.get(position);
        OrderDataViewHolder vh = (OrderDataViewHolder) holder;
        vh.titleView.setText(item.order_m.order_no + " " + item.order_m.linkman);
        vh.titleView.setTextColor(Color.BLACK);
        vh.authorView.setText(item.order_m.client_address);
        vh.authorView.setTextColor(Color.BLACK);
        vh.data = item;
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class OrderDataViewHolder extends SwipeToAction.ViewHolder<Order> {

        public TextView titleView;
        public TextView authorView;
        public TextView lot;
        public TextView quantity;
        public ImageView imageView;

        public OrderDataViewHolder(View v) {
            super(v);

            titleView = (TextView) v.findViewById(R.id.title);
            authorView = (TextView) v.findViewById(R.id.author);
            lot = (TextView) v.findViewById(R.id.lot);
            quantity = (TextView) v.findViewById(R.id.quantity);
            imageView = (ImageView) v.findViewById(R.id.image);
        }
    }
}
