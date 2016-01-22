package com.meruvian.pxc.selfservice.content.database.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.util.Log;

import com.meruvian.pxc.selfservice.SignageVariables;
import com.meruvian.pxc.selfservice.content.MidasContentProvider;
import com.meruvian.pxc.selfservice.content.database.model.AssigmentDetailDatabaseModel;
import com.meruvian.pxc.selfservice.content.database.model.DefaultPersistenceModel;
import com.meruvian.pxc.selfservice.entity.AssigmentDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by meruvian on 04/08/15.
 */
public class AssigmentDetailDatabaseAdapter extends DefaultDatabaseAdapter {
    private Uri dbUriAssigmentDetail = Uri.parse(MidasContentProvider.CONTENT_PATH
            + MidasContentProvider.TABLES[14]);

    private Context context;

    public AssigmentDetailDatabaseAdapter(Context context) {
        this.context = context;
    }

    public void saveAssigmentDetails(List<AssigmentDetail> assigmentDetails) {
        for (AssigmentDetail assigmentDetail : assigmentDetails) {
            ContentValues contentValues = new ContentValues();

            if (findAssigmentDetailByRefId(assigmentDetail.getRefId()) == null) {
                UUID uuid = UUID.randomUUID();
                String id = String.valueOf(uuid);

                Log.d(getClass().getSimpleName(), "Save AssigmentDetail: " + id);
                contentValues.put(AssigmentDetailDatabaseModel.ID, id);
                contentValues.put(AssigmentDetailDatabaseModel.CREATE_BY, assigmentDetail
                        .getLogInformation().getCreateBy());
                contentValues.put(AssigmentDetailDatabaseModel.CREATE_DATE, assigmentDetail
                        .getLogInformation().getCreateDate().getTime());
                contentValues.put(AssigmentDetailDatabaseModel.UPDATE_BY, assigmentDetail
                        .getLogInformation().getLastUpdateBy());
                contentValues.put(AssigmentDetailDatabaseModel.UPDATE_DATE, assigmentDetail
                        .getLogInformation().getLastUpdateDate().getTime());

                contentValues.put(AssigmentDetailDatabaseModel.ASSIGMENT_ID, assigmentDetail.getAssigment().getId());
                contentValues.put(AssigmentDetailDatabaseModel.AGENT_ID, assigmentDetail.getAgent().getId());
                contentValues.put(AssigmentDetailDatabaseModel.REF_ID, assigmentDetail.getRefId());

                contentValues.put(DefaultPersistenceModel.SYNC_STATUS, 0);
                contentValues.put(DefaultPersistenceModel.STATUS_FLAG, 1);

                context.getContentResolver().insert(dbUriAssigmentDetail, contentValues);
            } else {
                Log.d(getClass().getSimpleName(), "Update AssigmentDetail: " + assigmentDetail.getRefId());

                contentValues.put(AssigmentDetailDatabaseModel.UPDATE_BY, assigmentDetail
                        .getLogInformation().getLastUpdateBy());
                contentValues.put(AssigmentDetailDatabaseModel.UPDATE_DATE, assigmentDetail
                        .getLogInformation().getLastUpdateDate().getTime());

                contentValues.put(AssigmentDetailDatabaseModel.ASSIGMENT_ID, assigmentDetail.getAssigment().getId());
                contentValues.put(AssigmentDetailDatabaseModel.AGENT_ID, assigmentDetail.getAgent().getId());
                contentValues.put(AssigmentDetailDatabaseModel.REF_ID, assigmentDetail.getRefId());

                contentValues.put(DefaultPersistenceModel.SYNC_STATUS, 0);

                context.getContentResolver().update(dbUriAssigmentDetail, contentValues,
                        AssigmentDetailDatabaseModel.REF_ID + " = ?", new String[] { assigmentDetail.getRefId() });
            }
        }
    }

