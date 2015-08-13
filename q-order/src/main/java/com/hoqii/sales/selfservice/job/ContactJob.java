package com.hoqii.sales.selfservice.job;

import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hoqii.sales.selfservice.SignageAppication;
import com.hoqii.sales.selfservice.content.database.adapter.ContactDatabaseAdapter;
import com.hoqii.sales.selfservice.entity.Contact;
import com.hoqii.sales.selfservice.event.GenericEvent;
import com.hoqii.sales.selfservice.util.JsonRequestUtils;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.meruvian.midas.core.job.Priority;

import de.greenrobot.event.EventBus;

/**
 * Created by meruvian on 30/07/15.
 */
public class ContactJob extends Job {
    public static final int PROCESS_ID = 21;
    private JsonRequestUtils.HttpResponseWrapper<Contact> response;
    private String contactId;
    private String url;

    public ContactJob(String contactId, String url) {
        super(new Params(Priority.HIGH).requireNetwork().persist());
        this.contactId = contactId;
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
        JsonRequestUtils request = new JsonRequestUtils(url + HoqiiUri.CONTACT);

        ContactDatabaseAdapter contactDatabaseAdapter = new ContactDatabaseAdapter(SignageAppication.getInstance());
        Contact contact = contactDatabaseAdapter.findContactById(contactId);
        contact.setId(null);

        Log.d(getClass().getSimpleName(), "Contact Id:" + contact.getId());
        response = request.post(contact, new TypeReference<Contact>() {});
        HttpResponse r = response.getHttpResponse();

        if (r.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            Contact ct = response.getContent();
            contactDatabaseAdapter.updateSyncStatusById(contactId, ct.getId());

            Log.d(getClass().getSimpleName(), "Response Code :" + r.getStatusLine().getStatusCode() + "Entity ID :" + contactId + "Ref ID :" + ct.getId());
            EventBus.getDefault().post(new GenericEvent.RequestSuccess(PROCESS_ID, response, ct.getId(), contactId));
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
