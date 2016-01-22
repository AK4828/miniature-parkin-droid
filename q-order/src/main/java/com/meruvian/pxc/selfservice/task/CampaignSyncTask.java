package com.meruvian.pxc.selfservice.task;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.meruvian.pxc.selfservice.R;
import com.meruvian.pxc.selfservice.SignageVariables;
import com.meruvian.pxc.selfservice.content.database.adapter.CampaignDatabaseAdapter;
import com.meruvian.pxc.selfservice.entity.Campaign;

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
public class CampaignSyncTask extends AsyncTask<String, Void, JSONObject> {
    private Context context;
    private TaskService service;
    private SharedPreferences preferences;

    private List<String> idCampaigns = new ArrayList<String>();
    private CampaignDatabaseAdapter campaignDbAdapter;

    public CampaignSyncTask(Context context, TaskService service) {
        this.context = context;
        this.service = service;

        campaignDbAdapter = new CampaignDatabaseAdapter(context);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);

        idCampaigns = campaignDbAdapter.getAllId();
    }

    @Override
    protected void onCancelled() {
        service.onCancel(SignageVariables.CAMPAIGN_GET_TASK, context.getString(R.string.cancel));
    }

    @Override
    protected void onPreExecute() {
        Log.d("SyncCampaign", "Begin synchronization");
        service.onExecute(SignageVariables.CAMPAIGN_GET_TASK);
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        Log.d("SyncCampaign", "Synchronize");
        return ConnectionUtil.get(preferences.getString("server_url", "") + "/api/campaigns?siteId=" + params[0] + "&q=" + preferences.getString("campaign_name", "none"));
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        Log.d("SyncCampaign", "Fetching data");

        try {
            if (result != null) {
                JSONArray jsonArray = result.getJSONArray("entityList");
                List<Campaign> campaigns = new ArrayList<Campaign>();

                for (int a = 0; a < jsonArray.length(); a++) {
                    JSONObject json = jsonArray.getJSONObject(a);

                    Campaign campaign = new Campaign();

                    campaign.setRefId(json.getString("id"));
                    campaign.setName(json.getString("name"));
                    campaign.setDescription(json.getString("description"));
                    campaign.setShowOnAndroid(json.getBoolean("showOnAndroid"));

                    campaigns.add(campaign);

                    if (idCampaigns.contains(campaign.getId())) {
                        idCampaigns.remove(campaign.getId());
                    }
                }

                campaignDbAdapter.saveCampaignFindByRefId(campaigns);
                campaignDbAdapter.delete(idCampaigns);

                if (campaigns.size() < 1) {
                    service.onError(SignageVariables.CAMPAIGN_GET_TASK, context.getString(R.string.error));
                } else {
                    service.onSuccess(SignageVariables.CAMPAIGN_GET_TASK, campaigns.get(0));
                }
            } else {
                service.onError(SignageVariables.CAMPAIGN_GET_TASK, context.getString(R.string.error));
            }
        } catch (JSONException e) {
            Log.e(e.getMessage(), e.getMessage(), e);
            service.onError(SignageVariables.CAMPAIGN_GET_TASK, context.getString(R.string.error));
        }

        Log.d("SyncCampaign", "Finish synchronization");
    }
}
