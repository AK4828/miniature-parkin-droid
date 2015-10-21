package com.hoqii.sales.selfservice.job;

import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hoqii.sales.selfservice.entity.Payment;
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
public class PayUpdateJob extends Job {
    public static final int PROCESS_ID = 21;
    private JsonRequestUtils.HttpResponseWrapper<Payment> response;
    private String orderId;
    private String url;

    public PayUpdateJob(String orderId, String url) {
        super(new Params(Priority.HIGH).requireNetwork().persist());
        this.orderId = orderId;
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

        Payment payment = new Payment();
        payment.getOrder().setId(orderId);
        payment.setStatus(Payment.PaymentStatus.PAID);

        Log.d(getClass().getSimpleName(), "Order Id:" + orderId);
        response = request.post(payment, new TypeReference<Payment>() {});
        HttpResponse r = response.getHttpResponse();

        if (r.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            Payment pmn = response.getContent();

            Log.d(getClass().getSimpleName(), "Response Code :" + r.getStatusLine().getStatusCode() + "Entity ID :" + orderId + "Ref ID :" + pmn.getId());
            EventBus.getDefault().post(new GenericEvent.RequestSuccess(PROCESS_ID, response, pmn.getId(), orderId));
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
