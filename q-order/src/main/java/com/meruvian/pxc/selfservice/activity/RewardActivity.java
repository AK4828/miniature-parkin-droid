package com.meruvian.pxc.selfservice.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.meruvian.pxc.selfservice.R;
import com.meruvian.pxc.selfservice.SignageAppication;
import com.meruvian.pxc.selfservice.SignageVariables;
import com.meruvian.pxc.selfservice.job.PointJob;
import com.meruvian.pxc.selfservice.service.JobStatus;
import com.meruvian.pxc.selfservice.util.AuthenticationUtils;
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

        jobManager = SignageAppication.getInstance().getJobManager();
        jobManager.addJobInBackground(PointJob.newInstance());


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
            userPoint.setText(Double.toString(point));
            preferences = getSharedPreferences(SignageVariables.PREFS_SERVER, 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putLong("default_point", Long.parseLong(Double.toString(point)));
            editor.commit();
        }
        if (status == JobStatus.USER_ERROR) {
            Toast.makeText(this, "Failed retrieve data" ,Toast.LENGTH_LONG);
            preferences = getSharedPreferences(SignageVariables.PREFS_SERVER, 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putLong("default_point", 0);
            editor.commit();
        }
    }
}
