package com.hoqii.sales.selfservice.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hoqii.sales.selfservice.R;
import com.hoqii.sales.selfservice.activity.ContactActivity;
import com.hoqii.sales.selfservice.adapter.OrderListAdapter;
import com.hoqii.sales.selfservice.content.database.adapter.OrderDatabaseAdapter;
import com.hoqii.sales.selfservice.content.database.adapter.OrderMenuDatabaseAdapter;
import com.hoqii.sales.selfservice.content.database.adapter.ProductDatabaseAdapter;
import com.hoqii.sales.selfservice.entity.OrderMenu;

import org.meruvian.midas.core.defaults.DefaultFragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by meruvian on 14/07/15.
 */
public class OrderListFragment extends DefaultFragment {
    @InjectView(R.id.list_order) ListView listOrder;
    @InjectView(R.id.text_total_item) TextView textTotalItem;
    @InjectView(R.id.text_total_order) TextView textTotalOrder;

    private OrderListAdapter orderListAdapter;
    private ProductDatabaseAdapter productDbAdapter;
    private OrderDatabaseAdapter orderDbAdapter;
    private OrderMenuDatabaseAdapter orderMenuDbAdapter;

    private List<OrderMenu> orderMenus = new ArrayList<OrderMenu>();
    private String orderId;
    private DecimalFormat decimalFormat = new DecimalFormat("#,###");
    private long totalPrice;

    @Override
    protected int layout() {
        return R.layout.fragment_order_list;
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
            Log.d(getClass().getSimpleName(), "Click: menu_pay_order");

            Bundle bundle = new Bundle();
            bundle.putString("order_id", orderId);
            Log.d(getClass().getSimpleName(), "Bundle Order ID : " + orderId);

            Intent intent = new Intent(getActivity(), ContactActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        productDbAdapter = new ProductDatabaseAdapter(getActivity());
        orderDbAdapter = new OrderDatabaseAdapter(getActivity());
        orderMenuDbAdapter = new OrderMenuDatabaseAdapter(getActivity());

        orderListAdapter = new OrderListAdapter(getActivity(), R.layout.adapter_order_list,
                new ArrayList<OrderMenu>(), this);

        orderId = orderDbAdapter.getOrderId();

        if(orderId != null) {
            totalPrice = 0;

            Log.d(getClass().getSimpleName(), "Order Id = " + orderId);
            orderMenus = orderMenuDbAdapter.findOrderMenuByOrderId(orderId);

            for (OrderMenu om : orderMenus) {
                totalPrice += om.getProduct().getSellPrice()*om.getQty();
            }

            textTotalItem.setText("Jumlah Item: " + orderMenus.size());
            textTotalOrder.setText("Total Order: " + "Rp " + decimalFormat.format(totalPrice));

            orderListAdapter.addAll(orderMenus);
        }

        listOrder.setAdapter(orderListAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        setTotalOrderTab();
    }

    private void updateOrder(){
       if (orderMenus.size() > 0){
           orderDbAdapter.updateOrder(orderId);
           orderListAdapter.clear();
           Toast.makeText(getActivity(), "Pesanan Anda sudah Kami Proses", Toast.LENGTH_SHORT).show();
       }
    }

    public void setTotalOrderTab() {
        if(orderId != null) {
            totalPrice = 0;
            orderMenus = orderMenuDbAdapter.findOrderMenuByOrderId(orderId);

            for (OrderMenu om : orderMenus) {
                totalPrice += om.getProduct().getSellPrice() * om.getQty();
            }

            textTotalItem.setText("Jumlah Item: " + orderMenus.size());
            textTotalOrder.setText("Total Order: " + "Rp " + decimalFormat.format(totalPrice));
        }
    }

}
