package org.meruvian.esales.collector.adapter;

import android.content.Context;
import android.view.View;

import org.meruvian.esales.collector.entity.Contact;
import org.meruvian.esales.collector.holder.BuyerHolder;
import org.meruvian.midas.core.defaults.DefaultAdapter;

import java.util.List;

/**
 * Created by meruvian on 15/09/15.
 */
public class BuyerListAdapter extends DefaultAdapter<Contact, BuyerHolder> {

    public BuyerListAdapter(Context context, int layout, List<Contact> contents) {
        super(context, layout, contents);
    }

    @Override
    public BuyerHolder ViewHolder(View view) {
        return new BuyerHolder(view);
    }

    @Override
    public View createdView(View view, BuyerHolder holder, Contact contact) {
        holder.buyerName.setText(contact.getContactName());
        holder.buyerRecipientName.setText(contact.getRecipient());
        holder.buyerTelp.setText(contact.getPhone());

        return view;
    }
}