    public void saveAssigmentDetailByRefId(AssigmentDetail assigmentDetail) {
        ContentValues contentValues = new ContentValues();

        if (findAssigmentDetailByRefId(assigmentDetail.getRefId()) == null) {
            UUID uuid = UUID.randomUUID();
            String id = String.valueOf(uuid);

            Log.d(getClass().getSimpleName(), "Save AssigmentDetail: " + id);
            contentValues.put(AssigmentDetailDatabaseModel.ID, id);
            contentValues.put(AssigmentDetailDatabaseModel.CREATE_BY, assigmentDetail
                    .getLogInformation().getCreateBy());
            contentValues.put(AssigmentDetailDatabaseModel.CREATE_DATE, assigmentDetail
                    .getLogInformation().getCreateDate().getTime());
            contentValues.put(AssigmentDetailDatabaseModel.UPDATE_BY, assigmentDetail
                    .getLogInformation().getLastUpdateBy());
            contentValues.put(AssigmentDetailDatabaseModel.UPDATE_DATE, assigmentDetail
                    .getLogInformation().getLastUpdateDate().getTime());

            contentValues.put(AssigmentDetailDatabaseModel.ASSIGMENT_ID, assigmentDetail.getAssigment().getId());
            contentValues.put(AssigmentDetailDatabaseModel.AGENT_ID, assigmentDetail.getAgent().getId());
            contentValues.put(AssigmentDetailDatabaseModel.REF_ID, assigmentDetail.getRefId());

            contentValues.put(DefaultPersistenceModel.SYNC_STATUS, 0);
            contentValues.put(DefaultPersistenceModel.STATUS_FLAG, 1);

            context.getContentResolver().insert(dbUriAssigmentDetail, contentValues);
        } else {
            Log.d(getClass().getSimpleName(), "Update AssigmentDetail: " + assigmentDetail.getRefId());

            contentValues.put(AssigmentDetailDatabaseModel.UPDATE_BY, assigmentDetail
                    .getLogInformation().getLastUpdateBy());
            contentValues.put(AssigmentDetailDatabaseModel.UPDATE_DATE, assigmentDetail
                    .getLogInformation().getLastUpdateDate().getTime());

            contentValues.put(AssigmentDetailDatabaseModel.ASSIGMENT_ID, assigmentDetail.getAssigment().getId());
            contentValues.put(AssigmentDetailDatabaseModel.AGENT_ID, assigmentDetail.getAgent().getId());
            contentValues.put(AssigmentDetailDatabaseModel.REF_ID, assigmentDetail.getRefId());

            contentValues.put(DefaultPersistenceModel.SYNC_STATUS, 0);

            context.getContentResolver().update(dbUriAssigmentDetail, contentValues,
                    AssigmentDetailDatabaseModel.REF_ID + " = ?", new String[] { assigmentDetail.getRefId() });
        }
    }

    public String saveAssigmentDetail(AssigmentDetail assigmentDetail) {
        ContentValues contentValues = new ContentValues();
        UUID uuid = UUID.randomUUID();
        String id = String.valueOf(uuid);

        contentValues.put(AssigmentDetailDatabaseModel.ID, id);
        contentValues.put(AssigmentDetailDatabaseModel.CREATE_BY, assigmentDetail
                .getLogInformation().getCreateBy());
        contentValues.put(AssigmentDetailDatabaseModel.CREATE_DATE, assigmentDetail
                .getLogInformation().getCreateDate().getTime());
        contentValues.put(AssigmentDetailDatabaseModel.UPDATE_BY, assigmentDetail
                .getLogInformation().getLastUpdateBy());
        contentValues.put(AssigmentDetailDatabaseModel.UPDATE_DATE, assigmentDetail
                .getLogInformation().getLastUpdateDate().getTime());
        contentValues.put(AssigmentDetailDatabaseModel.ASSIGMENT_ID, assigmentDetail.getAssigment().getId());
        contentValues.put(AssigmentDetailDatabaseModel.AGENT_ID, assigmentDetail.getAgent().getId());

        contentValues.put(DefaultPersistenceModel.REF_ID, assigmentDetail.getRefId());
        contentValues.put(DefaultPersistenceModel.SYNC_STATUS, 0);
        contentValues.put(DefaultPersistenceModel.STATUS_FLAG, 1);

        context.getContentResolver().insert(dbUriAssigmentDetail, contentValues);
        return id;
    }

    public String updateAssigmentDetail(AssigmentDetail assigmentDetail) {
        ContentValues contentValues = new ContentValues();

        if (assigmentDetail.getId() != null) {
            Log.d(getClass().getSimpleName(), "Update AssigmentDetail: " + assigmentDetail.getId());

            contentValues.put(AssigmentDetailDatabaseModel.UPDATE_BY, assigmentDetail
                    .getLogInformation().getLastUpdateBy());
            contentValues.put(AssigmentDetailDatabaseModel.UPDATE_DATE, assigmentDetail
                    .getLogInformation().getLastUpdateDate().getTime());
            contentValues.put(AssigmentDetailDatabaseModel.ASSIGMENT_ID, assigmentDetail.getAssigment().getId());
            contentValues.put(AssigmentDetailDatabaseModel.AGENT_ID, assigmentDetail.getAgent().getId());
            contentValues.put(DefaultPersistenceModel.SYNC_STATUS, 0);

            context.getContentResolver().update(dbUriAssigmentDetail, contentValues,
                    AssigmentDetailDatabaseModel.ID + " = ?", new String[] { assigmentDetail.getId() });
            return assigmentDetail.getId();
        } else {
            UUID uuid = UUID.randomUUID();
            String id = String.valueOf(uuid);

            Log.d(getClass().getSimpleName(), "Save AssigmentDetail: " + id);
            contentValues.put(AssigmentDetailDatabaseModel.ID, id);
            contentValues.put(AssigmentDetailDatabaseModel.CREATE_BY, assigmentDetail
                    .getLogInformation().getCreateBy());
            contentValues.put(AssigmentDetailDatabaseModel.CREATE_DATE, assigmentDetail
                    .getLogInformation().getCreateDate().getTime());
            contentValues.put(AssigmentDetailDatabaseModel.UPDATE_BY, assigmentDetail
                    .getLogInformation().getLastUpdateBy());
            contentValues.put(AssigmentDetailDatabaseModel.UPDATE_DATE, assigmentDetail
                    .getLogInformation().getLastUpdateDate().getTime());
            contentValues.put(AssigmentDetailDatabaseModel.ASSIGMENT_ID, assigmentDetail.getAssigment().getId());
            contentValues.put(AssigmentDetailDatabaseModel.AGENT_ID, assigmentDetail.getAgent().getId());

            contentValues.put(DefaultPersistenceModel.SYNC_STATUS, 0);
            contentValues.put(DefaultPersistenceModel.STATUS_FLAG, 1);

            context.getContentResolver().insert(dbUriAssigmentDetail, contentValues);
            return id;
        }
    }

