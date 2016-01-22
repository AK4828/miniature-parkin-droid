package com.meruvian.pxc.selfservice.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

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
 * Created by akm on 07/01/16.
 */
public class RequestTokenFxpc extends AsyncTask<String, Void, String> {

    private Context context;
    private TaskService service;
    private Authentication authentication;

    public RequestTokenFxpc(Context context, TaskService service) {
        this.context = context;
        this.service = service;

    }

    @Override
    public void onPreExecute() {
        super.onPreExecute();
        service.onExecute(SignageVariables.FXPC_REQUEST_TOKEN_TASK);
    }

    @Override
    public String doInBackground(String... params) {

        Retrofit retrofit = SignageAppication.getInstance().getRetrofit();
        LoginService loginService = retrofit.create(LoginService.class);

        authentication = AuthenticationUtils.getCurrentAuthentication();

        Map<String, String> param = new HashMap<>();

        param.put("grant_type", GrantType.AUTHORIZATION_CODE.toString());
        param.put("redirect_uri", SignageVariables.FXPC_CALLBACK);
        param.put("client_id", SignageVariables.FXPC_APP_ID);
        param.put("client_secret", SignageVariables.FXPC_APP_SECRET);
        param.put("scope", "read write");
        param.put("code", params[0]);

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
    public void onPostExecute(String s) {
        Log.d("cek", s);
        service.onSuccess(SignageVariables.FXPC_REQUEST_TOKEN_TASK, s);
    }
}
