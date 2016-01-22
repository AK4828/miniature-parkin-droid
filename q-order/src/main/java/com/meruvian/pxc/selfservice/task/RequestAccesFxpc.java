package com.meruvian.pxc.selfservice.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.meruvian.pxc.selfservice.R;
import com.meruvian.pxc.selfservice.SignageVariables;

import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.meruvian.midas.core.service.TaskService;

/**
 * Created by akm on 07/01/16.
 */
public class RequestAccesFxpc extends AsyncTask<Void, Void, String> {

    private Context context;
    private TaskService taskService;

    public RequestAccesFxpc(Context context, TaskService service) {
        this.context = context;
        this.taskService = service;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        taskService.onExecute(SignageVariables.FXPC_REQUEST_ACCESS);
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            OAuthClientRequest request = OAuthClientRequest.authorizationLocation(SignageVariables.FXPC_AUTH_URL)
                    .setResponseType(ResponseType.CODE.toString())
                    .setClientId(SignageVariables.FXPC_APP_ID)
                    .setRedirectURI(SignageVariables.FXPC_CALLBACK)
                    .setScope("read write")
                    .buildQueryMessage();

            Log.d(getClass().getSimpleName(), "URI Request Access YAMAId: " + request.getLocationUri());

            return request.getLocationUri();
        } catch (OAuthSystemException e) {
            e.printStackTrace();
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(String string) {
        if (string != null) {
            taskService.onSuccess(SignageVariables.FXPC_REQUEST_ACCESS, string);
        } else {
            taskService.onError(SignageVariables.FXPC_REQUEST_ACCESS, context.getString(R.string.failed_recieve));
        }
    }
}
