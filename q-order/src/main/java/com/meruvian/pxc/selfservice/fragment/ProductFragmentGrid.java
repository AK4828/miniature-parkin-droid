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

import com.meruvian.pxc.selfservice.R;
import com.meruvian.pxc.selfservice.activity.MainActivity;
import com.meruvian.pxc.selfservice.activity.MainActivityMaterial;
import com.meruvian.pxc.selfservice.activity.OrderActivity;
import com.meruvian.pxc.selfservice.adapter.ProductAdapter;
import com.meruvian.pxc.selfservice.content.database.adapter.OrderDatabaseAdapter;
import com.meruvian.pxc.selfservice.content.database.adapter.OrderMenuDatabaseAdapter;
import com.meruvian.pxc.selfservice.content.database.adapter.ProductDatabaseAdapter;
import com.meruvian.pxc.selfservice.entity.OrderMenu;
import com.meruvian.pxc.selfservice.entity.Product;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by miftakhul on 11/16/15.
 */
public class ProductFragmentGrid extends Fragment {

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private GridView gridView;
    private ProductAdapter productAdapter;
    private int tx = 2;
    private DecimalFormat decimalFormat = new DecimalFormat("#,###");
    private ProductDatabaseAdapter productDatabaseAdapter;
    private OrderDatabaseAdapter orderDbAdapter;
    private OrderMenuDatabaseAdapter orderMenuDbAdapter;
    private List<OrderMenu> orderMenus = new ArrayList<OrderMenu>();
    private boolean isMinLoli = false;

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

        productDatabaseAdapter = new ProductDatabaseAdapter(getActivity());
        orderDbAdapter = new OrderDatabaseAdapter(getActivity());
        orderMenuDbAdapter = new OrderMenuDatabaseAdapter(getActivity());

        productAdapter = new ProductAdapter(getActivity(), dataProduct());

        gridView = (GridView) view.findViewById(R.id.grid_image);
        gridView.setAdapter(productAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (tx == 0) {
                    Intent intentOrder = new Intent(getActivity(), OrderActivity.class);

                    intentOrder.putExtra("tx", 0);
                    intentOrder.putExtra("productId", dataProduct().get(position).getId());
                    View startView = gridView.getChildAt(position).findViewById(R.id.image);

                    ((MainActivity) getActivity()).order(intentOrder, startView);

                } else if (tx == 1) {
                    Intent intentOrder = new Intent(getActivity(), OrderActivity.class);
                    intentOrder.putExtra("tx", 1);
                    intentOrder.putExtra("productId", dataProduct().get(position).getId());
                    View startView = gridView.getChildAt(position).findViewById(R.id.image);

                    ((MainActivity) getActivity()).order(intentOrder, startView);

                } else if (tx == 2) {
                    Intent intentOrder = new Intent(getActivity(), OrderActivity.class);

                    intentOrder.putExtra("tx", 2);
                    intentOrder.putExtra("productId", dataProduct().get(position).getId());
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


    private List<Product> dataProduct() {
        List<Product> products = new ArrayList<Product>();

        String parentCategory = null;
        String category = null;
        String name = null;

        if (getArguments() != null) {
            parentCategory = getArguments().getString("parent_category", null);
            category = getArguments().getString("category", null);
            name = getArguments().getString("name", null);
        }


        if (parentCategory != null) {
            products = productDatabaseAdapter.getMenuByParentCategory(parentCategory);
        } else if (category != null) {
            products = productDatabaseAdapter.getMenuByCategory(category);
        } else if (name != null) {
            products = productDatabaseAdapter.getMenuByName(name);
        } else {
            products = productDatabaseAdapter.getMenu();
        }

        Log.d("jumlah total", Integer.toString(products.size()));
        return products;
    }


}
