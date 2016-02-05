package com.meruvian.pxc.selfservice.holder;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.meruvian.pxc.selfservice.R;

import org.meruvian.midas.core.defaults.DefaultHolder;

import butterknife.Bind;


/**
 * Created by meruvian on 03/10/15.
 */
public class SettelItemHolder extends DefaultHolder {
    public SettelItemHolder(View view) {
        super(view);
    }

    @Bind(R.id.text_item_name)
    public TextView textItem;
    @Bind(R.id.button_min_item)
    public Button btnMin;
    @Bind(R.id.edit_quantity)
    public EditText editQuantity;
    @Bind(R.id.button_add_item)
    public Button btnAdd;
    @Bind(R.id.button_cancel)
    public ImageButton btnCancel;

}
