package com.hoqii.sales.selfservice.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hoqii.sales.selfservice.R;
import com.hoqii.sales.selfservice.SignageAppication;
import com.hoqii.sales.selfservice.SignageVariables;
import com.hoqii.sales.selfservice.content.database.adapter.ContactDatabaseAdapter;
import com.hoqii.sales.selfservice.content.database.adapter.OrderDatabaseAdapter;
import com.hoqii.sales.selfservice.content.database.adapter.OrderMenuDatabaseAdapter;
import com.hoqii.sales.selfservice.entity.Contact;
import com.hoqii.sales.selfservice.entity.Order;
import com.hoqii.sales.selfservice.event.GenericEvent;
import com.hoqii.sales.selfservice.job.ContactJob;
import com.hoqii.sales.selfservice.job.ContactUpdateJob;
import com.hoqii.sales.selfservice.job.OrderMenuJob;
import com.hoqii.sales.selfservice.job.OrderUpdateJob;
import com.hoqii.sales.selfservice.task.RequestOrderSyncTask;
import com.hoqii.sales.selfservice.util.AuthenticationUtils;
import com.path.android.jobqueue.JobManager;

import org.meruvian.midas.core.service.TaskService;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

/**
 * Created by meruvian on 04/08/15.
 */
public class ContactActivity extends ActionBarActivity implements TaskService {
    @InjectView(R.id.spinner_address_label)
    Spinner addressLabel;
    @InjectView(R.id.edit_contact_name)
    EditText editContactName;
    @InjectView(R.id.edit_receive_name)
    EditText editReceiveName;
    @InjectView(R.id.edit_receive_telp)
    EditText editReceiveTelp;

    @InjectView(R.id.edit_province)
    EditText editProvince;
    @InjectView(R.id.edit_city)
    EditText editCity;
    @InjectView(R.id.edit_sub_district)
    EditText editSubDistrict;

    /*@InjectView(R.id.spinner_province)
    Spinner province;
    @InjectView(R.id.spinner_city)
    Spinner city;
    @InjectView(R.id.spinner_sub_district)
    Spinner subDistrict;*/

    @InjectView(R.id.edit_street)
    EditText editStreet;
    @InjectView(R.id.edit_pos_code)
    EditText editPosCode;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    private ContactDatabaseAdapter contactDbAdapter;
    private OrderDatabaseAdapter orderDbAdapter;
    private OrderMenuDatabaseAdapter orderMenuDbAdapter;
    private RequestOrderSyncTask requestOrderSyncTask;
    private JobManager jobManager;

    private String orderId = null, contactId = null, addressId = null;
    private SharedPreferences preferences;
    private List<String> orderMenuIdes;
    private List<Contact> contacts = new ArrayList<Contact>();
    private int orderMenuCount = 0;
    private int totalOrderMenus = 0;
    private String orderRefId;
    private ProgressDialog dialog;
    private MenuItem item;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.order, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.item = item;
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.menu_pay_order:
//                item.setEnabled(false);
                setEnabledMenuItem(item, false);
                dialog = new ProgressDialog(this);
                dialog.setMessage("Menyimpan data ...");
                dialog.show();
                dialog.setCancelable(false);

                contactId = saveContact(addressId);

                if (addressId == null) {
                    syncContact();
                } else {
                    Contact contact = contactDbAdapter.findContactById(contactId);
                    syncUpdateContact(contact.getRefId());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        ButterKnife.inject(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Bundle bundle = getIntent().getExtras();
        orderId = bundle.getString("order_id");
        Log.d(getClass().getSimpleName(), "Bundle Order ID : " + orderId);

        preferences = this.getSharedPreferences(SignageVariables.PREFS_SERVER, 0);
        EventBus.getDefault().register(this);
        contactDbAdapter = new ContactDatabaseAdapter(this);
        orderDbAdapter = new OrderDatabaseAdapter(this);
        orderMenuDbAdapter = new OrderMenuDatabaseAdapter(this);

        findAllContact();
        ArrayAdapter<Contact> contactArrayAdapter = new ArrayAdapter<Contact>(this, android.support.v7.appcompat.R.layout.support_simple_spinner_dropdown_item, contacts);
        addressLabel.setAdapter(contactArrayAdapter);
        addressLabel.setSelection(0);
        addressLabel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Contact contact = contacts.get(position);
                if (contact.getContactName().equals("- Kontak Baru -")) {
                    if (contacts.size() > 1) {
                        addressId = null;
                        editContactName.getText().clear();
                        editReceiveName.getText().clear();
                        editReceiveTelp.getText().clear();
                        editProvince.getText().clear();
                        editCity.getText().clear();
                        editSubDistrict.getText().clear();
                        editStreet.getText().clear();
                        editPosCode.getText().clear();
                    }
                } else {
                    addressId = contact.getId();
                    editContactName.setText(contact.getContactName());
                    editReceiveName.setText(contact.getRecipient());
                    editReceiveTelp.setText(contact.getPhone());
                    editProvince.setText(contact.getProvince());
                    editCity.setText(contact.getCity());
                    editSubDistrict.setText(contact.getSubDistrict());
                    editStreet.setText(contact.getAddress());
                    editPosCode.setText(contact.getZip());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Contact contact = contacts.get(addressLabel.getSelectedItemPosition());
                if (contact.getContactName().equals("- Kontak Baru -")) {
                    if (contacts.size() > 1) {
                        addressId = null;
                        editContactName.getText().clear();
                        editReceiveName.getText().clear();
                        editReceiveTelp.getText().clear();
                        editProvince.getText().clear();
                        editCity.getText().clear();
                        editSubDistrict.getText().clear();
                        editStreet.getText().clear();
                        editPosCode.getText().clear();
                    }
                } else {
                    addressId = contact.getId();
                    editContactName.setText(contact.getContactName());
                    editReceiveName.setText(contact.getRecipient());
                    editReceiveTelp.setText(contact.getPhone());
                    editProvince.setText(contact.getProvince());
                    editCity.setText(contact.getCity());
                    editSubDistrict.setText(contact.getSubDistrict());
                    editStreet.setText(contact.getAddress());
                    editPosCode.setText(contact.getZip());
                }
            }
        });

