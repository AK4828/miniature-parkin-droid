package com.hoqii.sales.selfservice.content.database.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.util.Log;

import com.hoqii.sales.selfservice.SignageVariables;
import com.hoqii.sales.selfservice.content.MidasContentProvider;
import com.hoqii.sales.selfservice.content.database.model.SettleDatabaseModel;
import com.hoqii.sales.selfservice.content.database.model.DefaultPersistenceModel;
import com.hoqii.sales.selfservice.entity.Settle;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by meruvian on 04/08/15.
 */
public class SettleDatabaseAdapter extends DefaultDatabaseAdapter {
    private Uri dbUriSettle = Uri.parse(MidasContentProvider.CONTENT_PATH
            + MidasContentProvider.TABLES[16]);

    private Context context;

    public SettleDatabaseAdapter(Context context) {
        this.context = context;
    }

    public void saveSettles(List<Settle> settles) {
        for (Settle settle : settles) {
            ContentValues contentValues = new ContentValues();

            if (findSettleByRefId(settle.getRefId()) == null) {
                UUID uuid = UUID.randomUUID();
                String id = String.valueOf(uuid);

                Log.d(getClass().getSimpleName(), "Save Settle: " + id);
                contentValues.put(SettleDatabaseModel.ID, id);
                contentValues.put(SettleDatabaseModel.CREATE_BY, settle
                        .getLogInformation().getCreateBy());
                contentValues.put(SettleDatabaseModel.CREATE_DATE, settle
                        .getLogInformation().getCreateDate().getTime());
                contentValues.put(SettleDatabaseModel.UPDATE_BY, settle
                        .getLogInformation().getLastUpdateBy());
                contentValues.put(SettleDatabaseModel.UPDATE_DATE, settle
                        .getLogInformation().getLastUpdateDate().getTime());

                contentValues.put(SettleDatabaseModel.ASSIGMENT_DETAIL_ID, settle.getAssigmentDetail().getId());
                contentValues.put(SettleDatabaseModel.PRODUCT_ID, settle.getProduct().getId());
                contentValues.put(SettleDatabaseModel.QUANTITY, settle.getQty());
                contentValues.put(SettleDatabaseModel.SELL_PRICE, settle.getSellPrice());

                contentValues.put(SettleDatabaseModel.REF_ID, settle.getRefId());

                contentValues.put(DefaultPersistenceModel.SYNC_STATUS, 0);
                contentValues.put(DefaultPersistenceModel.STATUS_FLAG, 1);

                context.getContentResolver().insert(dbUriSettle, contentValues);
            } else {
                Log.d(getClass().getSimpleName(), "Update Settle: " + settle.getRefId());

                contentValues.put(SettleDatabaseModel.UPDATE_BY, settle
                        .getLogInformation().getLastUpdateBy());
                contentValues.put(SettleDatabaseModel.UPDATE_DATE, settle
                        .getLogInformation().getLastUpdateDate().getTime());

                contentValues.put(SettleDatabaseModel.ASSIGMENT_DETAIL_ID, settle.getAssigmentDetail().getId());
                contentValues.put(SettleDatabaseModel.PRODUCT_ID, settle.getProduct().getId());
                contentValues.put(SettleDatabaseModel.QUANTITY, settle.getQty());
                contentValues.put(SettleDatabaseModel.SELL_PRICE, settle.getSellPrice());

                contentValues.put(SettleDatabaseModel.REF_ID, settle.getRefId());

                contentValues.put(DefaultPersistenceModel.SYNC_STATUS, 0);

                context.getContentResolver().update(dbUriSettle, contentValues,
                        SettleDatabaseModel.REF_ID + " = ?", new String[] { settle.getRefId() });
            }
        }
    }

