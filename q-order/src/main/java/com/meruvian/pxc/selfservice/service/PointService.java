package com.meruvian.pxc.selfservice.service;

import com.meruvian.pxc.selfservice.entity.MainBody;
import com.meruvian.pxc.selfservice.entity.Point;
import com.meruvian.pxc.selfservice.entity.Transaction;

import java.util.Map;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.QueryMap;

/**
 * Created by akm on 02/02/16.
 */
public interface PointService {
    @GET("/api/points/partner")
    Call<Point>getUserPoint(@QueryMap Map<String, String> param);

    @GET("/api/points/me")
    Call<Point>getUserPointPXC(@QueryMap Map<String, String> param);

    @POST("/api/points/transaction")
    Call<Transaction>sendOrderStatus(@Body Transaction transaction);

}
