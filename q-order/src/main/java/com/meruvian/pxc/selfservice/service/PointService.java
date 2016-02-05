package com.meruvian.pxc.selfservice.service;

import com.meruvian.pxc.selfservice.entity.MainBody;
import com.meruvian.pxc.selfservice.entity.Point;

import java.util.Map;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.QueryMap;

/**
 * Created by akm on 02/02/16.
 */
public interface PointService {
    @GET("/api/points/me")
    Call<MainBody<Point>>getUserPoint(@QueryMap Map<String, String> param);

}
