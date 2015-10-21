package com.hoqii.sales.selfservice.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.hoqii.sales.selfservice.entity.Contact;
import com.hoqii.sales.selfservice.holder.ContactOrderHolder;

import org.meruvian.midas.core.defaults.DefaultAdapter;

import java.util.List;

/**
 * Created by meruvian on 30/09/15.
 */
public class ContactOrderAdapter extends DefaultAdapter<Contact, ContactOrderHolder> {
    private int selectedPosition = -1;
    private int position;

    public ContactOrderAdapter(Context context, int layout, List<Contact> contents) {
        super(context, layout, contents);
    }

    @Override
    public ContactOrderHolder ViewHolder(View view) {
        return new ContactOrderHolder(view);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        this.position = position;

        return super.getView(position, convertView, parent);
    }

    @Override
    public View createdView(View view, ContactOrderHolder holder, Contact contact) {
        holder.contactName.setText(contact.getRecipient());
        holder.contactTelp.setText(contact.getPhone());

        Log.d(getClass().getSimpleName(), "1. position: " + position
                + "selected_position: " + selectedPosition);

        if(position == selectedPosition) {
            holder.checkContact.setChecked(true);
        } else {
            holder.checkContact.setChecked(false);
        }

        holder.checkContact.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    selectedPosition = position;
                }
                /*else {
                    selectedPosition = 0;
                }*/
                notifyDataSetChanged();
            }
        });

        Log.d(getClass().getSimpleName(), "2. position: " + position
                + "selected_position: " + selectedPosition);

        return view;
    }

    public void setItemSelected(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }

}
