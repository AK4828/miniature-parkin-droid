package com.hoqii.sales.selfservice.adapter;

import android.content.Context;
import android.view.View;

import com.hoqii.sales.selfservice.entity.Order;
import com.hoqii.sales.selfservice.holder.BuyerOrderHolder;

import org.meruvian.midas.core.defaults.DefaultAdapter;

import java.util.List;

/**
 * Created by meruvian on 15/09/15.
 */
public class BuyerOrderAdapter extends DefaultAdapter<Order, BuyerOrderHolder> {

    public BuyerOrderAdapter(Context context, int layout, List<Order> contents) {
        super(context, layout, contents);
    }

    @Override
    public BuyerOrderHolder ViewHolder(View view) {
        return new BuyerOrderHolder(view);
    }

    @Override
    public View createdView(View view, BuyerOrderHolder holder, Order order) {
        holder.textBuyerNoOrder.setText(order.getReceiptNumber());

        return view;
    }
}
