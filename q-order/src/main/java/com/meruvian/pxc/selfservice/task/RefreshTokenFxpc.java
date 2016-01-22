package com.meruvian.pxc.selfservice.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;

import com.meruvian.pxc.selfservice.SignageAppication;
import com.meruvian.pxc.selfservice.SignageVariables;
import com.meruvian.pxc.selfservice.entity.Authentication;
import com.meruvian.pxc.selfservice.service.LoginService;
import com.meruvian.pxc.selfservice.util.AuthenticationUtils;

import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.meruvian.midas.core.service.TaskService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit.Call;
import retrofit.Retrofit;

/**
 * Created by akm on 14/01/16.
 */
public class RefreshTokenFxpc extends AsyncTask<String, Void, String> {

    private Context context;
    private TaskService service;
    private Authentication authentication;

    public RefreshTokenFxpc(Context context, TaskService service){
        this.context = context;
        this.service = service;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        service.onExecute(SignageVariables.FXPC_REFRESH_TOKEN_TASK);
    }

    @Override
    protected String doInBackground(String... strings) {
        Retrofit retrofit = SignageAppication.getInstance().getRetrofit();
        LoginService loginService = retrofit.create(LoginService.class);

        authentication = AuthenticationUtils.getCurrentAuthentication();

        Map<String, String> param = new HashMap<>();

        param.put("grant_type", GrantType.REFRESH_TOKEN.toString());
        param.put("redirect_uri", SignageVariables.FXPC_CALLBACK);
        param.put("client_id", SignageVariables.FXPC_APP_ID);
        param.put("client_secret", SignageVariables.FXPC_APP_SECRET);
        param.put("scope", "read write");
        param.put("code", strings[0]);

        String authorization = new String(Base64.encode((SignageVariables.FXPC_APP_ID + ":" + SignageVariables.FXPC_APP_SECRET).getBytes(), Base64.NO_WRAP));

        Call<Authentication> callAuth = loginService.requestTokenFxpc("Basic " + authorization, "fxpc.demo.meruvian.org", param);

        try {
            authentication = callAuth.execute().body();
            AuthenticationUtils.registerAuthentication(authentication);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return authentication.getAccessToken();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        service.onSuccess(SignageVariables.FXPC_REQUEST_TOKEN_TASK, s);
    }
}
