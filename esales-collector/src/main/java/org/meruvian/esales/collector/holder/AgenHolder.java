package org.meruvian.esales.collector.holder;

import android.view.View;
import android.widget.TextView;

import org.meruvian.esales.collector.R;
import org.meruvian.midas.core.defaults.DefaultHolder;

import butterknife.InjectView;

/**
 * Created by meruvian on 15/09/15.
 */
public class AgenHolder extends DefaultHolder {
    @InjectView(R.id.text_agent_name)
    public TextView textAgentName;

    public AgenHolder(View view) {
        super(view);
    }

}