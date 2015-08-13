package com.hoqii.sales.selfservice.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hoqii.sales.selfservice.R;

import org.meruvian.midas.core.defaults.DefaultHolder;

import butterknife.InjectView;

/**
 * Created by ludviantoovandi on 28/01/15.
 */
public class ProductGridHolder extends DefaultHolder {
    @InjectView(R.id.image) public ImageView image;
    @InjectView(R.id.text_name) public TextView name;
    @InjectView(R.id.progressbar) public ProgressBar progressBar;

    public ProductGridHolder(View view) {
        super(view);
    }



}
