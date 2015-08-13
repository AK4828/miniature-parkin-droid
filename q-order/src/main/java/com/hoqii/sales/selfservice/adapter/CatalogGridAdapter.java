package com.hoqii.sales.selfservice.adapter;

import android.content.Context;
import android.view.View;

import com.hoqii.sales.selfservice.entity.Product;
import com.hoqii.sales.selfservice.holder.CatalogGridHolder;

import org.meruvian.midas.core.defaults.DefaultAdapter;

import java.util.List;

/**
 * Created by ludviantoovandi on 28/01/15.
 */
public class CatalogGridAdapter extends DefaultAdapter<Product, CatalogGridHolder> {

    public CatalogGridAdapter(Context context, int layout, List<Product> contents) {
        super(context, layout, contents);
    }

    @Override
    public CatalogGridHolder ViewHolder(View view) {
        return new CatalogGridHolder(view);
    }

    @Override
    public View createdView(View view, CatalogGridHolder holder, Product object) {

        return view;
    }
}
