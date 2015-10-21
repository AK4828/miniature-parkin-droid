package org.meruvian.esales.collector.fragment;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.path.android.jobqueue.JobManager;

import org.meruvian.esales.collector.R;
import org.meruvian.esales.collector.SignageAppication;
import org.meruvian.esales.collector.SignageVariables;
import org.meruvian.esales.collector.adapter.SettleDetailAdapter;
import org.meruvian.esales.collector.content.database.adapter.AssigmentDetailDatabaseAdapter;
import org.meruvian.esales.collector.content.database.adapter.SettleDatabaseAdapter;
import org.meruvian.esales.collector.entity.AssigmentDetail;
import org.meruvian.esales.collector.entity.Settle;
import org.meruvian.esales.collector.event.GenericEvent;
import org.meruvian.esales.collector.job.AssigmentDetailPutJob;
import org.meruvian.esales.collector.job.SettleJob;
import org.meruvian.midas.core.defaults.DefaultFragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by meruvian on 15/09/15.
 */
public class SettleDetailFragment extends DefaultFragment {
    @InjectView(R.id.text_total_item)
    TextView textTotalItem;
    @InjectView(R.id.text_total_order)
    TextView textTotalOrder;

    @InjectView(R.id.list_item_settle)
    ListView listItem;

    private JobManager jobManager;
    private SharedPreferences preferences;
    private ProgressDialog dialog;

    private SettleDetailAdapter settleDetailAdapter;
    private SettleDatabaseAdapter settleDbAdapter;
    private AssigmentDetailDatabaseAdapter assigmentDetailDbAdapter;

    private List<Settle> settles = new ArrayList<Settle>();
    private DecimalFormat decimalFormat = new DecimalFormat("#,###");

    private String asgDetailRefId;
    private long totalPrice = 0, totalItem = 0;

    @Override
    protected int layout() {
        return R.layout.fragment_settel_detail;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        asgDetailRefId = getArguments().getString("asg_detail_ref_id", null);
        dialog = new ProgressDialog(getActivity());
        preferences = getActivity().getSharedPreferences(SignageVariables.PREFS_SERVER, 0);
        EventBus.getDefault().register(this);
        jobManager = SignageAppication.getInstance().getJobManager();

        settleDbAdapter = new SettleDatabaseAdapter(getActivity());
        assigmentDetailDbAdapter = new AssigmentDetailDatabaseAdapter(getActivity());
        settleDetailAdapter = new SettleDetailAdapter(getActivity(), R.layout.adapter_settle_detail,
                new ArrayList<Settle>());

        listItem.setAdapter(settleDetailAdapter);

        jobManager.addJobInBackground(new SettleJob(preferences.getString("server_url", ""),
                asgDetailRefId));

        listItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    public void onEventMainThread(GenericEvent.RequestInProgress requestInProgress) {
        switch (requestInProgress.getProcessId()) {
            case SettleJob.PROCESS_ID:
                startDialog("Sinkronisasi Settlement");
                break;
            case AssigmentDetailPutJob.PROCESS_ID:
                startDialog("Update Data");
                break;
        }
    }

    public void onEventMainThread(GenericEvent.RequestSuccess requestSuccess) {
        try {
            switch (requestSuccess.getProcessId()){
                case SettleJob.PROCESS_ID:
                    settleDetailAdapter.addAll(findSettles());
                    dialog.dismiss();
                    break;
                case AssigmentDetailPutJob.PROCESS_ID:
                    dialog.dismiss();
                    dialogSuccessSettle();
                    break;
            }
        } catch (Exception e){
            Log.e(getClass().getName(), e.getMessage(), e.getCause());
        }
    }

    public void onEventMainThread(GenericEvent.RequestFailed failed) {
        Toast.makeText(getActivity(), "Tidak dapat menerima data", Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }

    @OnClick(R.id.button_settle)
    public void settleAction() {
        AssigmentDetail asd = findAssigmentDetail(asgDetailRefId);

        jobManager.addJobInBackground(new AssigmentDetailPutJob(asd.getRefId(), asd.getId(),
                preferences.getString("server_url", "")));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private List<Settle> findSettles() {
        List<Settle> settles = settleDbAdapter.findAllSettle(asgDetailRefId);

        for (Settle st : settles) {
            totalPrice += st.getSellPrice() * st.getQty();
            totalItem += st.getQty();
        }

        textTotalItem.setText("Jumlah Item: " + totalItem);
        textTotalOrder.setText("Total Order: " + totalPrice);

        return settles;
    }

    private AssigmentDetail findAssigmentDetail(String asgDetailRefId){
        assigmentDetailDbAdapter.updateStatusByRefId(asgDetailRefId,
                AssigmentDetail.AssigmentDetailStatus.CLOSE);
        AssigmentDetail assigmentDetail = assigmentDetailDbAdapter
                .findAssigmentDetailByRefId(asgDetailRefId);

        return assigmentDetail;
    }

    private void startDialog(String message) {
        dialog.setMessage(message + " ...");
        dialog.show();
        dialog.setCancelable(false);
    }

    private void dialogSuccessSettle() {
        View view = View.inflate(getActivity(), R.layout.view_add_to_cart, null);

        TextView textItem = (TextView) view.findViewById(R.id.text_item_cart);

        textItem.setText("Proses Settlement Berhasil");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setTitle(R.string.settle_success);
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                clearBackStack();
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

}
