package com.hoqii.sales.selfservice.task;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.hoqii.sales.selfservice.R;
import com.hoqii.sales.selfservice.SignageVariables;
import com.hoqii.sales.selfservice.content.database.adapter.CampaignDatabaseAdapter;
import com.hoqii.sales.selfservice.content.database.adapter.CampaignDetailDatabaseAdapter;
import com.hoqii.sales.selfservice.entity.Campaign;
import com.hoqii.sales.selfservice.entity.CampaignDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.meruvian.midas.core.service.TaskService;
import org.meruvian.midas.core.util.ConnectionUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by meruvian on 24/03/15.
 */
public class CampaignDetailSyncTask extends AsyncTask<Campaign, Void, JSONObject> {
    private Context context;
    private TaskService service;
    private Campaign campaign;

    private CampaignDetailDatabaseAdapter campaignDetailDbAdapter;
    private CampaignDatabaseAdapter campaignDbAdapter;
    private SharedPreferences preferences;

    public CampaignDetailSyncTask(Context context, TaskService service, Campaign campaign) {
        this.context = context;
        this.service = service;
        this.campaign = campaign;

        campaignDetailDbAdapter = new CampaignDetailDatabaseAdapter(context);
        campaignDbAdapter = new CampaignDatabaseAdapter(context);
        preferences = context.getSharedPreferences(SignageVariables.PREFS_SERVER, 0);
    }

    @Override
    protected void onCancelled() {
        service.onCancel(SignageVariables.CAMPAIGN_DETAIL_TASK, context.getString(R.string.cancel));
    }

    @Override
    protected void onPreExecute() {
        service.onExecute(SignageVariables.CAMPAIGN_DETAIL_TASK);
    }

    @Override
    protected JSONObject doInBackground(Campaign... params) {
        return ConnectionUtil.get(preferences.getString("server_url", "") + "/api/campaigns/" + params[0].getRefId() + "/contents");
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        try {
            if (result != null) {
                List<CampaignDetail> campaignDetails = campaignDetailDbAdapter.findCampaignDetailByCampaignId(campaign.getId());
                campaignDetailDbAdapter.delete(campaignDetails);

                JSONArray jsonArray = result.getJSONArray("entityList");
                List<CampaignDetail> campaigns = new ArrayList<CampaignDetail>();

                for (int a = 0; a < jsonArray.length(); a++) {
                    JSONObject json = jsonArray.getJSONObject(a);

                    CampaignDetail campaignDetail = new CampaignDetail();

                    campaignDetail.setRefId(json.getString("id"));
                    campaignDetail.getCampaign().setId(campaign.getId());
                    campaignDetail.setDescription(json.getString("description"));

                    campaigns.add(campaignDetail);
                }

                campaignDetailDbAdapter.saveCampaignDetailByFindRefId(campaigns);

                service.onSuccess(SignageVariables.CAMPAIGN_DETAIL_TASK, campaign);
            } else {
                service.onError(SignageVariables.CAMPAIGN_DETAIL_TASK, context.getString(R.string.error));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            service.onError(SignageVariables.CAMPAIGN_DETAIL_TASK, context.getString(R.string.error));
        }

    }
}
