package com.hoqii.sales.selfservice.fragment;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.hoqii.sales.selfservice.R;
import com.hoqii.sales.selfservice.adapter.BuyerOrderDetailAdapter;
import com.hoqii.sales.selfservice.entity.OrderMenu;

import org.meruvian.midas.core.defaults.DefaultFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by meruvian on 15/09/15.
 */
public class BuyerOrderDetailFragment extends DefaultFragment {
    @InjectView(R.id.list_item_buyer_detail)
    ListView listBuyerDetail;

    private BuyerOrderDetailAdapter buyerOrderDetailAdapter;
    private List<OrderMenu> orderMenus = new ArrayList<OrderMenu>();

    @Override
    protected int layout() {
        return R.layout.fragment_buyer_detail_order;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.order, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== R.id.menu_pay_order){
            clearBackStack();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        buyerOrderDetailAdapter = new BuyerOrderDetailAdapter(getActivity(),
                R.layout.adapter_buyer_detail_order, new ArrayList<OrderMenu>(), this);

        buyerOrderDetailAdapter.addAll(getOrderMenus());
        listBuyerDetail.setAdapter(buyerOrderDetailAdapter);


    }

    private List<OrderMenu> getOrderMenus() {
        List<OrderMenu> orderMenus = new ArrayList<OrderMenu>();
        OrderMenu orderMenu = new OrderMenu();
        orderMenu.getProduct().setSellPrice(1500000);
        orderMenu.setQty(1);
        orderMenu.getProduct().getProduct().setName("Elevate Y2");
        orderMenu.setDescription("Putih");
        orderMenus.add(orderMenu);

        OrderMenu orderMenu1 = new OrderMenu();
        orderMenu1.getProduct().setSellPrice(1300000);
        orderMenu1.setQty(1);
        orderMenu1.getProduct().getProduct().setName("Winner Y1");
        orderMenu1.setDescription("Hitam");
        orderMenus.add(orderMenu1);

        return orderMenus;
    }

    private void clearBackStack() {
        final FragmentManager fragmentManager = getFragmentManager();
        while (fragmentManager.getBackStackEntryCount() != 0) {
            fragmentManager.popBackStackImmediate();
        }
    }

}