    public String saveSettle(Settle settle) {
        ContentValues contentValues = new ContentValues();
        UUID uuid = UUID.randomUUID();
        String id = String.valueOf(uuid);

        contentValues.put(SettleDatabaseModel.ID, id);
        contentValues.put(SettleDatabaseModel.CREATE_BY, settle
                .getLogInformation().getCreateBy());
        contentValues.put(SettleDatabaseModel.CREATE_DATE, settle
                .getLogInformation().getCreateDate().getTime());
        contentValues.put(SettleDatabaseModel.UPDATE_BY, settle
                .getLogInformation().getLastUpdateBy());
        contentValues.put(SettleDatabaseModel.UPDATE_DATE, settle
                .getLogInformation().getLastUpdateDate().getTime());

        contentValues.put(SettleDatabaseModel.ASSIGMENT_DETAIL_ID, settle.getAssigmentDetail().getId());
        contentValues.put(SettleDatabaseModel.PRODUCT_ID, settle.getProduct().getId());
        contentValues.put(SettleDatabaseModel.QUANTITY, settle.getQty());
        contentValues.put(SettleDatabaseModel.SELL_PRICE, settle.getSellPrice());

        contentValues.put(DefaultPersistenceModel.SYNC_STATUS, 0);
        contentValues.put(DefaultPersistenceModel.STATUS_FLAG, 1);

        context.getContentResolver().insert(dbUriSettle, contentValues);
        return id;
    }

    public String updateSettle(Settle settle) {
        ContentValues contentValues = new ContentValues();

        if (settle.getId() != null) {
            Log.d(getClass().getSimpleName(), "Update Settle: " + settle.getId());

            contentValues.put(SettleDatabaseModel.UPDATE_BY, settle
                    .getLogInformation().getLastUpdateBy());
            contentValues.put(SettleDatabaseModel.UPDATE_DATE, settle
                    .getLogInformation().getLastUpdateDate().getTime());

            contentValues.put(SettleDatabaseModel.ASSIGMENT_DETAIL_ID, settle.getAssigmentDetail().getId());
            contentValues.put(SettleDatabaseModel.PRODUCT_ID, settle.getProduct().getId());
            contentValues.put(SettleDatabaseModel.QUANTITY, settle.getQty());
            contentValues.put(SettleDatabaseModel.SELL_PRICE, settle.getSellPrice());

            contentValues.put(DefaultPersistenceModel.SYNC_STATUS, 0);

            context.getContentResolver().update(dbUriSettle, contentValues,
                    SettleDatabaseModel.ID + " = ?", new String[] { settle.getId() });
            return settle.getId();
        } else {
            UUID uuid = UUID.randomUUID();
            String id = String.valueOf(uuid);

            Log.d(getClass().getSimpleName(), "Save Settle: " + id);
            contentValues.put(SettleDatabaseModel.ID, id);
            contentValues.put(SettleDatabaseModel.CREATE_BY, settle
                    .getLogInformation().getCreateBy());
            contentValues.put(SettleDatabaseModel.CREATE_DATE, settle
                    .getLogInformation().getCreateDate().getTime());
            contentValues.put(SettleDatabaseModel.UPDATE_BY, settle
                    .getLogInformation().getLastUpdateBy());
            contentValues.put(SettleDatabaseModel.UPDATE_DATE, settle
                    .getLogInformation().getLastUpdateDate().getTime());

            contentValues.put(SettleDatabaseModel.ASSIGMENT_DETAIL_ID, settle.getAssigmentDetail().getId());
            contentValues.put(SettleDatabaseModel.PRODUCT_ID, settle.getProduct().getId());
            contentValues.put(SettleDatabaseModel.QUANTITY, settle.getQty());
            contentValues.put(SettleDatabaseModel.SELL_PRICE, settle.getSellPrice());

            contentValues.put(DefaultPersistenceModel.SYNC_STATUS, 0);
            contentValues.put(DefaultPersistenceModel.STATUS_FLAG, 1);

            context.getContentResolver().insert(dbUriSettle, contentValues);
            return id;
        }
    }

