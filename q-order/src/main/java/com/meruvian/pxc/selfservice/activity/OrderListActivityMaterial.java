//package com.hoqii.app.selfservice.activity;
//
//import android.app.AlertDialog;
//import android.app.FragmentManager;
//import android.app.ProgressDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.hoqii.app.selfservice.R;
//import com.hoqii.app.selfservice.SignageAppication;
//import com.hoqii.app.selfservice.SignageVariables;
//import com.hoqii.app.selfservice.adapter.OrderListActAdapter;
//import com.hoqii.app.selfservice.content.database.adapter.ContactDatabaseAdapter;
//import com.hoqii.app.selfservice.content.database.adapter.OrderDatabaseAdapter;
//import com.hoqii.app.selfservice.content.database.adapter.OrderMenuDatabaseAdapter;
//import com.hoqii.app.selfservice.entity.Contact;
//import com.hoqii.app.selfservice.entity.Order;
//import com.hoqii.app.selfservice.entity.OrderMenu;
//import com.hoqii.app.selfservice.event.GenericEvent;
//import com.hoqii.app.selfservice.job.ContactJob;
//import com.hoqii.app.selfservice.job.ContactUpdateJob;
//import com.hoqii.app.selfservice.job.OrderMenuJob;
//import com.hoqii.app.selfservice.job.OrderUpdateJob;
//import com.hoqii.app.selfservice.task.RequestOrderSyncTask;
//import com.path.android.jobqueue.JobManager;
//
//import org.meruvian.midas.core.service.TaskService;
//
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.List;
//
//import de.greenrobot.event.EventBus;
//
///**
// * Created by miftakhul on 11/18/15.
// */
//public class OrderListActivityMaterial extends AppCompatActivity implements TaskService {
//
//    private Toolbar toolbar;
//    private ListView listOrder;
//    private TextView textTotalItem;
//    private TextView textTotalOrder;
//    private TextView textContact;
//
//    private OrderDatabaseAdapter orderDbAdapter;
//    private OrderMenuDatabaseAdapter orderMenuDbAdapter;
//    private ContactDatabaseAdapter contactDbAdapter;
//
//    private OrderListActAdapter orderListAdapter;
//
//
//    private RequestOrderSyncTask requestOrderSyncTask;
//
//    private JobManager jobManager;
//    private SharedPreferences preferences;
//    private ProgressDialog dialog;
//    private MenuItem item;
//
//    private List<OrderMenu> orderMenus = new ArrayList<OrderMenu>();
//    private List<Contact> contacts = new ArrayList<Contact>();
//    private List<String> orderMenuIdes;
//
//    private Contact contact = null;
//
//    private int orderMenuCount = 0;
//    private int totalOrderMenus = 0;
//    private int positionItem = -1;
//    private String orderId = null, contactId = null, addressId = null;
//    private DecimalFormat decimalFormat = new DecimalFormat("#,###");
//    private long totalPrice;
//
//    private TextView text_total_item;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_order_list);
//
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        listOrder = (ListView) findViewById(R.id.list_order);
//        textTotalItem = (TextView) findViewById(R.id.text_total_item);
//        textTotalOrder = (TextView) findViewById(R.id.text_total_order);
////        textContact = (TextView) findViewById(R.id.text_contact_order);
//
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//
//        jobManager = SignageAppication.getInstance().getJobManager();
//        preferences = this.getSharedPreferences(SignageVariables.PREFS_SERVER, 0);
//        EventBus.getDefault().register(this);
//
//        orderDbAdapter = new OrderDatabaseAdapter(this);
//        orderMenuDbAdapter = new OrderMenuDatabaseAdapter(this);
//        contactDbAdapter = new ContactDatabaseAdapter(this);
//
//        orderListAdapter = new OrderListActAdapter(this, R.layout.adapter_order_list,
//                new ArrayList<OrderMenu>(), this);
//
//        orderId = orderDbAdapter.getOrderId();
//
//        if (orderId != null) {
//            totalPrice = 0;
//
//            Log.d(getClass().getSimpleName(), "Order Id = " + orderId);
//            orderMenus = orderMenuDbAdapter.findOrderMenuByOrderId(orderId);
//
//            for (OrderMenu om : orderMenus) {
//                totalPrice += om.getProduct().getSellPrice() * om.getQty();
//            }
//
//            textTotalItem.setText("Jumlah Item: " + orderMenus.size());
//            textTotalOrder.setText("Total Order: " + "Rp " + decimalFormat.format(totalPrice));
//
//            orderListAdapter.addAll(orderMenus);
//        }
//
//        listOrder.setAdapter(orderListAdapter);
//
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.order, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        this.item = item;
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                this.finish();
//                return true;
//            case R.id.menu_pay_order:
//
////                Log.d(getClass().getSimpleName(), "SaveOrder.RefId: " + contact.getRefId());
//
//                setEnabledMenuItem(item, false);
//                dialog = new ProgressDialog(this);
//                dialog.setMessage("Menyimpan data ...");
//                dialog.show();
//                dialog.setCancelable(false);
//
//                saveOrder();
//
//
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        setTotalOrderTab();
//    }
//
//    private void setEnabledMenuItem(MenuItem item, boolean flag) {
//        item.setEnabled(flag);
//    }
//
//    public void setTotalOrderTab() {
//        if (orderId != null) {
//            totalPrice = 0;
//            orderMenus = orderMenuDbAdapter.findOrderMenuByOrderId(orderId);
//
//            for (OrderMenu om : orderMenus) {
//                totalPrice += om.getProduct().getSellPrice() * om.getQty();
//            }
//
//            textTotalItem.setText("Jumlah Item: " + orderMenus.size());
//            textTotalOrder.setText("Total Order: " + "Rp " + decimalFormat.format(totalPrice));
//        }
//    }
//
//    private void saveOrder() {
////        orderId = orderDbAdapter.getOrderId();
//        requestOrderSyncTask = new RequestOrderSyncTask(OrderListActivityMaterial.this,
//                OrderListActivityMaterial.this, orderId);
//        requestOrderSyncTask.execute();
//
//    }
//
//    @Override
//    public void onExecute(int code) {
//
//    }
//
//    @Override
//    public void onSuccess(int code, Object result) {
//        Log.d(getClass().getSimpleName(), result + "");
//
//        if (result != null) {
//            if (code == SignageVariables.REQUEST_ORDER) {
//                Log.d(getClass().getSimpleName(), result + ">> RequestOrderSyncTask * Success");
//                Order order = orderDbAdapter.findOrderById(orderId);
//
//                Log.d(getClass().getSimpleName(), "Order ID : " + orderId
//                        + " Update Parameter : >> RefId " + order.getRefId()
//                        + " \n>> EntittyOrderId : " + order.getId() + " | "
//                        + preferences.getString("server_url", ""));
//
//                jobManager.addJobInBackground(new OrderUpdateJob(order.getRefId(), order.getId(),
//                        preferences.getString("server_url", "")));
//            }
//        }
//    }
//
//    @Override
//    public void onCancel(int code, String message) {
//        Log.d(getClass().getSimpleName(), message);
//    }
//
//    @Override
//    public void onError(int code, String message) {
//        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
//        Log.e(getClass().getSimpleName(), message);
//    }
//
//    public void onEventMainThread(GenericEvent.RequestInProgress requestInProgress) {
//        Log.d(getClass().getSimpleName(), "RequestInProgress: " + requestInProgress.getProcessId());
//    }
//
//    public void onEventMainThread(GenericEvent.RequestSuccess requestSuccess) {
//        try {
//            switch (requestSuccess.getProcessId()) {
//                case ContactJob.PROCESS_ID: {
//                    Log.d(getClass().getSimpleName(), "Response ContactRefId : " + requestSuccess.getRefId());
//                    saveOrder();
//                    break;
//                }
//                case ContactUpdateJob.PROCESS_ID: {
//                    Log.d(getClass().getSimpleName(), "Response ContactRefId : " + requestSuccess.getRefId());
//                    saveOrder();
//                    break;
//                }
//                case OrderUpdateJob.PROCESS_ID: {
//                    Log.d(getClass().getSimpleName(), "RequestSuccess OrderUpdateJob: >> RefId : "
//                            + requestSuccess.getRefId() + "\n >> Entity id: " + requestSuccess.getEntityId());
//
//                    orderMenuIdes = orderMenuDbAdapter.findOrderMenuIdesByOrderId(requestSuccess.getEntityId());
//                    totalOrderMenus = orderMenuIdes.size();
//
//                    Log.d(getClass().getSimpleName(), "Order Menu Ides Size : " + orderMenuIdes.size());
//
//                    for (String id : orderMenuIdes) {
//                        Log.d(getClass().getSimpleName(), "ORDER REF ID : " + requestSuccess.getRefId());
//                        jobManager.addJobInBackground(new OrderMenuJob(requestSuccess.getRefId(), id,
//                                preferences.getString("server_url", "")));
//                    }
//
//                    break;
//                }
//                case OrderMenuJob.PROCESS_ID: {
//                    orderMenuCount++;
//                    Log.d(getClass().getSimpleName(), "Count OM: " + orderMenuCount + " <<>> "
//                            + "Total OM: " + totalOrderMenus);
//
//                    if (orderMenuCount == totalOrderMenus) {
//                        dialog.dismiss();
//                        dialogSuccessOrder();
//                        Log.d(getClass().getSimpleName(), "Success ");
//                    }
//
//                    Log.d(getClass().getSimpleName(), "RequestSuccess OrderMenuId: "
//                            + requestSuccess.getRefId());
//                    break;
//                }
//            }
//
//        } catch (Exception e) {
//            Log.e(getClass().getSimpleName(), e.getMessage(), e);
//        }
//    }
//
//    public void onEventMainThread(GenericEvent.RequestFailed failed) {
//        dialog.dismiss();
//        Toast.makeText(OrderListActivityMaterial.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
//        setEnabledMenuItem(item, true);
//        Log.e(getClass().getSimpleName(),
//                failed.getResponse().getHttpResponse().getStatusLine().getStatusCode() + " :"
//                        + failed.getResponse().getHttpResponse().getStatusLine().getReasonPhrase());
//    }
//
//    private void dialogSuccessOrder() {
//        View view = View.inflate(this, R.layout.view_add_to_cart, null);
//
//        TextView textItem = (TextView) view.findViewById(R.id.text_item_cart);
//
//        textItem.setText("Pesanan Anda sedang kami proses");
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setView(view);
//        builder.setTitle(R.string.order_success);
//        builder.setPositiveButton(getString(R.string.continue_shopping), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                clearBackStack();
//                finish();
//                startActivity(new Intent(OrderListActivityMaterial.this, MainActivity.class));
//            }
//        });
//
//        final AlertDialog dialog = builder.create();
//        dialog.show();
//        dialog.setCancelable(false);
//    }
//
//    private void clearBackStack() {
//        final FragmentManager fragmentManager = getFragmentManager();
//        while (fragmentManager.getBackStackEntryCount() != 0) {
//            fragmentManager.popBackStackImmediate();
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }
//
//
//}
