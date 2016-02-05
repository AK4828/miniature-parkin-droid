package com.meruvian.pxc.selfservice.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

import com.meruvian.pxc.selfservice.R;
import com.meruvian.pxc.selfservice.SignageAppication;
import com.meruvian.pxc.selfservice.job.RefreshTokenJob;
import com.meruvian.pxc.selfservice.util.AuthenticationUtils;
import com.path.android.jobqueue.JobManager;

import org.meruvian.midas.core.defaults.DefaultActivity;

import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;

/**
 * Created by ludviantoovandi on 15/10/14.
 */
public class SplashActivity extends DefaultActivity {
    private SharedPreferences preferences;

    @Override
    public int layout() {
        return R.layout.activity_splash;
    }

    @Override
    public void onViewCreated(Bundle bundle) {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        new Handler().postDelayed(new Runnable() {
            public void run() {

                if (preferences.getBoolean("has_sync_point", false)) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else if (!preferences.getBoolean("has_sync_point", false)) {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }

                finish();
            }
        }, 2000);

    }

    @Override
    protected void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(RefreshTokenJob.RefreshEvent event) {
        int status = event.getStatus();

        if (status == RefreshTokenJob.RefreshEvent.REFRESH_FAILED) {
            AuthenticationUtils.logout();
        }

    }
}