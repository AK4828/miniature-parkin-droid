package com.meruvian.pxc.selfservice.holder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.meruvian.pxc.selfservice.R;

import org.meruvian.midas.core.defaults.DefaultHolder;

import butterknife.Bind;

/**
 * Created by meruvian on 03/10/15.
 */
public class SettelAgentHolder extends DefaultHolder {
    public SettelAgentHolder(View view) {
        super(view);
    }

    @Bind(R.id.text_no_order)
    public TextView textNoOrder;
    @Bind(R.id.text_contact_name)
    public TextView textContactName;
    @Bind(R.id.text_contact_telp)
    public TextView textContactTelp;
    @Bind(R.id.button_cancel)
    public ImageButton btnCancel;

}
