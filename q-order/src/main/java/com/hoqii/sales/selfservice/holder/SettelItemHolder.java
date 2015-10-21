package com.hoqii.sales.selfservice.holder;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hoqii.sales.selfservice.R;

import org.meruvian.midas.core.defaults.DefaultHolder;

import butterknife.InjectView;

/**
 * Created by meruvian on 03/10/15.
 */
public class SettelItemHolder extends DefaultHolder {
    public SettelItemHolder(View view) {
        super(view);
    }

    @InjectView(R.id.text_item_name)
    public TextView textItem;
    @InjectView(R.id.button_min_item)
    public Button btnMin;
    @InjectView(R.id.edit_quantity)
    public EditText editQuantity;
    @InjectView(R.id.button_add_item)
    public Button btnAdd;
    @InjectView(R.id.button_cancel)
    public ImageButton btnCancel;

}
