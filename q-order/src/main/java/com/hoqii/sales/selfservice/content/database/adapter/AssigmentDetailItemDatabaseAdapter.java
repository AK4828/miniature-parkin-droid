package com.hoqii.sales.selfservice.content.database.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.util.Log;

import com.hoqii.sales.selfservice.SignageVariables;
import com.hoqii.sales.selfservice.content.MidasContentProvider;
import com.hoqii.sales.selfservice.content.database.model.AssigmentDetailItemDatabaseModel;
import com.hoqii.sales.selfservice.content.database.model.DefaultPersistenceModel;
import com.hoqii.sales.selfservice.entity.AssigmentDetailItem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by meruvian on 04/08/15.
 */
public class AssigmentDetailItemDatabaseAdapter extends DefaultDatabaseAdapter {
    private Uri dbUriAssigmentDetailItem = Uri.parse(MidasContentProvider.CONTENT_PATH
            + MidasContentProvider.TABLES[15]);

    private Context context;

    public AssigmentDetailItemDatabaseAdapter(Context context) {
        this.context = context;
    }

    public void saveAssigmentDetailItems(List<AssigmentDetailItem> assigmentDetailItems) {
        for (AssigmentDetailItem assigmentDetailItem : assigmentDetailItems) {
            ContentValues contentValues = new ContentValues();

            if (findAssigmentDetailItemByRefId(assigmentDetailItem.getRefId()) == null) {
                UUID uuid = UUID.randomUUID();
                String id = String.valueOf(uuid);

                Log.d(getClass().getSimpleName(), "Save AssigmentDetailItem: " + id);
                contentValues.put(AssigmentDetailItemDatabaseModel.ID, id);
                contentValues.put(AssigmentDetailItemDatabaseModel.CREATE_BY, assigmentDetailItem
                        .getLogInformation().getCreateBy());
                contentValues.put(AssigmentDetailItemDatabaseModel.CREATE_DATE, assigmentDetailItem
                        .getLogInformation().getCreateDate().getTime());
                contentValues.put(AssigmentDetailItemDatabaseModel.UPDATE_BY, assigmentDetailItem
                        .getLogInformation().getLastUpdateBy());
                contentValues.put(AssigmentDetailItemDatabaseModel.UPDATE_DATE, assigmentDetailItem
                        .getLogInformation().getLastUpdateDate().getTime());

                contentValues.put(AssigmentDetailItemDatabaseModel.ASSIGMENT_DETAIL_ID, assigmentDetailItem.getAssigmentDetail().getId());
                contentValues.put(AssigmentDetailItemDatabaseModel.PRODUCT_ID, assigmentDetailItem.getProduct().getId());
                contentValues.put(AssigmentDetailItemDatabaseModel.QUANTITY, assigmentDetailItem.getQty());

                contentValues.put(AssigmentDetailItemDatabaseModel.REF_ID, assigmentDetailItem.getRefId());

                contentValues.put(DefaultPersistenceModel.SYNC_STATUS, 0);
                contentValues.put(DefaultPersistenceModel.STATUS_FLAG, 1);

                context.getContentResolver().insert(dbUriAssigmentDetailItem, contentValues);
            } else {
                Log.d(getClass().getSimpleName(), "Update AssigmentDetailItem: " + assigmentDetailItem.getRefId());

                contentValues.put(AssigmentDetailItemDatabaseModel.UPDATE_BY, assigmentDetailItem
                        .getLogInformation().getLastUpdateBy());
                contentValues.put(AssigmentDetailItemDatabaseModel.UPDATE_DATE, assigmentDetailItem
                        .getLogInformation().getLastUpdateDate().getTime());

                contentValues.put(AssigmentDetailItemDatabaseModel.ASSIGMENT_DETAIL_ID, assigmentDetailItem.getAssigmentDetail().getId());
                contentValues.put(AssigmentDetailItemDatabaseModel.PRODUCT_ID, assigmentDetailItem.getProduct().getId());
                contentValues.put(AssigmentDetailItemDatabaseModel.QUANTITY, assigmentDetailItem.getQty());

                contentValues.put(AssigmentDetailItemDatabaseModel.REF_ID, assigmentDetailItem.getRefId());

                contentValues.put(DefaultPersistenceModel.SYNC_STATUS, 0);

                context.getContentResolver().update(dbUriAssigmentDetailItem, contentValues,
                        AssigmentDetailItemDatabaseModel.REF_ID + " = ?", new String[] { assigmentDetailItem.getRefId() });
            }
        }
    }

