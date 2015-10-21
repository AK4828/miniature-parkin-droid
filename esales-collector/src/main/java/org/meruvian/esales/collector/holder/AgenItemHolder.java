package org.meruvian.esales.collector.holder;

import android.view.View;
import android.widget.TextView;

import org.meruvian.esales.collector.R;
import org.meruvian.midas.core.defaults.DefaultHolder;

import butterknife.InjectView;

/**
 * Created by meruvian on 06/10/15.
 */
public class AgenItemHolder extends DefaultHolder {
    @InjectView(R.id.text_agent_name)
    public TextView textAgentName;

    public AgenItemHolder(View view) {
        super(view);
    }
}
