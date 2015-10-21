package org.meruvian.esales.collector.job;

import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.meruvian.esales.collector.SignageAppication;
import org.meruvian.esales.collector.content.database.adapter.SettleDatabaseAdapter;
import org.meruvian.esales.collector.entity.PageEntity;
import org.meruvian.esales.collector.entity.Settle;
import org.meruvian.esales.collector.event.GenericEvent;
import org.meruvian.esales.collector.util.JsonRequestUtils;
import org.meruvian.midas.core.job.Priority;

import java.util.Formatter;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by meruvian on 12/09/15.
 */
public class SettleJob extends Job {
    public static final int PROCESS_ID = 42;
    private JsonRequestUtils.HttpResponseWrapper<PageEntity<Settle>> response;
    private String url;
    private String assigmentDetailId;

    public  SettleJob(String url, String assigmentDetailId) {
        super(new Params(Priority.HIGH).requireNetwork().persist());
        this.url = url;
        this.assigmentDetailId = assigmentDetailId;
    }

    @Override
    public void onAdded() {
        Log.d(getClass().getSimpleName(), "onAdded");
        EventBus.getDefault().post(new GenericEvent.RequestInProgress(PROCESS_ID));
    }

    @Override
    public void onRun() throws Throwable {
        Log.d(getClass().getSimpleName(), "onRun");
        JsonRequestUtils request = new JsonRequestUtils(new Formatter().format(
                url + CollectorUri.GET_SETTLE, assigmentDetailId).toString());

        response = request.get(new TypeReference<PageEntity<Settle>>() {});

        HttpResponse r = response.getHttpResponse();

        if (r.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            List<Settle> asds = response.getContent().getContent();

            SettleDatabaseAdapter settleDbAdapter = new SettleDatabaseAdapter(SignageAppication.getInstance());
            settleDbAdapter.saveSettles(asds);

            Log.d(getClass().getSimpleName(), "Response Code :" + r.getStatusLine().getStatusCode()
                    + "RESPONSE.ASDS.SIZE: " + asds.size());
            EventBus.getDefault().post(new GenericEvent.RequestSuccess(PROCESS_ID, response, null, assigmentDetailId));
        } else {
            Log.d(getClass().getSimpleName(), "Response Code :" + r.getStatusLine().getStatusCode()
                    + " " + r.getStatusLine().getReasonPhrase());
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