    public String saveAssigmentDetailItem(AssigmentDetailItem assigmentDetailItem) {
        ContentValues contentValues = new ContentValues();
        UUID uuid = UUID.randomUUID();
        String id = String.valueOf(uuid);

        contentValues.put(AssigmentDetailItemDatabaseModel.ID, id);
        contentValues.put(AssigmentDetailItemDatabaseModel.CREATE_BY, assigmentDetailItem
                .getLogInformation().getCreateBy());
        contentValues.put(AssigmentDetailItemDatabaseModel.CREATE_DATE, assigmentDetailItem
                .getLogInformation().getCreateDate().getTime());
        contentValues.put(AssigmentDetailItemDatabaseModel.UPDATE_BY, assigmentDetailItem
                .getLogInformation().getLastUpdateBy());
        contentValues.put(AssigmentDetailItemDatabaseModel.UPDATE_DATE, assigmentDetailItem
                .getLogInformation().getLastUpdateDate().getTime());

        contentValues.put(AssigmentDetailItemDatabaseModel.ASSIGMENT_DETAIL_ID, assigmentDetailItem.getAssigmentDetail().getId());
        contentValues.put(AssigmentDetailItemDatabaseModel.PRODUCT_ID, assigmentDetailItem.getProduct().getId());
        contentValues.put(AssigmentDetailItemDatabaseModel.QUANTITY, assigmentDetailItem.getQty());

        contentValues.put(DefaultPersistenceModel.SYNC_STATUS, 0);
        contentValues.put(DefaultPersistenceModel.STATUS_FLAG, 1);

        context.getContentResolver().insert(dbUriAssigmentDetailItem, contentValues);
        return id;
    }

    public String updateAssigmentDetailItem(AssigmentDetailItem assigmentDetailItem) {
        ContentValues contentValues = new ContentValues();

        if (assigmentDetailItem.getId() != null) {
            Log.d(getClass().getSimpleName(), "Update AssigmentDetailItem: " + assigmentDetailItem.getId());

            contentValues.put(AssigmentDetailItemDatabaseModel.UPDATE_BY, assigmentDetailItem
                    .getLogInformation().getLastUpdateBy());
            contentValues.put(AssigmentDetailItemDatabaseModel.UPDATE_DATE, assigmentDetailItem
                    .getLogInformation().getLastUpdateDate().getTime());

            contentValues.put(AssigmentDetailItemDatabaseModel.ASSIGMENT_DETAIL_ID, assigmentDetailItem.getAssigmentDetail().getId());
            contentValues.put(AssigmentDetailItemDatabaseModel.PRODUCT_ID, assigmentDetailItem.getProduct().getId());
            contentValues.put(AssigmentDetailItemDatabaseModel.QUANTITY, assigmentDetailItem.getQty());

            contentValues.put(DefaultPersistenceModel.SYNC_STATUS, 0);

            context.getContentResolver().update(dbUriAssigmentDetailItem, contentValues,
                    AssigmentDetailItemDatabaseModel.ID + " = ?", new String[] { assigmentDetailItem.getId() });
            return assigmentDetailItem.getId();
        } else {
            UUID uuid = UUID.randomUUID();
            String id = String.valueOf(uuid);

            Log.d(getClass().getSimpleName(), "Save AssigmentDetailItem: " + id);
            contentValues.put(AssigmentDetailItemDatabaseModel.ID, id);
            contentValues.put(AssigmentDetailItemDatabaseModel.CREATE_BY, assigmentDetailItem
                    .getLogInformation().getCreateBy());
            contentValues.put(AssigmentDetailItemDatabaseModel.CREATE_DATE, assigmentDetailItem
                    .getLogInformation().getCreateDate().getTime());
            contentValues.put(AssigmentDetailItemDatabaseModel.UPDATE_BY, assigmentDetailItem
                    .getLogInformation().getLastUpdateBy());
            contentValues.put(AssigmentDetailItemDatabaseModel.UPDATE_DATE, assigmentDetailItem
                    .getLogInformation().getLastUpdateDate().getTime());

            contentValues.put(AssigmentDetailItemDatabaseModel.ASSIGMENT_DETAIL_ID, assigmentDetailItem.getAssigmentDetail().getId());
            contentValues.put(AssigmentDetailItemDatabaseModel.PRODUCT_ID, assigmentDetailItem.getProduct().getId());
            contentValues.put(AssigmentDetailItemDatabaseModel.QUANTITY, assigmentDetailItem.getQty());

            contentValues.put(DefaultPersistenceModel.SYNC_STATUS, 0);
            contentValues.put(DefaultPersistenceModel.STATUS_FLAG, 1);

            context.getContentResolver().insert(dbUriAssigmentDetailItem, contentValues);
            return id;
        }
    }

