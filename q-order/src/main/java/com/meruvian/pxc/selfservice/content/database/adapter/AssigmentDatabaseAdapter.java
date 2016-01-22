package com.meruvian.pxc.selfservice.content.database.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.util.Log;

import com.meruvian.pxc.selfservice.SignageVariables;
import com.meruvian.pxc.selfservice.content.MidasContentProvider;
import com.meruvian.pxc.selfservice.content.database.model.AssigmentDatabaseModel;
import com.meruvian.pxc.selfservice.content.database.model.DefaultPersistenceModel;
import com.meruvian.pxc.selfservice.entity.Assigment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by meruvian on 04/08/15.
 */
public class AssigmentDatabaseAdapter extends DefaultDatabaseAdapter {
    private Uri dbUriAssigment = Uri.parse(MidasContentProvider.CONTENT_PATH
            + MidasContentProvider.TABLES[13]);

    private Context context;

    public AssigmentDatabaseAdapter(Context context) {
        this.context = context;
    }

    public void saveAssigments(List<Assigment> assigments) {
        for (Assigment assigment : assigments) {
            ContentValues contentValues = new ContentValues();

            if (findAssigmentByRefId(assigment.getRefId()) == null) {
                UUID uuid = UUID.randomUUID();
                String id = String.valueOf(uuid);

                Log.d(getClass().getSimpleName(), "Save Assigment: " + id);
                contentValues.put(AssigmentDatabaseModel.ID, id);
                contentValues.put(AssigmentDatabaseModel.CREATE_BY, assigment
                        .getLogInformation().getCreateBy());
                contentValues.put(AssigmentDatabaseModel.CREATE_DATE, assigment
                        .getLogInformation().getCreateDate().getTime());
                contentValues.put(AssigmentDatabaseModel.UPDATE_BY, assigment
                        .getLogInformation().getLastUpdateBy());
                contentValues.put(AssigmentDatabaseModel.UPDATE_DATE, assigment
                        .getLogInformation().getLastUpdateDate().getTime());

                contentValues.put(AssigmentDatabaseModel.COLLECTOR_ID, assigment.getCollector().getId());
                contentValues.put(AssigmentDatabaseModel.STATUS, assigment.getStatus().name());
                contentValues.put(AssigmentDatabaseModel.REF_ID, assigment.getRefId());

                contentValues.put(DefaultPersistenceModel.SYNC_STATUS, 0);
                contentValues.put(DefaultPersistenceModel.STATUS_FLAG, 1);

                context.getContentResolver().insert(dbUriAssigment, contentValues);
            } else {
                Log.d(getClass().getSimpleName(), "Update Assigment: " + assigment.getRefId());

                contentValues.put(AssigmentDatabaseModel.UPDATE_BY, assigment
                        .getLogInformation().getLastUpdateBy());
                contentValues.put(AssigmentDatabaseModel.UPDATE_DATE, assigment
                        .getLogInformation().getLastUpdateDate().getTime());
                contentValues.put(AssigmentDatabaseModel.COLLECTOR_ID, assigment.getCollector().getId());
                contentValues.put(AssigmentDatabaseModel.STATUS, assigment.getStatus().name());
                contentValues.put(AssigmentDatabaseModel.REF_ID, assigment.getRefId());

                contentValues.put(DefaultPersistenceModel.SYNC_STATUS, 0);

                context.getContentResolver().update(dbUriAssigment, contentValues,
                        AssigmentDatabaseModel.REF_ID + " = ?", new String[] { assigment.getRefId() });
            }
        }
    }

    public String saveAssigment(Assigment assigment) {
        ContentValues contentValues = new ContentValues();
        UUID uuid = UUID.randomUUID();
        String id = String.valueOf(uuid);

        contentValues.put(AssigmentDatabaseModel.ID, id);
        contentValues.put(AssigmentDatabaseModel.CREATE_BY, assigment
                .getLogInformation().getCreateBy());
        contentValues.put(AssigmentDatabaseModel.CREATE_DATE, assigment
                .getLogInformation().getCreateDate().getTime());
        contentValues.put(AssigmentDatabaseModel.UPDATE_BY, assigment
                .getLogInformation().getLastUpdateBy());
        contentValues.put(AssigmentDatabaseModel.UPDATE_DATE, assigment
                .getLogInformation().getLastUpdateDate().getTime());
        contentValues.put(AssigmentDatabaseModel.COLLECTOR_ID, assigment.getCollector().getId());
        contentValues.put(AssigmentDatabaseModel.STATUS, assigment.getStatus().name());

        contentValues.put(DefaultPersistenceModel.SYNC_STATUS, 0);
        contentValues.put(DefaultPersistenceModel.STATUS_FLAG, 1);

        context.getContentResolver().insert(dbUriAssigment, contentValues);
        return id;
    }

