package com.meruvian.pxc.selfservice.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.meruvian.pxc.selfservice.R;
import com.meruvian.pxc.selfservice.SignageAppication;
import com.meruvian.pxc.selfservice.SignageVariables;
import com.meruvian.pxc.selfservice.adapter.SellerOrderMenuAdapter;
import com.meruvian.pxc.selfservice.core.LogInformation;
import com.meruvian.pxc.selfservice.entity.Contact;
import com.meruvian.pxc.selfservice.entity.Order;
import com.meruvian.pxc.selfservice.entity.OrderMenu;
import com.meruvian.pxc.selfservice.entity.Product;
import com.meruvian.pxc.selfservice.entity.Shipment;
import com.meruvian.pxc.selfservice.event.GenericEvent;
import com.meruvian.pxc.selfservice.job.MenuUpdateJob;
import com.meruvian.pxc.selfservice.job.ShipmentJob;
import com.meruvian.pxc.selfservice.util.AuthenticationUtils;
import com.path.android.jobqueue.JobManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.meruvian.midas.core.service.TaskService;
import org.meruvian.midas.core.util.ConnectionUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;


/**
 * Created by miftakhul on 12/8/15.
 */
public class SellerOrderMenuListActivity extends AppCompatActivity implements TaskService {
    private int requestScannerCode = 123;

    private List<OrderMenu> orderMenuList = new ArrayList<OrderMenu>();
    private SharedPreferences preferences;
    private RecyclerView recyclerView;
    private SellerOrderMenuAdapter sellerOrderMenuAdapter;
    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String orderId;
    private CoordinatorLayout coordinatorLayout;
    private TextView omDate, omReceipt, omBusinessPartner;
    private String orderUrl = "/api/orders/";

    private JobManager jobManager;
    private Order orderShip = new Order();