    public List<String> findAssigmentDetailItemIdesByUserId(String assigmentDetailId) {
        String query = AssigmentDetailItemDatabaseModel.ASSIGMENT_DETAIL_ID + " = ?";
        String[] parameter = { assigmentDetailId };

        Cursor cursor = context.getContentResolver().query(dbUriAssigmentDetailItem, null, query, parameter, null);

        List<String> assigmentDetailItemIdes = new ArrayList<String>();

        if(cursor != null) {
            if(cursor.getCount() > 0) {
                try {
                    while (cursor.moveToNext()) {
                        assigmentDetailItemIdes.add(cursor.getString(cursor.getColumnIndex(AssigmentDetailItemDatabaseModel.ID)));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        cursor.close();

        return assigmentDetailItemIdes;
    }

    public AssigmentDetailItem findAssigmentDetailItemByRefId(String refId) {
        String criteria = AssigmentDetailItemDatabaseModel.REF_ID + " = ?";
        String[] parameter = { refId };
        Cursor cursor = context.getContentResolver().query(dbUriAssigmentDetailItem,
                null, criteria, parameter, null);

        AssigmentDetailItem assigmentDetailItem  = null;

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                assigmentDetailItem = new AssigmentDetailItem();
                assigmentDetailItem.setId(cursor.getString(cursor.getColumnIndex(AssigmentDetailItemDatabaseModel.ID)));

                assigmentDetailItem.getAssigmentDetail().setId(cursor.getString(cursor.getColumnIndex(AssigmentDetailItemDatabaseModel.ASSIGMENT_DETAIL_ID)));
                assigmentDetailItem.getProduct().setId(cursor.getString(cursor.getColumnIndex(AssigmentDetailItemDatabaseModel.PRODUCT_ID)));
                assigmentDetailItem.setQty(cursor.getInt(cursor.getColumnIndex(AssigmentDetailItemDatabaseModel.PRODUCT_ID)));

                assigmentDetailItem.setRefId(cursor.getString(cursor.getColumnIndex(AssigmentDetailItemDatabaseModel.REF_ID)));

            }
        }

        cursor.close();

        return assigmentDetailItem;
    }


    public AssigmentDetailItem findAssigmentDetailItemById(String id) {
        String criteria = AssigmentDetailItemDatabaseModel.ID + " = ?";
        String[] parameter = { id };
        Cursor cursor = context.getContentResolver().query(dbUriAssigmentDetailItem,
                null, criteria, parameter, null);

        AssigmentDetailItem assigmentDetailItem = new AssigmentDetailItem();

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                assigmentDetailItem.setId(cursor.getString(cursor.getColumnIndex(AssigmentDetailItemDatabaseModel.ID)));

                assigmentDetailItem.getAssigmentDetail().setId(cursor.getString(cursor.getColumnIndex(AssigmentDetailItemDatabaseModel.ASSIGMENT_DETAIL_ID)));
                assigmentDetailItem.getProduct().setId(cursor.getString(cursor.getColumnIndex(AssigmentDetailItemDatabaseModel.PRODUCT_ID)));
                assigmentDetailItem.setQty(cursor.getInt(cursor.getColumnIndex(AssigmentDetailItemDatabaseModel.PRODUCT_ID)));

                assigmentDetailItem.setRefId(cursor.getString(cursor.getColumnIndex(AssigmentDetailItemDatabaseModel.REF_ID)));

            }
        }

        cursor.close();

        return assigmentDetailItem;
    }