    public List<String> findAssigmentDetailIdesByUserId(String agentId) {
        String query = AssigmentDetailDatabaseModel.AGENT_ID + " = ?";
        String[] parameter = { agentId };

        Cursor cursor = context.getContentResolver().query(dbUriAssigmentDetail, null, query, parameter, null);

        List<String> assigmentDetailIdes = new ArrayList<String>();

        if(cursor != null) {
            if(cursor.getCount() > 0) {
                try {
                    while (cursor.moveToNext()) {
                        assigmentDetailIdes.add(cursor.getString(cursor.getColumnIndex(AssigmentDetailDatabaseModel.ID)));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        cursor.close();

        return assigmentDetailIdes;
    }

    public AssigmentDetail findAssigmentDetailByRefId(String refId) {
        String criteria = AssigmentDetailDatabaseModel.REF_ID + " = ?";
        String[] parameter = { refId };
        Cursor cursor = context.getContentResolver().query(dbUriAssigmentDetail,
                null, criteria, parameter, null);

        AssigmentDetail assigmentDetail  = null;

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                assigmentDetail = new AssigmentDetail();
                assigmentDetail.setId(cursor.getString(cursor.getColumnIndex(AssigmentDetailDatabaseModel.ID)));
                assigmentDetail.getAgent().setId(cursor.getString(cursor.getColumnIndex(AssigmentDetailDatabaseModel.AGENT_ID)));
                assigmentDetail.getAssigment().setId(cursor.getString(cursor.getColumnIndex(AssigmentDetailDatabaseModel.ASSIGMENT_ID)));
                assigmentDetail.setRefId(cursor.getString(cursor.getColumnIndex(AssigmentDetailDatabaseModel.REF_ID)));

            }
        }

        cursor.close();

        return assigmentDetail;
    }


    public AssigmentDetail findAssigmentDetailById(String id) {
        String criteria = AssigmentDetailDatabaseModel.ID + " = ?";
        String[] parameter = { id };
        Cursor cursor = context.getContentResolver().query(dbUriAssigmentDetail,
                null, criteria, parameter, null);

        AssigmentDetail assigmentDetail = new AssigmentDetail();

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                assigmentDetail.setId(cursor.getString(cursor.getColumnIndex(AssigmentDetailDatabaseModel.ID)));
                assigmentDetail.getAgent().setId(cursor.getString(cursor.getColumnIndex(AssigmentDetailDatabaseModel.AGENT_ID)));
                assigmentDetail.getAssigment().setId(cursor.getString(cursor.getColumnIndex(AssigmentDetailDatabaseModel.ASSIGMENT_ID)));
                assigmentDetail.setRefId(cursor.getString(cursor.getColumnIndex(AssigmentDetailDatabaseModel.REF_ID)));

            }
        }

        cursor.close();

        return assigmentDetail;
    }

