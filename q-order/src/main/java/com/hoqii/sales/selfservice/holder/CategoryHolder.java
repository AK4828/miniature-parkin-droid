package com.hoqii.sales.selfservice.holder;

import android.view.View;
import android.widget.TextView;

import com.hoqii.sales.selfservice.R;

import org.meruvian.midas.core.defaults.DefaultHolder;

import butterknife.InjectView;

/**
 * Created by ludviantoovandi on 28/01/15.
 */
public class CategoryHolder extends DefaultHolder {
    @InjectView(R.id.text_name) public TextView name;

    public CategoryHolder(View view) {
        super(view);
    }
}
