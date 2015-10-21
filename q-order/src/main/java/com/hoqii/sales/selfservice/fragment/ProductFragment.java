package com.hoqii.sales.selfservice.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.hoqii.sales.selfservice.R;
import com.hoqii.sales.selfservice.adapter.ProductGridAdapter;
import com.hoqii.sales.selfservice.content.database.adapter.ProductDatabaseAdapter;
import com.hoqii.sales.selfservice.entity.Product;

import org.meruvian.midas.core.defaults.DefaultFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnItemClick;

/**
 * Created by ludviantoovandi on 29/01/15.
 */
public class ProductFragment extends DefaultFragment {
    @InjectView(R.id.grid_image) GridView gridView;

    private ProductGridAdapter productGridAdapter;
    private ProductDatabaseAdapter productDbAdapter;
    private ArrayList<String> productIds;

    @Override
    protected int layout() {
        return R.layout.fragment_product_grid;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        String parentCategory = getArguments().getString("parent_category", null);
        String category = getArguments().getString("category", null);
        String name = getArguments().getString("name", null);

        productDbAdapter = new ProductDatabaseAdapter(getActivity());

        productGridAdapter = new ProductGridAdapter(getActivity(), R.layout.adapter_product_grid, new ArrayList<Product>());
        gridView.setAdapter(productGridAdapter);

        List<Product> products = new ArrayList<>();

        if (parentCategory != null) {
            products = productDbAdapter.getMenuByParentCategory(parentCategory);
        } else if (category != null) {
            products = productDbAdapter.getMenuByCategory(category);
        } else if (name != null) {
            products = productDbAdapter.getMenuByName(name);
        }

        productIds = new ArrayList<>();
        for (Product product : products) {
            productIds.add(product.getId());
        }

        productGridAdapter.addAll(products);
    }

    @OnItemClick(R.id.grid_image)
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("product_id", productGridAdapter.getItem(position).getId());
        bundle.putInt("position", position);
        bundle.putStringArrayList("products", productIds);

        Fragment fragment = new ProductDetailFragment();
        fragment.setArguments(bundle);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
//        getFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();
    }

}