    public String updateAssigment(Assigment assigment) {
        ContentValues contentValues = new ContentValues();

        if (assigment.getId() != null) {
            Log.d(getClass().getSimpleName(), "Update Assigment: " + assigment.getId());

            contentValues.put(AssigmentDatabaseModel.UPDATE_BY, assigment
                    .getLogInformation().getLastUpdateBy());
            contentValues.put(AssigmentDatabaseModel.UPDATE_DATE, assigment
                    .getLogInformation().getLastUpdateDate().getTime());
            contentValues.put(AssigmentDatabaseModel.COLLECTOR_ID, assigment.getCollector().getId());
            contentValues.put(AssigmentDatabaseModel.STATUS, assigment.getStatus().name());
            contentValues.put(DefaultPersistenceModel.SYNC_STATUS, 0);

            context.getContentResolver().update(dbUriAssigment, contentValues,
                    AssigmentDatabaseModel.ID + " = ?", new String[] { assigment.getId() });
            return assigment.getId();
        } else {
            UUID uuid = UUID.randomUUID();
            String id = String.valueOf(uuid);

            Log.d(getClass().getSimpleName(), "Save Assigment: " + id);
            contentValues.put(AssigmentDatabaseModel.ID, id);
            contentValues.put(AssigmentDatabaseModel.CREATE_BY, assigment
                    .getLogInformation().getCreateBy());
            contentValues.put(AssigmentDatabaseModel.CREATE_DATE, assigment
                    .getLogInformation().getCreateDate().getTime());
            contentValues.put(AssigmentDatabaseModel.UPDATE_BY, assigment
                    .getLogInformation().getLastUpdateBy());
            contentValues.put(AssigmentDatabaseModel.UPDATE_DATE, assigment
                    .getLogInformation().getLastUpdateDate().getTime());
            contentValues.put(AssigmentDatabaseModel.COLLECTOR_ID, assigment.getCollector().getId());
            contentValues.put(AssigmentDatabaseModel.STATUS, assigment.getStatus().name());

            contentValues.put(DefaultPersistenceModel.SYNC_STATUS, 0);
            contentValues.put(DefaultPersistenceModel.STATUS_FLAG, 1);

            context.getContentResolver().insert(dbUriAssigment, contentValues);
            return id;
        }
    }

    public List<String> findAssigmentIdesByUserId(String collectorId) {
        String query = AssigmentDatabaseModel.COLLECTOR_ID + " = ?";
        String[] parameter = { collectorId };

        Cursor cursor = context.getContentResolver().query(dbUriAssigment, null, query, parameter, null);

        List<String> assigmentIdes = new ArrayList<String>();

        if(cursor != null) {
            if(cursor.getCount() > 0) {
                try {
                    while (cursor.moveToNext()) {
                        assigmentIdes.add(cursor.getString(cursor.getColumnIndex(AssigmentDatabaseModel.ID)));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        cursor.close();

        return assigmentIdes;
    }

    public Assigment findAssigmentByRefId(String refId) {
        String criteria = AssigmentDatabaseModel.REF_ID + " = ?";
        String[] parameter = { refId };
        Cursor cursor = context.getContentResolver().query(dbUriAssigment,
                null, criteria, parameter, null);

        Assigment assigment  = null;

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                assigment = new Assigment();
                assigment.setId(cursor.getString(cursor.getColumnIndex(AssigmentDatabaseModel.ID)));

                assigment.getCollector().setId(cursor.getString(cursor.getColumnIndex(
                        AssigmentDatabaseModel.COLLECTOR_ID)));
                assigment.setStatus(Assigment.AssigmentStatus.valueOf(cursor.getString(
                        cursor.getColumnIndex(AssigmentDatabaseModel.STATUS))));

                assigment.setRefId(cursor.getString(cursor.getColumnIndex(AssigmentDatabaseModel.REF_ID)));

            }
        }

        cursor.close();

        return assigment;
    }


    public Assigment findAssigmentById(String id) {
        String criteria = AssigmentDatabaseModel.ID + " = ?";
        String[] parameter = { id };
        Cursor cursor = context.getContentResolver().query(dbUriAssigment,
                null, criteria, parameter, null);

        Assigment assigment = new Assigment();

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                assigment.setId(cursor.getString(cursor.getColumnIndex(AssigmentDatabaseModel.ID)));

                assigment.getCollector().setId(cursor.getString(cursor.getColumnIndex(
                        AssigmentDatabaseModel.COLLECTOR_ID)));
                assigment.setStatus(Assigment.AssigmentStatus.valueOf(
                        cursor.getString(cursor.getColumnIndex(AssigmentDatabaseModel.STATUS))));

                assigment.setRefId(cursor.getString(cursor.getColumnIndex(AssigmentDatabaseModel.REF_ID)));

            }
        }

        cursor.close();

        return assigment;
    }

