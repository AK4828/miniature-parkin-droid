package com.meruvian.pxc.selfservice.task;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.meruvian.pxc.selfservice.SignageVariables;
import com.meruvian.pxc.selfservice.content.database.adapter.ProductDatabaseAdapter;
import com.meruvian.pxc.selfservice.util.AuthenticationUtils;
import com.meruvian.pxc.selfservice.util.ImageUtil;

import org.meruvian.midas.core.service.TaskService;

import java.io.IOException;

/**
 * Created by akm on 11/12/15.
 */
public class ImageJsonTask extends AsyncTask<Void, Void, Void> {

    private Context context;
    private String id;
    private TaskService service;
    private SharedPreferences preferences;

    private ProductDatabaseAdapter productDbAdapter;

    public ImageJsonTask(Context c, String id) {
        this.context = c;
        this.id = id;
        preferences = context.getSharedPreferences(SignageVariables.PREFS_SERVER, 0);

    }

    @Override
    protected void onPreExecute() {
        service.onExecute(SignageVariables.IMAGE_PRODUCT_TASK);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        for (String id : productDbAdapter.getProductByImage()) {
            try {
                Log.d(getClass().getSimpleName(), "?access_token= " + AuthenticationUtils.getCurrentAuthentication().getAccessToken());
                ImageUtil.save(context, "/api/products", id, "/image?access_token=" + AuthenticationUtils.getCurrentAuthentication().getAccessToken(), preferences.getString("server_url_point", ""));
            } catch (IOException e) {
                Log.e(getClass().getName(), e.getMessage(), e);
            }
        }

        ImageUtil.clearImageCache();

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        service.onSuccess(SignageVariables.IMAGE_PRODUCT_TASK, true);
    }
}
