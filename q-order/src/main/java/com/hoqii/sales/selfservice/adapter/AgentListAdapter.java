package com.hoqii.sales.selfservice.adapter;

import android.content.Context;
import android.view.View;

import com.hoqii.sales.selfservice.core.commons.User;
import com.hoqii.sales.selfservice.holder.AgenHolder;

import org.meruvian.midas.core.defaults.DefaultAdapter;

import java.util.List;

/**
 * Created by meruvian on 15/09/15.
 */
public class AgentListAdapter extends DefaultAdapter<User, AgenHolder> {

    public AgentListAdapter(Context context, int layout, List<User> contents) {
        super(context, layout, contents);
    }

    @Override
    public AgenHolder ViewHolder(View view) {
        return new AgenHolder(view);
    }

    @Override
    public View createdView(View view, AgenHolder holder, User user) {
        holder.textAgentName.setText(user.getUsername());

        return view;
    }
}
