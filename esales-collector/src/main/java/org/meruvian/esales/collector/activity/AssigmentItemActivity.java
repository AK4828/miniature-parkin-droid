package org.meruvian.esales.collector.activity;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.path.android.jobqueue.JobManager;

import org.meruvian.esales.collector.R;
import org.meruvian.esales.collector.SignageAppication;
import org.meruvian.esales.collector.SignageVariables;
import org.meruvian.esales.collector.adapter.AssigmentItemAdapter;
import org.meruvian.esales.collector.content.database.adapter.AssigmentDetailDatabaseAdapter;
import org.meruvian.esales.collector.content.database.adapter.AssigmentDetailItemDatabaseAdapter;
import org.meruvian.esales.collector.content.database.adapter.OrderMenuDatabaseAdapter;
import org.meruvian.esales.collector.entity.AssigmentDetail;
import org.meruvian.esales.collector.entity.AssigmentDetailItem;
import org.meruvian.esales.collector.entity.Settle;
import org.meruvian.esales.collector.event.GenericEvent;
import org.meruvian.esales.collector.job.AssigmentItemJob;
import org.meruvian.esales.collector.job.AssigmentItemPostJob;
import org.meruvian.midas.core.defaults.DefaultActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by meruvian on 17/10/15.
 */
public class AssigmentItemActivity extends DefaultActivity {
    @InjectView(R.id.list_settle_items)
    ListView listItem;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    private JobManager jobManager;
    private SharedPreferences preferences;
    private ProgressDialog dialog;

    private AssigmentItemAdapter assigmentItemAdapter;
    private OrderMenuDatabaseAdapter orderMenuDbAdapter;
    private AssigmentDetailDatabaseAdapter assigmentDetailDbAdapter;
    private AssigmentDetailItemDatabaseAdapter detailItemDbAdapter;

    private AssigmentDetail assigmentDetail;

    private String asgDetailRefId = null;
    private int itemsSize = 0, countItem = 0;

    @Override
    protected int layout() {
        return R.layout.activity_assigment_item;
    }

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
    public void onViewCreated(Bundle bundle) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            asgDetailRefId = extras.getString("asg_detail_ref_id");
        }

        Log.d(getClass().getSimpleName(), "AsgRefId: " + asgDetailRefId);

        dialog = new ProgressDialog(this);
        preferences = getSharedPreferences(SignageVariables.PREFS_SERVER, 0);
        EventBus.getDefault().register(this);
        jobManager = SignageAppication.getInstance().getJobManager();

        assigmentDetailDbAdapter = new AssigmentDetailDatabaseAdapter(this);
        detailItemDbAdapter = new AssigmentDetailItemDatabaseAdapter(this);

        assigmentItemAdapter = new AssigmentItemAdapter(this, R.layout.adapter_settle_item,
                new ArrayList<Settle>());
        listItem.setAdapter(assigmentItemAdapter);

        assigmentDetail = assigmentDetailDbAdapter.findAssigmentDetailByRefId(asgDetailRefId);

        jobManager.addJobInBackground(new AssigmentItemJob(preferences.getString("server_url", ""),
                assigmentDetail.getAgent().getId(), asgDetailRefId));

    }

    public void onEventMainThread(GenericEvent.RequestInProgress requestInProgress) {
        switch (requestInProgress.getProcessId()) {
            case AssigmentItemJob.PROCESS_ID:
                startDialog("Sinkronisasi Item");
                break;
            case AssigmentItemPostJob.PROCESS_ID:
                if (countItem == 0) {
                    startDialog("Menyimpan Data");
                }
                break;
        }
    }

    public void onEventMainThread(GenericEvent.RequestSuccess requestSuccess) {
        try {
            switch (requestSuccess.getProcessId()){
                case AssigmentItemJob.PROCESS_ID:
                    assigmentItemAdapter.addAll(findDetailItems(asgDetailRefId));
                    dialog.dismiss();
                    break;
                case AssigmentItemPostJob.PROCESS_ID:
                    countItem++;
                    Log.d(getClass().getSimpleName(), "Count Item: " + countItem + " = " + itemsSize);
                    if (countItem == itemsSize){
                        dialog.dismiss();
                        dialogSuccessSettle();
                    }
                    break;
            }
        } catch (Exception e){
            Log.e(getClass().getName(), e.getMessage(), e.getCause());
        }
    }

    public void onEventMainThread(GenericEvent.RequestFailed failed) {
        Toast.makeText(this, "Tidak dapat menerima data", Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }

    @OnClick(R.id.button_receive)
    public void settleAction() {
        List<AssigmentDetailItem> items = new ArrayList<AssigmentDetailItem>();
        items = assigmentItemAdapter.getList();
        itemsSize = items.size();

        for (AssigmentDetailItem item : items) {
            jobManager.addJobInBackground(new AssigmentItemPostJob(item.getId(),
                    preferences.getString("server_url", "")));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private List<AssigmentDetailItem> findDetailItems(String id) {
        List<AssigmentDetailItem> detailItems = detailItemDbAdapter.findDetailItemsByAsdId(id);
        return detailItems;
    }

    private void startDialog(String message) {
        dialog.setMessage(message + " ...");
        dialog.show();
        dialog.setCancelable(false);
    }

    private void dialogSuccessSettle() {
        View view = View.inflate(this, R.layout.view_add_to_cart, null);

        TextView textItem = (TextView) view.findViewById(R.id.text_item_cart);

        textItem.setText("Proses Settlement Berhasil");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setTitle(R.string.settle_success);
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                clearBackStack();
                finish();
                startActivity(new Intent(AssigmentItemActivity.this, MainActivity.class));
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);
    }

    private void clearBackStack() {
        final FragmentManager fragmentManager = getFragmentManager();
        while (fragmentManager.getBackStackEntryCount() != 0) {
            fragmentManager.popBackStack();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
