package com.meruvian.pxc.selfservice.holder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.meruvian.pxc.selfservice.R;

import org.meruvian.midas.core.defaults.DefaultHolder;

import butterknife.Bind;


/**
 * Created by meruvian on 15/08/15.
 */
public class ContactHolder extends DefaultHolder {
    public ContactHolder(View view) {
        super(view);
    }

//    @InjectView(R.id.text_contact_name) public TextView contactName;
    @Bind(R.id.text_recipient_name) public TextView recipientName;
    @Bind(R.id.text_contact_telp) public TextView contactTelp;
    @Bind(R.id.image_button_delete_contact) public ImageButton buttonDelete;
    @Bind(R.id.image_button_edit_contact) public ImageButton buttonEdit;

}
