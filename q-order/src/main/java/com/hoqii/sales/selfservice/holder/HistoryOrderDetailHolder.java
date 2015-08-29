package com.hoqii.sales.selfservice.holder;

import android.view.View;
import android.widget.TextView;

import com.hoqii.sales.selfservice.R;

import org.meruvian.midas.core.defaults.DefaultHolder;

import butterknife.InjectView;

/**
 * Created by meruvian on 14/07/15.
 */
public class HistoryOrderDetailHolder extends DefaultHolder {
    @InjectView(R.id.text_menu_name) public TextView menuName;
    @InjectView(R.id.text_menu_quantity) public TextView menuQuantity;
    @InjectView(R.id.text_total_price) public TextView totalPrice;

    public HistoryOrderDetailHolder(View view) {
        super(view);
    }
}
