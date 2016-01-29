package com.meruvian.pxc.selfservice.job;

import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.meruvian.pxc.selfservice.SignageAppication;
import com.meruvian.pxc.selfservice.SignageVariables;
import com.meruvian.pxc.selfservice.entity.Authentication;
import com.meruvian.pxc.selfservice.event.LoginEvent;
import com.meruvian.pxc.selfservice.util.AuthenticationUtils;
import com.meruvian.pxc.selfservice.util.JsonRequestUtils;
import com.path.android.jobqueue.Params;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.meruvian.midas.core.job.Priority;

import de.greenrobot.event.EventBus;

/**
 * Created by akm on 19/01/16.
 */
public class RefreshTokenJob extends LoginJob {

    private SharedPreferences preferences;

    public RefreshTokenJob() {
        super(new Params(Priority.HIGH).requireNetwork().persist());
    }


    @Override
    public void onRun() throws Throwable {
        preferences = SignageAppication.getInstance().getSharedPreferences(SignageVariables.PREFS_SERVER, 0);
        Log.d(getClass().getSimpleName(), preferences.getString("server_url_point", ""));

        JsonRequestUtils requestUtils = new JsonRequestUtils(preferences.getString("server_url_point", "") + SignageVariables.PGA_REQUEST_TOKEN);
        requestUtils.addQueryParam("grant_type", String.valueOf(GrantType.REFRESH_TOKEN));
        requestUtils.addQueryParam("client_id", SignageVariables.PGA_APP_ID);
        requestUtils.addQueryParam("client_secret", SignageVariables.PGA_API_SECRET);
        requestUtils.addQueryParam("refresh_token", AuthenticationUtils.getCurrentAuthentication().getRefreshToken());

        String authorization = SignageVariables.PGA_APP_ID + ":" + SignageVariables.PGA_API_SECRET;
        authorization = Base64.encodeToString(authorization.getBytes(), Base64.DEFAULT);

        requestUtils.addHeader("Authorization", "Basic " + authorization);

        JsonRequestUtils.HttpResponseWrapper<Authentication> responseWrapper =
                requestUtils.post(null, new TypeReference<Authentication>() {});
        HttpResponse response = responseWrapper.getHttpResponse();

        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            registerAuthentication(responseWrapper);
        } else {
            Log.e(RefreshTokenJob.class.getSimpleName(), "Access Code: " + response.getStatusLine().getStatusCode() + " " +response.getStatusLine().getReasonPhrase());
            EventBus.getDefault().post(new LoginEvent.LoginFailed(response.getStatusLine().getStatusCode()));
        }
    }
}
