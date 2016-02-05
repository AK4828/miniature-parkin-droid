package com.meruvian.pxc.selfservice.holder;

import android.view.View;
import android.widget.TextView;

import com.meruvian.pxc.selfservice.R;

import org.meruvian.midas.core.defaults.DefaultHolder;

import butterknife.Bind;


/**
 * Created by meruvian on 03/10/15.
 */
public class SettleDetailHolder extends DefaultHolder {
    public SettleDetailHolder(View view) {
        super(view);
    }

    @Bind(R.id.text_item_name)
    public TextView textItem;
    @Bind(R.id.text_item_quantity)
    public TextView textQuantity;
    @Bind(R.id.text_total_price)
    public TextView textPrice;
}
