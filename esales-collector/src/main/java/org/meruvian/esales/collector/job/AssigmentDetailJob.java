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
import org.meruvian.esales.collector.entity.PageEntity;
import org.meruvian.esales.collector.event.GenericEvent;
import org.meruvian.esales.collector.util.JsonRequestUtils;
import org.meruvian.midas.core.job.Priority;

import java.util.Formatter;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by meruvian on 12/09/15.
 */
public class AssigmentDetailJob extends Job {
    public static final int PROCESS_ID = 41;
    private JsonRequestUtils.HttpResponseWrapper<PageEntity<AssigmentDetail>> response;
    private String url;
    private String asgId;

    public AssigmentDetailJob(String url, String asgId) {
        super(new Params(Priority.HIGH).requireNetwork().persist());
        this.url = url;
        this.asgId = asgId;
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
                url + CollectorUri.GET_ASG_DETAIL, asgId).toString());

        response = request.get(new TypeReference<PageEntity<AssigmentDetail>>() {});

        HttpResponse r = response.getHttpResponse();

        if (r.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            List<AssigmentDetail> asds = response.getContent().getContent();
            Log.d(getClass().getSimpleName(), "1Response Code :" + r.getStatusLine().getStatusCode()
                    + "response.asds.size: " + asds.size());

            AssigmentDetailDatabaseAdapter assigmentDbAdapter = new AssigmentDetailDatabaseAdapter(SignageAppication.getInstance());
            assigmentDbAdapter.saveAssigmentDetails(asds);

            EventBus.getDefault().post(new GenericEvent.RequestSuccess(PROCESS_ID, response, "refId", asgId));
        } else {
            Log.d(getClass().getSimpleName(), "2Response Code :" + r.getStatusLine().getStatusCode()
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
