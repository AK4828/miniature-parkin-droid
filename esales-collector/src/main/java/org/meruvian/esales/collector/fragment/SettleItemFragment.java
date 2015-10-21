package org.meruvian.esales.collector.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.meruvian.esales.collector.R;
import org.meruvian.esales.collector.adapter.AgentListAdapter;
import org.meruvian.esales.collector.content.database.adapter.UserDatabaseAdapter;
import org.meruvian.esales.collector.core.commons.User;
import org.meruvian.midas.core.defaults.DefaultFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by meruvian on 15/09/15.
 */
public class SettleItemFragment extends DefaultFragment {
    @InjectView(R.id.list_agents)
    ListView listAgent;

    private AgentListAdapter agentListAdapter;
    private UserDatabaseAdapter userDatabaseAdapter;
    private List<User> users = new ArrayList<User>();

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
        userDatabaseAdapter = new UserDatabaseAdapter(getActivity());
        /*agentListAdapter = new AgentListAdapter(getActivity(), R.layout.adapter_agent_item,
                new ArrayList<User>());*/

//        agentListAdapter.addAll(findAgents());

        listAgent.setAdapter(agentListAdapter);

        listAgent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               /* User user = agentListAdapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putString("agent_name", user.getName().getFirst()
                        + " "  + user.getName().getLast());
                bundle.putString("agent_ref_id", user.getRefId());

                Fragment fragment = new BuyerFragment();
                fragment.setArguments(bundle);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame, fragment);
                transaction.addToBackStack(null);
                transaction.commit();*/
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

    private List<User> findAgents() {
        List<User> users = userDatabaseAdapter.findAllUser();
        return users;
    }

}
