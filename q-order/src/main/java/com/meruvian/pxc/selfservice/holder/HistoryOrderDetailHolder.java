package com.meruvian.pxc.selfservice.holder;

import android.view.View;
import android.widget.TextView;

import com.meruvian.pxc.selfservice.R;

import org.meruvian.midas.core.defaults.DefaultHolder;

import butterknife.Bind;

/**
 * Created by meruvian on 14/07/15.
 */
public class HistoryOrderDetailHolder extends DefaultHolder {
    @Bind(R.id.text_menu_name) public TextView menuName;
    @Bind(R.id.text_menu_quantity) public TextView menuQuantity;
    @Bind(R.id.text_total_price) public TextView totalPrice;

    public HistoryOrderDetailHolder(View view) {
        super(view);
    }
}
