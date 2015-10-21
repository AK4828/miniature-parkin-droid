package org.meruvian.esales.collector.adapter;

import android.content.Context;
import android.view.View;

import org.meruvian.esales.collector.entity.AssigmentDetailItem;
import org.meruvian.esales.collector.holder.AssigmentItemHolder;
import org.meruvian.midas.core.defaults.DefaultAdapter;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by meruvian on 06/10/15.
 */
public class AssigmentItemAdapter extends DefaultAdapter<AssigmentDetailItem,AssigmentItemHolder> {
    private DecimalFormat decimalFormat = new DecimalFormat("#,###");
    private double totalPrice;

    public AssigmentItemAdapter(Context context, int layout, List contents) {
        super(context, layout, contents);
    }

    @Override
    public AssigmentItemHolder ViewHolder(View view) {
        return new AssigmentItemHolder(view);
    }

    @Override
    public View createdView(View view, AssigmentItemHolder holder, AssigmentDetailItem detailItem) {
        holder.textItem.setText(detailItem.getProduct().getProduct().getName());
        holder.textItemQty.setText(String.valueOf(detailItem.getQty()));

        return view;
    }
}
