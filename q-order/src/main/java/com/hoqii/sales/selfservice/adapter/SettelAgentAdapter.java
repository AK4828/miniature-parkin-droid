package com.hoqii.sales.selfservice.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.hoqii.sales.selfservice.content.database.adapter.OrderDatabaseAdapter;
import com.hoqii.sales.selfservice.entity.Order;
import com.hoqii.sales.selfservice.activity.SettleAgentActivity;
import com.hoqii.sales.selfservice.holder.SettelAgentHolder;

import org.meruvian.midas.core.defaults.DefaultActivity;
import org.meruvian.midas.core.defaults.DefaultAdapter;

import java.util.List;

/**
 * Created by meruvian on 03/10/15.
 */
public class SettelAgentAdapter extends DefaultAdapter<Order, SettelAgentHolder> {
    private int selectedPosition = -1;
    private int position;
    private DefaultActivity activity;
    private OrderDatabaseAdapter orderDbAdapter;

    public SettelAgentAdapter(Context context, int layout, List<Order> contents, DefaultActivity activity) {
        super(context, layout, contents);
        this.activity = activity;

        orderDbAdapter = new OrderDatabaseAdapter(context);
    }

    @Override
    public SettelAgentHolder ViewHolder(View view) {
        return new SettelAgentHolder(view);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        this.position = position;
        return super.getView(position, convertView, parent);
    }

    @Override
    public View createdView(View view, SettelAgentHolder holder, Order order) {
        holder.textNoOrder.setText(order.getReceiptNumber());
        holder.textContactName.setText(order.getContact().getRecipient());
        holder.textContactTelp.setText(order.getContact().getPhone());

        holder.btnCancel.setOnClickListener(new OnClickData(getContext(), getPosition(), order.getId()));

        return view;
    }

    private class OnClickData extends ImageButton implements ImageButton.OnClickListener {
        private int location;
        private String orderId;

        public OnClickData(Context context, int location, String orderId) {
            super(context);
            this.location = location;
            this.orderId = orderId;
        }

        @Override
        public void onClick(View v) {
            confirmDialog(location, orderId);
        }
    }

    private void confirmDialog(final int location, final String orderId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Konfirmasi");
        builder.setMessage("Hapus order ?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                SettleAgentActivity settleAgentActivity = (SettleAgentActivity) activity;

                getList().remove(location);
                notifyDataSetChanged();
                orderDbAdapter.updateOrderStatusById(orderId, Order.OrderStatus.CANCELED.name());

//                orderListActivity.setTotalOrderTab();
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setCancelable(false);
        builder.create().show();
    }

    public void setItemSelected(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }

}



