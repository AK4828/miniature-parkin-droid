package org.meruvian.esales.collector.job;

import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.meruvian.esales.collector.SignageAppication;
import org.meruvian.esales.collector.content.database.adapter.UserDatabaseAdapter;
import org.meruvian.esales.collector.core.commons.User;
import org.meruvian.esales.collector.entity.PageEntity;
import org.meruvian.esales.collector.event.GenericEvent;
import org.meruvian.esales.collector.util.JsonRequestUtils;
import org.meruvian.midas.core.job.Priority;

import java.util.Formatter;

import de.greenrobot.event.EventBus;

/**
 * Created by meruvian on 12/09/15.
 */
public class AgentGetJob extends Job {
    public static final int PROCESS_ID = 20;
    private JsonRequestUtils.HttpResponseWrapper<PageEntity<User>> response;
    private String agentCode = null, url, userId = null;

    public AgentGetJob(String url, String agentCode) {
        super(new Params(Priority.HIGH).requireNetwork().persist());
        this.agentCode = agentCode;
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
        JsonRequestUtils request = new JsonRequestUtils(new Formatter()
                .format(url + CollectorUri.GET_AGENTS, agentCode).toString());

        response = request.get(new TypeReference<PageEntity<User>>() {});

        HttpResponse r = response.getHttpResponse();

        if (r.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            Log.d(getClass().getSimpleName(), "Response Code: " + r.getStatusLine().getStatusCode() + " " + r.getStatusLine().getReasonPhrase());
            Log.d(getClass().getSimpleName(), "Total Page(s): " + response.getContent().getTotalPages());

            UserDatabaseAdapter userDatabaseAdapter = new UserDatabaseAdapter(SignageAppication.getInstance());
            userDatabaseAdapter.saveUsers(response.getContent().getContent());

            EventBus.getDefault().post(new GenericEvent.RequestSuccess(PROCESS_ID, response, agentCode, userId));
        } else {
            Log.d(getClass().getSimpleName(), "E. Response Code :" + r.getStatusLine().getStatusCode()
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
