package com.hoqii.sales.selfservice.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.hoqii.sales.selfservice.entity.OrderMenu;
import com.hoqii.sales.selfservice.holder.SettelItemHolder;

import org.meruvian.midas.core.defaults.DefaultAdapter;

import java.util.List;

/**
 * Created by meruvian on 03/10/15.
 */
public class SettelItemAdapter extends DefaultAdapter<OrderMenu, SettelItemHolder> {
    private int selectedPosition = -1;
    private int position;

    public SettelItemAdapter(Context context, int layout, List<OrderMenu> contents) {
        super(context, layout, contents);
    }

    @Override
    public SettelItemHolder ViewHolder(View view) {
        return new SettelItemHolder(view);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        this.position = position;
        return super.getView(position, convertView, parent);
    }

    @Override
    public View createdView(View view, SettelItemHolder holder, OrderMenu orderMenu) {
        holder.textItem.setText(orderMenu.getProduct().getProduct().getName());
        holder.editQuantity.setText(String.valueOf(orderMenu.getQty()));

        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.btnMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.btnCancel.setOnClickListener(new OnClickData(getContext(), getPosition(), orderMenu.getId()));

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
//                OrderListActivity orderListActivity = (OrderListActivity) activity;

                getList().remove(location);
                notifyDataSetChanged();
//                orderMenuDbAdapter.deleteOrderMenu(orderMenuId);
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
