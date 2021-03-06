package org.meruvian.esales.collector.job;

import android.content.SharedPreferences;
import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.meruvian.esales.collector.SignageAppication;
import org.meruvian.esales.collector.SignageVariables;
import org.meruvian.esales.collector.core.commons.Role;
import org.meruvian.esales.collector.core.commons.Site;
import org.meruvian.esales.collector.core.commons.User;
import org.meruvian.esales.collector.core.commons.UserRole;
import org.meruvian.esales.collector.entity.Authentication;
import org.meruvian.esales.collector.entity.PageEntity;
import org.meruvian.esales.collector.event.LoginEvent;
import org.meruvian.esales.collector.util.AuthenticationUtils;
import org.meruvian.esales.collector.util.JsonRequestUtils;

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

            AuthenticationUtils.registerAuthentication(authentication);
            Log.i(getClass().getSimpleName(), "ACCESS_TOKEN : " + authentication.getAccessToken());

            EventBus.getDefault().post(new LoginEvent.LoginSuccess(responseWrapper.getContent()));
        } else {
            EventBus.getDefault().post(new LoginEvent.LoginFailed(response.getStatusLine().getStatusCode()));
        }
    }

    protected User requestUser() {
        preferences = SignageAppication.getInstance().getSharedPreferences(SignageVariables.PREFS_SERVER, 0);
        JsonRequestUtils requestUtils = new JsonRequestUtils(preferences.getString("server_url", "") + SignageVariables.PGA_CURRENT_ME);
        return requestUtils.get(new TypeReference<User>() {}).getContent();
    }

    protected Site requestSite() {
        preferences = SignageAppication.getInstance().getSharedPreferences(SignageVariables.PREFS_SERVER, 0);
        JsonRequestUtils requestUtils = new JsonRequestUtils(preferences.getString("server_url", "") + SignageVariables.PGA_CURRENT_SITE);
        return requestUtils.get(new TypeReference<Site>() {}).getContent();
    }

    protected PageEntity<UserRole> requestRoles() {
        preferences = SignageAppication.getInstance().getSharedPreferences(SignageVariables.PREFS_SERVER, 0);
        JsonRequestUtils requestUtils = new JsonRequestUtils(preferences.getString("server_url", "") + SignageVariables.PGA_CURRENT_ROLE);
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
