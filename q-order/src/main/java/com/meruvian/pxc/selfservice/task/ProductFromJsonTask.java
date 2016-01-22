package com.meruvian.pxc.selfservice.task;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.meruvian.pxc.selfservice.R;
import com.meruvian.pxc.selfservice.SignageVariables;
import com.meruvian.pxc.selfservice.entity.Category;
import com.meruvian.pxc.selfservice.entity.Product;
import com.meruvian.pxc.selfservice.entity.ProductUom;
import com.meruvian.pxc.selfservice.util.AuthenticationUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.meruvian.midas.core.service.TaskService;
import org.meruvian.midas.core.util.ConnectionUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akm on 11/12/15.
 */
public class ProductFromJsonTask extends AsyncTask<String, Void, JSONObject> {

    private TaskService taskService;
    private Context context;
    private SharedPreferences preferences;
    private List<Product> products = new ArrayList<Product>();
    private List<String> idProducts = new ArrayList<String>();

    public ProductFromJsonTask(Context context, TaskService service) {
        this.taskService = service;
        this.context = context;

        preferences = context.getSharedPreferences(SignageVariables.PREFS_SERVER, 0);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        taskService.onExecute(SignageVariables.PRODUCT_GET_TASK);
    }

    @Override
    protected JSONObject doInBackground(String... strings) {
        Log.d(getClass().getSimpleName(), "?access_token= " + AuthenticationUtils.getCurrentAuthentication().getAccessToken());
        return ConnectionUtil.get(preferences.getString("server_url_point", "") + "/api/products?access_token="
                + AuthenticationUtils.getCurrentAuthentication().getAccessToken());
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        super.onPostExecute(result);
        try {
            if (result != null) {

                JSONArray jsonArray = result.getJSONArray("content");
                for (int a = 0; a < jsonArray.length(); a++) {
                    JSONObject json = jsonArray.getJSONObject(a);

                    Log.d(getClass().getSimpleName(), "JSON PROD: " + json.toString());

                    Category parentCategory = new Category();
                    if (!json.isNull("parentCategory")) {
                        parentCategory.setId(json.getJSONObject("parentCategory").getString("id"));
                    }

                    Category category = new Category();
                    if (!json.isNull("category")) {
                        category.setId(json.getJSONObject("category").getString("id"));
                    }

                    ProductUom uom = new ProductUom();
                    if (!json.isNull("uom")) {
                        uom.setId(json.getJSONObject("uom").getString("id"));
                    }

                    Product product = new Product();
                    product.setId(json.getString("id"));
                    product.setName(json.getString("name"));
                    if (!json.isNull("sellPrice")) {
                        product.setSellPrice(json.getLong("sellPrice"));
                    } else {
                        product.setSellPrice(0);
                    }
                    product.setParentCategory(parentCategory);
                    product.setCategory(category);
                    product.setUom(uom);
                    product.setCode(json.getString("code"));
                    product.setDescription(json.getString("description"));

                    products.add(product);

                    product.setImage(1);

                    if(idProducts.contains(product.getId())) {
                        idProducts.remove(product.getId());
                    }
                }
                taskService.onSuccess(SignageVariables.PRODUCT_GET_TASK, true);
            } else {
                taskService.onError(SignageVariables.PRODUCT_GET_TASK, context.getString(R.string.error));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            taskService.onError(SignageVariables.PRODUCT_GET_TASK, context.getString(R.string.error));
        }
    }
}
