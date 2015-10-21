package org.meruvian.esales.collector.adapter;

import android.content.Context;
import android.view.View;

import org.meruvian.esales.collector.entity.Settle;
import org.meruvian.esales.collector.holder.SettleDetailHolder;
import org.meruvian.midas.core.defaults.DefaultAdapter;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by meruvian on 06/10/15.
 */
public class SettleDetailAdapter extends DefaultAdapter<Settle,SettleDetailHolder> {
    private DecimalFormat decimalFormat = new DecimalFormat("#,###");
    private double totalPrice;

    public SettleDetailAdapter(Context context, int layout, List contents) {
        super(context, layout, contents);
    }

    @Override
    public SettleDetailHolder ViewHolder(View view) {
        return new SettleDetailHolder(view);
    }

    @Override
    public View createdView(View view, SettleDetailHolder holder, Settle settle) {
        totalPrice = settle.getProduct().getSellPrice() * (settle.getQty());
        String q = String.valueOf(settle.getQty());

        holder.textItem.setText(settle.getProduct().getProduct().getName());
        holder.textQty.setText(q);
        holder.textTotal.setText("Rp " + decimalFormat.format(totalPrice));

        return view;
    }
}
