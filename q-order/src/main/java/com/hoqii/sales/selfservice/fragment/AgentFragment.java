package com.hoqii.sales.selfservice.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hoqii.sales.selfservice.R;
import com.hoqii.sales.selfservice.adapter.AgentListAdapter;
import com.hoqii.sales.selfservice.core.commons.User;

import org.meruvian.midas.core.defaults.DefaultFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by meruvian on 15/09/15.
 */
public class AgentFragment extends DefaultFragment {
    @InjectView(R.id.list_agents)
    ListView listAgent;

    private AgentListAdapter agentListAdapter;
    private List<User> users = new ArrayList<User>();

    @Override
    protected int layout() {
        return R.layout.fragment_agent_list;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        agentListAdapter = new AgentListAdapter(getActivity(), R.layout.adapter_agent_list,
                new ArrayList<User>());
        agentListAdapter.addAll(getUsers());

        listAgent.setAdapter(agentListAdapter);

        listAgent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment = new BuyerFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

    }

    private List<User> getUsers() {
        List<User> users = new ArrayList<User>();
        for (int i = 0; i < 7; i++) {
            User user = new User();
            user.setUsername("Agent0" + (i + 1));
            users.add(user);
        }
        
        return users;
    }

}