    public List<AssigmentDetail> findAllAssigmentDetail(String assigmentId) {
        String criteria = AssigmentDetailDatabaseModel.STATUS_FLAG + " = ? AND " + AssigmentDetailDatabaseModel.ASSIGMENT_ID + " = ?";
        String[] parameter = { SignageVariables.ACTIVE, assigmentId };
        Cursor cursor = context.getContentResolver().query(dbUriAssigmentDetail,
                null, criteria, parameter, null);

        List<AssigmentDetail> assigmentDetails = new ArrayList<AssigmentDetail>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                AssigmentDetail assigmentDetail = new AssigmentDetail();

                assigmentDetail.setId(cursor.getString(cursor.getColumnIndex(AssigmentDetailDatabaseModel.ID)));
                assigmentDetail.getAgent().setId(cursor.getString(cursor.getColumnIndex(AssigmentDetailDatabaseModel.AGENT_ID)));
                assigmentDetail.getAssigment().setId(cursor.getString(cursor.getColumnIndex(AssigmentDetailDatabaseModel.ASSIGMENT_ID)));
                assigmentDetail.setRefId(cursor.getString(cursor.getColumnIndex(AssigmentDetailDatabaseModel.REF_ID)));

                assigmentDetails.add(assigmentDetail);
            }
        }
        cursor.close();
        return assigmentDetails;
    }

    public List<AssigmentDetail> findAssigmentDetailByUserId(String assigmentId) {
        String criteria = AssigmentDetailDatabaseModel.ASSIGMENT_ID + " = ?";
        String[] parameter = { assigmentId };
        Cursor cursor = context.getContentResolver().query(dbUriAssigmentDetail,
                null, criteria, parameter, null);

        List<AssigmentDetail> assigmentDetails = new ArrayList<AssigmentDetail>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                AssigmentDetail assigmentDetail = new AssigmentDetail();

                assigmentDetail.setId(cursor.getString(cursor.getColumnIndex(AssigmentDetailDatabaseModel.ID)));
                assigmentDetail.getAgent().setId(cursor.getString(cursor.getColumnIndex(AssigmentDetailDatabaseModel.AGENT_ID)));
                assigmentDetail.getAssigment().setId(cursor.getString(cursor.getColumnIndex(AssigmentDetailDatabaseModel.ASSIGMENT_ID)));
                assigmentDetail.setRefId(cursor.getString(cursor.getColumnIndex(AssigmentDetailDatabaseModel.REF_ID)));

                assigmentDetails.add(assigmentDetail);
            }
        }
        cursor.close();

        return assigmentDetails;
    }

    public void deleteAssigmentDetail(String menuId) {
        String criteria = AssigmentDetailDatabaseModel.ID + " = ?";
        String[] parameter = { menuId };

        context.getContentResolver()
                .delete(dbUriAssigmentDetail, criteria, parameter);

    }

    public String getAssigmentDetailRefIdById(String assigmentDetailId) {
        String criteria = AssigmentDetailDatabaseModel.ID + " = ?";
        String[] parameter = { assigmentDetailId };
        Cursor cursor = context.getContentResolver().query(dbUriAssigmentDetail,
                null, criteria, parameter, null);

        String id = null;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                id = cursor.getString(cursor
                        .getColumnIndex(AssigmentDetailDatabaseModel.REF_ID));
            }
        }

        cursor.close();

        return id;
    }

    public String getAssigmentDetailId() {
        Cursor cursor = context.getContentResolver().query(dbUriAssigmentDetail,
                null, null, null,
                AssigmentDetailDatabaseModel.CREATE_DATE + " DESC LIMIT 1");

        String id = null;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                id = cursor.getString(cursor
                        .getColumnIndex(AssigmentDetailDatabaseModel.ID));
            }
        }

        cursor.close();

        return id;
    }

    public List<String> getAllRefId() {
        Cursor cursor = context.getContentResolver().query(dbUriAssigmentDetail, null, null, null, null);

        List<String> categories = new ArrayList<String>();

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    categories.add(cursor.getString(cursor
                            .getColumnIndex(AssigmentDetailDatabaseModel.REF_ID)));
                }
            }
        }

        cursor.close();

        return categories;
    }

    public void updateSyncStatusById(String id, String refId) {
        Log.d(getClass().getSimpleName(), "updateSyncStatusById.Success1");
        ContentValues values = new ContentValues();

        values.put(AssigmentDetailDatabaseModel.SYNC_STATUS, 1);
        values.put(AssigmentDetailDatabaseModel.REF_ID, refId);

        context.getContentResolver().update(dbUriAssigmentDetail, values, AssigmentDetailDatabaseModel.ID + " = ? ", new String[]{id});
    }

    public void deleteByRefIds(List<String> refIds) {
        for(String id : refIds) {
            ContentValues values = new ContentValues();
            values.put(AssigmentDetailDatabaseModel.STATUS_FLAG, 0);

            context.getContentResolver().update(dbUriAssigmentDetail, values, AssigmentDetailDatabaseModel.REF_ID + " = ? ", new String[] { id });
        }
    }

    public void deleteAssigmentDetailById(String assigmentDetailId) {
        String criteria = AssigmentDetailDatabaseModel.ID + " = ?";
        String[] parameter = { assigmentDetailId };

        ContentValues values = new ContentValues();
        values.put(AssigmentDetailDatabaseModel.STATUS_FLAG, 0);

        context.getContentResolver().update(dbUriAssigmentDetail, values, criteria, parameter);
    }


}
