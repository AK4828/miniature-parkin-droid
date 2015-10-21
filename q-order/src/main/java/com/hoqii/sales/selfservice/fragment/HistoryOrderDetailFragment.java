package com.hoqii.sales.selfservice.fragment;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.hoqii.sales.selfservice.R;
import com.hoqii.sales.selfservice.SignageAppication;
import com.hoqii.sales.selfservice.SignageVariables;
import com.hoqii.sales.selfservice.adapter.HistoryOrderDetailAdapter;
import com.hoqii.sales.selfservice.content.database.adapter.ContactDatabaseAdapter;
import com.hoqii.sales.selfservice.content.database.adapter.OrderDatabaseAdapter;
import com.hoqii.sales.selfservice.content.database.adapter.OrderMenuDatabaseAdapter;
import com.hoqii.sales.selfservice.entity.Contact;
import com.hoqii.sales.selfservice.entity.Order;
import com.hoqii.sales.selfservice.entity.OrderMenu;
import com.hoqii.sales.selfservice.entity.Payment;
import com.hoqii.sales.selfservice.job.PayGetJob;
import com.hoqii.sales.selfservice.job.PayUpdateJob;
import com.path.android.jobqueue.JobManager;

import org.meruvian.midas.core.defaults.DefaultFragment;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.InjectView;

/**
 * Created by meruvian on 28/08/15.
 */
public class HistoryOrderDetailFragment extends DefaultFragment {
    @InjectView(R.id.text_order_id)
    TextView textOrderId;
    @InjectView(R.id.text_contact_name_receipt)
    TextView textContactName;
    @InjectView(R.id.text_date_order)
    TextView textDate;
    @InjectView(R.id.text_address_street)
    TextView textAddressStreet;
    @InjectView(R.id.text_place_zip)
    TextView textPlaceZip;
    @InjectView(R.id.text_receipt_phone)
    TextView textReceiptPhone;
    @InjectView(R.id.text_total_order_detail)
    TextView textTotalOrder;
    @InjectView(R.id.list_detail_order_items)
    ListView listOrderItems;
    /*@InjectView(R.id.button_confirm_pay)
    Button buttonConfirmPay;*/

    private OrderDatabaseAdapter orderDatabaseAdapter;
    private OrderMenuDatabaseAdapter orderMenuDatabaseAdapter;
    private ContactDatabaseAdapter contactDatabaseAdapter;
    private HistoryOrderDetailAdapter historyOrderDetailAdapter;

    private String orderId;
    private long totalPrice;
    private Order order;
    private Contact contact;
    private List<OrderMenu> orderMenus = new ArrayList<OrderMenu>();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss", new Locale("in", "ID", "ID"));
    private DecimalFormat decimalFormat = new DecimalFormat("#,###");

    private JobManager jobManager;
    private SharedPreferences preferences;
    private ProgressDialog dialog;

    @Override
    protected int layout() {
        return R.layout.fragment_history_detail;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        orderDatabaseAdapter = new OrderDatabaseAdapter(getActivity());
        orderMenuDatabaseAdapter = new OrderMenuDatabaseAdapter(getActivity());
        contactDatabaseAdapter = new ContactDatabaseAdapter(getActivity());
        historyOrderDetailAdapter = new HistoryOrderDetailAdapter(getActivity(), R.layout.adapter_detail_order, new ArrayList<OrderMenu>(), this);

        preferences = getActivity().getSharedPreferences(SignageVariables.PREFS_SERVER, 0);

        orderId = getArguments().getString("order_id");
        order = orderDatabaseAdapter.findOrderById(orderId);
        contact = contactDatabaseAdapter.findContactByRefId(order.getContact().getId());
        orderMenus = orderMenuDatabaseAdapter.findOrderMenuByOrderId(orderId);

        historyOrderDetailAdapter.addAll(orderMenus);
        listOrderItems.setAdapter(historyOrderDetailAdapter);

        setViewOrderContact(order, contact, orderMenus);
        jobManager = SignageAppication.getInstance().getJobManager();
    }

    private void setViewOrderContact(Order order, Contact contact, List<OrderMenu> orderMenus) {
        totalPrice = 0;

        textOrderId.setText(order.getReceiptNumber());
        textContactName.setText("(" + contact.getContactName() + ") " + contact.getRecipient());
        textDate.setText(dateFormat.format(order.getLogInformation().getLastUpdateDate()));
        textAddressStreet.setText(contact.getAddress());
        textPlaceZip.setText(contact.getSubDistrict() + ", " + contact.getCity() + ", " + contact.getProvince() + ", " + contact.getZip());
        textReceiptPhone.setText(contact.getPhone());
        textTotalOrder.setText("Total Order: " + countTotalOrder(orderMenus));

    }

    private String countTotalOrder(List<OrderMenu> orderMenus) {
        String totalPrice = null;
        double totalOrder = 0;
        for (OrderMenu om : orderMenus) {
             totalOrder = om.getProduct().getSellPrice() * om.getQty();
        }

        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);
        totalPrice = kursIndonesia.format(totalOrder);

        return totalPrice;
    }

    private void getPayment() {
        jobManager.addJobInBackground(new PayGetJob(preferences.getString("server_url", "")));
    }

    private void syncPayment(Payment payment) {
        jobManager.addJobInBackground(new PayUpdateJob(orderId,preferences.getString("server_url", "")));
    }


}