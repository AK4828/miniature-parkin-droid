package com.meruvian.pxc.selfservice.job;

import android.content.SharedPreferences;
import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.meruvian.pxc.selfservice.SignageAppication;
import com.meruvian.pxc.selfservice.SignageVariables;
import com.meruvian.pxc.selfservice.core.commons.Role;
import com.meruvian.pxc.selfservice.core.commons.Site;
import com.meruvian.pxc.selfservice.core.commons.User;
import com.meruvian.pxc.selfservice.core.commons.UserRole;
import com.meruvian.pxc.selfservice.entity.Authentication;
import com.meruvian.pxc.selfservice.entity.PageEntity;
import com.meruvian.pxc.selfservice.event.LoginEvent;
import com.meruvian.pxc.selfservice.util.AuthenticationUtils;
import com.meruvian.pxc.selfservice.util.JsonRequestUtils;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by meruvian on 29/07/15.
 */
public abstract class LoginJob extends Job {
    private SharedPreferences preferences;

    protected LoginJob(Params params) {
        super(params);
    }

    @Override
    public void onAdded() {
        EventBus.getDefault().post(new LoginEvent.DoLogin());
    }

    protected void registerAuthentication(JsonRequestUtils.HttpResponseWrapper<Authentication> responseWrapper) {
        HttpResponse response = responseWrapper.getHttpResponse();

        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            Authentication authentication = responseWrapper.getContent();
            AuthenticationUtils.registerAuthentication(authentication);
            User user = requestUser();
            Site site = requestSite();

            user.setRoles(new ArrayList<Role>());
            for (UserRole userRole : requestRoles().getContent()) {
                user.getRoles().add(userRole.getRole());
            }
            authentication.setUser(user);
            authentication.setSite(site);

            long loginTime = System.currentTimeMillis();
            authentication.setLoginTime(loginTime);

            AuthenticationUtils.registerAuthentication(authentication);
            Log.i(getClass().getSimpleName(), "ACCESS_TOKEN : " + authentication.getAccessToken());

            EventBus.getDefault().post(new LoginEvent.LoginSuccess(responseWrapper.getContent()));
        } else {
            EventBus.getDefault().post(new LoginEvent.LoginFailed(response.getStatusLine().getStatusCode()));
        }
    }

    protected User requestUser() {
        preferences = SignageAppication.getInstance().getSharedPreferences(SignageVariables.PREFS_SERVER, 0);
        JsonRequestUtils requestUtils = new JsonRequestUtils(preferences.getString("server_url_point", "") + SignageVariables.PGA_CURRENT_ME);
        return requestUtils.get(new TypeReference<User>() {}).getContent();
    }

    protected Site requestSite() {
        preferences = SignageAppication.getInstance().getSharedPreferences(SignageVariables.PREFS_SERVER, 0);
        JsonRequestUtils requestUtils = new JsonRequestUtils(preferences.getString("server_url_point", "") + SignageVariables.PGA_CURRENT_SITE);
        return requestUtils.get(new TypeReference<Site>() {}).getContent();
    }

    protected PageEntity<UserRole> requestRoles() {
        preferences = SignageAppication.getInstance().getSharedPreferences(SignageVariables.PREFS_SERVER, 0);
        JsonRequestUtils requestUtils = new JsonRequestUtils(preferences.getString("server_url_point", "") + SignageVariables.PGA_CURRENT_ROLE);
        return requestUtils.get(new TypeReference<PageEntity<UserRole>>() {}).getContent();
    }

    @Override
    protected void onCancel() {
        EventBus.getDefault().post(new LoginEvent.LoginFailed(0));
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        Log.e(LoginJob.class.getSimpleName(), throwable.getMessage(), throwable);

        return false;
    }
}
