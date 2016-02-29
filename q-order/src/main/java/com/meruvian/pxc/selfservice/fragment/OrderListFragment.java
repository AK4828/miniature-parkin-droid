package com.meruvian.pxc.selfservice.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.meruvian.pxc.selfservice.R;
import com.meruvian.pxc.selfservice.SignageAppication;
import com.meruvian.pxc.selfservice.SignageVariables;
import com.meruvian.pxc.selfservice.activity.MainActivity;
import com.meruvian.pxc.selfservice.adapter.OrderListActAdapter;
import com.meruvian.pxc.selfservice.content.database.adapter.ContactDatabaseAdapter;
import com.meruvian.pxc.selfservice.content.database.adapter.OrderDatabaseAdapter;
import com.meruvian.pxc.selfservice.content.database.adapter.OrderMenuDatabaseAdapter;
import com.meruvian.pxc.selfservice.entity.Contact;
import com.meruvian.pxc.selfservice.entity.Order;
import com.meruvian.pxc.selfservice.entity.OrderMenu;
import com.meruvian.pxc.selfservice.entity.Transaction;
import com.meruvian.pxc.selfservice.event.GenericEvent;
import com.meruvian.pxc.selfservice.job.ContactJob;
import com.meruvian.pxc.selfservice.job.ContactUpdateJob;
import com.meruvian.pxc.selfservice.job.OrderMenuJob;
import com.meruvian.pxc.selfservice.job.OrderUpdateJob;
import com.meruvian.pxc.selfservice.job.PointJob;
import com.meruvian.pxc.selfservice.service.JobStatus;
import com.meruvian.pxc.selfservice.service.PointService;
import com.meruvian.pxc.selfservice.task.RequestOrderSyncTask;
import com.path.android.jobqueue.JobManager;

import org.meruvian.midas.core.service.TaskService;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by miftakhul on 12/4/15.
 */
public class OrderListFragment extends Fragment implements TaskService{

    private Toolbar toolbar;
    private ListView listOrder;
    private TextView textTotalItem;
    private TextView textTotalOrder;
    private TextView textContact;

    private OrderDatabaseAdapter orderDbAdapter;
    private OrderMenuDatabaseAdapter orderMenuDbAdapter;
    private ContactDatabaseAdapter contactDbAdapter;

    private OrderListActAdapter orderListAdapter;


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
    private String orderId = null, contactId = null, addressId = null;
    private DecimalFormat decimalFormat = new DecimalFormat("#,###");
    private long totalPrice;
    private double point;

