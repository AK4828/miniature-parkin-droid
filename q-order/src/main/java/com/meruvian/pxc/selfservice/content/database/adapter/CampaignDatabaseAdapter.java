package com.meruvian.pxc.selfservice.content.database.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;

import com.meruvian.pxc.selfservice.content.MidasContentProvider;
import com.meruvian.pxc.selfservice.content.database.model.CampaignDatabaseModel;
import com.meruvian.pxc.selfservice.entity.Campaign;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by meruvian on 24/03/15.
 */
public class CampaignDatabaseAdapter extends DefaultDatabaseAdapter {
    private Uri dbUriCampaign = Uri.parse(MidasContentProvider.CONTENT_PATH
            + MidasContentProvider.TABLES[2]);

    private Context context;

    public CampaignDatabaseAdapter(Context context) {
        this.context = context;
    }

    public void saveCampaign(List<Campaign> campaigns) {
        for (Campaign ca : campaigns) {
            ContentValues values = new ContentValues();

            if (ca.getId() == null){
                ca.setId(generateId());
            }

            if (findCampaignById(ca.getId()) == null) {
                values.put(CampaignDatabaseModel.ID, ca.getId());
                values.put(CampaignDatabaseModel.NAME, ca.getName());
                values.put(CampaignDatabaseModel.DESCRIPTION, ca.getDescription());
                values.put(CampaignDatabaseModel.SHOW_ON_ANDROID, ca.isShowOnAndroid() ? 1 : 0);
                values.put(CampaignDatabaseModel.REF_ID, ca.getRefId());
                values.put(CampaignDatabaseModel.STATUS_FLAG, 1);

                context.getContentResolver().insert(dbUriCampaign, values);
            } else {
                values.put(CampaignDatabaseModel.NAME, ca.getName());
                values.put(CampaignDatabaseModel.DESCRIPTION, ca.getDescription());
                values.put(CampaignDatabaseModel.SHOW_ON_ANDROID, ca.isShowOnAndroid() ? 1 : 0);
                values.put(CampaignDatabaseModel.REF_ID, ca.getRefId());
                values.put(CampaignDatabaseModel.STATUS_FLAG, 1);

                context.getContentResolver().update(dbUriCampaign, values, CampaignDatabaseModel.ID + " = ?", new String[] { ca.getId() });
            }
        }
    }

    public void saveCampaignFindByRefId(List<Campaign> campaigns) {
        for (Campaign ca : campaigns) {
            ContentValues values = new ContentValues();

            if (findCampaignByRefId(ca.getRefId()) == null) {
                String id =  generateId();
                ca.setId(id);
                values.put(CampaignDatabaseModel.ID, id);
                values.put(CampaignDatabaseModel.NAME, ca.getName());
                values.put(CampaignDatabaseModel.DESCRIPTION, ca.getDescription());
                values.put(CampaignDatabaseModel.SHOW_ON_ANDROID, ca.isShowOnAndroid() ? 1 : 0);
                values.put(CampaignDatabaseModel.REF_ID, ca.getRefId());
                values.put(CampaignDatabaseModel.STATUS_FLAG, 1);

                context.getContentResolver().insert(dbUriCampaign, values);
            } else {
                ca.setId(findIdByRefId(ca.getRefId()));
                values.put(CampaignDatabaseModel.NAME, ca.getName());
                values.put(CampaignDatabaseModel.DESCRIPTION, ca.getDescription());
                values.put(CampaignDatabaseModel.SHOW_ON_ANDROID, ca.isShowOnAndroid() ? 1 : 0);
                values.put(CampaignDatabaseModel.REF_ID, ca.getRefId());
                values.put(CampaignDatabaseModel.STATUS_FLAG, 1);

                context.getContentResolver().update(dbUriCampaign, values, CampaignDatabaseModel.ID + " = ?", new String[] { ca.getId() });
            }
        }
    }



    public Campaign findCampaignById(String id) {
        String query = CampaignDatabaseModel.ID + " = ?";
        String[] parameter = { id };

        Cursor cursor = context.getContentResolver().query(dbUriCampaign, null, query, parameter, null);

        Campaign campaign = null;

        if(cursor != null) {
            if(cursor.getCount() > 0) {
                try {
                    cursor.moveToFirst();

                    campaign = new Campaign();
                    campaign.setId(cursor.getString(cursor.getColumnIndex(CampaignDatabaseModel.ID)));
                    campaign.setName(cursor.getString(cursor.getColumnIndex(CampaignDatabaseModel.NAME)));
                    campaign.setDescription(cursor.getString(cursor.getColumnIndex(CampaignDatabaseModel.DESCRIPTION)));
                    campaign.setShowOnAndroid(cursor.getInt(cursor.getColumnIndex(CampaignDatabaseModel.SHOW_ON_ANDROID)) == 1 ? true : false );
                    campaign.setRefId(cursor.getString(cursor.getColumnIndex(CampaignDatabaseModel.REF_ID)));
                    
                } catch (SQLException e) {
                    e.printStackTrace();

                    campaign = null;
                }
            }
        }

        cursor.close();

        return campaign;
    }

