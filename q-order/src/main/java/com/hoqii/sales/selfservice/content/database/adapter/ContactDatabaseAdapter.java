package com.hoqii.sales.selfservice.content.database.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.util.Log;

import com.hoqii.sales.selfservice.SignageVariables;
import com.hoqii.sales.selfservice.content.MidasContentProvider;
import com.hoqii.sales.selfservice.content.database.model.ContactDatabaseModel;
import com.hoqii.sales.selfservice.content.database.model.DefaultPersistenceModel;
import com.hoqii.sales.selfservice.entity.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by meruvian on 04/08/15.
 */
public class ContactDatabaseAdapter extends DefaultDatabaseAdapter {
    private Uri dbUriContact = Uri.parse(MidasContentProvider.CONTENT_PATH
            + MidasContentProvider.TABLES[7]);

    private Context context;

    public ContactDatabaseAdapter(Context context) {
        this.context = context;
    }

    public void saveContacts(List<Contact> contacts) {
        for (Contact contact : contacts) {
            ContentValues contentValues = new ContentValues();

            if (findContactByRefId(contact.getRefId()) == null) {
                UUID uuid = UUID.randomUUID();
                String id = String.valueOf(uuid);

                Log.d(getClass().getSimpleName(), "Save Contact: " + id);
                contentValues.put(ContactDatabaseModel.ID, id);
                contentValues.put(ContactDatabaseModel.CREATE_BY, contact
                        .getLogInformation().getCreateBy());
                contentValues.put(ContactDatabaseModel.CREATE_DATE, contact
                        .getLogInformation().getCreateDate().getTime());
                contentValues.put(ContactDatabaseModel.UPDATE_BY, contact
                        .getLogInformation().getLastUpdateBy());
                contentValues.put(ContactDatabaseModel.UPDATE_DATE, contact
                        .getLogInformation().getLastUpdateDate().getTime());
                contentValues.put(ContactDatabaseModel.DEFAULT_CONTACT, contact.isDefaultContact());
                contentValues.put(ContactDatabaseModel.USER_ID, contact.getUser().getId());
                contentValues.put(ContactDatabaseModel.RECIPIENT, contact.getRecipient());
                contentValues.put(ContactDatabaseModel.PHONE, contact.getPhone());
                contentValues.put(ContactDatabaseModel.CITY, contact.getCity());
                contentValues.put(ContactDatabaseModel.SUB_DISTRICT, contact.getSubDistrict());
                contentValues.put(ContactDatabaseModel.PROVINCE, contact.getProvince());
                contentValues.put(ContactDatabaseModel.ZIP, contact.getZip());
                contentValues.put(ContactDatabaseModel.ADDRESS, contact.getAddress());
                contentValues.put(ContactDatabaseModel.CONTACT_NAME, contact.getContactName());
                contentValues.put(ContactDatabaseModel.REF_ID, contact.getRefId());

                contentValues.put(DefaultPersistenceModel.SYNC_STATUS, 0);
                contentValues.put(DefaultPersistenceModel.STATUS_FLAG, 1);

                context.getContentResolver().insert(dbUriContact, contentValues);
            } else {
                Log.d(getClass().getSimpleName(), "Update Contact: " + contact.getRefId());

                contentValues.put(ContactDatabaseModel.UPDATE_BY, contact
                        .getLogInformation().getLastUpdateBy());
                contentValues.put(ContactDatabaseModel.UPDATE_DATE, contact
                        .getLogInformation().getLastUpdateDate().getTime());
                contentValues.put(ContactDatabaseModel.DEFAULT_CONTACT, contact.isDefaultContact());
                contentValues.put(ContactDatabaseModel.USER_ID, contact.getUser().getId());
                contentValues.put(ContactDatabaseModel.RECIPIENT, contact.getRecipient());
                contentValues.put(ContactDatabaseModel.PHONE, contact.getPhone());
                contentValues.put(ContactDatabaseModel.CITY, contact.getCity());
                contentValues.put(ContactDatabaseModel.SUB_DISTRICT, contact.getSubDistrict());
                contentValues.put(ContactDatabaseModel.PROVINCE, contact.getProvince());
                contentValues.put(ContactDatabaseModel.ZIP, contact.getZip());
                contentValues.put(ContactDatabaseModel.ADDRESS, contact.getAddress());
                contentValues.put(ContactDatabaseModel.CONTACT_NAME, contact.getContactName());
                contentValues.put(ContactDatabaseModel.REF_ID, contact.getRefId());

                contentValues.put(DefaultPersistenceModel.SYNC_STATUS, 0);

                context.getContentResolver().update(dbUriContact, contentValues,
                        ContactDatabaseModel.REF_ID + " = ?", new String[] { contact.getRefId() });
            }
        }
    }

