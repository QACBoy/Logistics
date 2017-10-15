package com.example.scandemo5.Adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.scandemo5.Data.Order;
import com.example.scandemo5.R;

import java.util.List;

import co.dift.ui.SwipeToAction;

/**
 * Created by JC on 2017/9/21.
 */

public class OrderDetailDataAdapter extends   RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Order> orders;
    private OnClick onClick;

    public OrderDetailDataAdapter(List<Order> orders){
        this.orders = orders;
    }

    public interface OnClick{
        void onClick(int postion, Order order);
    }

    public void setOnClickListener(OnClick clickListener){
        this.onClick = clickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view, parent, false);

        final OrderDataViewHolder orderDataViewHolder = new OrderDataViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onClick(orderDataViewHolder.getLayoutPosition(),orderDataViewHolder.data);
            }
        });
        return orderDataViewHolder;
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
