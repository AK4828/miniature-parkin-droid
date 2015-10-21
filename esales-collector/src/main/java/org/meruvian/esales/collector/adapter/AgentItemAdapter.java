package org.meruvian.esales.collector.adapter;

import android.content.Context;
import android.view.View;

import org.meruvian.esales.collector.core.commons.User;
import org.meruvian.esales.collector.holder.AgenItemHolder;
import org.meruvian.midas.core.defaults.DefaultAdapter;

import java.util.List;

/**
 * Created by meruvian on 06/10/15.
 */
public class AgentItemAdapter extends DefaultAdapter<User,AgenItemHolder> {

    public AgentItemAdapter(Context context, int layout, List<User> contents) {
        super(context, layout, contents);
    }

    @Override
    public AgenItemHolder ViewHolder(View view) {
        return new AgenItemHolder(view);
    }

    @Override
    public View createdView(View view, AgenItemHolder holder, User user) {
        holder.textAgentName.setText(user.getName().getFirst()
                + " "  + user.getName().getLast());

        return view;
    }
}
