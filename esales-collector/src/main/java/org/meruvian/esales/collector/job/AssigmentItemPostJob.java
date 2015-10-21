package org.meruvian.esales.collector.job;

import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.meruvian.esales.collector.SignageAppication;
import org.meruvian.esales.collector.content.database.adapter.AssigmentDetailItemDatabaseAdapter;
import org.meruvian.esales.collector.entity.AssigmentDetailItem;
import org.meruvian.esales.collector.event.GenericEvent;
import org.meruvian.esales.collector.util.JsonRequestUtils;
import org.meruvian.midas.core.job.Priority;

import de.greenrobot.event.EventBus;

/**
 * Created by meruvian on 30/07/15.
 */
public class AssigmentItemPostJob extends Job {
    public static final int PROCESS_ID = 50;
    private JsonRequestUtils.HttpResponseWrapper<AssigmentDetailItem> response;
    private String asgItemId;
    private String url;

    public AssigmentItemPostJob(String asgItemId, String url) {
        super(new Params(Priority.HIGH).requireNetwork().persist());
        this.asgItemId = asgItemId;
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
        JsonRequestUtils request = new JsonRequestUtils(url + CollectorUri.POST_ASG_ITEM);
        AssigmentDetailItemDatabaseAdapter asgItemDatabaseAdapter = new AssigmentDetailItemDatabaseAdapter(SignageAppication.getInstance());

        AssigmentDetailItem asgItem = asgItemDatabaseAdapter.findAssigmentDetailItemById(asgItemId);
        Log.d(getClass().getSimpleName(), "AssigmentDetailItem Id:" + asgItem.getId());

        asgItem.setId(null);

        response = request.post(asgItem, new TypeReference<AssigmentDetailItem>() {});
        HttpResponse r = response.getHttpResponse();

        if (r.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            AssigmentDetailItem st = response.getContent();
            asgItemDatabaseAdapter.updateSyncStatusById(asgItemId, st.getId());

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
