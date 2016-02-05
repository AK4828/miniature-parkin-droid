package com.meruvian.pxc.selfservice.activity;

import android.content.Intent;
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
import com.meruvian.pxc.selfservice.task.CategorySyncTask;
import com.meruvian.pxc.selfservice.task.CategoryTotalElementsTask;
import com.meruvian.pxc.selfservice.task.ContactSyncTask;
import com.meruvian.pxc.selfservice.task.ImageProductTask;
import com.meruvian.pxc.selfservice.task.ProductStoreSyncTask;
import com.meruvian.pxc.selfservice.task.ProductSyncTask;
import com.meruvian.pxc.selfservice.task.ProductTotalElementsTask;
import com.meruvian.pxc.selfservice.task.ProductUomsSyncTask;

import org.meruvian.midas.core.defaults.DefaultActivity;
import org.meruvian.midas.core.service.TaskService;
import org.meruvian.midas.core.util.ConnectionUtil;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ludviantoovandi on 30/01/15.
 */
public class SyncActivity extends DefaultActivity implements TaskService {
    @Bind(R.id.text_sync) TextView textSync;
    @Bind(R.id.button_sync) Button buttonSync;
    @Bind(R.id.progressbar) ProgressBar progressBar;

    private CategoryTotalElementsTask categoryTotalElementsTask;
    private ProductTotalElementsTask productTotalElementsTask;

    private CategorySyncTask categorySyncTask;
    private ProductUomsSyncTask productUomsSyncTask;
    private ProductSyncTask productSyncTask;
    private ImageProductTask imageProductTask;
    private ContactSyncTask contactSyncTask;
    private ProductStoreSyncTask productStoreSyncTask;
    private int a =1, i = 0;

    @Override
    protected int layout() {
        return R.layout.activity_sync;
    }

    @Override
    public void onViewCreated(Bundle bundle) {
        if (ConnectionUtil.isInternetAvailable(this)) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
//                    categorySyncTask = new CategorySyncTask(SyncActivity.this, SyncActivity.this);
//                    categorySyncTask.execute("0");
                    categoryTotalElementsTask = new CategoryTotalElementsTask(SyncActivity.this, SyncActivity.this);
                    categoryTotalElementsTask.execute();

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
            categoryTotalElementsTask = new CategoryTotalElementsTask(SyncActivity.this, SyncActivity.this);
            categoryTotalElementsTask.execute();

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
        }  else if (code == SignageVariables.CATEGORY_GET_TASK) {
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
        } else if(code == SignageVariables.PRODUCT_STORE_GET_TASK) {
            progressBar.setVisibility(View.VISIBLE);
            textSync.setVisibility(View.VISIBLE);
            textSync.setText(R.string.sync_product);
        }
    }

    @Override
    public void onSuccess(int code, Object result) {
        if (result != null) {
            if (code == SignageVariables.CATEGORY_ELEMENTS_TASK) {
                categorySyncTask = new CategorySyncTask(this, this);
                categorySyncTask.execute(result.toString());
                Log.e(getClass().getSimpleName(), "CATEGORY_GET_TASK: Finish -> categoorySyncTask");

            } else if (code == SignageVariables.CATEGORY_GET_TASK) {

                productUomsSyncTask = new ProductUomsSyncTask(this, this);
                productUomsSyncTask.execute();
            }
      if (code == SignageVariables.PRODUCT_UOM_GET_TASK) {
                contactSyncTask = new ContactSyncTask(this, this);
                contactSyncTask.execute();
                productTotalElementsTask = new ProductTotalElementsTask(this, this);
                productTotalElementsTask.execute();
            } else if (code == SignageVariables.CONTACT_GET_TASK) {
//                productTotalElementsTask = new ProductTotalElementsTask(this, this);
//                productTotalElementsTask.execute();
                Log.e(getClass().getSimpleName(), "CONTACT_GET_TASK: Finish -> productStoreSyncTask");
                productStoreSyncTask = new ProductStoreSyncTask(this, this, 0);
                productStoreSyncTask.execute();

            } else if (code == SignageVariables.PRODUCT_STORE_GET_TASK) {
                if ((int)result > 1 && (int) result > a) {
                    for(i=1; i < (int) result; i++) {
                        productStoreSyncTask = new ProductStoreSyncTask(this, this, i);
                        productStoreSyncTask.execute();
                        a++;
                    }
                } else {
                    imageProductTask = new ImageProductTask(this, this);
                    imageProductTask.execute();
                }
            } else if (code == SignageVariables.PRODUCT_ELEMENTS_TASK) {
                Log.e(getClass().getSimpleName(), "PRODUCT_ELEMENTS_TASK: Finish");
                productSyncTask = new ProductSyncTask(this, this);
                productSyncTask.execute(result.toString());

            } else if (code == SignageVariables.PRODUCT_GET_TASK) {
                Log.e(getClass().getSimpleName(), "PRODUCT_GET_TASK: Finish");
                imageProductTask = new ImageProductTask(this, this);
                imageProductTask.execute();
            } else if (code == SignageVariables.IMAGE_PRODUCT_TASK) {
                progressBar.setVisibility(View.GONE);
                textSync.setText(R.string.finish_sync);

                Log.e(getClass().getSimpleName(), "IMAGE_PRODUCT_TASK: Finish");

                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(SyncActivity.this).edit();
                        editor.putBoolean("has_sync_point", true);
                        editor.commit();

                        if (getIntent().getBooleanExtra("just_sync", false)) {
                            Log.e(getClass().getSimpleName(), "IMAGE_PRODUCT_TASK: Handler-just_sync");
                            finish();
                        } else {
                            Log.e(getClass().getSimpleName(), "IMAGE_PRODUCT_TASK: Handler");
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
