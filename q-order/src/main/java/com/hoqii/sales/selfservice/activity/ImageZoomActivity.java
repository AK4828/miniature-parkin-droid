package com.hoqii.sales.selfservice.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.hoqii.sales.selfservice.R;
import com.hoqii.sales.selfservice.util.ImageUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by ludviantoovandi on 22/01/15.
 */
public class ImageZoomActivity extends Activity {
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private PhotoViewAttacher attacher;

    @InjectView(R.id.image) PhotoView image;
    @InjectView(R.id.progressbar) ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_zoom);

        ButterKnife.inject(this);

        imageLoader.displayImage("file://" + ImageUtil.getImagePath(this, getIntent().getStringExtra("product_id")), image,  new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                progressBar.setVisibility(View.GONE);
                image.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                progressBar.setVisibility(View.GONE);
                image.setImageResource(R.drawable.no_image);
            }
        });
        attacher = new PhotoViewAttacher(image);
        attacher.setZoomable(true);
        attacher.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
    }

    @OnClick({R.id.close})
    public void onClick(ImageButton imageButton) {
        if (imageButton.getId() == R.id.close) {
            finish();
        }
    }

}