    public List<String> findSettleIdesByUserId(String assigmentDetailId) {
        String query = SettleDatabaseModel.ASSIGMENT_DETAIL_ID + " = ?";
        String[] parameter = { assigmentDetailId };

        Cursor cursor = context.getContentResolver().query(dbUriSettle, null, query, parameter, null);

        List<String> settleIdes = new ArrayList<String>();

        if(cursor != null) {
            if(cursor.getCount() > 0) {
                try {
                    while (cursor.moveToNext()) {
                        settleIdes.add(cursor.getString(cursor.getColumnIndex(SettleDatabaseModel.ID)));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        cursor.close();

        return settleIdes;
    }

    public Settle findSettleByRefId(String refId) {
        String criteria = SettleDatabaseModel.REF_ID + " = ?";
        String[] parameter = { refId };
        Cursor cursor = context.getContentResolver().query(dbUriSettle,
                null, criteria, parameter, null);

        Settle settle  = null;

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                settle = new Settle();
                settle.setId(cursor.getString(cursor.getColumnIndex(SettleDatabaseModel.ID)));

                settle.getAssigmentDetail().setId(cursor.getString(cursor.getColumnIndex(SettleDatabaseModel.ASSIGMENT_DETAIL_ID)));
                settle.getProduct().setId(cursor.getString(cursor.getColumnIndex(SettleDatabaseModel.PRODUCT_ID)));
                settle.setQty(cursor.getInt(cursor.getColumnIndex(SettleDatabaseModel.QUANTITY)));
                settle.setSellPrice(cursor.getDouble(cursor.getColumnIndex(SettleDatabaseModel.SELL_PRICE)));

                settle.setRefId(cursor.getString(cursor.getColumnIndex(SettleDatabaseModel.REF_ID)));

            }
        }

        cursor.close();

        return settle;
    }


    public Settle findSettleById(String id) {
        String criteria = SettleDatabaseModel.ID + " = ?";
        String[] parameter = { id };
        Cursor cursor = context.getContentResolver().query(dbUriSettle,
                null, criteria, parameter, null);

        Settle settle = new Settle();

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                settle.setId(cursor.getString(cursor.getColumnIndex(SettleDatabaseModel.ID)));

                settle.getAssigmentDetail().setId(cursor.getString(cursor.getColumnIndex(SettleDatabaseModel.ASSIGMENT_DETAIL_ID)));
                settle.getProduct().setId(cursor.getString(cursor.getColumnIndex(SettleDatabaseModel.PRODUCT_ID)));
                settle.setQty(cursor.getInt(cursor.getColumnIndex(SettleDatabaseModel.QUANTITY)));
                settle.setSellPrice(cursor.getDouble(cursor.getColumnIndex(SettleDatabaseModel.SELL_PRICE)));

                settle.setRefId(cursor.getString(cursor.getColumnIndex(SettleDatabaseModel.REF_ID)));

            }
        }

        cursor.close();

        return settle;
    }

