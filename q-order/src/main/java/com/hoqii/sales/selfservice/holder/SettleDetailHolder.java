package com.hoqii.sales.selfservice.holder;

import android.view.View;
import android.widget.TextView;

import com.hoqii.sales.selfservice.R;

import org.meruvian.midas.core.defaults.DefaultHolder;

import butterknife.InjectView;

/**
 * Created by meruvian on 03/10/15.
 */
public class SettleDetailHolder extends DefaultHolder {
    public SettleDetailHolder(View view) {
        super(view);
    }

    @InjectView(R.id.text_item_name)
    public TextView textItem;
    @InjectView(R.id.text_item_quantity)
    public TextView textQuantity;
    @InjectView(R.id.text_total_price)
    public TextView textPrice;
}
