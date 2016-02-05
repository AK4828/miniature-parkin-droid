package com.meruvian.pxc.selfservice.holder;

import android.view.View;
import android.widget.TextView;

import com.meruvian.pxc.selfservice.R;

import org.meruvian.midas.core.defaults.DefaultHolder;

import butterknife.Bind;

/**
 * Created by meruvian on 15/08/15.
 */
public class BuyerHolder extends DefaultHolder {
    public BuyerHolder(View view) {
        super(view);
    }

    @Bind(R.id.text_contact_name_buyer) public TextView buyerName;
    @Bind(R.id.text_recipient_name_buyer) public TextView buyerRecipientName;
    @Bind(R.id.text_contact_telp_buyer) public TextView buyerTelp;

}
