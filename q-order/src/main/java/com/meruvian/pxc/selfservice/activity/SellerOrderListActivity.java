package com.meruvian.pxc.selfservice.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.meruvian.pxc.selfservice.R;
import com.meruvian.pxc.selfservice.SignageVariables;
import com.meruvian.pxc.selfservice.adapter.SellerOrderAdapter;
import com.meruvian.pxc.selfservice.core.LogInformation;
import com.meruvian.pxc.selfservice.entity.BusinessPartner;
import com.meruvian.pxc.selfservice.entity.Contact;
import com.meruvian.pxc.selfservice.entity.Order;
import com.meruvian.pxc.selfservice.util.AuthenticationUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.meruvian.midas.core.service.TaskService;
import org.meruvian.midas.core.util.ConnectionUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by miftakhul on 12/8/15.
 */
public class SellerOrderListActivity extends AppCompatActivity implements TaskService {

    private List<Order> orderList = new ArrayList<Order>();
    private SharedPreferences preferences;
    private RecyclerView recyclerView;
    private SellerOrderAdapter sellerOrderAdapter;
    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;

    private String orderUrl = "/api/orders";
    private String orderType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_order_list);

        preferences = getSharedPreferences(SignageVariables.PREFS_SERVER, 0);

        toolbar = (Toolbar)findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (getIntent().getExtras().get("orderListType") != null){
            orderType = getIntent().getExtras().get("orderListType").toString();
            Log.d(getClass().getSimpleName(), "order type "+ orderType);

            if (getIntent().getExtras().get("orderListType").toString().equalsIgnoreCase("purchaseOrderList")){
                orderUrl = "/api/purchaseOrders";
            }else if (getIntent().getExtras().get("orderListType").toString().equalsIgnoreCase("orderList")){
                orderUrl = "/api/orders";
            }
        }

        sellerOrderAdapter = new SellerOrderAdapter(this, orderType);

        recyclerView = (RecyclerView) findViewById(R.id.order_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(sellerOrderAdapter);

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
                OrderSync orderSync = new OrderSync(SellerOrderListActivity.this, SellerOrderListActivity.this);
                orderSync.execute();
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                super.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onExecute(int code) {

    }

    @Override
    public void onSuccess(int code, Object result) {
        swipeRefreshLayout.setRefreshing(false);
        sellerOrderAdapter = new SellerOrderAdapter(this, orderList, orderType);
        recyclerView.setAdapter(sellerOrderAdapter);



    }

    @Override
    public void onCancel(int code, String message) {

    }

    @Override
    public void onError(int code, String message) {

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
            return ConnectionUtil.get(preferences.getString("server_url_point", "") + orderUrl+"?access_token="
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

                    List<Order> orders = new ArrayList<Order>();

                    Log.d("result order =====", result.toString());
                    JSONArray jsonArray = result.getJSONArray("content");
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject object = jsonArray.getJSONObject(a);

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
                        if (!object.isNull("contact")){
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

                            JSONObject businessPartnerObject = new JSONObject();
                            if (!contactObject.isNull("businessPartner")){
                                businessPartnerObject = contactObject.getJSONObject("businessPartner");
                                BusinessPartner businessPartner = new BusinessPartner();
                                businessPartner.setId(businessPartnerObject.getString("id"));
                                businessPartner.setName(businessPartnerObject.getString("name"));
                                businessPartner.setEmail(businessPartnerObject.getString("email"));
                                businessPartner.setAddress(businessPartnerObject.getString("address"));
                                businessPartner.setZipCode(businessPartnerObject.getString("zipCode"));

                                contact.setBusinessPartner(businessPartner);
                            }
                            order.setContact(contact);
                        }

                        orders.add(order);
                    }

                    orderList = orders;

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

    private void refreshContent(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                OrderSync orderSync = new OrderSync(SellerOrderListActivity.this, SellerOrderListActivity.this);
                orderSync.execute();
            }
        },2000);

    }

}
