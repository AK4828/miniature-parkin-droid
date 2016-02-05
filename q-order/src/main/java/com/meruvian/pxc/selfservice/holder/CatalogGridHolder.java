package com.meruvian.pxc.selfservice.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.meruvian.pxc.selfservice.R;

import org.meruvian.midas.core.defaults.DefaultHolder;

import butterknife.Bind;


/**
 * Created by ludviantoovandi on 28/01/15.
 */
public class CatalogGridHolder extends DefaultHolder {
    @Bind(R.id.image) public ImageView image;
    @Bind(R.id.text_name) public TextView name;

    public CatalogGridHolder(View view) {
        super(view);
    }
}
