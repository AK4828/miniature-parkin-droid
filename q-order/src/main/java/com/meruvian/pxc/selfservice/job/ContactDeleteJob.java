package com.meruvian.pxc.selfservice.job;

import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.meruvian.pxc.selfservice.SignageAppication;
import com.meruvian.pxc.selfservice.content.database.adapter.ContactDatabaseAdapter;
import com.meruvian.pxc.selfservice.entity.Contact;
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
public class ContactDeleteJob extends Job {
    public static final int PROCESS_ID = 23;
    private JsonRequestUtils.HttpResponseWrapper<Contact> response;
    private String contactId;
    private String contactRefId;
    private String url;

    public ContactDeleteJob(String contactRefId, String contactId, String url) {
        super(new Params(Priority.HIGH).requireNetwork().persist());
        this.contactId = contactId;
        this.contactRefId = contactRefId;
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
        JsonRequestUtils request = new JsonRequestUtils(new Formatter().format(url + ESalesUri.UPDATE_CONTACT, contactRefId).toString());

        ContactDatabaseAdapter contactDatabaseAdapter = new ContactDatabaseAdapter(SignageAppication.getInstance());
        Contact contact = contactDatabaseAdapter.findContactById(contactId);
        contact.setId(contactRefId);

        response = request.delete(contact, new TypeReference<Contact>() {});
        HttpResponse r = response.getHttpResponse();

        Log.d(getClass().getSimpleName(), "Status Code: " + r.getStatusLine().getStatusCode());

        if (r.getStatusLine().getStatusCode() == HttpStatus.SC_NO_CONTENT) {
            Log.d(getClass().getSimpleName(), "1.1.1.Response Code :" + r.getStatusLine().getStatusCode() + "Entity ID :" + contactId + "Ref ID :" + contactRefId);
            EventBus.getDefault().post(new GenericEvent.RequestSuccess(PROCESS_ID, response, contactRefId, contactId));
        } else {
            Log.d(getClass().getSimpleName(), "2.2.2.Response Code :" + r.getStatusLine().getStatusCode() + " " + r.getStatusLine().getReasonPhrase());
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
