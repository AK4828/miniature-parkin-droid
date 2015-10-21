package org.meruvian.esales.collector.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.meruvian.esales.collector.R;
import org.meruvian.esales.collector.activity.SettleDetailActivity;
import org.meruvian.esales.collector.adapter.AgentListAdapter;
import org.meruvian.esales.collector.content.database.adapter.AssigmentDatabaseAdapter;
import org.meruvian.esales.collector.content.database.adapter.AssigmentDetailDatabaseAdapter;
import org.meruvian.esales.collector.entity.Assigment;
import org.meruvian.esales.collector.entity.AssigmentDetail;
import org.meruvian.esales.collector.util.AuthenticationUtils;
import org.meruvian.midas.core.defaults.DefaultFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by meruvian on 15/09/15.
 */
public class AgentSettleFragment extends DefaultFragment {
    @InjectView(R.id.list_agents)
    ListView listAgent;

    private AgentListAdapter agentListAdapter;

    private AssigmentDatabaseAdapter assigmentDbAdapter;
    private AssigmentDetailDatabaseAdapter assigmentDetailDbAdapter;
    private Assigment assigment;

    @Override
    protected int layout() {
        return R.layout.fragment_agent_item;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        assigmentDbAdapter = new AssigmentDatabaseAdapter(getActivity());
        assigmentDetailDbAdapter = new AssigmentDetailDatabaseAdapter(getActivity());

        agentListAdapter = new AgentListAdapter(getActivity(), R.layout.adapter_agent_item,
                new ArrayList<AssigmentDetail>());
        listAgent.setAdapter(agentListAdapter);

        Log.d(getClass().getSimpleName(), "Kolektor ID: "
                + AuthenticationUtils.getCurrentAuthentication().getUser().getId());
        assigment = assigmentDbAdapter.findAssigmentByUserId
                (AuthenticationUtils.getCurrentAuthentication().getUser().getId());

        Log.d(getClass().getSimpleName(), "Assigment ID: " + assigment.getId());
        agentListAdapter.addAll(findAgents(assigment.getRefId()));

        listAgent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AssigmentDetail ad = agentListAdapter.getItem(position);

               /* Bundle bundle = new Bundle();
                bundle.putString("asg_detail_ref_id", ad.getRefId());

                Fragment fragment = new SettleDetailFragment();
                fragment.setArguments(bundle);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame, fragment);
                transaction.addToBackStack(null);
                transaction.commit();*/

                Intent intent = new Intent();
                intent.setClass(getActivity(), SettleDetailActivity.class);
                intent.putExtra("asg_detail_ref_id", ad.getRefId());
                startActivity(intent);
            }
        });

    }

    private List<AssigmentDetail> findAgents(String assigmentId) {
        List<AssigmentDetail> assigmentDetails = assigmentDetailDbAdapter
                .findAllAssigmentDetail(assigmentId);
        Log.d(getClass().getSimpleName(), "Size AssigmentDetails: "
                + assigmentDetails.size());
        return assigmentDetails;
    }

}
