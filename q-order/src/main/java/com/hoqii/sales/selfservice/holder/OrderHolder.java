package com.hoqii.sales.selfservice.holder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hoqii.sales.selfservice.R;

import org.meruvian.midas.core.defaults.DefaultHolder;

import butterknife.InjectView;

/**
 * Created by meruvian on 14/07/15.
 */
public class OrderHolder extends DefaultHolder {
    @InjectView(R.id.text_menu_name) public TextView menuName;
    @InjectView(R.id.text_menu_quantity) public TextView menuQuantity;
    @InjectView(R.id.text_total_price) public TextView totalPrice;
    @InjectView(R.id.image_button_delete) public ImageButton buttonDelete;
    //    @InjectView(R.id.image_button_edit) public ImageButton buttonEdit;

    public OrderHolder(View view) {
        super(view);
    }
}