    public Campaign findCampaignByRefId(String refId) {
        String query = CampaignDatabaseModel.REF_ID + " = ?";
        String[] parameter = { refId };

        Cursor cursor = context.getContentResolver().query(dbUriCampaign, null, query, parameter, null);

        Campaign campaign = null;

        if(cursor != null) {
            if(cursor.getCount() > 0) {
                try {
                    cursor.moveToFirst();

                    campaign = new Campaign();
                    campaign.setId(cursor.getString(cursor.getColumnIndex(CampaignDatabaseModel.ID)));
                    campaign.setName(cursor.getString(cursor.getColumnIndex(CampaignDatabaseModel.NAME)));
                    campaign.setDescription(cursor.getString(cursor.getColumnIndex(CampaignDatabaseModel.DESCRIPTION)));
                    campaign.setShowOnAndroid(cursor.getInt(cursor.getColumnIndex(CampaignDatabaseModel.SHOW_ON_ANDROID)) == 1 ? true : false );
                    campaign.setRefId(cursor.getString(cursor.getColumnIndex(CampaignDatabaseModel.REF_ID)));

                } catch (SQLException e) {
                    e.printStackTrace();

                    campaign = null;
                }
            }
        }

        cursor.close();

        return campaign;
    }

    public List<String> getAllActiveId() {
        String query = CampaignDatabaseModel.STATUS_FLAG + " = ?";
        String[] parameter = { "1" };
        Cursor cursor = context.getContentResolver().query(dbUriCampaign, null, query, parameter, null);
        List<String> campaigns = new ArrayList<String>();

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    campaigns.add(cursor.getString(cursor .getColumnIndex(CampaignDatabaseModel.ID)));
                }
            }
        }

        cursor.close();

        return campaigns;
    }

    public List<String> getAllActiveRefId() {
        String query = CampaignDatabaseModel.STATUS_FLAG + " = ?";
        String[] parameter = { "1" };
        Cursor cursor = context.getContentResolver().query(dbUriCampaign, null, query, parameter, null);
        List<String> campaigns = new ArrayList<String>();

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    campaigns.add(cursor.getString(cursor .getColumnIndex(CampaignDatabaseModel.REF_ID)));
                }
            }
        }

        cursor.close();

        return campaigns;
    }

    public List<Campaign> getAllActiveCampaign() {
        String query = CampaignDatabaseModel.STATUS_FLAG + " = ?";
        String[] parameter = { "1" };
        Cursor cursor = context.getContentResolver().query(dbUriCampaign, null, query, parameter, null);
        List<Campaign> campaignDetails = new ArrayList<Campaign>();

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Campaign campaign = new Campaign();
                    campaign.setId(cursor.getString(cursor.getColumnIndex(CampaignDatabaseModel.ID)));
                    campaign.setName(cursor.getString(cursor.getColumnIndex(CampaignDatabaseModel.NAME)));
                    campaign.setDescription(cursor.getString(cursor.getColumnIndex(CampaignDatabaseModel.DESCRIPTION)));
                    campaign.setShowOnAndroid(cursor.getInt(cursor.getColumnIndex(CampaignDatabaseModel.SHOW_ON_ANDROID)) == 1 ? true : false);
                    campaign.setRefId(cursor.getString(cursor.getColumnIndex(CampaignDatabaseModel.REF_ID)));

                    campaignDetails.add(campaign);
                }
            }
        }

        cursor.close();

        return campaignDetails;
    }

    public String findIdByRefId(String refId) {
        String query = CampaignDatabaseModel.REF_ID + " = ?";
        String[] parameter = { refId };
        Cursor cursor = context.getContentResolver().query(dbUriCampaign, null, query, parameter, null);

        String id = null;

        if (cursor != null) {
            if (cursor.getCount() >0) {
                cursor.moveToFirst();
                id = cursor.getString(cursor .getColumnIndex(CampaignDatabaseModel.ID));
            }
        }

        cursor.close();
        return id;
    }

    public List<String> getAllId() {
        Cursor cursor = context.getContentResolver().query(dbUriCampaign, null, null, null, null);
        List<String> campaigns = new ArrayList<String>();

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    campaigns.add(cursor.getString(cursor .getColumnIndex(CampaignDatabaseModel.ID)));
                }
            }
        }

        cursor.close();

        return campaigns;
    }

    public void delete(List<String> ids) {
        for(String id : ids) {
            ContentValues values = new ContentValues();
            values.put(CampaignDatabaseModel.STATUS_FLAG, 0);

            context.getContentResolver().update(dbUriCampaign, values, CampaignDatabaseModel.ID + " = ? ", new String[] { id });
        }
    }

}
