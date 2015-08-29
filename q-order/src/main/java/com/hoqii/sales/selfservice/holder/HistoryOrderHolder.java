package com.hoqii.sales.selfservice.holder;

import android.view.View;
import android.widget.TextView;

import com.hoqii.sales.selfservice.R;

import org.meruvian.midas.core.defaults.DefaultHolder;

import butterknife.InjectView;

/**
 * Created by meruvian on 15/08/15.
 */
public class HistoryOrderHolder extends DefaultHolder {
    public HistoryOrderHolder(View view) {
        super(view);
    }

    @InjectView(R.id.text_no_order) public TextView noOrder;
    @InjectView(R.id.text_order_date) public TextView orderDate;
    @InjectView(R.id.text_contact_name) public TextView contactName;

}
