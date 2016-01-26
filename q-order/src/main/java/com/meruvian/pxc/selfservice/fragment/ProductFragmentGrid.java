package com.meruvian.pxc.selfservice.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.meruvian.pxc.selfservice.R;
import com.meruvian.pxc.selfservice.SignageAppication;
import com.meruvian.pxc.selfservice.activity.MainActivity;
import com.meruvian.pxc.selfservice.activity.MainActivityMaterial;
import com.meruvian.pxc.selfservice.activity.OrderActivity;
import com.meruvian.pxc.selfservice.adapter.ProductAdapter;
import com.meruvian.pxc.selfservice.content.database.adapter.OrderDatabaseAdapter;
import com.meruvian.pxc.selfservice.content.database.adapter.OrderMenuDatabaseAdapter;
import com.meruvian.pxc.selfservice.content.database.adapter.ProductDatabaseAdapter;
import com.meruvian.pxc.selfservice.entity.Category;
import com.meruvian.pxc.selfservice.entity.MainBody;
import com.meruvian.pxc.selfservice.entity.OrderMenu;
import com.meruvian.pxc.selfservice.entity.Product;
import com.meruvian.pxc.selfservice.entity.ProductUom;
import com.meruvian.pxc.selfservice.service.ProductService;
import com.meruvian.pxc.selfservice.util.AuthenticationUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by miftakhul on 11/16/15.
 */
public class ProductFragmentGrid extends Fragment {

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private GridView gridView;
    private ProductAdapter productAdapter;
    private ProductDatabaseAdapter productDbAdapter;
    private int tx = 2;
    private DecimalFormat decimalFormat = new DecimalFormat("#.###");
    private List<Product> productList = new ArrayList<Product>();
    private boolean isMinLoli = false;
    private List<String> idProducts = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            tx = getArguments().getInt("tx_id");
            Log.d("tx_id", Integer.toString(tx));
        }

        if (getArguments().getBoolean("mainActivityMaterial", false)){
            Log.d("argument",String.valueOf(getArguments().getBoolean("mainActivityMaterial", false)));
            tx = 2;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            isMinLoli = true;
        } else {
            isMinLoli = false;
        }

    }


    @Override
    public View onCreateView(final LayoutInflater infater, final ViewGroup container, Bundle savedInstanceState) {
        View view = infater.inflate(R.layout.fragment_product_grid, container, false);

        if (!imageLoader.isInited()) {
            imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        }
        productAdapter = new ProductAdapter(getActivity());
        productDbAdapter = new ProductDatabaseAdapter(getActivity());

        SignageAppication application = SignageAppication.getInstance();
        Map<String, String> param = new HashMap<>();
        param.put("access_token", AuthenticationUtils.getCurrentAuthentication().getAccessToken());


        gridView = (GridView) view.findViewById(R.id.grid_image);
        gridView.setAdapter(productAdapter);

        try {
            String limitData = "100";
            Map<String, String> limit = new HashMap<>();
            limit.put("max", limitData);
            ProductService productService = application.getRetrofit().create(ProductService.class);
            Call<MainBody<Product>> products = productService.getAllProducts(param, limit);
            products.enqueue(new Callback<MainBody<Product>>() {
                @Override
                public void onResponse(Response<MainBody<Product>> response, Retrofit retrofit) {
                    MainBody<Product> mainBody = response.body();
                    List<Product> pulledProducts = new ArrayList<Product>();
                    for (Product p : mainBody.getContent()) {
                        productAdapter.addProducts(p);
                        Log.d("cek null", String.valueOf(p.getCategory()));
                        productList.add(p);

                        Product product = new Product();
                        product.setId(p.getId());
                        product.setName(p.getName());
                        if (p.getSellPrice() >= 1) {
                            product.setSellPrice(p.getSellPrice());
                        } else {
                            product.setSellPrice(0);
                        }

                        Category parentCategory = new Category();
                        if (p.getParentCategory() != null) {
                            parentCategory.setId(p.getParentCategory().getId());
                            Log.d("parent", p.getParentCategory().getId());
                        }

                        Category category = new Category();

                        if (p.getCategory() != null) {
                            category.setId(p.getCategory().getId());
                            Log.d("category", p.getCategory().getId());
                        }

                        ProductUom uom = new ProductUom();
                        if (p.getUom() != null) {
                            uom.setId(p.getUom().getId());
                            Log.d("uom", p.getUom().getId());
                        }
                        product.setParentCategory(parentCategory);
                        product.setCategory(category);
                        product.setUom(uom);
                        product.setCode(p.getCode());
                        product.setDescription(p.getDescription());

                        pulledProducts.add(product);


                    }
                    productDbAdapter.saveProducts(pulledProducts);
                }

                @Override
                public void onFailure(Throwable t) {

                    Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (tx == 0) {
                    Intent intentOrder = new Intent(getActivity(), OrderActivity.class);

                    intentOrder.putExtra("tx", 0);
                    intentOrder.putExtra("productId", productList.get(position).getId());
                    View startView = gridView.getChildAt(position).findViewById(R.id.image);

                    ((MainActivity) getActivity()).order(intentOrder, startView);

                } else if (tx == 1) {
                    Intent intentOrder = new Intent(getActivity(), OrderActivity.class);
                    intentOrder.putExtra("tx", 1);
                    intentOrder.putExtra("productId", productList.get(position).getId());
                    View startView = gridView.getChildAt(position).findViewById(R.id.image);

                    ((MainActivity) getActivity()).order(intentOrder, startView);

                } else if (tx == 2) {
                    Intent intentOrder = new Intent(getActivity(), OrderActivity.class);

                    intentOrder.putExtra("tx", 2);
                    intentOrder.putExtra("productId", productList.get(position).getId());
                    View startView = gridView.getChildAt(position).findViewById(R.id.image);

// ((MainActivity)getActivity()).order(intentOrder, startView);

                    if (!getArguments().getBoolean("mainActivityMaterial", false)) {
                        ((MainActivity) getActivity()).order(intentOrder, startView);

                        Log.d("status","MAIN ACTIVITY");
                    } else {
                        Log.d("status","MAIN ACTIVITY material");


// intentOrder.putExtra("type", "produk");
                        ((MainActivityMaterial) getActivity()).order(intentOrder, startView);
                    }
                }
            }
        });


        return view;
    }
}
