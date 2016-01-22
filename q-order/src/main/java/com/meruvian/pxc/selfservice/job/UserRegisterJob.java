package com.meruvian.pxc.selfservice.job;

import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.meruvian.pxc.selfservice.SignageAppication;
import com.meruvian.pxc.selfservice.content.database.adapter.ContactDatabaseAdapter;
import com.meruvian.pxc.selfservice.core.commons.User;
import com.meruvian.pxc.selfservice.entity.Contact;
import com.meruvian.pxc.selfservice.event.GenericEvent;
import com.meruvian.pxc.selfservice.util.JsonRequestUtils;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.meruvian.midas.core.job.Priority;

import de.greenrobot.event.EventBus;

/**
 * Created by meruvian on 11/09/15.
 */
public class UserRegisterJob extends Job {
    public static final int PROCESS_ID = 41;
    private JsonRequestUtils.HttpResponseWrapper<User> response;
    private String userId;
    private String url;

    protected UserRegisterJob(String userId, String url) {
        super(new Params(Priority.HIGH).requireNetwork().persist());
        this.userId = userId;
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

        ContactDatabaseAdapter contactDatabaseAdapter = new ContactDatabaseAdapter(SignageAppication.getInstance());
        Contact contact = contactDatabaseAdapter.findContactById(userId);
        contact.setId(null);

        Log.d(getClass().getSimpleName(), "Contact Id:" + contact.getId());
        response = request.post(contact, new TypeReference<User>() {});
        HttpResponse r = response.getHttpResponse();

        if (r.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            User user = response.getContent();
//            contactDatabaseAdapter.updateSyncStatusById(userId, ct.getId());

            Log.d(getClass().getSimpleName(), "Response Code :" + r.getStatusLine().getStatusCode() + "Entity ID :" + userId + "Ref ID :" + user.getId());
            EventBus.getDefault().post(new GenericEvent.RequestSuccess(PROCESS_ID, response, user.getId(), userId));
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
