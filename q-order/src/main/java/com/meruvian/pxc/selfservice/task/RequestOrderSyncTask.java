package com.meruvian.pxc.selfservice.task;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.meruvian.pxc.selfservice.R;
import com.meruvian.pxc.selfservice.SignageVariables;
import com.meruvian.pxc.selfservice.content.database.adapter.OrderDatabaseAdapter;
import com.meruvian.pxc.selfservice.entity.Order;
import com.meruvian.pxc.selfservice.util.AuthenticationUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.meruvian.midas.core.service.TaskService;
import org.meruvian.midas.core.util.ConnectionUtil;

/**
 * Created by ludviantoovandi on 28/01/15.
 */
public class RequestOrderSyncTask extends AsyncTask<String, Void, JSONObject> {
    private Context context;
    private TaskService service;
    private SharedPreferences preferences;
    private OrderDatabaseAdapter orderDbAdapter;
    private String orderId;

    public RequestOrderSyncTask(Context context, TaskService service, String orderId) {
        this.context = context;
        this.service = service;
        this.orderId = orderId;


        preferences = context.getSharedPreferences(SignageVariables.PREFS_SERVER, 0);
        orderDbAdapter = new OrderDatabaseAdapter(context);
    }

    @Override
    protected void onCancelled() {
        service.onCancel(SignageVariables.REQUEST_ORDER, context.getString(R.string.cancel));
    }

    @Override
    protected void onPreExecute() {
        service.onExecute(SignageVariables.REQUEST_ORDER);
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        Log.d(getClass().getSimpleName(), "?access_token= " + AuthenticationUtils.getCurrentAuthentication().getAccessToken());
        return ConnectionUtil.get(preferences.getString("server_url_point", "") + "/api/orders/receiptnumber?access_token="
                + AuthenticationUtils.getCurrentAuthentication().getAccessToken());
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        try {
            if (result != null) {
                JSONObject json = result;

                Log.d(getClass().getSimpleName(), "JSON : " + json.toString());

                Order order = new Order();

                order.setId(orderId);
                order.setRefId(json.getString("id"));
                order.setReceiptNumber(json.getString("receiptNumber"));

                orderDbAdapter.updateContactOrder(order);

                service.onSuccess(SignageVariables.REQUEST_ORDER, orderId);
            } else {
                service.onError(SignageVariables.REQUEST_ORDER, context.getString(R.string.error));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            service.onError(SignageVariables.REQUEST_ORDER, context.getString(R.string.error));
        }
    }
}
