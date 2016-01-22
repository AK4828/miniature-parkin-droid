package com.meruvian.pxc.selfservice.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.meruvian.pxc.selfservice.R;
import com.meruvian.pxc.selfservice.SignageVariables;
import com.meruvian.pxc.selfservice.content.database.adapter.CampaignDatabaseAdapter;
import com.meruvian.pxc.selfservice.content.database.adapter.CampaignDetailDatabaseAdapter;
import com.meruvian.pxc.selfservice.entity.Campaign;
import com.meruvian.pxc.selfservice.entity.CampaignDetail;
import com.meruvian.pxc.selfservice.task.CampaignDetailSyncTask;
import com.meruvian.pxc.selfservice.task.CampaignSyncTask;
import com.meruvian.pxc.selfservice.task.ImageCampaignSyncTask;

import org.meruvian.midas.core.defaults.DefaultActivity;
import org.meruvian.midas.core.service.TaskService;
import org.meruvian.midas.core.util.ConnectionUtil;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by meruvian on 24/03/15.
 */
public class SyncCampaignActivity extends DefaultActivity implements TaskService {
    @InjectView(R.id.text_sync_campaign)
    TextView textSync;
    @InjectView(R.id.button_sync_campaign)
    Button buttonSync;
    @InjectView(R.id.progressbar_campaign)
    ProgressBar progressBar;

    private CampaignSyncTask campaignSyncTask;
    private CampaignDetailSyncTask campaignDetailSyncTask;
    private ImageCampaignSyncTask imageCampaignSyncTask;

    private CampaignDatabaseAdapter campaignDbAdapter;
    private CampaignDetailDatabaseAdapter campaignDetailDbAdapter;
    private List<String> listIdCampaigns;
    private SharedPreferences preferences;

    @Override
    protected int layout() {
        return R.layout.activity_campaign_sync;
    }

    @Override
    public void onViewCreated(Bundle bundle) {
        campaignDbAdapter = new CampaignDatabaseAdapter(this);
        campaignDetailDbAdapter = new CampaignDetailDatabaseAdapter(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (ConnectionUtil.isInternetAvailable(this)) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    campaignSyncTask = new CampaignSyncTask(SyncCampaignActivity.this, SyncCampaignActivity.this);
                    campaignSyncTask.execute(preferences.getString("site_name", "none"));
                }
            }, 2000);
        } else {
            Toast.makeText(this, getString(R.string.check_internet), Toast.LENGTH_LONG).show();

            buttonSync.setVisibility(View.VISIBLE);
            textSync.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.button_sync_campaign)
    public void onClick(Button button) {
        if (button.getId() == R.id.button_sync) {
            campaignSyncTask = new CampaignSyncTask(this, this);
            campaignSyncTask.execute(preferences.getString("site_name", "none"));
        }
    }

    @Override
    public void onExecute(int code) {
        if (buttonSync.getVisibility() == View.VISIBLE) {
            buttonSync.setVisibility(View.GONE);
        }

        if (code == SignageVariables.CAMPAIGN_GET_TASK) {
            progressBar.setVisibility(View.VISIBLE);
            textSync.setVisibility(View.VISIBLE);
            textSync.setText(R.string.sync_campaign);
        } else if (code == SignageVariables.CAMPAIGN_DETAIL_TASK) {
            progressBar.setVisibility(View.VISIBLE);
            textSync.setVisibility(View.VISIBLE);
            textSync.setText(R.string.sync_campaign_detail);
        } else if (code == SignageVariables.IMAGE_CAMPAIGN_PRODUCT_TASK) {
            progressBar.setVisibility(View.VISIBLE);
            textSync.setVisibility(View.VISIBLE);
            textSync.setText(R.string.sync_image_campaign);
        }
    }

    @Override
    public void onSuccess(int code, Object result) {
        Log.d("SyncCampaign", "Success, code: " + code);
        Log.d("SyncCampaign", "Result Class: " + result);

        if (result instanceof Campaign) {
            Campaign campaign = (Campaign) result;
            Log.d("SyncCampaign", "Campaign ID: " + campaign.getId());
        }

        if (result != null) {
            if (code == SignageVariables.CAMPAIGN_GET_TASK) {
                Campaign campaign = (Campaign) result;
                campaignDetailSyncTask = new CampaignDetailSyncTask(this, this, campaign);
                campaignDetailSyncTask.execute(campaign);
            } else if (code == SignageVariables.CAMPAIGN_DETAIL_TASK) {
                Campaign campaign = (Campaign) result;
                List<CampaignDetail> details = campaignDetailDbAdapter.findCampaignDetailByCampaignId(campaign.getId());
                Log.d("SyncCampaign", "Fetching details, found: " + details.size());

                imageCampaignSyncTask = new ImageCampaignSyncTask(this, this);
                imageCampaignSyncTask.execute(details.toArray(new CampaignDetail[0]));
            } else if (code == SignageVariables.IMAGE_CAMPAIGN_PRODUCT_TASK) {
                progressBar.setVisibility(View.GONE);
                textSync.setText(R.string.finish_sync);

                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(SyncCampaignActivity.this).edit();
                        editor.putBoolean("has_sync_campaign", true);
                        editor.commit();

                        finish();
                    }
                }, 2000);
            }
        }

    }

    @Override
    public void onCancel(int code, String message) {
        if (buttonSync.getVisibility() == View.GONE) {
            buttonSync.setVisibility(View.VISIBLE);
            textSync.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(int code, String message) {
        if (buttonSync.getVisibility() == View.GONE) {
            buttonSync.setVisibility(View.VISIBLE);
            textSync.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
