package com.meruvian.pxc.selfservice.content.database.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;

import com.meruvian.pxc.selfservice.content.MidasContentProvider;
import com.meruvian.pxc.selfservice.content.database.model.CampaignDatabaseModel;
import com.meruvian.pxc.selfservice.content.database.model.CampaignDetailDatabaseModel;
import com.meruvian.pxc.selfservice.entity.CampaignDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by meruvian on 24/03/15.
 */
public class CampaignDetailDatabaseAdapter extends DefaultDatabaseAdapter {
    private Uri dbUriCampaignDetail = Uri.parse(MidasContentProvider.CONTENT_PATH
            + MidasContentProvider.TABLES[3]);

    private Context context;

    public CampaignDetailDatabaseAdapter(Context context) {
        this.context = context;
    }

    public void saveCampaignDetail(CampaignDetail ca) {
        ContentValues values = new ContentValues();

        if (ca.getId() == null) {
            ca.setId(generateId());
        }

        if (findCampaignDetailById(ca.getId()) == null) {
            values.put(CampaignDetailDatabaseModel.ID, ca.getId());
            values.put(CampaignDetailDatabaseModel.CAMPAIGN_ID, ca.getCampaign().getId());
            values.put(CampaignDetailDatabaseModel.PATH, ca.getPath());
            values.put(CampaignDetailDatabaseModel.DESCRIPTION, ca.getDescription());
            values.put(CampaignDetailDatabaseModel.REF_ID, ca.getRefId());
            values.put(CampaignDetailDatabaseModel.STATUS_FLAG, 1);

            context.getContentResolver().insert(dbUriCampaignDetail, values);
        } else {
            values.put(CampaignDetailDatabaseModel.CAMPAIGN_ID, ca.getCampaign().getId());
            values.put(CampaignDetailDatabaseModel.PATH, ca.getPath());
            values.put(CampaignDetailDatabaseModel.DESCRIPTION, ca.getDescription());
            values.put(CampaignDetailDatabaseModel.REF_ID, ca.getRefId());
            values.put(CampaignDetailDatabaseModel.STATUS_FLAG, 1);

            context.getContentResolver().update(dbUriCampaignDetail, values, CampaignDetailDatabaseModel.ID + " = ?", new String[] { ca.getId() });
        }
    }

    public void saveCampaignDetail(List<CampaignDetail> campaignDetails) {
        for (CampaignDetail ca : campaignDetails) {
            ContentValues values = new ContentValues();

            if (ca.getId() == null) {

            }

            if (findCampaignDetailById(ca.getId()) == null) {
                values.put(CampaignDetailDatabaseModel.ID, ca.getId());
                values.put(CampaignDetailDatabaseModel.CAMPAIGN_ID, ca.getCampaign().getId());
                values.put(CampaignDetailDatabaseModel.PATH, ca.getPath());
                values.put(CampaignDetailDatabaseModel.DESCRIPTION, ca.getDescription());
                values.put(CampaignDetailDatabaseModel.REF_ID, ca.getRefId());
                values.put(CampaignDetailDatabaseModel.STATUS_FLAG, 1);

                context.getContentResolver().insert(dbUriCampaignDetail, values);
            } else {
                values.put(CampaignDetailDatabaseModel.CAMPAIGN_ID, ca.getCampaign().getId());
                values.put(CampaignDetailDatabaseModel.PATH, ca.getPath());
                values.put(CampaignDetailDatabaseModel.DESCRIPTION, ca.getDescription());
                values.put(CampaignDetailDatabaseModel.REF_ID, ca.getRefId());
                values.put(CampaignDetailDatabaseModel.STATUS_FLAG, 1);

                context.getContentResolver().update(dbUriCampaignDetail, values, CampaignDetailDatabaseModel.ID + " = ?", new String[] { ca.getId() });
            }
        }
    }

