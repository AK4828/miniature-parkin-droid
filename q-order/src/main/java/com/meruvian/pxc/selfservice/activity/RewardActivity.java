package com.meruvian.pxc.selfservice.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.meruvian.pxc.selfservice.R;
import com.meruvian.pxc.selfservice.SignageAppication;
import com.meruvian.pxc.selfservice.SignageVariables;
import com.meruvian.pxc.selfservice.job.PXCPointJob;
import com.meruvian.pxc.selfservice.job.PointJob;
import com.meruvian.pxc.selfservice.service.JobStatus;
import com.meruvian.pxc.selfservice.util.AuthenticationUtils;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.JobManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by akm on 27/01/16.
 */
public class RewardActivity extends AppCompatActivity{

    @Bind(R.id.main_toolbar) Toolbar toolbar;
    @Bind(R.id.site_name) TextView username;
    @Bind(R.id.site_email) TextView userEmail;
    @Bind(R.id.point_count) TextView userPoint;
    private JobManager jobManager;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);
        toolbar = (Toolbar)findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        preferences = this.getSharedPreferences(SignageVariables.PREFS_SERVER, 0);
        jobManager = SignageAppication.getInstance().getJobManager();
        if (preferences.getString("login status", "").equals("fxpc user")) {
            jobManager.addJobInBackground(PointJob.newInstance());
        } else if (preferences.getString("login status", "").equals("pxc user")) {
            jobManager.addJobInBackground(PXCPointJob.newInstance());
        }

        username.setText(AuthenticationUtils.getCurrentAuthentication().getUser().getName().getFirst());
        userEmail.setText(AuthenticationUtils.getCurrentAuthentication().getUser().getEmail());
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


    public void onEventMainThread(PointJob.PointEvent event) {
        int status = event.getStatus();
        double point = event.getPoint();

        if (status == JobStatus.SUCCESS) {
            SharedPreferences.Editor editor = preferences.edit();
            userPoint.setText(Double.toString(point) + " Point");
        }
        if (status == JobStatus.USER_ERROR) {
            Toast.makeText(this, "No Point Available" ,Toast.LENGTH_LONG).show();
        }
        if (status == JobStatus.SYSTEM_ERROR) {
            Toast.makeText(this, "Failed" ,Toast.LENGTH_LONG).show();
        }
        if (status == JobStatus.ABORTED) {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
        }
    }
}
