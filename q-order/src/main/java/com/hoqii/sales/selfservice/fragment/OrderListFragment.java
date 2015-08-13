package com.hoqii.sales.selfservice.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.hoqii.sales.selfservice.SignageVariables;
import com.hoqii.sales.selfservice.activity.ContactActivity;
import com.hoqii.sales.selfservice.adapter.OrderListAdapter;
import com.hoqii.sales.selfservice.content.database.adapter.OrderDatabaseAdapter;
import com.hoqii.sales.selfservice.content.database.adapter.OrderMenuDatabaseAdapter;
import com.hoqii.sales.selfservice.content.database.adapter.ProductDatabaseAdapter;
import com.hoqii.sales.selfservice.entity.Order;
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
//    @InjectView(R.id.text_receipt_number) TextView textReceiptNumber;
    @InjectView(R.id.text_total_item) TextView textTotalItem;
    @InjectView(R.id.text_total_order) TextView textTotalOrder;

    private OrderListAdapter orderListAdapter;
    private ProductDatabaseAdapter productDbAdapter;
    private OrderDatabaseAdapter orderDbAdapter;
    private OrderMenuDatabaseAdapter orderMenuDbAdapter;

//    private CartDatabaseAdapter cartDbAdapter;
//    private CartMenuDatabaseAdapter cartMenuDbAdapter;

    private List<OrderMenu> orderMenus = new ArrayList<OrderMenu>();
//    private List<CartMenu> cartMenus = new ArrayList<CartMenu>();
    private String orderId;
    private DecimalFormat decimalFormat = new DecimalFormat("#,###");
    private long totalPrice;
    private Order order;
//    private Cart cart;
//    private JobManager jobManager;
    private List<String> orderIdes;
    private List<String> orderMenuIdes;
    private int orderCount = 0;
    private int totalOrders = 0;
    private SharedPreferences preferences;
//    private String cartId = null;

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
//            setTabClear();

            Bundle bundle = new Bundle();
            bundle.putString("order_id", orderId);
            Log.d(getClass().getSimpleName(), "Bundle Order ID : " + orderId);

            Intent intent = new Intent(getActivity(), ContactActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);

//            updateOrder();
//            syncOrder();
//            startActivity(new Intent(getActivity(), LoginActivity.class));

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        preferences = getActivity().getSharedPreferences(SignageVariables.PREFS_SERVER, 0);

        productDbAdapter = new ProductDatabaseAdapter(getActivity());
        orderDbAdapter = new OrderDatabaseAdapter(getActivity());
        orderMenuDbAdapter = new OrderMenuDatabaseAdapter(getActivity());

        orderListAdapter = new OrderListAdapter(getActivity(), R.layout.adapter_order_list, new ArrayList<OrderMenu>(), this);

        orderId = orderDbAdapter.getOrderId();

        if(orderId != null) {
            order = orderDbAdapter.findOrderById(orderId);
            totalPrice = 0;

            Log.d(getClass().getSimpleName(), "Order Id = " + orderId);
            orderMenus = orderMenuDbAdapter.findOrderMenuByOrderId(orderId);
//            cartMenus = cartMenuDbAdapter.findCartMenuByCartId(cartId);

            for (OrderMenu om : orderMenus) {
                totalPrice += om.getProduct().getSellPrice()*om.getQty();
            }

//            for (OrderMenu om : orderMenus) {
//                totalPrice += om.getProduct().getSellPrice()*om.getQty();
//            }

//            textReceiptNumber.setText("No: " + order.getReceiptNumber());
            textTotalItem.setText("Jumlah Item: " + orderMenus.size());
            textTotalOrder.setText("Total Order: " + "Rp " + decimalFormat.format(totalPrice));

            orderListAdapter.addAll(orderMenus);
//            orderListAdapter.addAll(cartMenus);
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

    /*public void setTabClear() {
//        textReceiptNumber.setText("No: " + rn);
        textTotalItem.setText("Jumlah Item: " + 0);
        textTotalOrder.setText("Total Order: " + "Rp " + decimalFormat.format(0));
    }*/

   /* public void onEventMainThread(GenericEvent.RequestInProgress requestInProgress) {
        Log.d(getClass().getSimpleName(), "RequestInProgress: " + requestInProgress.getProcessId());
    }

    public void onEventMainThread(GenericEvent.RequestSuccess requestSuccess) {
        try {
            switch (requestSuccess.getProcessId()) {
                case OrderJob.PROCESS_ID: {
                    Log.d(getClass().getSimpleName(), "Response OrderRefId : " + requestSuccess.getRefId());

                    orderCount++;

                    Log.d(getClass().getSimpleName(), "RequestSuccess OrderId: " + requestSuccess.getRefId());
                    orderMenuIdes = orderMenuDbAdapter.findOrderMenuIdesByOrderId(requestSuccess.getEntityId());
                    for (String id : orderMenuIdes) {
                        Log.d(getClass().getSimpleName(), "Size Order menu Ides: " + orderMenuIdes.size());
                        jobManager.addJobInBackground(new OrderMenuJob(requestSuccess.getRefId(), id, preferences.getString("server_url", "")));
                    }

                    if (orderCount == totalOrders){
                        finish();
                        startActivity(new Intent(getActivity(), MainActivity.class));
                    }
                    break;
                }
                case OrderMenuJob.PROCESS_ID: {
                    Log.d(getClass().getSimpleName(), "RequestSuccess OrderMenuId: " + requestSuccess.getRefId());
                    break;
                }
            }

        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    public void onEventMainThread(GenericEvent.RequestFailed failed) {
        Log.e(getClass().getSimpleName(),
                failed.getResponse().getHttpResponse().getStatusLine().getStatusCode() + " :"
                        + failed.getResponse().getHttpResponse().getStatusLine().getReasonPhrase());
    }

    public void syncOrder() {
        orderIdes = orderDbAdapter.findAllIdOrder();
        totalOrders = orderIdes.size();

        if (totalOrders <= 0) {
            Toast.makeText(getActivity(), "Data Order Kosong", Toast.LENGTH_SHORT).show();
        }

        for (String id : orderIdes) {
            jobManager.addJobInBackground(new OrderJob(id, preferences.getString("server_url", "")));
        }
    }*/
}
