package com.meruvian.pxc.selfservice.holder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.meruvian.pxc.selfservice.R;

import org.meruvian.midas.core.defaults.DefaultHolder;

import butterknife.InjectView;

/**
 * Created by meruvian on 30/09/15.
 */
public class ContactOrderHolder extends DefaultHolder {

    public ContactOrderHolder(View view) {
        super(view);
    }

    @InjectView(R.id.text_contact_name) public TextView contactName;
    @InjectView(R.id.text_contact_telp) public TextView contactTelp;
    @InjectView(R.id.check_contact) public CheckBox checkContact;
}
