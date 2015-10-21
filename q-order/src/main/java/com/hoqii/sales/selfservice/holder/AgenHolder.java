package com.hoqii.sales.selfservice.holder;

import android.view.View;
import android.widget.TextView;

import com.hoqii.sales.selfservice.R;

import org.meruvian.midas.core.defaults.DefaultHolder;

import butterknife.InjectView;

/**
 * Created by meruvian on 15/09/15.
 */
public class AgenHolder extends DefaultHolder {

    public AgenHolder(View view) {
        super(view);
    }

    @InjectView(R.id.text_agent_name) public TextView textAgentName;

}