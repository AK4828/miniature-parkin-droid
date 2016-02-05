package com.meruvian.pxc.selfservice.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.meruvian.pxc.selfservice.R;

import org.meruvian.midas.core.defaults.DefaultHolder;

import butterknife.Bind;

/**
 * Created by ludviantoovandi on 05/02/15.
 */
public class PromoHolder extends DefaultHolder {
    @Bind(R.id.image) public ImageView image;
    @Bind(R.id.progressbar) public ProgressBar progressBar;

    public PromoHolder(View view) {
        super(view);
    }
}
