package com.hoqii.sales.selfservice.adapter;

import android.content.Context;
import android.view.View;

import com.hoqii.sales.selfservice.content.database.adapter.OrderDatabaseAdapter;
import com.hoqii.sales.selfservice.entity.Order;
import com.hoqii.sales.selfservice.holder.HistoryOrderHolder;

import org.meruvian.midas.core.defaults.DefaultAdapter;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by meruvian on 15/08/15.
 */
public class HistoryOrderAdapter extends DefaultAdapter<Order, HistoryOrderHolder> {
    private OrderDatabaseAdapter orderDatabaseAdapter;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss", new Locale("in", "ID", "ID"));

    public HistoryOrderAdapter(Context context, int layout, List<Order> contents) {
        super(context, layout, contents);

        orderDatabaseAdapter = new OrderDatabaseAdapter(context);
    }

    @Override
    public HistoryOrderHolder ViewHolder(View view) {
        return new HistoryOrderHolder(view);
    }

    @Override
    public View createdView(View view, HistoryOrderHolder holder, Order order) {
        holder.noOrder.setText(order.getReceiptNumber());
        holder.orderDate.setText(dateFormat.format(order.getLogInformation().getLastUpdateDate()));
        holder.contactName.setText("(" + order.getContact().getContactName() + ") " + order.getContact().getRecipient());

        return view;
    }

}
