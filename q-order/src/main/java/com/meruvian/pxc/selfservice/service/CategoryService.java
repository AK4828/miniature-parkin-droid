package com.meruvian.pxc.selfservice.service;

import com.meruvian.pxc.selfservice.entity.Category;
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
public interface CategoryService {

    @GET("/api/product/categories")
    Call<MainBody<Category>>getAllCategories(@QueryMap Map<String, String> token);

    @GET("/api/product/categories/{id}")
    Call<MainBody<Category>>getCategoryById(@Path("id") String id);
}
