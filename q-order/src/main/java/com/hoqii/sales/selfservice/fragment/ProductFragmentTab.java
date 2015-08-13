package com.hoqii.sales.selfservice.fragment;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.hoqii.sales.selfservice.R;
import com.hoqii.sales.selfservice.adapter.ProductGridAdapter;
import com.hoqii.sales.selfservice.content.database.adapter.ProductDatabaseAdapter;
import com.hoqii.sales.selfservice.entity.Product;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;

/**
 * Created by ludviantoovandi on 29/01/15.
 */
public class ProductFragmentTab extends android.support.v4.app.Fragment {

    @InjectView(R.id.grid_image) GridView gridView;

    private ProductGridAdapter productGridAdapter;

    private ProductDatabaseAdapter productDbAdapter;

//    @Override
//    protected int layout() {
//        return R.layout.fragment_product_grid;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_grid, container, false);

        ButterKnife.inject(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        String parentCategory = getArguments().getString("parent_category", null);
        String category = getArguments().getString("category", null);
        String name = getArguments().getString("name", null);

        productDbAdapter = new ProductDatabaseAdapter(getActivity());

        productGridAdapter = new ProductGridAdapter(getActivity(), R.layout.adapter_product_grid, new ArrayList<Product>());
        gridView.setAdapter(productGridAdapter);

        if (parentCategory != null) {
            productGridAdapter.addAll(productDbAdapter.getMenuByParentCategory(parentCategory));
        } else if (category != null) {
            productGridAdapter.addAll(productDbAdapter.getMenuByCategory(category));
        } else if (name != null) {
            productGridAdapter.addAll(productDbAdapter.getMenuByName(name));
        }
    }

    @OnItemClick(R.id.grid_image)
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("product_id", productGridAdapter.getItem(position).getId());

//        Fragment fragment = new ProductDetailFragment();
//        fragment.setArguments(bundle);
//
//        getFragmentManager().beginTransaction().replace(    R.id.container, fragment).addToBackStack(null).commit();
    }

    private void showImage(Bitmap bitmap) {
        View view = View.inflate(getActivity(), R.layout.dialog_view_image, null);

        ImageView image = (ImageView) view.findViewById(R.id.image);
        ImageButton imageButton = (ImageButton) view.findViewById(R.id.button_close);

        image.setImageBitmap(bitmap);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.create().show();
    }
}
