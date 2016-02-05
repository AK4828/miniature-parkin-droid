package com.meruvian.pxc.selfservice.holder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.meruvian.pxc.selfservice.R;

import org.meruvian.midas.core.defaults.DefaultHolder;

import butterknife.Bind;

/**
 * Created by meruvian on 14/07/15.
 */
public class BuyerOrderDetailHolder extends DefaultHolder {
    @Bind(R.id.text_item_name) public TextView itemName;
    @Bind(R.id.text_item_desc) public TextView itemDesc;
    @Bind(R.id.text_item_price) public TextView itemPrice;
    @Bind(R.id.text_item_qty) public TextView itemQty;
    @Bind(R.id.text_item_imei) public TextView itemImei;
    @Bind(R.id.button_scan_imei) public Button btnImei;

    public BuyerOrderDetailHolder(View view) {
        super(view);
    }
}
