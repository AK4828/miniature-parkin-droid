package com.hoqii.sales.selfservice.task;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.hoqii.sales.selfservice.R;
import com.hoqii.sales.selfservice.SignageVariables;
import com.hoqii.sales.selfservice.content.database.adapter.ProductDatabaseAdapter;
import com.hoqii.sales.selfservice.entity.Category;
import com.hoqii.sales.selfservice.entity.Product;
import com.hoqii.sales.selfservice.entity.ProductUom;
import com.hoqii.sales.selfservice.util.AuthenticationUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.meruvian.midas.core.service.TaskService;
import org.meruvian.midas.core.util.ConnectionUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ludviantoovandi on 28/01/15.
 */
public class ProductSyncTask extends AsyncTask<Void, Void, JSONObject> {
    private Context context;
    private TaskService service;

    private String parentCategory = null;
    private String category = null;

    private ProductDatabaseAdapter productAdapter;
    private List<String> idProducts = new ArrayList<String>();
    private SharedPreferences preferences;

    public ProductSyncTask(Context context, TaskService service) {
        this.context = context;
        this.service = service;

        preferences = context.getSharedPreferences(SignageVariables.PREFS_SERVER, 0);
        productAdapter = new ProductDatabaseAdapter(context);
        idProducts = productAdapter.getAllProductId();
    }

    @Override
    protected void onCancelled() {
        service.onCancel(SignageVariables.PRODUCT_GET_TASK, context.getString(R.string.cancel));
    }

    @Override
    protected void onPreExecute() {
        service.onExecute(SignageVariables.PRODUCT_GET_TASK);
    }

    @Override
    protected JSONObject doInBackground(Void... arg0) {
        Log.d(getClass().getSimpleName(), "?access_token= " + AuthenticationUtils.getCurrentAuthentication().getAccessToken());
        return ConnectionUtil.get(preferences.getString("server_url", "") + "/api/products?access_token=" + AuthenticationUtils.getCurrentAuthentication().getAccessToken());
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        try {
            if (result != null) {
                List<Product> products = new ArrayList<Product>();

                JSONArray jsonArray = result.getJSONArray("content");
                for (int a = 0; a < jsonArray.length(); a++) {
                    JSONObject json = jsonArray.getJSONObject(a);

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

                productAdapter.saveProduct(products);
                productAdapter.voidProduct(idProducts);

                service.onSuccess(SignageVariables.PRODUCT_GET_TASK, true);
            } else {
                service.onError(SignageVariables.PRODUCT_GET_TASK, context.getString(R.string.error));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            service.onError(SignageVariables.PRODUCT_GET_TASK, context.getString(R.string.error));
        }
    }

    /*public String getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(String parentCategory) {
        this.parentCategory = parentCategory;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    private List<Product> phase1(JSONArray jsonArray) throws JSONException{
        List<Product> products = new ArrayList<Product>();

        for (int a = 0; a < jsonArray.length(); a++) {
            JSONObject json = jsonArray.getJSONObject(a);

            Category parentCategory = new Category();
            parentCategory.setId(json.getJSONObject(
                    "parentCategory").getString("id"));

            JSONObject parentCategoryJson = json.getJSONObject("parentCategory");
            if (parentCategoryJson.getString("id").equalsIgnoreCase(getParentCategory())) {
//                Category category = new Category();
//                if (!json.isNull("category")) {
//                    category.setId(json.getJSONObject("category")
//                            .getString("id"));
//                }

                Product product = new Product();
                product.setId(json.getString("id"));
                product.setName(json.getString("name"));
//                product.setParentCategory(parentCategory);
//                product.setCategory(category);

                if (!json.isNull("sellPrice")) {
                    product.setSellPrice(json.getLong("sellPrice"));
                } else {
                    product.setSellPrice(0);
                }

                products.add(product);
            }
        }

        return products;
    }

    private List<Product> phase2(JSONArray jsonArray) throws JSONException{
        List<Product> products = new ArrayList<Product>();

        for (int a = 0; a < jsonArray.length(); a++) {
            JSONObject json = jsonArray.getJSONObject(a);

            if (!json.isNull("category")) {
                JSONObject categoryJson = json.getJSONObject("category");
                if (categoryJson.getString("id").equalsIgnoreCase(getCategory())) {

                    Product product = new Product();
                    product.setId(json.getString("id"));
                    product.setName(json.getString("name"));

                    if (!json.isNull("sellPrice")) {
                        product.setSellPrice(json.getLong("sellPrice"));
                    } else {
                        product.setSellPrice(0);
                    }

                    products.add(product);
                }
            }
        }

        return products;
    }*/

    /*List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("site", SignageVariables.SITE_ID));
        nameValuePairs.add(new BasicNameValuePair("token", SignageVariables.TOKEN));*/

    //                JSONArray jsonArray = result.getJSONArray("entityList");
//                if (getParentCategory() != null && getCategory() == null) {
//                    service.onSuccess(SignageVariables.PRODUCT_GET_TASK, phase1(jsonArray));
//                } else if (getCategory() != null && getParentCategory() == null) {
//                    service.onSuccess(SignageVariables.PRODUCT_GET_TASK, phase2(jsonArray));
//                }


}