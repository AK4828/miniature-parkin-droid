package org.meruvian.esales.collector.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.path.android.jobqueue.JobManager;

import org.meruvian.esales.collector.R;
import org.meruvian.esales.collector.SignageAppication;
import org.meruvian.esales.collector.SignageVariables;
import org.meruvian.esales.collector.event.GenericEvent;
import org.meruvian.esales.collector.job.AssigmentDetailJob;
import org.meruvian.esales.collector.job.AssigmentJob;
import org.meruvian.esales.collector.util.AuthenticationUtils;
import org.meruvian.midas.core.defaults.DefaultActivity;

import butterknife.InjectView;
import de.greenrobot.event.EventBus;

/**
 * Created by meruvian on 22/09/15.
 */
public class SyncJobActivity extends DefaultActivity {
    @InjectView(R.id.text_sync)
    TextView textSync;
    @InjectView(R.id.button_sync)
    Button buttonSync;
    @InjectView(R.id.progressbar)
    ProgressBar progressBar;

    private JobManager jobManager;
    private SharedPreferences preferences;

    private String agenRefId;
    private int userTotalElements = 0;
    private int totalInvitation = 0;
    private int userCount = 0;
    private int invitationCount = 0;

    @Override
    protected int layout() {
        return R.layout.activity_sync;
    }

    @Override
    public void onViewCreated(Bundle bundle) {
        preferences = getSharedPreferences(SignageVariables.PREFS_SERVER, 0);

        EventBus.getDefault().register(this);
        jobManager = SignageAppication.getInstance().getJobManager();
        Log.d(getClass().getSimpleName(), "Kolektor ID: " +
                AuthenticationUtils.getCurrentAuthentication().getUser().getId());

        jobManager.addJobInBackground(new AssigmentJob(preferences.getString("server_url", ""),
                AuthenticationUtils.getCurrentAuthentication().getUser().getId()));
    }

    public void onEventMainThread(GenericEvent.RequestInProgress requestInProgress) {
        textSync.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        buttonSync.setVisibility(View.GONE);
        switch (requestInProgress.getProcessId()) {
            case AssigmentJob.PROCESS_ID:
                textSync.setText("Sinkronisasi Penugasan");
                break;
            case AssigmentDetailJob.PROCESS_ID:
                textSync.setText("Sinkronisasi Detail Penugasan");
                break;
        }
    }

    public void onEventMainThread(GenericEvent.RequestSuccess requestSuccess) {
        try {
            switch (requestSuccess.getProcessId()) {
                case AssigmentJob.PROCESS_ID:
                    jobManager.addJobInBackground(new AssigmentDetailJob(
                            preferences.getString("server_url", ""), requestSuccess.getRefId()));
                    break;
                case AssigmentDetailJob.PROCESS_ID:
                    Log.d(getClass().getSimpleName(), "Has Sync");
                    SharedPreferences.Editor edit = preferences.edit();
                    edit.putBoolean("has_sync", true);
                    edit.commit();

                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                    break;
            }
        } catch (Exception e){
            Log.e(getClass().getName(),e.getMessage(),e.getCause());
        }
    }

    public void onEventMainThread(GenericEvent.RequestFailed failed) {
        Toast.makeText(this, "Tidak dapat menerima data", Toast.LENGTH_SHORT).show();

        Log.e(getClass().getSimpleName(),"Status Code " + failed.getResponse().getHttpResponse()
                .getStatusLine().getStatusCode());

        textSync.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        buttonSync.setVisibility(View.VISIBLE);
        buttonSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jobManager.addJobInBackground(new AssigmentJob(preferences.getString("server_url", ""),
                        AuthenticationUtils.getCurrentAuthentication().getUser().getId()));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}



/*
case AgentGetJob.PROCESS_ID:
                    PageEntity<User> result = (PageEntity<User>) requestSuccess.getResponse().getContent();
                    if (result.getTotalElements() > 0) {
                        userTotalElements = result.getTotalElements();
                        List<User> users = result.getContent();
                        Log.i(getClass().getSimpleName(), "Users.size(): " + users.size());
                        for (User user : users) {
                            Log.d(getClass().getSimpleName(), "Agent Code: " + user.getAgentCode());
                            jobManager.addJobInBackground(new BuyerGetJob(preferences.getString("server_url", ""), user.getAgentCode()));
                        }
                    }
                    break;
                case BuyerGetJob.PROCESS_ID:
                    userCount++;
                    PageEntity<Contact> resultBuyer = (PageEntity<Contact>) requestSuccess.getResponse().getContent();
                    if (resultBuyer.getTotalElements() > 0) {
                        List<Contact> contacts = resultBuyer.getContent();
                        Log.i(getClass().getSimpleName(), "contacts.size(): " + contacts.size());
                        for (Contact contact : contacts) {
                            Log.d(getClass().getSimpleName(), "Agent Code: " + contact.getUser().getAgentCode() + "Buyer Id: " + contact.getId());
                            jobManager.addJobInBackground(new OrderBuyerJob(
                                    preferences.getString("server_url", ""),
                                    contact.getUser().getAgentCode(), contact.getId()));
                        }
                    }
                    break;
                case OrderBuyerJob.PROCESS_ID:
                    PageEntity<Order> resultOrder = (PageEntity<Order>) requestSuccess.getResponse().getContent();
                    if (resultOrder.getTotalElements() > 0) {
                        List<Order> orders = resultOrder.getContent();
                        Log.i(getClass().getSimpleName(), "orders.size(): " + orders.size());
                        for (Order order : orders) {
                            Log.d(getClass().getSimpleName(), "Order ID: " + order.getId());
                            jobManager.addJobInBackground(new OrderMenuGetJob(preferences.getString("server_url", ""), order.getId()));
                        }
                    }
                    break;

                case OrderMenuGetJob.PROCESS_ID:
                    Log.d(getClass().getSimpleName(), "User.Count: " + userCount
                            + " == " + "User.TotalElements: " + userTotalElements);
                    if (userCount == userTotalElements) {
                        Log.d(getClass().getSimpleName(), "Has Sync");
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("has_sync", true);

                        editor.commit();

                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    }
                    break;
*/
