package com.hoqii.sales.selfservice.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hoqii.sales.selfservice.R;
import com.hoqii.sales.selfservice.adapter.BuyerOrderAdapter;
import com.hoqii.sales.selfservice.entity.Order;

import org.meruvian.midas.core.defaults.DefaultFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by meruvian on 15/09/15.
 */
public class BuyerOrderFragment extends DefaultFragment {
    @InjectView(R.id.list_no_order_buyer)
    ListView listOrderBuyer;

    private BuyerOrderAdapter buyerOrderAdapter;
    private List<Order> orders = new ArrayList<Order>();

    @Override
    protected int layout() {
        return R.layout.fragment_buyer_list_order;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        buyerOrderAdapter = new BuyerOrderAdapter(getActivity(), R.layout.adapter_buyer_list_order,
                new ArrayList<Order>());
        buyerOrderAdapter.addAll(getOrders());
        listOrderBuyer.setAdapter(buyerOrderAdapter);

        listOrderBuyer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment = new BuyerOrderDetailFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    private List<Order> getOrders() {
        List<Order> orders = new ArrayList<Order>();
        for (int i = 0; i < 7; i++) {
            Order order = new Order();
            order.setReceiptNumber("201509270000" + i);
            orders.add(order);
        }

        return orders;
    }

}
