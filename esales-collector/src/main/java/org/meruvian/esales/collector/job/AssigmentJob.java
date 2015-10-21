package org.meruvian.esales.collector.job;

import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.meruvian.esales.collector.SignageAppication;
import org.meruvian.esales.collector.content.database.adapter.AssigmentDatabaseAdapter;
import org.meruvian.esales.collector.entity.Assigment;
import org.meruvian.esales.collector.event.GenericEvent;
import org.meruvian.esales.collector.util.JsonRequestUtils;
import org.meruvian.midas.core.job.Priority;

import java.util.Formatter;

import de.greenrobot.event.EventBus;

/**
 * Created by meruvian on 12/09/15.
 */
public class AssigmentJob extends Job {
    public static final int PROCESS_ID = 40;
    private JsonRequestUtils.HttpResponseWrapper<Assigment> response;
    private String url;
    private String collId;

    public AssigmentJob(String url, String collId) {
        super(new Params(Priority.HIGH).requireNetwork().persist());
        this.url = url;
        this.collId = collId;
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
                url + CollectorUri.GET_ASSIGMENT, collId).toString());

        response = request.get(new TypeReference<Assigment>() {});

        HttpResponse r = response.getHttpResponse();

        if (r.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            Assigment assd = response.getContent();

            AssigmentDatabaseAdapter assigmentDbAdapter = new AssigmentDatabaseAdapter(SignageAppication.getInstance());
            String asdId = assigmentDbAdapter.saveAssigment(assd);

            Log.d(getClass().getSimpleName(), "Response Code :" + r.getStatusLine().getStatusCode()
                    + "Entity ID :" + collId + "Ref ID :" + assd.getId());
            EventBus.getDefault().post(new GenericEvent.RequestSuccess(PROCESS_ID, response, assd.getId(), asdId));
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
