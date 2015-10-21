package org.meruvian.esales.collector.job;

import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.meruvian.esales.collector.SignageAppication;
import org.meruvian.esales.collector.content.database.adapter.AssigmentDetailDatabaseAdapter;
import org.meruvian.esales.collector.entity.AssigmentDetail;
import org.meruvian.esales.collector.event.GenericEvent;
import org.meruvian.esales.collector.util.JsonRequestUtils;
import org.meruvian.midas.core.job.Priority;

import java.util.Formatter;

import de.greenrobot.event.EventBus;

/**
 * Created by meruvian on 30/07/15.
 */
public class AssigmentDetailPutJob extends Job {
    public static final int PROCESS_ID = 33;
    private JsonRequestUtils.HttpResponseWrapper<AssigmentDetail> response;
    private String assdId;
    private String assdRefId;
    private String url;

    public AssigmentDetailPutJob(String assdRefId, String assdId, String url) {
        super(new Params(Priority.HIGH).requireNetwork().persist());
        this.assdId = assdId;
        this.url = url;
        this.assdRefId = assdRefId;
    }

    @Override
    public void onAdded() {
        Log.d(getClass().getSimpleName(), "onAdded");
        EventBus.getDefault().post(new GenericEvent.RequestInProgress(PROCESS_ID));
    }

    @Override
    public void onRun() throws Throwable {
        Log.d(getClass().getSimpleName(), "onRun" + "REF-ID: " + assdRefId + "ID: " + assdId);
        JsonRequestUtils request = new JsonRequestUtils(new Formatter().format(url
                + CollectorUri.PUT_ASG_DETAIL, assdRefId).toString());

        AssigmentDetailDatabaseAdapter assigmentDetailDbAdapter = new AssigmentDetailDatabaseAdapter(
                SignageAppication.getInstance());
        AssigmentDetail assigmentDetail = assigmentDetailDbAdapter.findAssigmentDetailById(assdId);
        assigmentDetail.setId(assdRefId);

        Log.d(getClass().getSimpleName(), "assigmentDetail Id: " + assigmentDetail.getId());
        response = request.put(assigmentDetail, new TypeReference<AssigmentDetail>() {});
        HttpResponse r = response.getHttpResponse();

        if (r.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            AssigmentDetail ad = response.getContent();
            assigmentDetailDbAdapter.updateSyncStatusById(assdRefId);

            Log.d(getClass().getSimpleName(), "Response Code :" + r.getStatusLine().getStatusCode()
                    + "Entity ID :" + assdId + "Ref ID :" + ad.getId());
            EventBus.getDefault().post(new GenericEvent.RequestSuccess(PROCESS_ID, response, ad.getId(), assdId));
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
