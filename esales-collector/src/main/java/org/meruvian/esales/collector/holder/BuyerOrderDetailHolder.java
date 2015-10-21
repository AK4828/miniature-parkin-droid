package org.meruvian.esales.collector.holder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.meruvian.esales.collector.R;
import org.meruvian.midas.core.defaults.DefaultHolder;

import butterknife.InjectView;

/**
 * Created by meruvian on 14/07/15.
 */
public class BuyerOrderDetailHolder extends DefaultHolder {
    @InjectView(R.id.text_item_name) public TextView itemName;
    @InjectView(R.id.text_item_desc) public TextView itemDesc;
    @InjectView(R.id.text_item_price) public TextView itemPrice;
    @InjectView(R.id.text_item_qty) public TextView itemQty;
    @InjectView(R.id.text_item_imei) public TextView itemImei;
    @InjectView(R.id.button_scan_imei) public Button btnImei;

    public BuyerOrderDetailHolder(View view) {
        super(view);
    }
}
