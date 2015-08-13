package com.hoqii.sales.selfservice.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.hoqii.sales.selfservice.R;
import com.hoqii.sales.selfservice.entity.Promo;
import com.hoqii.sales.selfservice.holder.PromoHolder;

import org.meruvian.midas.core.defaults.DefaultAdapter;

import java.util.List;

/**
 * Created by ludviantoovandi on 05/02/15.
 */
public class PromoAdapter extends DefaultAdapter<Promo, PromoHolder> {
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public PromoAdapter(Context context, int layout, List<Promo> contents) {
        super(context, layout, contents);
    }

    @Override
    public PromoHolder ViewHolder(View view) {
        return new PromoHolder(view);
    }

    @Override
    public View createdView(View view, final PromoHolder holder, Promo promo) {
        imageLoader.displayImage("drawable://" + promo.getImage(), holder.image, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                holder.progressBar.setVisibility(View.GONE);
                holder.image.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                holder.progressBar.setVisibility(View.GONE);
                holder.image.setVisibility(View.VISIBLE);
                holder.image.setImageResource(R.drawable.no_image);
            }
        });

        return view;
    }
}
