package com.meruvian.pxc.selfservice.job;

import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.meruvian.pxc.selfservice.entity.OrderMenu;
import com.meruvian.pxc.selfservice.event.GenericEvent;
import com.meruvian.pxc.selfservice.util.JsonRequestUtils;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.meruvian.midas.core.job.Priority;

import de.greenrobot.event.EventBus;

/**
 * Created by akm on 07/01/16.
 */
public class MenuUpdateJob extends Job {

    public static final int PROCESS_ID = 57;
    private JsonRequestUtils.HttpResponseWrapper<OrderMenu> responseUpdate;
    private JsonRequestUtils.HttpResponseWrapper<OrderMenu> responseGetOrderMenu;
    private String url, orderId, orderMenuId;


    public MenuUpdateJob(String url, String orderId, String orderMenuId) {
        super(new Params(Priority.HIGH).requireNetwork().persist());

        this.url = url;
        this.orderId = orderId;
        this.orderMenuId = orderMenuId;
        Log.d(getClass().getSimpleName(), "url : " + url + " order id " + orderId + " order menu id : " + orderMenuId);

    }

    @Override
    public void onAdded() {
        EventBus.getDefault().post(new GenericEvent.RequestInProgress(PROCESS_ID));
    }

    @Override
    public void onRun() throws Throwable {
        Log.d(getClass().getSimpleName(), "Update orde menu running");
        JsonRequestUtils request = new JsonRequestUtils(url + "/api/orders/" + orderId + "/menu/" + orderMenuId );

        responseGetOrderMenu = request.get(new TypeReference<OrderMenu>() {
        });

        HttpResponse rOm = responseGetOrderMenu.getHttpResponse();
        Log.d(getClass().getSimpleName(), "response : " + rOm.getStatusLine().getStatusCode());
        Log.d(getClass().getSimpleName(), "response : " + rOm.getStatusLine().getReasonPhrase());


        if (rOm.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            Log.d(getClass().getSimpleName(), "order menu Response Code :" + rOm.getStatusLine().getStatusCode());
            OrderMenu orderMenu = responseGetOrderMenu.getContent();

            Log.d(getClass().getSimpleName(), "order menu id : " + orderMenu.getId().toString());
            Log.d(getClass().getSimpleName(), "order menu data : " + orderMenu.toString());
            Log.d(getClass().getSimpleName(), "order menu data : " + String.valueOf(orderMenu));


//====================
            Log.d(getClass().getSimpleName(), "Updating order menu running");
            JsonRequestUtils requestUpdate = new JsonRequestUtils(url + "/api/orders/" + orderId + "/menu/" + orderMenuId );

            orderMenu.setStatus(OrderMenu.OrderMenuStatus.SHIPPED);

            Log.d(getClass().getSimpleName(), "data order menu " + orderMenu.toString());
            Log.d(getClass().getSimpleName(), "data order menu id" + orderMenu.getId());
            Log.d(getClass().getSimpleName(), "data order menu status" + orderMenu.getStatus().name().toString());

            responseUpdate = requestUpdate.put(orderMenu, new TypeReference<OrderMenu>() {
            });

            HttpResponse rUm = responseUpdate.getHttpResponse();
            Log.d(getClass().getSimpleName(), "response update : " + rUm.getStatusLine().getStatusCode());
            Log.d(getClass().getSimpleName(), "response update : " + rUm.getStatusLine().getReasonPhrase());
            if (rUm.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                Log.d(getClass().getSimpleName(), "update Response Code :" + rUm.getStatusLine().getStatusCode());
                OrderMenu om = responseUpdate.getContent();

                Log.d(getClass().getSimpleName(), "order menu id : " + om.getId().toString());

                EventBus.getDefault().post(new GenericEvent.RequestSuccess(PROCESS_ID, responseUpdate, om.getRefId(), om.getId()));
            } else {
                Log.d(getClass().getSimpleName(), "update Response Code :" + rUm.getStatusLine().getStatusCode());
                EventBus.getDefault().post(new GenericEvent.RequestFailed(PROCESS_ID, responseUpdate));
            }

            EventBus.getDefault().post(new GenericEvent.RequestSuccess(PROCESS_ID, responseUpdate, orderMenu.getRefId(), orderMenu.getId()));
        } else {
            Log.d(getClass().getSimpleName(), "get order menu Response Code :" + rOm.getStatusLine().getStatusCode());
            EventBus.getDefault().post(new GenericEvent.RequestFailed(PROCESS_ID, responseUpdate));
        }

    }

    @Override
    protected void onCancel() {
        EventBus.getDefault().post(new GenericEvent.RequestFailed(PROCESS_ID, responseUpdate));
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }
}
