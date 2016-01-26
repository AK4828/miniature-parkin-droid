package com.meruvian.pxc.selfservice.service;

import com.meruvian.pxc.selfservice.entity.MainBody;
import com.meruvian.pxc.selfservice.entity.Product;

import java.util.Map;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;

/**
 * Created by akm on 22/01/16.
 */
public interface ProductService {
    @GET("/api/products")
    Call<MainBody<Product>>getAllProducts(@QueryMap Map<String, String> param, @QueryMap Map<String, String> limit);

    @GET("/api/products/{id}")
    Call<MainBody<Product>>getProductById(@Path("id") String id);
}
