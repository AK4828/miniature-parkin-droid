package com.meruvian.pxc.selfservice.task;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.meruvian.pxc.selfservice.SignageVariables;
import com.meruvian.pxc.selfservice.content.database.adapter.CampaignDetailDatabaseAdapter;
import com.meruvian.pxc.selfservice.entity.CampaignDetail;
import com.meruvian.pxc.selfservice.util.ImageUtil;

import org.meruvian.midas.core.service.TaskService;

import java.io.File;
import java.io.IOException;

/**
 * Created by meruvian on 24/03/15.
 */
public class ImageCampaignSyncTask extends AsyncTask<CampaignDetail, Void, Void> {
    private Context context;
    private TaskService service;
    private int size;
    private SharedPreferences preferences;

    private CampaignDetailDatabaseAdapter campaignDetailDbAdapter;

    public ImageCampaignSyncTask(Context context, TaskService service) {
        this.context = context;
        this.service = service;

        preferences = context.getSharedPreferences(SignageVariables.PREFS_SERVER, 0);
        campaignDetailDbAdapter = new CampaignDetailDatabaseAdapter(context);
    }

    @Override
    protected void onPreExecute() {
        service.onExecute(SignageVariables.IMAGE_CAMPAIGN_PRODUCT_TASK);
    }

    @Override
    protected Void doInBackground(CampaignDetail... params) {
        if (params == null) {
            return null;
        }

        campaignDetailDbAdapter.deleteAll();

        int i = 1;
        for (CampaignDetail details : params) {
            try {
                File file = ImageUtil.save(context, "/api/campaigns/galleries", details.getRefId(), "/img?size=1", preferences.getString("server_url_point", ""));
                details.setPath(file.getPath());

                campaignDetailDbAdapter.saveCampaignDetail(details);
            } catch (IOException e) {
                Log.e(e.getMessage(), e.getMessage(), e);
                return null;
            }
        }

        ImageUtil.clearImageCache();

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        service.onSuccess(SignageVariables.IMAGE_CAMPAIGN_PRODUCT_TASK, true);
    }

    public File fileDestination() {
        String fileName = String.valueOf(System.currentTimeMillis());
        File dirtFile = new File(Environment.getExternalStorageDirectory()
                + File.separator + SignageVariables.PUBLIC_FOLDER);

        if (!dirtFile.exists()) {
            if (!dirtFile.mkdirs()) {
                Log.w("Folder", "failed to create directory");
            }
        }

        File mediaFile = new File(dirtFile.getPath() + File.separator + fileName);

        return mediaFile;
    }

}