    public String saveContact(Contact contact) {
        ContentValues contentValues = new ContentValues();
        UUID uuid = UUID.randomUUID();
        String id = String.valueOf(uuid);

        contentValues.put(ContactDatabaseModel.ID, id);
        contentValues.put(ContactDatabaseModel.CREATE_BY, contact
                .getLogInformation().getCreateBy());
        contentValues.put(ContactDatabaseModel.CREATE_DATE, contact
                .getLogInformation().getCreateDate().getTime());
        contentValues.put(ContactDatabaseModel.UPDATE_BY, contact
                .getLogInformation().getLastUpdateBy());
        contentValues.put(ContactDatabaseModel.UPDATE_DATE, contact
                .getLogInformation().getLastUpdateDate().getTime());
        contentValues.put(ContactDatabaseModel.DEFAULT_CONTACT, contact.isDefaultContact());
        contentValues.put(ContactDatabaseModel.USER_ID, contact.getUser().getId());
        contentValues.put(ContactDatabaseModel.RECIPIENT, contact.getRecipient());
        contentValues.put(ContactDatabaseModel.PHONE, contact.getPhone());
        contentValues.put(ContactDatabaseModel.CITY, contact.getCity());
        contentValues.put(ContactDatabaseModel.SUB_DISTRICT, contact.getSubDistrict());
        contentValues.put(ContactDatabaseModel.PROVINCE, contact.getProvince());
        contentValues.put(ContactDatabaseModel.ZIP, contact.getZip());
        contentValues.put(ContactDatabaseModel.ADDRESS, contact.getAddress());
        contentValues.put(ContactDatabaseModel.CONTACT_NAME, contact.getContactName());

        contentValues.put(DefaultPersistenceModel.SYNC_STATUS, 0);
        contentValues.put(DefaultPersistenceModel.STATUS_FLAG, 1);

        context.getContentResolver().insert(dbUriContact, contentValues);
        return id;
    }

    public String updateContact(Contact contact) {
        ContentValues contentValues = new ContentValues();

        if (contact.getId() != null) {
            Log.d(getClass().getSimpleName(), "Update Contact: " + contact.getId());

            contentValues.put(ContactDatabaseModel.UPDATE_BY, contact
                    .getLogInformation().getLastUpdateBy());
            contentValues.put(ContactDatabaseModel.UPDATE_DATE, contact
                    .getLogInformation().getLastUpdateDate().getTime());
            contentValues.put(ContactDatabaseModel.DEFAULT_CONTACT, contact.isDefaultContact());
            contentValues.put(ContactDatabaseModel.USER_ID, contact.getUser().getId());
            contentValues.put(ContactDatabaseModel.RECIPIENT, contact.getRecipient());
            contentValues.put(ContactDatabaseModel.PHONE, contact.getPhone());
            contentValues.put(ContactDatabaseModel.CITY, contact.getCity());
            contentValues.put(ContactDatabaseModel.SUB_DISTRICT, contact.getSubDistrict());
            contentValues.put(ContactDatabaseModel.PROVINCE, contact.getProvince());
            contentValues.put(ContactDatabaseModel.ZIP, contact.getZip());
            contentValues.put(ContactDatabaseModel.ADDRESS, contact.getAddress());
            contentValues.put(ContactDatabaseModel.CONTACT_NAME, contact.getContactName());
            contentValues.put(DefaultPersistenceModel.SYNC_STATUS, 0);

            context.getContentResolver().update(dbUriContact, contentValues,
                    ContactDatabaseModel.ID + " = ?", new String[] { contact.getId() });
            return contact.getId();
        } else {
            UUID uuid = UUID.randomUUID();
            String id = String.valueOf(uuid);

            Log.d(getClass().getSimpleName(), "Save Contact: " + id);
            contentValues.put(ContactDatabaseModel.ID, id);
            contentValues.put(ContactDatabaseModel.CREATE_BY, contact
                    .getLogInformation().getCreateBy());
            contentValues.put(ContactDatabaseModel.CREATE_DATE, contact
                    .getLogInformation().getCreateDate().getTime());
            contentValues.put(ContactDatabaseModel.UPDATE_BY, contact
                    .getLogInformation().getLastUpdateBy());
            contentValues.put(ContactDatabaseModel.UPDATE_DATE, contact
                    .getLogInformation().getLastUpdateDate().getTime());
            contentValues.put(ContactDatabaseModel.DEFAULT_CONTACT, contact.isDefaultContact());
            contentValues.put(ContactDatabaseModel.USER_ID, contact.getUser().getId());
            contentValues.put(ContactDatabaseModel.RECIPIENT, contact.getRecipient());
            contentValues.put(ContactDatabaseModel.PHONE, contact.getPhone());
            contentValues.put(ContactDatabaseModel.CITY, contact.getCity());
            contentValues.put(ContactDatabaseModel.SUB_DISTRICT, contact.getSubDistrict());
            contentValues.put(ContactDatabaseModel.PROVINCE, contact.getProvince());
            contentValues.put(ContactDatabaseModel.ZIP, contact.getZip());
            contentValues.put(ContactDatabaseModel.ADDRESS, contact.getAddress());
            contentValues.put(ContactDatabaseModel.CONTACT_NAME, contact.getContactName());

            contentValues.put(DefaultPersistenceModel.SYNC_STATUS, 0);
            contentValues.put(DefaultPersistenceModel.STATUS_FLAG, 1);

            context.getContentResolver().insert(dbUriContact, contentValues);
            return id;
        }
    }

