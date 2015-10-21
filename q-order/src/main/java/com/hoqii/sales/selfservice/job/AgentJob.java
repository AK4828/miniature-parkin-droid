package com.hoqii.sales.selfservice.job;

import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hoqii.sales.selfservice.core.commons.User;
import com.hoqii.sales.selfservice.event.GenericEvent;
import com.hoqii.sales.selfservice.util.JsonRequestUtils;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.meruvian.midas.core.job.Priority;

import de.greenrobot.event.EventBus;

/**
 * Created by meruvian on 12/09/15.
 */
public class AgentJob extends Job {
    public static final int PROCESS_ID = 21;
    private JsonRequestUtils.HttpResponseWrapper<User> response;
    private String userId = null;
    private String url;

    public AgentJob(String url) {
        super(new Params(Priority.HIGH).requireNetwork().persist());
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
        JsonRequestUtils request = new JsonRequestUtils(url + ESalesUri.CONTACT);
        response = request.get(new TypeReference<User>() {});

        HttpResponse r = response.getHttpResponse();

        if (r.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            User pmn = response.getContent();

            Log.d(getClass().getSimpleName(), "Response Code :" + r.getStatusLine().getStatusCode() + "Entity ID :" + userId + "Ref ID :" + pmn.getId());
            EventBus.getDefault().post(new GenericEvent.RequestSuccess(PROCESS_ID, response, pmn.getId(), userId));
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
