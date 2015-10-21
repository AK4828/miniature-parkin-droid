package com.hoqii.sales.selfservice.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hoqii.sales.selfservice.R;
import com.hoqii.sales.selfservice.adapter.HistoryOrderAdapter;
import com.hoqii.sales.selfservice.content.database.adapter.OrderDatabaseAdapter;
import com.hoqii.sales.selfservice.entity.Order;
import com.hoqii.sales.selfservice.util.AuthenticationUtils;

import org.meruvian.midas.core.defaults.DefaultFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by meruvian on 27/08/15.
 */
public class HistoryOrderFragment extends DefaultFragment {
    @InjectView(R.id.list_history_orders)
    ListView listHistoryOrder;

    private HistoryOrderAdapter historyOrderAdapter;
    private OrderDatabaseAdapter orderDatabaseAdapter;
    private List<Order> orders = new ArrayList<Order>();

    @Override
    protected int layout() {
        return R.layout.fragment_history_orders;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CatalogFragment catalogFragment = new CatalogFragment();
        catalogFragment.setVisibilityContainerCategory(View.GONE);
        catalogFragment.showOverflowMenu(false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        orderDatabaseAdapter = new OrderDatabaseAdapter(getActivity());
        historyOrderAdapter = new HistoryOrderAdapter(getActivity(), R.layout.adapter_history_order,
                new ArrayList<Order>());

        orders = orderDatabaseAdapter.getHistoryOrders(50, AuthenticationUtils
                .getCurrentAuthentication().getUser().getId());

        historyOrderAdapter.addAll(orders);

        listHistoryOrder.setAdapter(historyOrderAdapter);

        listHistoryOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Order order = orders.get(position);

                Bundle bundle = new Bundle();
                bundle.putString("order_id", order.getId());

                Fragment fragment = new HistoryOrderDetailFragment();
                fragment.setArguments(bundle);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }



}
