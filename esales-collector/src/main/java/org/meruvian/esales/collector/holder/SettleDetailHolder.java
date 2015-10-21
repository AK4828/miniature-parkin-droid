package org.meruvian.esales.collector.holder;

import android.view.View;
import android.widget.TextView;

import org.meruvian.esales.collector.R;
import org.meruvian.midas.core.defaults.DefaultHolder;

import butterknife.InjectView;

/**
 * Created by meruvian on 06/10/15.
 */
public class SettleDetailHolder extends DefaultHolder {
    @InjectView(R.id.text_item_name) public TextView textItem;
    @InjectView(R.id.text_total_price) public TextView textTotal;
    @InjectView(R.id.text_item_quantity) public TextView textQty;
//    @InjectView(R.id.text_item_imei) public TextView itemImei;
//    @InjectView(R.id.button_scan_imei) public Button btnImei;

    public SettleDetailHolder(View view) {
        super(view);
    }
}
