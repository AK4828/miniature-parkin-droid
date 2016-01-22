package com.meruvian.pxc.selfservice.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.meruvian.pxc.selfservice.R;
import com.meruvian.pxc.selfservice.activity.SellerOrderMenuListActivity;
import com.meruvian.pxc.selfservice.entity.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by miftakhul on 12/6/15.
 */
public class SellerOrderAdapter extends RecyclerView.Adapter<SellerOrderAdapter.ViewHolder> {


    private Context context;
    private List<Order> orderList = new ArrayList<Order>();
    private String orderType;


    public SellerOrderAdapter(Context context) {
        this.context = context;
    }

    public SellerOrderAdapter(Context context, String orderType) {
        this.context = context;
        this.orderType  = orderType;

        Log.d(getClass().getSimpleName(), orderType + "===================");
    }

    public SellerOrderAdapter(Context context, List<Order> orders, String orderType) {
        this.context = context;
        this.orderList = orders;
        this.orderType = orderType;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_seller_order_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.orderNumber.setText("Order number : "+orderList.get(position).getReceiptNumber());
        holder.orderDate.setText("Tanggal : "+orderList.get(position).getLogInformation().getCreateDate().toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SellerOrderMenuListActivity.class);
                intent.putExtra("orderId", orderList.get(position).getId());
                intent.putExtra("orderDate", orderList.get(position).getLogInformation().getCreateDate().getTime());
                Log.d("date send ", Long.toString(orderList.get(position).getLogInformation().getCreateDate().getTime()));
                intent.putExtra("orderReceipt", orderList.get(position).getReceiptNumber());


                if (orderType.equals("orderList")) {
                    Log.d(getClass().getSimpleName(), "orderListRunner");
                    intent.putExtra("orderMenuListType", "orderMenuList");
                }else if (orderType.equals("purchaseOrderList")){
                    Log.d(getClass().getSimpleName(), "purchaseOrderListRunner");
                    intent.putExtra("orderMenuListType", "purchaseOrderMenuList");
                }
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView orderNumber, orderDate;

        public ViewHolder(View itemView) {
            super(itemView);
            orderNumber = (TextView) itemView.findViewById(R.id.ol_number);
            orderDate = (TextView) itemView.findViewById(R.id.ol_tgl);
        }
    }

    public void add(List<Order> orders) {
        this.orderList = orders;
        notifyItemInserted(orderList.size() + 1);
    }


}
