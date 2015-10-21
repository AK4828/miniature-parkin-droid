package com.hoqii.sales.selfservice.activity;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hoqii.sales.selfservice.R;
import com.hoqii.sales.selfservice.SignageAppication;
import com.hoqii.sales.selfservice.SignageVariables;
import com.hoqii.sales.selfservice.adapter.ContactOrderAdapter;
import com.hoqii.sales.selfservice.adapter.OrderListActAdapter;
import com.hoqii.sales.selfservice.content.database.adapter.ContactDatabaseAdapter;
import com.hoqii.sales.selfservice.content.database.adapter.OrderDatabaseAdapter;
import com.hoqii.sales.selfservice.content.database.adapter.OrderMenuDatabaseAdapter;
import com.hoqii.sales.selfservice.entity.Contact;
import com.hoqii.sales.selfservice.entity.Order;
import com.hoqii.sales.selfservice.entity.OrderMenu;
import com.hoqii.sales.selfservice.event.GenericEvent;
import com.hoqii.sales.selfservice.job.ContactJob;
import com.hoqii.sales.selfservice.job.ContactUpdateJob;
import com.hoqii.sales.selfservice.job.OrderMenuJob;
import com.hoqii.sales.selfservice.job.OrderUpdateJob;
import com.hoqii.sales.selfservice.task.RequestOrderSyncTask;
import com.hoqii.sales.selfservice.util.AuthenticationUtils;
import com.path.android.jobqueue.JobManager;

import org.meruvian.midas.core.defaults.DefaultActivity;
import org.meruvian.midas.core.service.TaskService;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import de.greenrobot.event.EventBus;

/**
 * Created by meruvian on 30/09/15.
 */
