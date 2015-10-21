package com.hoqii.sales.selfservice.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.hoqii.sales.selfservice.activity.ContactListActivity;
import com.hoqii.sales.selfservice.content.database.adapter.ContactDatabaseAdapter;
import com.hoqii.sales.selfservice.entity.Contact;
import com.hoqii.sales.selfservice.holder.ContactHolder;

import org.meruvian.midas.core.defaults.DefaultActivity;
import org.meruvian.midas.core.defaults.DefaultAdapter;

import java.util.List;

/**
 * Created by meruvian on 15/08/15.
 */
public class ContactAdapter extends DefaultAdapter<Contact, ContactHolder> {
    private ContactDatabaseAdapter contactDatabaseAdapter;
    private DefaultActivity defaultActivity;

    public ContactAdapter(Context context, int layout, List<Contact> contents, DefaultActivity defaultActivity) {
        super(context, layout, contents);
        this.defaultActivity = defaultActivity;

        contactDatabaseAdapter = new ContactDatabaseAdapter(context);
    }

    @Override
    public ContactHolder ViewHolder(View view) {
        return new ContactHolder(view);
    }

    @Override
    public View createdView(View view, ContactHolder holder, Contact contact) {
//        holder.contactName.setText(contact.getContactName());
        holder.recipientName.setText(contact.getRecipient());
        holder.contactTelp.setText(contact.getPhone());

        holder.buttonDelete.setOnClickListener(new OnClickData(getContext(), getPosition(), contact.getId(), true));
        holder.buttonEdit.setOnClickListener(new OnClickData(getContext(), getPosition(), contact.getId(), false));

        Log.d(getClass().getSimpleName(), "1.Contact ID: " + contact.getId());

        return view;
    }

    private class OnClickData extends ImageButton implements ImageButton.OnClickListener {
        private boolean isDelete;
        private int location;
        private String contactId;

        public OnClickData(Context context, int location, String contactId, boolean isDelete) {
            super(context);
            this.location = location;
            this.contactId = contactId;
            this.isDelete = isDelete;
        }

        @Override
        public void onClick(View v) {
            if (isDelete){
                confirmDialog(location, contactId);
                Log.d(getClass().getSimpleName(), "2.Contact ID: " + contactId);
            } else {
                ContactListActivity contactListActivity = (ContactListActivity) defaultActivity;
                contactListActivity.dialogContact(contactId, true);
                /*getList().remove(location);
                notifyDataSetChanged();*/
            }
        }

        private void confirmDialog(final int location, final String cId) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Konfirmasi");
            builder.setMessage("Hapus Kontak ?");
            builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ContactListActivity contactListActivity = (ContactListActivity) defaultActivity;
                    contactListActivity.startDialog();

                    Log.d(getClass().getSimpleName(), "3.Contact ID: " + cId);

                    getList().remove(location);
                    notifyDataSetChanged();
                    contactListActivity.syncDeleteContact(contactDatabaseAdapter.getContactRefIdById(cId),cId);
                    contactDatabaseAdapter.deleteContactById(cId);

                }
            });
            builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.create().show();
        }

    }

}
