package com.meruvian.pxc.selfservice.service;

import com.meruvian.pxc.selfservice.entity.Authentication;

import java.util.Map;

import retrofit.Call;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.QueryMap;

/**
 * Created by akm on 07/01/16.
 */
public interface LoginService {

    @POST("/oauth/token")
    Call<Authentication> requestTokenFxpc(@Header("Authorization") String authorization, @Header("Host") String host, @QueryMap Map<String,String> queryParam);
}
