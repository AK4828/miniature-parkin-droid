package org.meruvian.esales.collector.holder;

import android.view.View;
import android.widget.TextView;

import org.meruvian.esales.collector.R;
import org.meruvian.midas.core.defaults.DefaultHolder;

import butterknife.InjectView;

/**
 * Created by meruvian on 05/10/15.
 */
public class AssigmentItemHolder extends DefaultHolder {
    @InjectView(R.id.text_item_name)
    public TextView textItem;
    @InjectView(R.id.text_item_quantity)
    public TextView textItemQty;

    public AssigmentItemHolder(View view) {
        super(view);
    }

}