    public void saveCampaignDetailByFindRefId(List<CampaignDetail> campaignDetails) {
        for (CampaignDetail ca : campaignDetails) {
            ContentValues values = new ContentValues();

            if (findCampaignDetailByRefId(ca.getRefId()) == null) {
                String id = generateId();
                ca.setId(id);
                values.put(CampaignDetailDatabaseModel.ID, id);
                values.put(CampaignDetailDatabaseModel.CAMPAIGN_ID, ca.getCampaign().getId());
                values.put(CampaignDetailDatabaseModel.PATH, ca.getPath());
                values.put(CampaignDetailDatabaseModel.DESCRIPTION, ca.getDescription());
                values.put(CampaignDetailDatabaseModel.REF_ID, ca.getRefId());
                values.put(CampaignDetailDatabaseModel.STATUS_FLAG, 1);

                context.getContentResolver().insert(dbUriCampaignDetail, values);
            } else {

                ca.setId(findIdByRefId(ca.getRefId()));
                values.put(CampaignDetailDatabaseModel.CAMPAIGN_ID, ca.getCampaign().getId());
                values.put(CampaignDetailDatabaseModel.PATH, ca.getPath());
                values.put(CampaignDetailDatabaseModel.DESCRIPTION, ca.getDescription());
                values.put(CampaignDetailDatabaseModel.REF_ID, ca.getRefId());
                values.put(CampaignDetailDatabaseModel.STATUS_FLAG, 1);

                context.getContentResolver().update(dbUriCampaignDetail, values, CampaignDetailDatabaseModel.ID + " = ?", new String[] { ca.getId() });
            }
        }
    }

    public CampaignDetail findCampaignDetailByRefId(String refId) {
        String query = CampaignDetailDatabaseModel.REF_ID + " = ?";
        String[] parameter = { refId };

        Cursor cursor = context.getContentResolver().query(dbUriCampaignDetail, null, query, parameter, null);

        CampaignDetail campaignDetail = null;

        if(cursor != null) {
            if(cursor.getCount() > 0) {
                try {
                    cursor.moveToFirst();

                    campaignDetail = new CampaignDetail();
                    campaignDetail.setId(cursor.getString(cursor.getColumnIndex(CampaignDetailDatabaseModel.ID)));
                    campaignDetail.getCampaign().setId(cursor.getString(cursor.getColumnIndex(CampaignDetailDatabaseModel.CAMPAIGN_ID)));
                    campaignDetail.setPath(cursor.getString(cursor.getColumnIndex(CampaignDetailDatabaseModel.PATH)));
                    campaignDetail.setRefId(cursor.getString(cursor.getColumnIndex(CampaignDetailDatabaseModel.REF_ID)));
                    campaignDetail.setDescription(cursor.getString(cursor.getColumnIndex(CampaignDetailDatabaseModel.DESCRIPTION)));

                } catch (SQLException e) {
                    e.printStackTrace();

                    campaignDetail = null;
                }
            }
        }

        cursor.close();

        return campaignDetail;
    }

    public String findIdByRefId(String refId) {
        String query = CampaignDetailDatabaseModel.REF_ID + " = ?";
        String[] parameter = { refId };

        Cursor cursor = context.getContentResolver().query(dbUriCampaignDetail, null, query, parameter, null);
        String id = null;

        if(cursor != null) {
            if (cursor.getCount() > 0) {
                try {
                    cursor.moveToFirst();

                    id = cursor.getString(cursor.getColumnIndex(CampaignDetailDatabaseModel.ID));

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        cursor.close();
        return id;
    }



    public CampaignDetail findCampaignDetailById(String id) {
        String query = CampaignDetailDatabaseModel.ID + " = ?";
        String[] parameter = { id };

        Cursor cursor = context.getContentResolver().query(dbUriCampaignDetail, null, query, parameter, null);

        CampaignDetail campaignDetail = null;

        if(cursor != null) {
            if(cursor.getCount() > 0) {
                try {
                    cursor.moveToFirst();

                    campaignDetail = new CampaignDetail();
                    campaignDetail.setId(cursor.getString(cursor.getColumnIndex(CampaignDetailDatabaseModel.ID)));
                    campaignDetail.getCampaign().setId(cursor.getString(cursor.getColumnIndex(CampaignDetailDatabaseModel.CAMPAIGN_ID)));
                    campaignDetail.setPath(cursor.getString(cursor.getColumnIndex(CampaignDetailDatabaseModel.PATH)));
                    campaignDetail.setRefId(cursor.getString(cursor.getColumnIndex(CampaignDetailDatabaseModel.REF_ID)));
                    campaignDetail.setDescription(cursor.getString(cursor.getColumnIndex(CampaignDetailDatabaseModel.DESCRIPTION)));

                } catch (SQLException e) {
                    e.printStackTrace();

                    campaignDetail = null;
                }
            }
        }

        cursor.close();

        return campaignDetail;
    }

    public List<String> getAllActiveId() {
        String query = CampaignDetailDatabaseModel.STATUS_FLAG + " = ?";
        String[] parameter = { "1" };
        Cursor cursor = context.getContentResolver().query(dbUriCampaignDetail, null, query, parameter, null);
        List<String> campaignDetails = new ArrayList<String>();

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    campaignDetails.add(cursor.getString(cursor .getColumnIndex(CampaignDatabaseModel.ID)));
                }
            }
        }

        cursor.close();

        return campaignDetails;
    }

