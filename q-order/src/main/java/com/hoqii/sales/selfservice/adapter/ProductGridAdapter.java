package com.hoqii.sales.selfservice.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.hoqii.sales.selfservice.R;
import com.hoqii.sales.selfservice.SignageVariables;
import com.hoqii.sales.selfservice.entity.Product;
import com.hoqii.sales.selfservice.holder.ProductGridHolder;
import com.hoqii.sales.selfservice.util.ImageUtil;

import org.meruvian.midas.core.defaults.DefaultAdapter;

import java.util.List;

/**
 * Created by ludviantoovandi on 28/01/15.
 */
public class ProductGridAdapter extends DefaultAdapter<Product, ProductGridHolder> {
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public ProductGridAdapter(Context context, int layout, List<Product> contents) {
        super(context, layout, contents);
    }

    @Override
    public ProductGridHolder ViewHolder(View view) {
        return new ProductGridHolder(view);
    }

    @Override
    public View createdView(View view, final ProductGridHolder holder, Product product) {
        imageLoader.displayImage("file://" + ImageUtil.getImagePath(getContext(), product.getId()), holder.image, new SimpleImageLoadingListener() {

            @Override
            public void onLoadingStarted(String imageUri, View view) {
                holder.progressBar.setVisibility(View.VISIBLE);
                holder.image.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                holder.progressBar.setVisibility(View.GONE);
                holder.image.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                holder.progressBar.setVisibility(View.GONE);
                holder.image.setImageResource(R.drawable.no_image);
            }
        });

        holder.name.setText(product.getName());

        return view;
    }

    /*private String imageUrl(String id) {
        return SignageVariables.SERVER_URL + "/product/image/" + id;
    }*/


}
