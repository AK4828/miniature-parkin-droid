package com.hoqii.sales.selfservice.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hoqii.sales.selfservice.R;
import com.hoqii.sales.selfservice.adapter.SettelDetailAdapter;
import com.hoqii.sales.selfservice.content.database.adapter.OrderDatabaseAdapter;
import com.hoqii.sales.selfservice.content.database.adapter.OrderMenuDatabaseAdapter;
import com.hoqii.sales.selfservice.entity.OrderMenu;

import org.meruvian.midas.core.defaults.DefaultFragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by meruvian on 03/10/15.
 */
public class SettleDetailFragment extends DefaultFragment {
    @InjectView(R.id.text_total_item)
    TextView textTotalItem;
    @InjectView(R.id.text_total_order)
    TextView textTotalOrder;
    @InjectView(R.id.list_item_settle)
    ListView listItems;
    @InjectView(R.id.button_settle)
    Button btnSettle;

    private SettelDetailAdapter settelDetailAdapter;
    private OrderDatabaseAdapter orderDbAdapter;
    private OrderMenuDatabaseAdapter orderMenuDbAdapter;

    private List<OrderMenu> orderMenus = new ArrayList<OrderMenu>();
    private String orderId;
    private DecimalFormat decimalFormat = new DecimalFormat("#,###");
    private long totalPrice;

    @Override
    protected int layout() {
        return R.layout.fragment_settel_detail;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        orderDbAdapter = new OrderDatabaseAdapter(getActivity());
        orderMenuDbAdapter = new OrderMenuDatabaseAdapter(getActivity());

        settelDetailAdapter = new SettelDetailAdapter(getActivity(), R.layout.adapter_settle_detail,
                new ArrayList<OrderMenu>());



    }


}