    public List<CampaignDetail> findCampaignDetailByCampaignId(String campaignId) {
        String query = CampaignDetailDatabaseModel.STATUS_FLAG + " = ? AND " + CampaignDetailDatabaseModel.CAMPAIGN_ID + " = ?";
        String[] parameter = { "1", campaignId };
        Cursor cursor = context.getContentResolver().query(dbUriCampaignDetail, null, query, parameter, null);
        List<CampaignDetail> campaignDetails = new ArrayList<CampaignDetail>();

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    CampaignDetail campaignDetail = new CampaignDetail();
                    campaignDetail.setId(cursor.getString(cursor.getColumnIndex(CampaignDetailDatabaseModel.ID)));
                    campaignDetail.getCampaign().setId(cursor.getString(cursor.getColumnIndex(CampaignDetailDatabaseModel.CAMPAIGN_ID)));
                    campaignDetail.setPath(cursor.getString(cursor.getColumnIndex(CampaignDetailDatabaseModel.PATH)));
                    campaignDetail.setRefId(cursor.getString(cursor.getColumnIndex(CampaignDetailDatabaseModel.REF_ID)));
                    campaignDetail.setDescription(cursor.getString(cursor.getColumnIndex(CampaignDetailDatabaseModel.DESCRIPTION)));

                    campaignDetails.add(campaignDetail);
                }
            }
        }

        cursor.close();

        return campaignDetails;
    }

    public List<CampaignDetail> findAllActiveCampaignDetail() {
        String query = CampaignDetailDatabaseModel.STATUS_FLAG + " = ?";
        String[] parameter = { "1" };
        Cursor cursor = context.getContentResolver().query(dbUriCampaignDetail, null, query, parameter, null);
        List<CampaignDetail> campaignDetails = new ArrayList<CampaignDetail>();

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    CampaignDetail campaignDetail = new CampaignDetail();
                    campaignDetail.setId(cursor.getString(cursor.getColumnIndex(CampaignDetailDatabaseModel.ID)));
                    campaignDetail.getCampaign().setId(cursor.getString(cursor.getColumnIndex(CampaignDetailDatabaseModel.CAMPAIGN_ID)));
                    campaignDetail.setPath(cursor.getString(cursor.getColumnIndex(CampaignDetailDatabaseModel.PATH)));
                    campaignDetail.setRefId(cursor.getString(cursor.getColumnIndex(CampaignDetailDatabaseModel.REF_ID)));
                    campaignDetail.setDescription(cursor.getString(cursor.getColumnIndex(CampaignDetailDatabaseModel.DESCRIPTION)));

                    campaignDetails.add(campaignDetail);
                }
            }
        }

        cursor.close();

        return campaignDetails;
    }

    public List<String> getAllActiveRefId() {
        String query = CampaignDetailDatabaseModel.STATUS_FLAG + " = ?";
        String[] parameter = { "1" };
        Cursor cursor = context.getContentResolver().query(dbUriCampaignDetail, null, query, parameter, null);
        List<String> campaignDetails = new ArrayList<String>();

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    campaignDetails.add(cursor.getString(cursor .getColumnIndex(CampaignDatabaseModel.REF_ID)));
                }
            }
        }

        cursor.close();

        return campaignDetails;
    }

    public List<String> getAllId() {
        Cursor cursor = context.getContentResolver().query(dbUriCampaignDetail, null, null, null, null);
        List<String> campaignDetails = new ArrayList<String>();

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    campaignDetails.add(cursor.getString(cursor .getColumnIndex(CampaignDetailDatabaseModel.ID)));
                }
            }
        }

        cursor.close();

        return campaignDetails;
    }

    public void deleteAll(){
        ContentValues values = new ContentValues();
        values.put(CampaignDetailDatabaseModel.STATUS_FLAG, 0);

        context.getContentResolver().update(dbUriCampaignDetail, values, null, null);
    }

    public void delete(List<CampaignDetail> ids) {
        for(CampaignDetail d : ids) {
            ContentValues values = new ContentValues();
            values.put(CampaignDetailDatabaseModel.STATUS_FLAG, 0);

            context.getContentResolver().update(dbUriCampaignDetail, values, CampaignDetailDatabaseModel.ID + " = ? ", new String[] { d.getId() });
        }
    }
}
