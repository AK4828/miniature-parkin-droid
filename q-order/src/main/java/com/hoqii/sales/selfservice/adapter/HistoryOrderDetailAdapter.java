package com.hoqii.sales.selfservice.adapter;

import android.app.Fragment;
import android.content.Context;
import android.view.View;

import com.hoqii.sales.selfservice.content.database.adapter.OrderMenuDatabaseAdapter;
import com.hoqii.sales.selfservice.entity.OrderMenu;
import com.hoqii.sales.selfservice.holder.HistoryOrderDetailHolder;

import org.meruvian.midas.core.defaults.DefaultAdapter;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by meruvian on 14/07/15.
 */
public class HistoryOrderDetailAdapter extends DefaultAdapter<OrderMenu, HistoryOrderDetailHolder> {
    private DecimalFormat decimalFormat = new DecimalFormat("#,###");
    private long totalPrice;
    private OrderMenuDatabaseAdapter orderMenuDbAdapter;
    private Fragment fragment;

    public HistoryOrderDetailAdapter(Context context, int layout, List contents, Fragment fragment) {
        super(context, layout, contents);
        this.fragment = fragment;

        orderMenuDbAdapter = new OrderMenuDatabaseAdapter(context);
    }

    @Override
    public HistoryOrderDetailHolder ViewHolder(View view) {
        return new HistoryOrderDetailHolder(view);
    }

    @Override
    public View createdView(View view, HistoryOrderDetailHolder holder, OrderMenu orderMenu) {
        totalPrice = orderMenu.getProduct().getSellPrice() * (orderMenu.getQty());
        String q = String.valueOf(orderMenu.getQty());

        holder.menuName.setText(orderMenu.getProduct().getName());
        holder.menuQuantity.setText(q);
        holder.totalPrice.setText("Rp " + decimalFormat.format(totalPrice));

        return view;
    }


}
