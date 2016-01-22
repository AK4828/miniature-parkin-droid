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
 * Created by meruvian on 30/07/15.
 */
public class AssignmentDetailPutJob extends Job {
    public static final int PROCESS_ID = 43;
    private JsonRequestUtils.HttpResponseWrapper<AssigmentDetail> response;
    private String assignmentDetailId;
    private String assignmentDetailRefId;
    private String url;

    public AssignmentDetailPutJob(String assignmentDetailRefId, String assignmentDetailId, String url) {
        super(new Params(Priority.HIGH).requireNetwork().persist());
        this.assignmentDetailId = assignmentDetailId;
        this.assignmentDetailRefId = assignmentDetailRefId;
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
        Log.d(getClass().getSimpleName(), "REF ID: " + assignmentDetailRefId
                + "\nID: " + assignmentDetailId);

        JsonRequestUtils request = new JsonRequestUtils(new Formatter().format(url + ESalesUri.PUT_ASSIGMENT_DETAIL, assignmentDetailRefId).toString());

        AssigmentDetailDatabaseAdapter assignmentDetailDatabaseAdapter = new AssigmentDetailDatabaseAdapter(SignageAppication.getInstance());
        AssigmentDetail assignmentDetail = assignmentDetailDatabaseAdapter.findAssigmentDetailById(assignmentDetailId);
        assignmentDetail.setId(assignmentDetailRefId);
        assignmentDetail.setStatus(AssigmentDetail.AssigmentDetailStatus.SETTLE);

        Log.d(getClass().getSimpleName(), "AssigmentDetail Id:" + assignmentDetail.getId());

        response = request.put(assignmentDetail, new TypeReference<AssigmentDetail>() {});
        HttpResponse r = response.getHttpResponse();

        if (r.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            AssigmentDetail ct = response.getContent();
            assignmentDetailDatabaseAdapter.updateSyncStatusById(assignmentDetailId, ct.getId());

            Log.d(getClass().getSimpleName(), "Response Code :" + r.getStatusLine().getStatusCode() + "Entity ID :" + assignmentDetailId + "Ref ID :" + ct.getId());
            EventBus.getDefault().post(new GenericEvent.RequestSuccess(PROCESS_ID, response, ct.getId(), assignmentDetailId));
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