    public List<String> findContactIdesByUserId(String orderId) {
        String query = ContactDatabaseModel.USER_ID + " = ?";
        String[] parameter = { orderId };

        Cursor cursor = context.getContentResolver().query(dbUriContact, null, query, parameter, null);

        List<String> contactIdes = new ArrayList<String>();

        if(cursor != null) {
            if(cursor.getCount() > 0) {
                try {
                    while (cursor.moveToNext()) {
                        contactIdes.add(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.ID)));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        cursor.close();

        return contactIdes;
    }

    public Contact findContactByRefId(String refId) {
        String criteria = ContactDatabaseModel.REF_ID + " = ?";
        String[] parameter = { refId };
        Cursor cursor = context.getContentResolver().query(dbUriContact,
                null, criteria, parameter, null);

        Contact contact  = null;

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                contact = new Contact();
                contact.setId(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.ID)));
                contact.setDefaultContact(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.DEFAULT_CONTACT))));
                contact.getUser().setId(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.USER_ID)));
                contact.setRecipient(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.RECIPIENT)));
                contact.setPhone(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.PHONE)));
                contact.setCity(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.CITY)));
                contact.setSubDistrict(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.SUB_DISTRICT)));
                contact.setProvince(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.PROVINCE)));
                contact.setZip(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.ZIP)));
                contact.setAddress(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.ADDRESS)));
                contact.setContactName(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.CONTACT_NAME)));
                contact.setRefId(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.REF_ID)));

            }
        }

        cursor.close();

        return contact;
    }


    public Contact findContactById(String id) {
        String criteria = ContactDatabaseModel.ID + " = ?";
        String[] parameter = { id };
        Cursor cursor = context.getContentResolver().query(dbUriContact,
                null, criteria, parameter, null);

        Contact contact = new Contact();

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                contact.setId(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.ID)));
                contact.setDefaultContact(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.DEFAULT_CONTACT))));
                contact.getUser().setId(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.USER_ID)));
                contact.setRecipient(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.RECIPIENT)));
                contact.setPhone(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.PHONE)));
                contact.setCity(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.CITY)));
                contact.setSubDistrict(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.SUB_DISTRICT)));
                contact.setProvince(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.PROVINCE)));
                contact.setZip(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.ZIP)));
                contact.setAddress(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.ADDRESS)));
                contact.setContactName(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.CONTACT_NAME)));
                contact.setRefId(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.REF_ID)));

            }
        }

        cursor.close();

        return contact;
    }

    public List<Contact> findAllContact(String userId) {
        String criteria = ContactDatabaseModel.STATUS_FLAG + " = ? AND " + ContactDatabaseModel.USER_ID + " = ?";
        String[] parameter = { SignageVariables.ACTIVE, userId };
        Cursor cursor = context.getContentResolver().query(dbUriContact,
                null, criteria, parameter, null);

        List<Contact> contacts = new ArrayList<Contact>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Contact contact = new Contact();

                contact.setId(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.ID)));
                contact.setDefaultContact(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.DEFAULT_CONTACT))));
                contact.getUser().setId(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.USER_ID)));
                contact.setRecipient(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.RECIPIENT)));
                contact.setPhone(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.PHONE)));
                contact.setCity(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.CITY)));
                contact.setSubDistrict(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.SUB_DISTRICT)));
                contact.setProvince(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.PROVINCE)));
                contact.setZip(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.ZIP)));
                contact.setAddress(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.ADDRESS)));
                contact.setContactName(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.CONTACT_NAME)));
                contact.setRefId(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.REF_ID)));

                contacts.add(contact);
            }
        }
        cursor.close();
        return contacts;
    }

    public List<Contact> findContactByUserId(String userId) {
        String criteria = ContactDatabaseModel.USER_ID + " = ?";
        String[] parameter = { userId };
        Cursor cursor = context.getContentResolver().query(dbUriContact,
                null, criteria, parameter, null);

        List<Contact> contacts = new ArrayList<Contact>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Contact contact = new Contact();

                contact.setId(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.ID)));
                contact.setDefaultContact(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.DEFAULT_CONTACT))));
                contact.getUser().setId(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.USER_ID)));
                contact.setRecipient(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.RECIPIENT)));
                contact.setPhone(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.PHONE)));
                contact.setCity(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.CITY)));
                contact.setSubDistrict(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.SUB_DISTRICT)));
                contact.setProvince(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.PROVINCE)));
                contact.setZip(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.ZIP)));
                contact.setAddress(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.ADDRESS)));
                contact.setContactName(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.CONTACT_NAME)));
                contact.setRefId(cursor.getString(cursor.getColumnIndex(ContactDatabaseModel.REF_ID)));

                contacts.add(contact);
            }
        }
        cursor.close();

        return contacts;
    }

    public void deleteContact(String menuId) {
        String criteria = ContactDatabaseModel.ID + " = ?";
        String[] parameter = { menuId };

        context.getContentResolver()
                .delete(dbUriContact, criteria, parameter);

    }

    public String getContactRefIdById(String contactId) {
        String criteria = ContactDatabaseModel.ID + " = ?";
        String[] parameter = { contactId };
        Cursor cursor = context.getContentResolver().query(dbUriContact,
                null, criteria, parameter, null);

        String id = null;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                id = cursor.getString(cursor
                        .getColumnIndex(ContactDatabaseModel.REF_ID));
            }
        }

        cursor.close();

        return id;
    }

    public String getContactId() {
        Cursor cursor = context.getContentResolver().query(dbUriContact,
                null, null, null,
                ContactDatabaseModel.CREATE_DATE + " DESC LIMIT 1");

        String id = null;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                id = cursor.getString(cursor
                        .getColumnIndex(ContactDatabaseModel.ID));
            }
        }

        cursor.close();

        return id;
    }

    public List<String> getAllRefId() {
        Cursor cursor = context.getContentResolver().query(dbUriContact, null, null, null, null);

        List<String> categories = new ArrayList<String>();

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    categories.add(cursor.getString(cursor
                            .getColumnIndex(ContactDatabaseModel.REF_ID)));
                }
            }
        }

        cursor.close();

        return categories;
    }

    public void updateSyncStatusById(String id, String refId) {
        Log.d(getClass().getSimpleName(), "updateSyncStatusById.Success1");
        ContentValues values = new ContentValues();

        values.put(ContactDatabaseModel.SYNC_STATUS, 1);
        values.put(ContactDatabaseModel.REF_ID, refId);

        context.getContentResolver().update(dbUriContact, values, ContactDatabaseModel.ID + " = ? ", new String[]{id});
    }

    public void deleteByRefIds(List<String> refIds) {
        for(String id : refIds) {
            ContentValues values = new ContentValues();
            values.put(ContactDatabaseModel.STATUS_FLAG, 0);

            context.getContentResolver().update(dbUriContact, values, ContactDatabaseModel.REF_ID + " = ? ", new String[] { id });
        }
    }

    public void deleteContactById(String contactId) {
        String criteria = ContactDatabaseModel.ID + " = ?";
        String[] parameter = { contactId };

        ContentValues values = new ContentValues();
        values.put(ContactDatabaseModel.STATUS_FLAG, 0);

        context.getContentResolver().update(dbUriContact, values, criteria, parameter);
    }


}
