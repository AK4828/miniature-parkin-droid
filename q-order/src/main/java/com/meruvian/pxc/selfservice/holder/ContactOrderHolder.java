package com.meruvian.pxc.selfservice.holder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.meruvian.pxc.selfservice.R;

import org.meruvian.midas.core.defaults.DefaultHolder;

import butterknife.Bind;

/**
 * Created by meruvian on 30/09/15.
 */
public class ContactOrderHolder extends DefaultHolder {

    public ContactOrderHolder(View view) {
        super(view);
    }

    @Bind(R.id.text_contact_name) public TextView contactName;
    @Bind(R.id.text_contact_telp) public TextView contactTelp;
    @Bind(R.id.check_contact) public CheckBox checkContact;
}
