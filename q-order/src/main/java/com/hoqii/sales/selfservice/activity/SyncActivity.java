package com.hoqii.sales.selfservice.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hoqii.sales.selfservice.R;
import com.hoqii.sales.selfservice.SignageVariables;
import com.hoqii.sales.selfservice.task.CategorySyncTask;
import com.hoqii.sales.selfservice.task.ContactSyncTask;
import com.hoqii.sales.selfservice.task.ImageProductTask;
import com.hoqii.sales.selfservice.task.ProductSyncTask;
import com.hoqii.sales.selfservice.task.ProductUomsSyncTask;

import org.meruvian.midas.core.defaults.DefaultActivity;
import org.meruvian.midas.core.service.TaskService;
import org.meruvian.midas.core.util.ConnectionUtil;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by ludviantoovandi on 30/01/15.
 */
public class SyncActivity extends DefaultActivity implements TaskService {
    @InjectView(R.id.text_sync) TextView textSync;
    @InjectView(R.id.button_sync) Button buttonSync;
    @InjectView(R.id.progressbar) ProgressBar progressBar;

    private CategorySyncTask categorySyncTask;
    private ProductUomsSyncTask productUomsSyncTask;
    private ProductSyncTask productSyncTask;
    private ImageProductTask imageProductTask;
    private ContactSyncTask contactSyncTask;


    @Override
    protected int layout() {
        return R.layout.activity_sync;
    }

    @Override
    public void onViewCreated(Bundle bundle) {
        if (ConnectionUtil.isInternetAvailable(this)) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    categorySyncTask = new CategorySyncTask(SyncActivity.this, SyncActivity.this);
                    categorySyncTask.execute();
                }
            }, 2000);
        } else {
            Toast.makeText(this, getString(R.string.check_internet), Toast.LENGTH_LONG).show();

            buttonSync.setVisibility(View.VISIBLE);
            textSync.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.button_sync)
    public void onClick(Button button) {
        if (button.getId() == R.id.button_sync) {
            categorySyncTask = new CategorySyncTask(this, this);
            categorySyncTask.execute();
        }
    }

    @Override
    public void onExecute(int code) {
        if (buttonSync.getVisibility() == View.VISIBLE) {
            buttonSync.setVisibility(View.GONE);
        }

        if (code == SignageVariables.PRODUCT_GET_TASK) {
            progressBar.setVisibility(View.VISIBLE);
            textSync.setVisibility(View.VISIBLE);
            textSync.setText(R.string.sync_product);
        } else if (code == SignageVariables.CATEGORY_GET_TASK) {
            progressBar.setVisibility(View.VISIBLE);
            textSync.setVisibility(View.VISIBLE);
            textSync.setText(R.string.sync_category);
        } else if (code == SignageVariables.IMAGE_PRODUCT_TASK) {
            progressBar.setVisibility(View.VISIBLE);
            textSync.setVisibility(View.VISIBLE);
            textSync.setText(R.string.sync_image);
        } else if (code == SignageVariables.PRODUCT_UOM_GET_TASK) {
            progressBar.setVisibility(View.VISIBLE);
            textSync.setVisibility(View.VISIBLE);
            textSync.setText(R.string.sync_product_uom);
        } else if(code == SignageVariables.CONTACT_GET_TASK) {
            progressBar.setVisibility(View.VISIBLE);
            textSync.setVisibility(View.VISIBLE);
            textSync.setText(R.string.sync_contact);
        }
    }

    @Override
    public void onSuccess(int code, Object result) {
        if (result != null) {
            if (code == SignageVariables.CATEGORY_GET_TASK) {
                productUomsSyncTask = new ProductUomsSyncTask(this, this);
                productUomsSyncTask.execute();
            } else if (code == SignageVariables.PRODUCT_UOM_GET_TASK) {
                contactSyncTask = new ContactSyncTask(this, this);
                contactSyncTask.execute();
            } else if (code == SignageVariables.CONTACT_GET_TASK) {
                productSyncTask = new ProductSyncTask(this, this);
                productSyncTask.execute();
            } else if (code == SignageVariables.PRODUCT_GET_TASK) {
                imageProductTask = new ImageProductTask(this, this);
                imageProductTask.execute();
            } else if (code == SignageVariables.IMAGE_PRODUCT_TASK) {
                progressBar.setVisibility(View.GONE);
                textSync.setText(R.string.finish_sync);

                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(SyncActivity.this).edit();
                        editor.putBoolean("has_sync", true);
                        editor.commit();

                        if (getIntent().getBooleanExtra("just_sync", false)) {
                            finish();
                        } else {
                            startActivity(new Intent(SyncActivity.this, MainActivity.class));
                            finish();
                        }
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
