package com.meruvian.pxc.selfservice.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


import com.meruvian.pxc.selfservice.R;
import com.meruvian.pxc.selfservice.entity.OrderMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by miftakhul on 12/6/15.
 */
public class SellerOrderMenuAdapter extends RecyclerView.Adapter<SellerOrderMenuAdapter.ViewHolder> {


    private Context context;
    private String orderId;
    private List<OrderMenu> orderMenuList = new ArrayList<OrderMenu>();
    private List<String> orderMenuListSerial = new ArrayList<String>();

    public SellerOrderMenuAdapter(Context context, String orderId) {
        this.context = context;
        this.orderId = orderId;

    }

//    public SellerOrderMenuAdapter(Context context, List<OrderMenu> orderMenus) {
//        this.context = context;
//        this.orderMenuList = orderMenus;
//    }

    public SellerOrderMenuAdapter(Context context, String orderId, List<OrderMenu> orderMenus, List<String> orderMenuListSerial) {
        this.context = context;
        this.orderId = orderId;
        this.orderMenuList = orderMenus;
        this.orderMenuListSerial = orderMenuListSerial;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_seller_order_menu_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.productName.setText("Product : " + orderMenuList.get(position).getProduct().getName());
        holder.productCount.setText("Jumlah order : " + Integer.toString(orderMenuList.get(position).getQty()));

        for (int x = 0; x < orderMenuListSerial.size(); x++) {
            Log.d(getClass().getSimpleName(), "order menu serial size " + orderMenuListSerial.size());
            Log.d(getClass().getSimpleName(), "order menu id " + orderMenuList.get(position).getId());
            Log.d(getClass().getSimpleName(), "order menu serial id " + orderMenuListSerial.get(x).toString());

            if (orderMenuList.get(position).getId().equals(orderMenuListSerial.get(x).toString())) {
                Log.d(getClass().getSimpleName(), "same id detected ");
//                holder.imageStatus.setColorFilter(R.color.colorPrimaryDark);
                holder.imageStatus.setVisibility(View.VISIBLE);
            }
        }

        final List<Integer> count = new ArrayList<Integer>();
        int maxCount = orderMenuList.get(position).getQty();

        for (int x = 1; x <= maxCount; x++) {
            count.add(x);
        }

        ArrayAdapter<Integer> newCountAdapter = new ArrayAdapter<Integer>(context, android.R.layout.simple_spinner_dropdown_item, count);
//        holder.newCount.setAdapter(newCountAdapter);

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent data = new Intent(context, ScannerActivityCustom.class);
//                data.putExtra("productName", orderMenuList.get(position).getProduct().getName());
//                data.putExtra("orderMenuId", orderMenuList.get(position).getId());
//                data.putExtra("position", position);
//
//                ((SellerOrderMenuListActivity) context).openScanner(data);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return orderMenuList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView productName, productCount;
        private ImageView imageStatus;
        private Spinner newCount;

        public ViewHolder(View itemView) {
            super(itemView);
            productName = (TextView) itemView.findViewById(R.id.om_name);
            productCount = (TextView) itemView.findViewById(R.id.om_count);
            imageStatus = (ImageView) itemView.findViewById(R.id.ol_img);
//            newCount = (Spinner) itemView.findViewById(R.id.om_spin_new_count);
        }
    }


    public void addOrderMenuSerial(String orderMenuId) {
        orderMenuListSerial.add(orderMenuId);
        Log.d(getClass().getSimpleName(), "item serial added");
    }


}
