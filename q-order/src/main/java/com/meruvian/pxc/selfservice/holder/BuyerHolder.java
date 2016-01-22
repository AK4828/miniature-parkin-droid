package com.meruvian.pxc.selfservice.holder;

import android.view.View;
import android.widget.TextView;

import com.meruvian.pxc.selfservice.R;

import org.meruvian.midas.core.defaults.DefaultHolder;

import butterknife.InjectView;

/**
 * Created by meruvian on 15/08/15.
 */
public class BuyerHolder extends DefaultHolder {
    public BuyerHolder(View view) {
        super(view);
    }

    @InjectView(R.id.text_contact_name_buyer) public TextView buyerName;
    @InjectView(R.id.text_recipient_name_buyer) public TextView buyerRecipientName;
    @InjectView(R.id.text_contact_telp_buyer) public TextView buyerTelp;

}