    private List<String> orderMenuListSerial = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_order_menu_list);

        preferences = getSharedPreferences(SignageVariables.PREFS_SERVER, 0);
        orderId = getIntent().getExtras().getString("orderId");

        if (getIntent().getExtras().get("orderMenuListType") != null) {
            Log.d(getClass().getSimpleName(), getIntent().getExtras().get("orderMenuListType").toString());

            if (getIntent().getExtras().get("orderMenuListType").toString().equalsIgnoreCase("purchaseOrderMenuList")) {
                orderUrl = "/api/purchaseOrders/";
            } else if (getIntent().getExtras().get("orderMenuListType").toString().equalsIgnoreCase("orderMenuList")) {
                orderUrl = "/api/orders/";
            }
        }

        jobManager = SignageAppication.getInstance().getJobManager();
        EventBus.getDefault().register(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mengirim Shipment");

        omDate = (TextView) findViewById(R.id.om_date);
        omReceipt = (TextView) findViewById(R.id.om_receipt);
//        omBusinessPartner = (TextView) findViewById(R.id.om_business);

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordianotorLayout);

        sellerOrderMenuAdapter = new SellerOrderMenuAdapter(this, orderId);

        recyclerView = (RecyclerView) findViewById(R.id.orderMenu_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(sellerOrderMenuAdapter);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiptRefress);
        swipeRefreshLayout.setColorSchemeResources(R.color.green, R.color.yellow, R.color.blue, R.color.red);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });


        new Handler().post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                OrderSync orderSync = new OrderSync(SellerOrderMenuListActivity.this, SellerOrderMenuListActivity.this);
                orderSync.execute(orderId);

                OrderMenuSync orderMenuSync = new OrderMenuSync(SellerOrderMenuListActivity.this, SellerOrderMenuListActivity.this);
                orderMenuSync.execute(orderId);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.shipment_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onExecute(int code) {

    }

    @Override
    public void onSuccess(int code, Object result) {
        swipeRefreshLayout.setRefreshing(false);
        sellerOrderMenuAdapter = new SellerOrderMenuAdapter(this, orderId, orderMenuList, orderMenuListSerial);
        recyclerView.setAdapter(sellerOrderMenuAdapter);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        date.setTime(getIntent().getLongExtra("orderDate", 0));

        omDate.setText(simpleDateFormat.format(date).toString());
        omReceipt.setText("Order Number : " + getIntent().getExtras().getString("orderReceipt"));

    }

    @Override
    public void onCancel(int code, String message) {

    }

    @Override
    public void onError(int code, String message) {

    }


    class OrderMenuSync extends AsyncTask<String, Void, JSONObject> {

        private Context context;
        private TaskService taskService;

        public OrderMenuSync(Context context, TaskService taskService) {
            this.context = context;
            this.taskService = taskService;
        }


        @Override
        protected JSONObject doInBackground(String... JsonObject) {
            Log.d(getClass().getSimpleName(), "?acces_token= " + AuthenticationUtils.getCurrentAuthentication().getAccessToken());
            Log.d(getClass().getSimpleName(), " param : " + JsonObject[0]);
            return ConnectionUtil.get(preferences.getString("server_url_point", "") + orderUrl + JsonObject[0] + "/menus?access_token="
                    + AuthenticationUtils.getCurrentAuthentication().getAccessToken());
        }

        @Override
        protected void onCancelled() {
            taskService.onCancel(SignageVariables.SELLER_ORDER_MENU_GET_TASK, "Batal");
        }

        @Override
        protected void onPreExecute() {
            taskService.onExecute(SignageVariables.SELLER_ORDER_MENU_GET_TASK);
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            try {
                if (result != null) {

                    List<OrderMenu> orderMenus = new ArrayList<OrderMenu>();

                    Log.d("result order =====", result.toString());
                    JSONArray jsonArray = result.getJSONArray("content");
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject object = jsonArray.getJSONObject(a);

                        OrderMenu orderMenu = new OrderMenu();
                        orderMenu.setId(object.getString("id"));
                        orderMenu.setQty(object.getInt("qty"));
                        orderMenu.setDescription(object.getString("description"));


                        JSONObject productObject = new JSONObject();
                        if (!object.isNull("product")) {
                            productObject = object.getJSONObject("product");

                            Product product = new Product();
                            product.setId(productObject.getString("id"));
                            product.setName(productObject.getString("name"));

                            orderMenu.setProduct(product);

                        }

                        orderMenus.add(orderMenu);

                    }

                    orderMenuList = orderMenus;

                    taskService.onSuccess(SignageVariables.SELLER_ORDER_MENU_GET_TASK, true);
                } else {
                    taskService.onError(SignageVariables.SELLER_ORDER_MENU_GET_TASK, "Error");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                taskService.onError(SignageVariables.SELLER_ORDER_MENU_GET_TASK, "Error");
            }


        }
    }


    class OrderSync extends AsyncTask<String, Void, JSONObject> {

        private Context context;
        private TaskService taskService;

        public OrderSync(Context context, TaskService taskService) {
            this.context = context;
            this.taskService = taskService;
        }


        @Override
        protected JSONObject doInBackground(String... JsonObject) {
            Log.d(getClass().getSimpleName(), "?acces_token= " + AuthenticationUtils.getCurrentAuthentication().getAccessToken());
            return ConnectionUtil.get(preferences.getString("server_url_point", "") + orderUrl + JsonObject[0] + "?access_token="
                    + AuthenticationUtils.getCurrentAuthentication().getAccessToken());
        }

        @Override
        protected void onCancelled() {
            taskService.onCancel(SignageVariables.SELLER_ORDER_GET_TASK, "Batal");
        }

        @Override
        protected void onPreExecute() {
            taskService.onExecute(SignageVariables.SELLER_ORDER_GET_TASK);
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            try {
                if (result != null) {

//                    List<Order> orders = new ArrayList<Order>();

                    Log.d("result order =====", result.toString());
//                    JSONArray jsonArray = result.getJSONArray("content");
//                    for (int a = 0; a < jsonArray.length(); a++) {
//                        JSONObject object = jsonArray.getJSONObject(a);
                    JSONObject object = result;

                    Order order = new Order();
                    order.setId(object.getString("id"));
                    order.setReceiptNumber(object.getString("receiptNumber"));

                    JSONObject logInformationObject = new JSONObject();
                    if (!object.isNull("logInformation")) {
                        logInformationObject = object.getJSONObject("logInformation");

                        LogInformation logInformation = new LogInformation();
                        logInformation.setCreateDate(new Date(logInformationObject.getLong("createDate")));

                        order.setLogInformation(logInformation);
                    }

                    JSONObject contactObject = new JSONObject();
                    if (!object.isNull("contact")) {
                        contactObject = object.getJSONObject("contact");

                        Contact contact = new Contact();
                        contact.setId(contactObject.getString("id"));
                        contact.setFirstName(contactObject.getString("firstName"));
                        contact.setLastName(contactObject.getString("lastName"));
                        contact.setOfficePhone(contactObject.getString("officePhone"));
                        contact.setMobile(contactObject.getString("mobile"));
                        contact.setHomePhone(contactObject.getString("homePhone"));
                        contact.setOtherPhone(contactObject.getString("otherPhone"));
                        contact.setEmail(contactObject.getString("email"));
                        contact.setOtherEmail(contactObject.getString("otherEmail"));

//                        JSONObject businessPartnerObject = new JSONObject();
//                        if (!contactObject.isNull("businessPartner")) {
//                            businessPartnerObject = contactObject.getJSONObject("businessPartner");
//                            BusinessPartner businessPartner = new BusinessPartner();
//                            businessPartner.setId(businessPartnerObject.getString("id"));
//                            businessPartner.setName(businessPartnerObject.getString("name"));
//                            businessPartner.setEmail(businessPartnerObject.getString("email"));
//                            businessPartner.setAddress(businessPartnerObject.getString("address"));
//                            businessPartner.setZipCode(businessPartnerObject.getString("zipCode"));
//
//                            contact.setBusinessPartner(businessPartner);
//                        }
                        order.setContact(contact);
                    }

//                        orders.add(order);
                    orderShip = order;

//                    }


                    taskService.onSuccess(SignageVariables.SELLER_ORDER_GET_TASK, true);
                } else {
                    taskService.onError(SignageVariables.SELLER_ORDER_GET_TASK, "Error");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                taskService.onError(SignageVariables.SELLER_ORDER_GET_TASK, "Error");
            }


        }
    }


    private void refreshContent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                OrderMenuSync orderMenuSync = new OrderMenuSync(SellerOrderMenuListActivity.this, SellerOrderMenuListActivity.this);
                orderMenuSync.execute(orderId);
            }
        }, 2000);

    }

    public void onEventMainThread(GenericEvent.RequestInProgress requestInProgress) {
        Log.d(getClass().getSimpleName(), "RequestInProgress: " + requestInProgress.getProcessId());
        switch (requestInProgress.getProcessId()){
            case ShipmentJob.PROCESS_ID:
                progressDialog.setTitle("shipping");
                break;

        }
    }

    public void onEventMainThread(GenericEvent.RequestSuccess requestSuccess) {
        Log.d(getClass().getSimpleName(), "success event : " + requestSuccess);
        try {
            switch (requestSuccess.getProcessId()) {
                case ShipmentJob.PROCESS_ID:
                    Log.d(getClass().getSimpleName(), "shipment sucess");
                    Log.d("yahiiii", String.valueOf(orderMenuList.size()));
                    for (OrderMenu om : orderMenuList) {
                        Log.d(getClass().getSimpleName(), "progress");
                        String orderMenuId = om.getId();
                        Log.d("CEK", orderMenuId);
                        jobManager.addJobInBackground(new MenuUpdateJob(preferences.getString("server_url_point",""), orderShip.getId(), orderMenuId));
                    }

                    progressDialog.dismiss();
                    AlertMessage("Proses selesai");
                    break;
            }

        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    public void onEventMainThread(GenericEvent.RequestFailed failed) {
        progressDialog.dismiss();

        switch (failed.getProcessId()){
            case ShipmentJob.PROCESS_ID:
                AlertMessage("Gagal mengirim shipment");
                break;
        }


        Log.e(getClass().getSimpleName(),
                failed.getResponse().getHttpResponse().getStatusLine().getStatusCode() + " :"
                        + failed.getResponse().getHttpResponse().getStatusLine().getReasonPhrase());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == requestScannerCode) {
            if (resultCode == RESULT_OK) {
                if (data != null) {

                    int position = data.getIntExtra("position", 0);
                    String orderMenuId = data.getStringExtra("orderMenuId");

                    orderMenuListSerial.add(orderMenuId);
                    sellerOrderMenuAdapter.addOrderMenuSerial(orderMenuId);
                    sellerOrderMenuAdapter.notifyItemChanged(position);

                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void AlertMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SellerOrderMenuListActivity.this);
        builder.setTitle("Shipment");
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

//    public void openScanner(Intent data) {
//        Intent scanner = new Intent(this, ScannerActivityCustom.class);
//        scanner.putExtra("productName", data.getStringExtra("productName"));
//        scanner.putExtra("orderId", orderId);
//        scanner.putExtra("orderMenuId", data.getStringExtra("orderMenuId"));
//        scanner.putExtra("position", data.getIntExtra("position", 0));
//
//        Log.d(getClass().getSimpleName(), "product name : " + data.getStringExtra("productName"));
//        Log.d(getClass().getSimpleName(), "order Menu id : " + data.getStringExtra("orderMenuId"));
//
//        startActivityForResult(scanner, requestScannerCode);
//    }

}