    private TextView text_total_item;
    private TextView textTotalPoint;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);

        listOrder = (ListView) view.findViewById(R.id.list_order);
        textTotalItem = (TextView) view.findViewById(R.id.text_total_item);
        textTotalOrder = (TextView) view.findViewById(R.id.text_total_order);
        textTotalPoint = (TextView) view.findViewById(R.id.text_total_point);


        jobManager = SignageAppication.getInstance().getJobManager();
        preferences = getActivity().getSharedPreferences(SignageVariables.PREFS_SERVER, 0);


        orderDbAdapter = new OrderDatabaseAdapter(getActivity());
        orderMenuDbAdapter = new OrderMenuDatabaseAdapter(getActivity());
        contactDbAdapter = new ContactDatabaseAdapter(getActivity());

        orderListAdapter = new OrderListActAdapter(getActivity(), R.layout.adapter_order_list,
                new ArrayList<OrderMenu>(), this);

        orderId = orderDbAdapter.getOrderId();

        if (orderId != null) {
            totalPrice = 0;

            orderMenus = orderMenuDbAdapter.findOrderMenuByOrderId(orderId);

            for (OrderMenu om : orderMenus) {
                totalPrice += om.getProduct().getSellPrice() * om.getQty();
            }

            textTotalItem.setText("Jumlah Item: " + orderMenus.size());
            textTotalOrder.setText("Total Order: "+ decimalFormat.format(totalPrice) + " Pt");
            textTotalPoint.setText("Total Point: "+ decimalFormat.format(preferences.getLong("default_point", 0)));

            orderListAdapter.addAll(orderMenus);
        }

        listOrder.setAdapter(orderListAdapter);

        return view;
    }

    public void onEventMainThread(PointJob.PointEvent event) {
        int status = event.getStatus();
        point = event.getPoint();

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.order_list, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.item = item;
        switch (item.getItemId()) {

            case R.id.menu_pay_order:

                orderId = orderDbAdapter.getOrderId();
                orderMenus = orderMenuDbAdapter.findOrderMenuByOrderId(orderId);

                if (orderMenus.size() <=0) {
                    Toast.makeText(getActivity(), "Order is empty", Toast.LENGTH_SHORT).show();
                } else if (point < totalPrice){
                    Toast.makeText(getActivity(),"Jumlah order anda melebihi point yang dimiliki", Toast.LENGTH_LONG).show();
                } else {
                    setEnabledMenuItem(item, false);
                    dialog = new ProgressDialog(getActivity());
                    dialog.setMessage("Menyimpan data ...");
                    dialog.show();
                    dialog.setCancelable(false);

                    saveOrder();
                }

            return true;

            case R.id.menu_add_order :
                ((MainActivity)getActivity()).orderOption();

                EventBus.getDefault().unregister(this);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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
        if (orderId != null) {
            totalPrice = 0;
            orderMenus = orderMenuDbAdapter.findOrderMenuByOrderId(orderId);

            for (OrderMenu om : orderMenus) {
                totalPrice += om.getProduct().getSellPrice() * om.getQty();
            }

            textTotalItem.setText("Jumlah Item: " + orderMenus.size());
            textTotalOrder.setText("Total Order: " + decimalFormat.format(totalPrice) + " Pt");
        }
    }

    private void saveOrder() {
        requestOrderSyncTask = new RequestOrderSyncTask(getActivity(), OrderListFragment.this, orderId);
        requestOrderSyncTask.execute();

    }

    public void onEventMainThread(GenericEvent.RequestInProgress requestInProgress) {
        Log.d(getClass().getSimpleName(), "RequestInProgress: " + requestInProgress.getProcessId());
    }

    public void onEventMainThread(GenericEvent.RequestSuccess requestSuccess) {
        try {
            switch (requestSuccess.getProcessId()) {
                case ContactJob.PROCESS_ID: {
                    saveOrder();
                    break;
                }
                case ContactUpdateJob.PROCESS_ID: {
                    saveOrder();
                    break;
                }
                case OrderUpdateJob.PROCESS_ID: {
                    orderMenuIdes = orderMenuDbAdapter.findOrderMenuIdesByOrderId(requestSuccess.getEntityId());
                    totalOrderMenus = orderMenuIdes.size();


                    for (String id : orderMenuIdes) {
                        Log.d(getClass().getSimpleName(), "ORDER REF ID : " + requestSuccess.getRefId());
                        jobManager.addJobInBackground(new OrderMenuJob(requestSuccess.getRefId(), id,
                                preferences.getString("server_url_point", "")));
                    }
                    break;
                }
                case OrderMenuJob.PROCESS_ID: {
                    orderMenuCount++;

                    if (orderMenuCount == totalOrderMenus) {

                        Order order = orderDbAdapter.findOrderById(orderId);
                        String status = "done";
                        Transaction transaction = new Transaction(status, order.getRefId());
                        SignageAppication application = SignageAppication.getInstance();
                        PointService pointService= application.getRetrofit().create(PointService.class);

                        Call<Transaction> sendStatus = pointService.sendOrderStatus(transaction);
                        sendStatus.enqueue(new Callback<Transaction>() {
                            @Override
                            public void onResponse(Response<Transaction> response, Retrofit retrofit) {

                            }

                            @Override
                            public void onFailure(Throwable t) {

                            }
                        });

                        dialog.dismiss();
                        dialogSuccessOrder();

                    }

                    break;
                }
            }

        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    public void onEventMainThread(GenericEvent.RequestFailed failed) {
        dialog.dismiss();
        Toast.makeText(getActivity(), "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
        setEnabledMenuItem(item, true);
        Log.e(getClass().getSimpleName(),
                failed.getResponse().getHttpResponse().getStatusLine().getStatusCode() + " :"
                        + failed.getResponse().getHttpResponse().getStatusLine().getReasonPhrase());
    }

    private void dialogSuccessOrder() {
        View view = View.inflate(getActivity(), R.layout.view_add_to_cart, null);
        TextView textItem = (TextView) view.findViewById(R.id.text_item_cart);
        textItem.setText("Pesanan Anda sedang kami proses");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setTitle(R.string.order_success);
        builder.setPositiveButton(getString(R.string.continue_shopping), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                OrderListFragment categoryFragmentGrid = new OrderListFragment();
                FragmentTransaction orderList = getFragmentManager().beginTransaction();
                orderList.replace(R.id.content_frame, categoryFragmentGrid);
                orderList.addToBackStack(null);
                orderList.commitAllowingStateLoss();
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
                        + preferences.getString("server_url_point", ""));

                jobManager.addJobInBackground(new OrderUpdateJob(order.getRefId(), order.getId(),
                        preferences.getString("server_url_point", "")));
            }
        }
    }

    @Override
    public void onCancel(int code, String message) {
        Log.d(getClass().getSimpleName(), message);
    }

    @Override
    public void onError(int code, String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
        Log.e(getClass().getSimpleName(), message);
    }
}
