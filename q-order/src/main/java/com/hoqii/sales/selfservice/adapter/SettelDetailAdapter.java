package com.hoqii.sales.selfservice.adapter;

import android.content.Context;
import android.view.View;

import com.hoqii.sales.selfservice.entity.OrderMenu;
import com.hoqii.sales.selfservice.holder.SettleDetailHolder;

import org.meruvian.midas.core.defaults.DefaultAdapter;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by meruvian on 03/10/15.
 */
public class SettelDetailAdapter extends DefaultAdapter<OrderMenu, SettleDetailHolder> {
    private DecimalFormat decimalFormat = new DecimalFormat("#,###");
    private double totalPrice;

    public SettelDetailAdapter(Context context, int layout, List<OrderMenu> contents) {
        super(context, layout, contents);
    }

    @Override
    public SettleDetailHolder ViewHolder(View view) {
        return new SettleDetailHolder(view);
    }

    @Override
    public View createdView(View view, SettleDetailHolder holder, OrderMenu orderMenu) {
        totalPrice = orderMenu.getProduct().getSellPrice() * (orderMenu.getQty());
        String q = String.valueOf(orderMenu.getQty());

        holder.textItem.setText(orderMenu.getProduct().getProduct().getName());
        holder.textQuantity.setText(q);
        holder.textPrice.setText("Rp " + decimalFormat.format(totalPrice));

        return view;
    }
}