    public List<AssigmentDetailItem> findAllAssigmentDetailItem(String assigmentDetailId) {
        String criteria = AssigmentDetailItemDatabaseModel.STATUS_FLAG + " = ? AND " + AssigmentDetailItemDatabaseModel.ASSIGMENT_DETAIL_ID + " = ?";
        String[] parameter = { SignageVariables.ACTIVE, assigmentDetailId };
        Cursor cursor = context.getContentResolver().query(dbUriAssigmentDetailItem,
                null, criteria, parameter, null);

        List<AssigmentDetailItem> assigmentDetailItems = new ArrayList<AssigmentDetailItem>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                AssigmentDetailItem assigmentDetailItem = new AssigmentDetailItem();

                assigmentDetailItem.setId(cursor.getString(cursor.getColumnIndex(AssigmentDetailItemDatabaseModel.ID)));

                assigmentDetailItem.getAssigmentDetail().setId(cursor.getString(cursor.getColumnIndex(AssigmentDetailItemDatabaseModel.ASSIGMENT_DETAIL_ID)));
                assigmentDetailItem.getProduct().setId(cursor.getString(cursor.getColumnIndex(AssigmentDetailItemDatabaseModel.PRODUCT_ID)));
                assigmentDetailItem.setQty(cursor.getInt(cursor.getColumnIndex(AssigmentDetailItemDatabaseModel.PRODUCT_ID)));

                assigmentDetailItem.setRefId(cursor.getString(cursor.getColumnIndex(AssigmentDetailItemDatabaseModel.REF_ID)));

                assigmentDetailItems.add(assigmentDetailItem);
            }
        }
        cursor.close();
        return assigmentDetailItems;
    }

    public List<AssigmentDetailItem> findAssigmentDetailItemByUserId(String assigmentDetailId) {
        String criteria = AssigmentDetailItemDatabaseModel.ASSIGMENT_DETAIL_ID + " = ?";
        String[] parameter = { assigmentDetailId };
        Cursor cursor = context.getContentResolver().query(dbUriAssigmentDetailItem,
                null, criteria, parameter, null);

        List<AssigmentDetailItem> assigmentDetailItems = new ArrayList<AssigmentDetailItem>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                AssigmentDetailItem assigmentDetailItem = new AssigmentDetailItem();

                assigmentDetailItem.setId(cursor.getString(cursor.getColumnIndex(AssigmentDetailItemDatabaseModel.ID)));

                assigmentDetailItem.getAssigmentDetail().setId(cursor.getString(cursor.getColumnIndex(AssigmentDetailItemDatabaseModel.ASSIGMENT_DETAIL_ID)));
                assigmentDetailItem.getProduct().setId(cursor.getString(cursor.getColumnIndex(AssigmentDetailItemDatabaseModel.PRODUCT_ID)));
                assigmentDetailItem.setQty(cursor.getInt(cursor.getColumnIndex(AssigmentDetailItemDatabaseModel.PRODUCT_ID)));

                assigmentDetailItem.setRefId(cursor.getString(cursor.getColumnIndex(AssigmentDetailItemDatabaseModel.REF_ID)));

                assigmentDetailItems.add(assigmentDetailItem);
            }
        }
        cursor.close();

        return assigmentDetailItems;
    }

    public void deleteAssigmentDetailItem(String menuId) {
        String criteria = AssigmentDetailItemDatabaseModel.ID + " = ?";
        String[] parameter = { menuId };

        context.getContentResolver()
                .delete(dbUriAssigmentDetailItem, criteria, parameter);

    }

    public String getAssigmentDetailItemRefIdById(String assigmentDetailItemId) {
        String criteria = AssigmentDetailItemDatabaseModel.ID + " = ?";
        String[] parameter = { assigmentDetailItemId };
        Cursor cursor = context.getContentResolver().query(dbUriAssigmentDetailItem,
                null, criteria, parameter, null);

        String id = null;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                id = cursor.getString(cursor
                        .getColumnIndex(AssigmentDetailItemDatabaseModel.REF_ID));
            }
        }

        cursor.close();

        return id;
    }

    public String getAssigmentDetailItemId() {
        Cursor cursor = context.getContentResolver().query(dbUriAssigmentDetailItem,
                null, null, null,
                AssigmentDetailItemDatabaseModel.CREATE_DATE + " DESC LIMIT 1");

        String id = null;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                id = cursor.getString(cursor
                        .getColumnIndex(AssigmentDetailItemDatabaseModel.ID));
            }
        }

        cursor.close();

        return id;
    }

    public List<String> getAllRefId() {
        Cursor cursor = context.getContentResolver().query(dbUriAssigmentDetailItem, null, null, null, null);

        List<String> categories = new ArrayList<String>();

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    categories.add(cursor.getString(cursor
                            .getColumnIndex(AssigmentDetailItemDatabaseModel.REF_ID)));
                }
            }
        }

        cursor.close();

        return categories;
    }

    public void updateSyncStatusById(String id, String refId) {
        Log.d(getClass().getSimpleName(), "updateSyncStatusById.Success1");
        ContentValues values = new ContentValues();

        values.put(AssigmentDetailItemDatabaseModel.SYNC_STATUS, 1);
        values.put(AssigmentDetailItemDatabaseModel.REF_ID, refId);

        context.getContentResolver().update(dbUriAssigmentDetailItem, values, AssigmentDetailItemDatabaseModel.ID + " = ? ", new String[]{id});
    }

    public void deleteByRefIds(List<String> refIds) {
        for(String id : refIds) {
            ContentValues values = new ContentValues();
            values.put(AssigmentDetailItemDatabaseModel.STATUS_FLAG, 0);

            context.getContentResolver().update(dbUriAssigmentDetailItem, values, AssigmentDetailItemDatabaseModel.REF_ID + " = ? ", new String[] { id });
        }
    }

    public void deleteAssigmentDetailItemById(String assigmentDetailItemId) {
        String criteria = AssigmentDetailItemDatabaseModel.ID + " = ?";
        String[] parameter = { assigmentDetailItemId };

        ContentValues values = new ContentValues();
        values.put(AssigmentDetailItemDatabaseModel.STATUS_FLAG, 0);

        context.getContentResolver().update(dbUriAssigmentDetailItem, values, criteria, parameter);
    }


}
