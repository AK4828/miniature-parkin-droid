package org.meruvian.esales.collector.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

import org.meruvian.esales.collector.R;
import org.meruvian.midas.core.defaults.DefaultActivity;

/**
 * Created by ludviantoovandi on 15/10/14.
 */
public class SplashActivity extends DefaultActivity {
    private SharedPreferences preferences;

    private ProgressDialog progressDialog;

    @Override
    public int layout() {
        return R.layout.activity_splash;
    }

    @Override
    public void onViewCreated(Bundle bundle) {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        new Handler().postDelayed(new Runnable() {
            public void run() {

                Log.d(getClass().getSimpleName(), "HAS Sync : " + preferences.getBoolean("has_sync", false));

                if (preferences.getBoolean("has_sync", false)) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else if (!preferences.getBoolean("has_sync", false)) {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
//                    startActivity(new Intent(SplashActivity.this, SyncActivity.class));
                }

                /*else if (!preferences.getBoolean("has_wizard", false) && !preferences.getBoolean("has_sync", false)) {
                    startActivity(new Intent(SplashActivity.this, WizardActivity.class));
                }*/

                finish();
            }
        }, 2000);

    }
}