    public List<Assigment> findAllAssigment(String collectorId) {
        String criteria = AssigmentDatabaseModel.STATUS_FLAG + " = ? AND " + AssigmentDatabaseModel.COLLECTOR_ID + " = ?";
        String[] parameter = { SignageVariables.ACTIVE, collectorId };
        Cursor cursor = context.getContentResolver().query(dbUriAssigment,
                null, criteria, parameter, null);

        List<Assigment> assigments = new ArrayList<Assigment>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Assigment assigment = new Assigment();

                assigment.setId(cursor.getString(cursor.getColumnIndex(AssigmentDatabaseModel.ID)));

                assigment.getCollector().setId(cursor.getString(cursor.getColumnIndex(
                        AssigmentDatabaseModel.COLLECTOR_ID)));
                assigment.setStatus(Assigment.AssigmentStatus.valueOf(cursor.getString(
                        cursor.getColumnIndex(AssigmentDatabaseModel.STATUS))));

                assigment.setRefId(cursor.getString(cursor.getColumnIndex(AssigmentDatabaseModel.REF_ID)));

                assigments.add(assigment);
            }
        }
        cursor.close();
        return assigments;
    }

    public List<Assigment> findAssigmentByUserId(String collectorId) {
        String criteria = AssigmentDatabaseModel.COLLECTOR_ID + " = ?";
        String[] parameter = { collectorId };
        Cursor cursor = context.getContentResolver().query(dbUriAssigment,
                null, criteria, parameter, null);

        List<Assigment> assigments = new ArrayList<Assigment>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Assigment assigment = new Assigment();

                assigment.setId(cursor.getString(cursor.getColumnIndex(AssigmentDatabaseModel.ID)));

                assigment.getCollector().setId(cursor.getString(cursor.getColumnIndex(
                        AssigmentDatabaseModel.COLLECTOR_ID)));
                assigment.setStatus(Assigment.AssigmentStatus.valueOf(cursor.getString(
                        cursor.getColumnIndex(AssigmentDatabaseModel.STATUS))));

                assigment.setRefId(cursor.getString(cursor.getColumnIndex(AssigmentDatabaseModel.REF_ID)));

                assigments.add(assigment);
            }
        }
        cursor.close();

        return assigments;
    }

    public void deleteAssigment(String menuId) {
        String criteria = AssigmentDatabaseModel.ID + " = ?";
        String[] parameter = { menuId };

        context.getContentResolver()
                .delete(dbUriAssigment, criteria, parameter);

    }

    public String getAssigmentRefIdById(String assigmentId) {
        String criteria = AssigmentDatabaseModel.ID + " = ?";
        String[] parameter = { assigmentId };
        Cursor cursor = context.getContentResolver().query(dbUriAssigment,
                null, criteria, parameter, null);

        String id = null;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                id = cursor.getString(cursor
                        .getColumnIndex(AssigmentDatabaseModel.REF_ID));
            }
        }

        cursor.close();

        return id;
    }

    public String getAssigmentId() {
        Cursor cursor = context.getContentResolver().query(dbUriAssigment,
                null, null, null,
                AssigmentDatabaseModel.CREATE_DATE + " DESC LIMIT 1");

        String id = null;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                id = cursor.getString(cursor
                        .getColumnIndex(AssigmentDatabaseModel.ID));
            }
        }

        cursor.close();

        return id;
    }

    public List<String> getAllRefId() {
        Cursor cursor = context.getContentResolver().query(dbUriAssigment, null, null, null, null);

        List<String> categories = new ArrayList<String>();

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    categories.add(cursor.getString(cursor
                            .getColumnIndex(AssigmentDatabaseModel.REF_ID)));
                }
            }
        }

        cursor.close();

        return categories;
    }

    public void updateSyncStatusById(String id, String refId) {
        Log.d(getClass().getSimpleName(), "updateSyncStatusById.Success1");
        ContentValues values = new ContentValues();

        values.put(AssigmentDatabaseModel.SYNC_STATUS, 1);
        values.put(AssigmentDatabaseModel.REF_ID, refId);

        context.getContentResolver().update(dbUriAssigment, values, AssigmentDatabaseModel.ID + " = ? ", new String[]{id});
    }

    public void deleteByRefIds(List<String> refIds) {
        for(String id : refIds) {
            ContentValues values = new ContentValues();
            values.put(AssigmentDatabaseModel.STATUS_FLAG, 0);

            context.getContentResolver().update(dbUriAssigment, values, AssigmentDatabaseModel.REF_ID + " = ? ", new String[] { id });
        }
    }

    public void deleteAssigmentById(String assigmentId) {
        String criteria = AssigmentDatabaseModel.ID + " = ?";
        String[] parameter = { assigmentId };

        ContentValues values = new ContentValues();
        values.put(AssigmentDatabaseModel.STATUS_FLAG, 0);

        context.getContentResolver().update(dbUriAssigment, values, criteria, parameter);
    }


}
