package com.meruvian.pxc.selfservice.job;

import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.meruvian.pxc.selfservice.SignageAppication;
import com.meruvian.pxc.selfservice.content.database.adapter.AssigmentDetailDatabaseAdapter;
import com.meruvian.pxc.selfservice.entity.AssigmentDetail;
import com.meruvian.pxc.selfservice.event.GenericEvent;
import com.meruvian.pxc.selfservice.util.JsonRequestUtils;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.meruvian.midas.core.job.Priority;

import java.util.Formatter;

import de.greenrobot.event.EventBus;

/**
 * Created by meruvian on 12/09/15.
 */
public class AssigmentDetailJob extends Job {
    public static final int PROCESS_ID = 41;
    private JsonRequestUtils.HttpResponseWrapper<AssigmentDetail> response;
    private String url;
    private String agentId;

    public AssigmentDetailJob(String url, String agentId) {
        super(new Params(Priority.HIGH).requireNetwork().persist());
        this.url = url;
        this.agentId = agentId;
    }

    @Override
    public void onAdded() {
        Log.d(getClass().getSimpleName(), "onAdded");
        EventBus.getDefault().post(new GenericEvent.RequestInProgress(PROCESS_ID));
    }

    @Override
    public void onRun() throws Throwable {
        Log.d(getClass().getSimpleName(), "onRun");
//        JsonRequestUtils request = new JsonRequestUtils(url + HoqiiUri.GET_ASSIGMENT_DETAIL);
        JsonRequestUtils request = new JsonRequestUtils(new Formatter().format(
                url + ESalesUri.GET_ASSIGMENT_DETAIL, agentId).toString());

        response = request.get(new TypeReference<AssigmentDetail>() {});

        HttpResponse r = response.getHttpResponse();

        if (r.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            AssigmentDetail assd = response.getContent();
            assd.setRefId(assd.getId());

            AssigmentDetailDatabaseAdapter assigmentDbAdapter = new AssigmentDetailDatabaseAdapter(SignageAppication.getInstance());
            assigmentDbAdapter.saveAssigmentDetailByRefId(assd);

            Log.d(getClass().getSimpleName(), "Response Code :" + r.getStatusLine().getStatusCode()
                    + "Entity ID :" + agentId + "Ref ID :" + assd.getId());
            EventBus.getDefault().post(new GenericEvent.RequestSuccess(PROCESS_ID, response, assd.getId(), agentId));
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
