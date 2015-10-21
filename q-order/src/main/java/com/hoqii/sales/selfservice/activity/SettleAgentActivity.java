package com.hoqii.sales.selfservice.activity;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hoqii.sales.selfservice.R;
import com.hoqii.sales.selfservice.SignageAppication;
import com.hoqii.sales.selfservice.SignageVariables;
import com.hoqii.sales.selfservice.adapter.SettelAgentAdapter;
import com.hoqii.sales.selfservice.content.database.adapter.AssigmentDetailDatabaseAdapter;
import com.hoqii.sales.selfservice.content.database.adapter.OrderDatabaseAdapter;
import com.hoqii.sales.selfservice.entity.AssigmentDetail;
import com.hoqii.sales.selfservice.entity.Order;
import com.hoqii.sales.selfservice.entity.Settle;
import com.hoqii.sales.selfservice.event.GenericEvent;
import com.hoqii.sales.selfservice.job.AssigmentDetailJob;
import com.hoqii.sales.selfservice.job.AssignmentDetailPutJob;
import com.hoqii.sales.selfservice.job.SettleJob;
import com.hoqii.sales.selfservice.util.AuthenticationUtils;
import com.path.android.jobqueue.JobManager;

import org.meruvian.midas.core.defaults.DefaultActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by meruvian on 01/10/15.
 */
public class SettleAgentActivity extends DefaultActivity {
    @InjectView(R.id.list_settle_agent)
    ListView listSettleAgent;
    @InjectView(R.id.button_settle)
    Button btnSettle;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    private SettelAgentAdapter settelAgentAdapter;

    private OrderDatabaseAdapter orderDatabaseAdapter;
    private AssigmentDetailDatabaseAdapter assigmentDetailDbAdapter;

    private List<Order> orders = new ArrayList<Order>();
    private List<Settle> settles = new ArrayList<Settle>();

    private JobManager jobManager;
    private SharedPreferences preferences;
    private ProgressDialog dialog;

    private int countSt = 0, sizeSt = 0;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected int layout() {
        return R.layout.activity_settle_agent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        jobManager = SignageAppication.getInstance().getJobManager();
    }

    @Override
    public void onViewCreated(Bundle bundle) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        preferences = this.getSharedPreferences(SignageVariables.PREFS_SERVER, 0);

        orderDatabaseAdapter = new OrderDatabaseAdapter(this);
        assigmentDetailDbAdapter = new AssigmentDetailDatabaseAdapter(this);

        settelAgentAdapter = new SettelAgentAdapter(this, R.layout.adapter_settle_agent,
                new ArrayList<Order>(), this);

        orders = orderDatabaseAdapter.getSettleOrders(AuthenticationUtils
                .getCurrentAuthentication().getUser().getId());

        settelAgentAdapter.addAll(orders);
        listSettleAgent.setAdapter(settelAgentAdapter);

        /*listSettleAgent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });*/


    }

    @OnClick(R.id.button_settle)
    public void submitSettle() {
        getAssigmentDetail(AuthenticationUtils
                .getCurrentAuthentication().getUser().getId());

        startDialog();
    }

    public void onEventMainThread(GenericEvent.RequestInProgress requestInProgress) {
        Log.d(getClass().getSimpleName(), "RequestInProgress: " + requestInProgress.getProcessId());
    }

    public void onEventMainThread(GenericEvent.RequestSuccess requestSuccess) {
        try {
            switch (requestSuccess.getProcessId()) {
                case AssigmentDetailJob.PROCESS_ID: {
                    settles = orderDatabaseAdapter.findSettleByOrder(AuthenticationUtils
                            .getCurrentAuthentication().getUser().getId(), requestSuccess.getRefId());
                    sizeSt = settles.size();
                    Log.d(getClass().getSimpleName(), "SETTLES.SIZE(): " + settles.size());

                    for (Settle s : settles) {
                        Log.d(getClass().getSimpleName(), "Settle.ID: " + s.getId());
                        jobManager.addJobInBackground(new SettleJob(s.getId(), preferences.getString("server_url", "")));
                    }
                    break;
                }
                case SettleJob.PROCESS_ID: {
                    countSt++;
                    Log.d(getClass().getSimpleName(), "CountSt: " + countSt + " >< " +  sizeSt);
                    if (countSt == sizeSt) {
                        List<Order> ods = orderDatabaseAdapter.getSettleOrders(AuthenticationUtils
                                .getCurrentAuthentication().getUser().getId());
                        orderDatabaseAdapter.updateOrdersStatusById(ods, Order.OrderStatus.SETTLE.name());

                        Log.d(getClass().getSimpleName(), "REQUESTSUCCESS.GETENTITY.ID: " + requestSuccess.getEntityId());
                        AssigmentDetail ad = assigmentDetailDbAdapter.findAssigmentDetailByRefId(requestSuccess.getEntityId());
                        jobManager.addJobInBackground(new AssignmentDetailPutJob(ad.getRefId(), ad.getId(), preferences.getString("server_url", "")));
                    }
                    break;
                }
                case AssignmentDetailPutJob.PROCESS_ID: {
                    dialog.dismiss();
                    dialogSuccessOrder();
                    Log.d(getClass().getSimpleName(), "Success ");
                    break;
                }

            }
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    public void onEventMainThread(GenericEvent.RequestFailed failed) {
        dialog.dismiss();
        Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();

        Log.e(getClass().getSimpleName(),
                failed.getResponse().getHttpResponse().getStatusLine().getStatusCode() + " : "
                        + failed.getResponse().getHttpResponse().getStatusLine().getReasonPhrase());
    }

    public void startDialog() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Menyimpan data ...");
        dialog.show();
        dialog.setCancelable(false);
    }

    private void dialogSuccessOrder() {
        View view = View.inflate(this, R.layout.view_add_to_cart, null);

        TextView textItem = (TextView) view.findViewById(R.id.text_item_cart);

        textItem.setText("Settle anda berhasil diproses.");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setTitle(R.string.order_success);
        builder.setPositiveButton(getString(R.string.continue_shopping), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                clearBackStack();
                finish();
                startActivity(new Intent(SettleAgentActivity.this, MainActivity.class));
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);
    }

    private void getAssigmentDetail(String agentId) {
        jobManager.addJobInBackground(new AssigmentDetailJob(preferences.getString("server_url", ""), agentId));
    }

    private void clearBackStack() {
        final FragmentManager fragmentManager = getFragmentManager();
        while (fragmentManager.getBackStackEntryCount() != 0) {
            fragmentManager.popBackStackImmediate();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
