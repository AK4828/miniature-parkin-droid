package com.meruvian.pxc.selfservice.holder;

import android.view.View;
import android.widget.TextView;

import com.meruvian.pxc.selfservice.R;

import org.meruvian.midas.core.defaults.DefaultHolder;

import butterknife.Bind;

/**
 * Created by meruvian on 15/08/15.
 */
public class HistoryOrderHolder extends DefaultHolder {
    public HistoryOrderHolder(View view) {
        super(view);
    }

    @Bind(R.id.text_no_order) public TextView noOrder;
    @Bind(R.id.text_order_date) public TextView orderDate;
    @Bind(R.id.text_contact_name) public TextView contactName;

}
