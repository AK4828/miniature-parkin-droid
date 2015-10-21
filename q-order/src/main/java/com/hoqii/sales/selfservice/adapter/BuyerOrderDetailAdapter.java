package com.hoqii.sales.selfservice.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.view.View;

import com.hoqii.sales.selfservice.R;
import com.hoqii.sales.selfservice.content.database.adapter.OrderMenuDatabaseAdapter;
import com.hoqii.sales.selfservice.entity.OrderMenu;
import com.hoqii.sales.selfservice.fragment.ScannerImeiFragment;
import com.hoqii.sales.selfservice.holder.BuyerOrderDetailHolder;

import org.meruvian.midas.core.defaults.DefaultAdapter;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by meruvian on 14/07/15.
 */
public class BuyerOrderDetailAdapter extends DefaultAdapter<OrderMenu, BuyerOrderDetailHolder> {
    private DecimalFormat decimalFormat = new DecimalFormat("#,###");
    private double totalPrice;
    private OrderMenuDatabaseAdapter orderMenuDbAdapter;
    private Fragment fragment;

    public BuyerOrderDetailAdapter(Context context, int layout, List contents, Fragment fragment) {
        super(context, layout, contents);

        orderMenuDbAdapter = new OrderMenuDatabaseAdapter(context);
        this.fragment = fragment;
    }

    @Override
    public BuyerOrderDetailHolder ViewHolder(View view) {
        return new BuyerOrderDetailHolder(view);
    }

    @Override
    public View createdView(View view, BuyerOrderDetailHolder holder, OrderMenu orderMenu) {
        totalPrice = orderMenu.getProduct().getSellPrice() * (orderMenu.getQty());
        String q = String.valueOf(orderMenu.getQty());

        holder.itemName.setText(orderMenu.getProduct().getProduct().getName());
        holder.itemDesc.setText(orderMenu.getDescription());
        holder.itemQty.setText(q);
        holder.itemPrice.setText("Rp " + decimalFormat.format(totalPrice));
        holder.itemImei.setText("");

        holder.btnImei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fg = new ScannerImeiFragment();
                FragmentTransaction transaction = fragment.getActivity().getFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame, fg);
                transaction.addToBackStack(null);
                transaction.commit();

//                removeFragment();
            }
        });

        return view;
    }

    private void removeFragment(){
        FragmentManager manager = fragment.getActivity().getFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove(fragment);
        trans.commit();
        manager.popBackStack();
    }
    
}
