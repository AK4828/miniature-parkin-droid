package com.hoqii.sales.selfservice.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.hoqii.sales.selfservice.R;

import org.meruvian.midas.core.defaults.DefaultHolder;

import butterknife.InjectView;

/**
 * Created by ludviantoovandi on 05/02/15.
 */
public class PromoHolder extends DefaultHolder {
    @InjectView(R.id.image) public ImageView image;
    @InjectView(R.id.progressbar) public ProgressBar progressBar;

    public PromoHolder(View view) {
        super(view);
    }
}
