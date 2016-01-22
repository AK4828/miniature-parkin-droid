package com.meruvian.pxc.selfservice.task;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.meruvian.pxc.selfservice.R;
import com.meruvian.pxc.selfservice.SignageVariables;
import com.meruvian.pxc.selfservice.content.database.adapter.ProductDatabaseAdapter;
import com.meruvian.pxc.selfservice.content.database.adapter.ProductStoreDatabaseAdapter;
import com.meruvian.pxc.selfservice.entity.Category;
import com.meruvian.pxc.selfservice.entity.Product;
import com.meruvian.pxc.selfservice.entity.ProductStore;
import com.meruvian.pxc.selfservice.entity.ProductUom;
import com.meruvian.pxc.selfservice.util.AuthenticationUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.meruvian.midas.core.service.TaskService;
import org.meruvian.midas.core.util.ConnectionUtil;

/**
 * Created by ludviantoovandi on 28/01/15.
 */
public class ProductStoreSyncTask extends AsyncTask<Integer, Void, JSONObject> {
    private Context context;
    private TaskService service;

    private String parentCategory = null;
    private String category = null;

    private ProductStoreDatabaseAdapter productStoreDbAdapter;
    private ProductDatabaseAdapter productDatabaseAdapter;
    private int totalPages = 0;
    private int page;
    
//    private List<String> idProductStores = new ArrayList<String>();
//    private List<String> idProducts = new ArrayList<String>();

    private SharedPreferences preferences;

    public ProductStoreSyncTask(Context context, TaskService service, int page) {
        this.context = context;
        this.service = service;
        this.page = page;

        Log.d(getClass().getSimpleName(), "Page: " + page);

        preferences = context.getSharedPreferences(SignageVariables.PREFS_SERVER, 0);
        productStoreDbAdapter = new ProductStoreDatabaseAdapter(context);
        productDatabaseAdapter = new ProductDatabaseAdapter(context);
//        idProductStores = productStoreDbAdapter.getAllProductStoreId();
//        idProducts = productDatabaseAdapter.getAllProductId();
        if (page == 0) {
            productDatabaseAdapter.voidAllProducts();
            productStoreDbAdapter.voidAllProductStores();
        }

    }

    @Override
    protected void onCancelled() {
        service.onCancel(SignageVariables.PRODUCT_STORE_GET_TASK, context.getString(R.string.cancel));
    }

    @Override
    protected void onPreExecute() {
        service.onExecute(SignageVariables.PRODUCT_STORE_GET_TASK);
    }

    @Override
    protected JSONObject doInBackground(Integer... params) {
        Log.d(getClass().getSimpleName(), "?access_token= " + AuthenticationUtils.getCurrentAuthentication().getAccessToken());
        return ConnectionUtil.get(preferences.getString("server_url_point", "") + "/api/product/store?access_token="
                + AuthenticationUtils.getCurrentAuthentication().getAccessToken()+ "&page=" + page);

    }

    @Override
    protected void onPostExecute(JSONObject result) {
        try {
            if (result != null) {
//                List<ProductStore> productStores = new ArrayList<ProductStore>();
//                List<Product> products = new ArrayList<Product>();

                totalPages = result.getInt("totalPages");
                JSONArray jsonArray = result.getJSONArray("content");
                for (int a = 0; a < jsonArray.length(); a++) {
                    JSONObject json = jsonArray.getJSONObject(a);

                    ProductStore productStore = new ProductStore();
                    productStore.setId(json.getString("id"));
                    productStore.setSellPrice(json.getDouble("sellPrice"));

                    JSONObject jsonProduct = json.getJSONObject("product");

                    Category parentCategory = new Category();
                    if (!jsonProduct.isNull("parentCategory")) {
                        parentCategory.setId(jsonProduct.getJSONObject("parentCategory").getString("id"));
                    }

                    Category category = new Category();
                    if (!jsonProduct.isNull("category")) {
                        category.setId(jsonProduct.getJSONObject("category").getString("id"));
                    }

                    ProductUom uom = new ProductUom();
                    if (!jsonProduct.isNull("uom")) {
                        uom.setId(jsonProduct.getJSONObject("uom").getString("id"));
                    }

                    Product product = new Product();
                    product.setId(jsonProduct.getString("id"));
                    product.setName(jsonProduct.getString("name"));
                    if (!jsonProduct.isNull("sellPrice")) {
                        product.setSellPrice(jsonProduct.getLong("sellPrice"));
                    } else {
                        product.setSellPrice(0);
                    }
                    product.setParentCategory(parentCategory);
                    product.setCategory(category);
                    product.setUom(uom);
                    product.setCode(jsonProduct.getString("code"));
                    product.setDescription(jsonProduct.getString("description"));
                    product.setImage(1);

                    productStore.setProduct(product);
//                    productStores.add(productStore);
//                    products.add(product);

                    productStoreDbAdapter.saveProductStore(productStore);
                    productDatabaseAdapter.saveProduct(product);
                }

//                productStoreDbAdapter.saveProductStores(productStores);
//                productDatabaseAdapter.saveProduct(products);

                service.onSuccess(SignageVariables.PRODUCT_STORE_GET_TASK, totalPages);
            } else {
                service.onError(SignageVariables.PRODUCT_STORE_GET_TASK, context.getString(R.string.error));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            service.onError(SignageVariables.PRODUCT_STORE_GET_TASK, context.getString(R.string.error));
        }
    }
}