public class OrderListActivity extends DefaultActivity implements TaskService {
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.list_order)
    ListView listOrder;
    @InjectView(R.id.text_total_item)
    TextView textTotalItem;
    @InjectView(R.id.text_total_order)
    TextView textTotalOrder;
    @InjectView(R.id.text_contact_order)
    TextView textContact;

    private OrderDatabaseAdapter orderDbAdapter;
    private OrderMenuDatabaseAdapter orderMenuDbAdapter;
    private ContactDatabaseAdapter contactDbAdapter;

    private OrderListActAdapter orderListAdapter;
    private ContactOrderAdapter contactAdapter;

    private RequestOrderSyncTask requestOrderSyncTask;

    private JobManager jobManager;
    private SharedPreferences preferences;
    private ProgressDialog dialog;
    private MenuItem item;

    private List<OrderMenu> orderMenus = new ArrayList<OrderMenu>();
    private List<Contact> contacts = new ArrayList<Contact>();
    private List<String> orderMenuIdes;

    private Contact contact = null;

    private int orderMenuCount = 0;
    private int totalOrderMenus = 0;
    private int positionItem = -1;
    private String orderId= null, contactId = null, addressId = null;
    private DecimalFormat decimalFormat = new DecimalFormat("#,###");
    private long totalPrice;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contact_submit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.item = item;
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.menu_add_contact:
                dialogContact();
                return true;
            case R.id.menu_submit_order:
                if (contactId == null) {
                    Toast.makeText(OrderListActivity.this, "Anda belum memilih pembeli !",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(getClass().getSimpleName(), "SaveOrder.RefId: " + contact.getRefId());

                    setEnabledMenuItem(item, false);
                    dialog = new ProgressDialog(this);
                    dialog.setMessage("Menyimpan data ...");
                    dialog.show();
                    dialog.setCancelable(false);

                    saveOrder(contact.getRefId());
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected int layout() {
        return R.layout.activity_order_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(Bundle bundle) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        jobManager = SignageAppication.getInstance().getJobManager();
        preferences = this.getSharedPreferences(SignageVariables.PREFS_SERVER, 0);
        EventBus.getDefault().register(this);

        orderDbAdapter = new OrderDatabaseAdapter(this);
        orderMenuDbAdapter = new OrderMenuDatabaseAdapter(this);
        contactDbAdapter = new ContactDatabaseAdapter(this);

        orderListAdapter = new OrderListActAdapter(this, R.layout.adapter_order_list,
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

    public void dialogContact() {
        View view = View.inflate(this, R.layout.view_contact_list, null);
        final ListView listContact = (ListView) view.findViewById(R.id.list_contacts_order);
        listContact.setItemsCanFocus(false);
        contactAdapter = new ContactOrderAdapter(this,
                R.layout.adapter_contact_order, new ArrayList<Contact>());
        listContact.setAdapter(contactAdapter);
        contactAdapter.setItemSelected(positionItem);

        contactAdapter.addAll(contactDbAdapter.findAllContact(
                AuthenticationUtils.getCurrentAuthentication().getUser().getId()));

        listContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                contactAdapter.setItemSelected(position);
                positionItem = position;
                contact = new Contact();
                contact = contactAdapter.getItem(position);
                contactId = contact.getId();
                Log.d(getClass().getSimpleName(), "Position: " + position
                        + "contact name: " + contact.getRecipient());
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setTitle(R.string.select_contact);
        builder.setPositiveButton(R.string.select, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (contact != null) {
                    textContact.setText("Pembeli: " + contact.getRecipient()
                            + " (" + contact.getPhone() + ")");

                    dialog.dismiss();
                } else {
                    Toast.makeText(OrderListActivity.this, "Anda belum memilih pembeli",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }

    @Override
    public void onResume() {
        super.onResume();
        setTotalOrderTab();
    }

    private void setEnabledMenuItem(MenuItem item, boolean flag) {
        item.setEnabled(flag);
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

    private void saveOrder(String contactId) {
//        orderId = orderDbAdapter.getOrderId();
        requestOrderSyncTask = new RequestOrderSyncTask(OrderListActivity.this,
                OrderListActivity.this, orderId, contactId);
        requestOrderSyncTask.execute();

    }

    @Override
    public void onExecute(int code) {

    }

    @Override
    public void onSuccess(int code, Object result) {
        Log.d(getClass().getSimpleName(), result + "");

        if (result != null) {
            if (code == SignageVariables.REQUEST_ORDER) {
                Log.d(getClass().getSimpleName(), result + ">> RequestOrderSyncTask * Success");
                Order order = orderDbAdapter.findOrderById(orderId);

                Log.d(getClass().getSimpleName(), "Order ID : " + orderId
                        + " Update Parameter : >> RefId " + order.getRefId()
                        + " \n>> EntittyOrderId : " + order.getId() + " | "
                        + preferences.getString("server_url", ""));

                jobManager.addJobInBackground(new OrderUpdateJob(order.getRefId(), order.getId(),
                        preferences.getString("server_url", "")));
            }
        }
    }

    @Override
    public void onCancel(int code, String message) {
        Log.d(getClass().getSimpleName(), message);
    }

    @Override
    public void onError(int code, String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        Log.e(getClass().getSimpleName(), message);
    }

    public void onEventMainThread(GenericEvent.RequestInProgress requestInProgress) {
        Log.d(getClass().getSimpleName(), "RequestInProgress: " + requestInProgress.getProcessId());
    }

    public void onEventMainThread(GenericEvent.RequestSuccess requestSuccess) {
        try {
            switch (requestSuccess.getProcessId()) {
                case ContactJob.PROCESS_ID: {
                    Log.d(getClass().getSimpleName(), "Response ContactRefId : " + requestSuccess.getRefId());
                    saveOrder(requestSuccess.getRefId());
                    break;
                }
                case ContactUpdateJob.PROCESS_ID: {
                    Log.d(getClass().getSimpleName(), "Response ContactRefId : " + requestSuccess.getRefId());
                    saveOrder(requestSuccess.getRefId());
                    break;
                }
                case OrderUpdateJob.PROCESS_ID: {
                    Log.d(getClass().getSimpleName(), "RequestSuccess OrderUpdateJob: >> RefId : "
                            + requestSuccess.getRefId() + "\n >> Entity id: " + requestSuccess.getEntityId());

                    orderMenuIdes = orderMenuDbAdapter.findOrderMenuIdesByOrderId(requestSuccess.getEntityId());
                    totalOrderMenus = orderMenuIdes.size();

                    Log.d(getClass().getSimpleName(), "Order Menu Ides Size : " + orderMenuIdes.size());

                    for (String id : orderMenuIdes) {
                        Log.d(getClass().getSimpleName(), "ORDER REF ID : " + requestSuccess.getRefId());
                        jobManager.addJobInBackground(new OrderMenuJob(requestSuccess.getRefId(), id,
                                preferences.getString("server_url", "")));
                    }

                    break;
                }
                case OrderMenuJob.PROCESS_ID: {
                    orderMenuCount++;
                    Log.d(getClass().getSimpleName(), "Count OM: " + orderMenuCount + " <<>> "
                            + "Total OM: " + totalOrderMenus);

                    if (orderMenuCount == totalOrderMenus) {
                        dialog.dismiss();
                        dialogSuccessOrder();
                        Log.d(getClass().getSimpleName(), "Success ");
                    }

                    Log.d(getClass().getSimpleName(), "RequestSuccess OrderMenuId: "
                            + requestSuccess.getRefId());
                    break;
                }
            }

        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    public void onEventMainThread(GenericEvent.RequestFailed failed) {
        dialog.dismiss();
        Toast.makeText(OrderListActivity.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
        setEnabledMenuItem(item, true);
        Log.e(getClass().getSimpleName(),
                failed.getResponse().getHttpResponse().getStatusLine().getStatusCode() + " :"
                        + failed.getResponse().getHttpResponse().getStatusLine().getReasonPhrase());
    }

    private void dialogSuccessOrder() {
        View view = View.inflate(this, R.layout.view_add_to_cart, null);

        TextView textItem = (TextView) view.findViewById(R.id.text_item_cart);

        textItem.setText("Pesanan Anda sedang kami proses");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setTitle(R.string.order_success);
        builder.setPositiveButton(getString(R.string.continue_shopping), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                clearBackStack();
                finish();
                startActivity(new Intent(OrderListActivity.this, MainActivity.class));
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);
    }

    private void clearBackStack() {
        final FragmentManager fragmentManager = getFragmentManager();
        while (fragmentManager.getBackStackEntryCount() != 0) {
            fragmentManager.popBackStackImmediate();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
