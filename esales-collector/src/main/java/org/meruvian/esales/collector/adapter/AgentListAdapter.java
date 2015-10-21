package org.meruvian.esales.collector.adapter;

import android.content.Context;
import android.view.View;

import org.meruvian.esales.collector.entity.AssigmentDetail;
import org.meruvian.esales.collector.holder.AgenHolder;
import org.meruvian.midas.core.defaults.DefaultAdapter;

import java.util.List;

/**
 * Created by meruvian on 15/09/15.
 */
public class AgentListAdapter extends DefaultAdapter<AssigmentDetail, AgenHolder> {

    public AgentListAdapter(Context context, int layout, List<AssigmentDetail> contents) {
        super(context, layout, contents);
    }

    @Override
    public AgenHolder ViewHolder(View view) {
        return new AgenHolder(view);
    }

    @Override
    public View createdView(View view, AgenHolder holder, AssigmentDetail assigmentDetail) {
        holder.textAgentName.setText(assigmentDetail.getAgent().getName().getFirst()
                + " "  + assigmentDetail.getAgent().getName().getLast());

        return view;
    }
}
