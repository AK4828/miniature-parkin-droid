package org.meruvian.esales.collector.holder;

import android.view.View;
import android.widget.TextView;

import org.meruvian.esales.collector.R;
import org.meruvian.midas.core.defaults.DefaultHolder;

import butterknife.InjectView;

/**
 * Created by meruvian on 15/09/15.
 */
public class BuyerOrderHolder extends DefaultHolder {
    public BuyerOrderHolder(View view) {
        super(view);
    }

    @InjectView(R.id.text_buyer_no_order) public TextView textBuyerNoOrder;

}