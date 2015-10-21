package org.meruvian.esales.collector.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.meruvian.esales.collector.R;
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

    private int a =1;

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
//            categorySyncTask = new CategorySyncTask(this, this);
//            categorySyncTask.execute();

        }
    }

    @Override
    public void onExecute(int code) {
        if (buttonSync.getVisibility() == View.VISIBLE) {
            buttonSync.setVisibility(View.GONE);
        }

       /* if (code == SignageVariables.PRODUCT_GET_TASK) {
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
        } else if(code == SignageVariables.PRODUCT_STORE_GET_TASK) {
            progressBar.setVisibility(View.VISIBLE);
            textSync.setVisibility(View.VISIBLE);
            textSync.setText(R.string.sync_product);
        }*/
    }

    @Override
    public void onSuccess(int code, Object result) {
        if (result != null) {
            /*if (code == SignageVariables.CATEGORY_ELEMENTS_TASK) {
                categorySyncTask = new CategorySyncTask(this, this);
                categorySyncTask.execute(result.toString());

            } else if (code == SignageVariables.CATEGORY_GET_TASK) {
                //Sync with Page
                *//*if ((int)result > 1 && (int) result > a) {
                    for(int i=1; i < (int) result; i++) {
                        categorySyncTask = new CategorySyncTask(SyncActivity.this, SyncActivity.this);
                        categorySyncTask.execute(String.valueOf(i));
                        a++;
                    }
                }*//*

                productUomsSyncTask = new ProductUomsSyncTask(this, this);
                productUomsSyncTask.execute();
            } else if (code == SignageVariables.PRODUCT_UOM_GET_TASK) {
                contactSyncTask = new ContactSyncTask(this, this);
                contactSyncTask.execute();
            } else if (code == SignageVariables.CONTACT_GET_TASK) {
//                productTotalElementsTask = new ProductTotalElementsTask(this, this);
//                productTotalElementsTask.execute();
                Log.e(getClass().getSimpleName(), "CONTACT_GET_TASK: Finish -> productStoreSyncTask");
                productStoreSyncTask = new ProductStoreSyncTask(this, this, 0);
                productStoreSyncTask.execute();

            } else if (code == SignageVariables.PRODUCT_STORE_GET_TASK) {
                int i = 0;
                if ((int)result > 1 && (int) result > a) {
                    for(i=1; i < (int) result; i++) {
                        Log.e(getClass().getSimpleName(), "PRODUCT_STORE_GET_TASK: Finish -> productStoreSyncTask");
                        productStoreSyncTask = new ProductStoreSyncTask(this, this, i);
                        productStoreSyncTask.execute();
                        a++;
                    }
                }
                
                if (i == ((int) result-1))  {
                    Log.e(getClass().getSimpleName(), "PRODUCT_STORE_GET_TASK: Finish " +
                            "-> imageProductTask i: " + i + " result: " + result + " : " + ((int) result-1));
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
                        editor.putBoolean("has_sync", true);
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
            }*/
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