        jobManager = SignageAppication.getInstance().getJobManager();

    }

    private String saveContact(String id) {
        Contact contact = new Contact();
        Log.d(getClass().getSimpleName(), " id : " + id);

        contact.getLogInformation().setCreateBy(AuthenticationUtils.getCurrentAuthentication().getUser().getId());
        contact.getLogInformation().setLastUpdateBy(AuthenticationUtils.getCurrentAuthentication().getUser().getId());

        contact.setId(id);
        contact.getUser().setId(AuthenticationUtils.getCurrentAuthentication().getUser().getId());
        contact.setContactName(editContactName.getText().toString());
        contact.setRecipient(editReceiveName.getText().toString());
        contact.setPhone(editReceiveTelp.getText().toString());
        contact.setCity(editCity.getText().toString());
        contact.setSubDistrict(editSubDistrict.getText().toString());
        contact.setProvince(editProvince.getText().toString());
        contact.setZip(editPosCode.getText().toString());
        contact.setAddress(editStreet.getText().toString());

        return contactDbAdapter.updateContact(contact);
    }

    private void saveOrder(String contactId) {
//        orderId = orderDbAdapter.getOrderId();
        requestOrderSyncTask = new RequestOrderSyncTask(ContactActivity.this, ContactActivity.this, orderId, contactId);
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

                Log.d(getClass().getSimpleName(), "Order ID : " + orderId + " Update Parameter : >> RefId " + order.getRefId()
                        + " \n>> EntittyOrderId : " + order.getId() + " | " + preferences.getString("server_url", ""));

                jobManager.addJobInBackground(new OrderUpdateJob(order.getRefId(), order.getId(), preferences.getString("server_url", "")));
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
                        jobManager.addJobInBackground(new OrderMenuJob(requestSuccess.getRefId(), id, preferences.getString("server_url", "")));
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

                    Log.d(getClass().getSimpleName(), "RequestSuccess OrderMenuId: " + requestSuccess.getRefId());
                    break;
                }
            }

        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    public void onEventMainThread(GenericEvent.RequestFailed failed) {
        dialog.dismiss();
        Toast.makeText(ContactActivity.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
        setEnabledMenuItem(item, true);
        Log.e(getClass().getSimpleName(),
                failed.getResponse().getHttpResponse().getStatusLine().getStatusCode() + " :"
                        + failed.getResponse().getHttpResponse().getStatusLine().getReasonPhrase());
    }

    public void syncContact() {
        jobManager.addJobInBackground(new ContactJob(contactId, preferences.getString("server_url", "")));
    }

    public void syncUpdateContact(String contactRefId) {
        jobManager.addJobInBackground(new ContactUpdateJob(contactRefId, contactId, preferences.getString("server_url", "")));
    }

    private void findAllContact() {
        contacts = contactDbAdapter.findAllContact(AuthenticationUtils.getCurrentAuthentication().getUser().getId());

        Contact contact = new Contact();
        contact.setContactName("- Kontak Baru -");
        contacts.add(contact);
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
                finish();
                startActivity(new Intent(ContactActivity.this, MainActivity.class));
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);
    }

    private void setEnabledMenuItem(MenuItem item, boolean flag) {
        item.setEnabled(flag);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}