    public List<Settle> findAllSettle(String assigmentDetailId) {
        String criteria = SettleDatabaseModel.STATUS_FLAG + " = ? AND " + SettleDatabaseModel.ASSIGMENT_DETAIL_ID + " = ?";
        String[] parameter = { SignageVariables.ACTIVE, assigmentDetailId };
        Cursor cursor = context.getContentResolver().query(dbUriSettle,
                null, criteria, parameter, null);

        List<Settle> settles = new ArrayList<Settle>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Settle settle = new Settle();

                settle.setId(cursor.getString(cursor.getColumnIndex(SettleDatabaseModel.ID)));

                settle.getAssigmentDetail().setId(cursor.getString(cursor.getColumnIndex(SettleDatabaseModel.ASSIGMENT_DETAIL_ID)));
                settle.getProduct().setId(cursor.getString(cursor.getColumnIndex(SettleDatabaseModel.PRODUCT_ID)));
                settle.setQty(cursor.getInt(cursor.getColumnIndex(SettleDatabaseModel.QUANTITY)));
                settle.setSellPrice(cursor.getDouble(cursor.getColumnIndex(SettleDatabaseModel.SELL_PRICE)));

                settle.setRefId(cursor.getString(cursor.getColumnIndex(SettleDatabaseModel.REF_ID)));

                settles.add(settle);
            }
        }
        cursor.close();
        return settles;
    }

    public List<Settle> findSettleByUserId(String assigmentDetailId) {
        String criteria = SettleDatabaseModel.ASSIGMENT_DETAIL_ID + " = ?";
        String[] parameter = { assigmentDetailId };
        Cursor cursor = context.getContentResolver().query(dbUriSettle,
                null, criteria, parameter, null);

        List<Settle> settles = new ArrayList<Settle>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Settle settle = new Settle();

                settle.setId(cursor.getString(cursor.getColumnIndex(SettleDatabaseModel.ID)));

                settle.getAssigmentDetail().setId(cursor.getString(cursor.getColumnIndex(SettleDatabaseModel.ASSIGMENT_DETAIL_ID)));
                settle.getProduct().setId(cursor.getString(cursor.getColumnIndex(SettleDatabaseModel.PRODUCT_ID)));
                settle.setQty(cursor.getInt(cursor.getColumnIndex(SettleDatabaseModel.QUANTITY)));
                settle.setSellPrice(cursor.getDouble(cursor.getColumnIndex(SettleDatabaseModel.SELL_PRICE)));

                settle.setRefId(cursor.getString(cursor.getColumnIndex(SettleDatabaseModel.REF_ID)));

                settles.add(settle);
            }
        }
        cursor.close();

        return settles;
    }

    public void deleteSettle(String menuId) {
        String criteria = SettleDatabaseModel.ID + " = ?";
        String[] parameter = { menuId };

        context.getContentResolver()
                .delete(dbUriSettle, criteria, parameter);

    }

    public String getSettleRefIdById(String settleId) {
        String criteria = SettleDatabaseModel.ID + " = ?";
        String[] parameter = { settleId };
        Cursor cursor = context.getContentResolver().query(dbUriSettle,
                null, criteria, parameter, null);

        String id = null;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                id = cursor.getString(cursor
                        .getColumnIndex(SettleDatabaseModel.REF_ID));
            }
        }

        cursor.close();

        return id;
    }

    public String getSettleId() {
        Cursor cursor = context.getContentResolver().query(dbUriSettle,
                null, null, null,
                SettleDatabaseModel.CREATE_DATE + " DESC LIMIT 1");

        String id = null;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                id = cursor.getString(cursor
                        .getColumnIndex(SettleDatabaseModel.ID));
            }
        }

        cursor.close();

        return id;
    }

    public List<String> getAllRefId() {
        Cursor cursor = context.getContentResolver().query(dbUriSettle, null, null, null, null);

        List<String> categories = new ArrayList<String>();

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    categories.add(cursor.getString(cursor
                            .getColumnIndex(SettleDatabaseModel.REF_ID)));
                }
            }
        }

        cursor.close();

        return categories;
    }

    public void updateSyncStatusById(String id, String refId) {
        Log.d(getClass().getSimpleName(), "updateSyncStatusById.Success1");
        ContentValues values = new ContentValues();

        values.put(SettleDatabaseModel.SYNC_STATUS, 1);
        values.put(SettleDatabaseModel.REF_ID, refId);

        context.getContentResolver().update(dbUriSettle, values, SettleDatabaseModel.ID + " = ? ", new String[]{id});
    }

    public void deleteByRefIds(List<String> refIds) {
        for(String id : refIds) {
            ContentValues values = new ContentValues();
            values.put(SettleDatabaseModel.STATUS_FLAG, 0);

            context.getContentResolver().update(dbUriSettle, values, SettleDatabaseModel.REF_ID + " = ? ", new String[] { id });
        }
    }

    public void deleteSettleById(String settleId) {
        String criteria = SettleDatabaseModel.ID + " = ?";
        String[] parameter = { settleId };

        ContentValues values = new ContentValues();
        values.put(SettleDatabaseModel.STATUS_FLAG, 0);

        context.getContentResolver().update(dbUriSettle, values, criteria, parameter);
    }


}
