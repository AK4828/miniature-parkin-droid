package com.meruvian.pxc.selfservice.job;

import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.meruvian.pxc.selfservice.SignageAppication;
import com.meruvian.pxc.selfservice.content.database.adapter.SettleDatabaseAdapter;
import com.meruvian.pxc.selfservice.entity.Settle;
import com.meruvian.pxc.selfservice.event.GenericEvent;
import com.meruvian.pxc.selfservice.util.JsonRequestUtils;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.meruvian.midas.core.job.Priority;

import de.greenrobot.event.EventBus;

/**
 * Created by meruvian on 30/07/15.
 */
public class SettleJob extends Job {
    public static final int PROCESS_ID = 42;
    private JsonRequestUtils.HttpResponseWrapper<Settle> response;
    private String settleId;
    private String url;

    public SettleJob(String settleId, String url) {
        super(new Params(Priority.HIGH).requireNetwork().persist());
        this.settleId = settleId;
        this.url = url;
    }

    @Override
    public void onAdded() {
        Log.d(getClass().getSimpleName(), "onAdded");
        EventBus.getDefault().post(new GenericEvent.RequestInProgress(PROCESS_ID));
    }

    @Override
    public void onRun() throws Throwable {
        Log.d(getClass().getSimpleName(), "onRun");
        JsonRequestUtils request = new JsonRequestUtils(url + ESalesUri.POST_SETTLE);
        SettleDatabaseAdapter settleDatabaseAdapter = new SettleDatabaseAdapter(SignageAppication.getInstance());

        Settle settle = settleDatabaseAdapter.findSettleById(settleId);
        Log.d(getClass().getSimpleName(), "Settle Id:" + settle.getId()
                + "\n Ad: " + settle.getAssigmentDetail().getId()
                + "\n Pdc: " + settle.getProduct().getId()
                + "\n Qty: " + settle.getQty()
                + "\n SlP: " + settle.getSellPrice()
        );


        settle.setId(null);

        response = request.post(settle, new TypeReference<Settle>() {});
        HttpResponse r = response.getHttpResponse();

        if (r.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            Settle st = response.getContent();
            settleDatabaseAdapter.updateSyncStatusById(settleId, st.getId());

            Log.d(getClass().getSimpleName(), "Response Code :" + r.getStatusLine().getStatusCode()
                    + "Entity ID :" + st.getAssigmentDetail().getId() + "Ref ID :" + st.getId());
            EventBus.getDefault().post(new GenericEvent.RequestSuccess(
                    PROCESS_ID, response, st.getId(), st.getAssigmentDetail().getId()));
        } else {
            Log.d(getClass().getSimpleName(), "Response Code :" + r.getStatusLine().getStatusCode() + " " + r.getStatusLine().getReasonPhrase());
            throw new RuntimeException();
        }
    }

    @Override
    protected void onCancel() {
        EventBus.getDefault().post(new GenericEvent.RequestFailed(PROCESS_ID, response));
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }
}
