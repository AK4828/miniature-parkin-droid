package com.hoqii.sales.selfservice.content.database.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.util.Log;

import com.hoqii.sales.selfservice.content.MidasContentProvider;
import com.hoqii.sales.selfservice.content.database.model.DefaultPersistenceModel;
import com.hoqii.sales.selfservice.content.database.model.OrderDatabaseModel;
import com.hoqii.sales.selfservice.entity.Order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by meruvian on 24/07/15.
 */
public class OrderDatabaseAdapter extends DefaultDatabaseAdapter {
    private Uri dbUriOrder = Uri.parse(MidasContentProvider.CONTENT_PATH
            + MidasContentProvider.TABLES[4]);

    private Context context;

    public OrderDatabaseAdapter(Context context) {
        this.context = context;
    }

    public String saveOrder(Order order) {
        UUID uuid = UUID.randomUUID();
        String id = String.valueOf(uuid);

        ContentValues values = new ContentValues();
        values.put(DefaultPersistenceModel.ID, id);
        values.put(DefaultPersistenceModel.SITE_ID, order.getSiteId());
        values.put(DefaultPersistenceModel.CREATE_BY, order.getLogInformation().getCreateBy());
        values.put(DefaultPersistenceModel.CREATE_DATE, order.getLogInformation().getCreateDate().getTime());
        values.put(DefaultPersistenceModel.UPDATE_BY, order.getLogInformation().getLastUpdateBy());
        values.put(DefaultPersistenceModel.UPDATE_DATE, order.getLogInformation().getLastUpdateDate().getTime());
        values.put(DefaultPersistenceModel.STATUS_FLAG, 0);
        values.put(DefaultPersistenceModel.SYNC_STATUS, 0);
        values.put(OrderDatabaseModel.RECIEPT_NUMBER, order.getReceiptNumber());
        values.put(OrderDatabaseModel.ORDER_TYPE, order.getOrderType());
        values.put(OrderDatabaseModel.REF_ID, order.getRefId());

        context.getContentResolver().insert(dbUriOrder, values);
        return id;
    }

    public void updateContactOrder(Order order) {
        Log.d(getClass().getSimpleName(), "updateContactOrder.ReceiptNumber :: " + order.getReceiptNumber());
        Log.d(getClass().getSimpleName(), "updateContactOrder.Contact().getId() :: " + order.getReceiptNumber());
        Log.d(getClass().getSimpleName(), "updateContactOrder.order.getRefId() :: " + order.getReceiptNumber());

        ContentValues values = new ContentValues();
        values.put(OrderDatabaseModel.RECIEPT_NUMBER, order.getReceiptNumber());
        values.put(OrderDatabaseModel.CONTACT_ID, order.getContact().getId());
        values.put(OrderDatabaseModel.REF_ID, order.getRefId());

        context.getContentResolver().update(dbUriOrder, values,
                OrderDatabaseModel.ID + " = ?", new String[]{order.getId()});
        Log.d(getClass().getSimpleName(), "updateContactOrder.Success");
    }

    public void updateOrder(String orderId) {
        ContentValues values = new ContentValues();
        values.put(OrderDatabaseModel.STATUS_FLAG, 1);

        context.getContentResolver().update(dbUriOrder, values,
                OrderDatabaseModel.ID + " = ?", new String[]{orderId});
    }

    public String getOrderId() {
        String query = OrderDatabaseModel.STATUS_FLAG + " = 0";
        Cursor cursor = context.getContentResolver().query(dbUriOrder, null,
                query, null, null);

        String id = null;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToLast();
                id = cursor.getString(cursor
                        .getColumnIndex(OrderDatabaseModel.ID));
            }
        }

        cursor.close();
        return id;
    }

    public String getLastOrderId() {
        String query = OrderDatabaseModel.STATUS_FLAG + " = 1";
        Cursor cursor = context.getContentResolver().query(dbUriOrder, null,
                query, null, null);

        String id = null;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToLast();
                id = cursor.getString(cursor
                        .getColumnIndex(OrderDatabaseModel.ID));
            }
        }

        cursor.close();

        return id;
    }

    public String getLastOrderIdByFlag(String token,  int flag) {
        String query = OrderDatabaseModel.CREATE_BY + " = ? AND " + OrderDatabaseModel.STATUS_FLAG + " = ?";
        String[] parameter = { token , String.valueOf(flag)};
        Cursor cursor = context.getContentResolver().query(dbUriOrder, null,
                query, parameter, null);

        String id = null;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToLast();
                id = cursor.getString(cursor
                        .getColumnIndex(OrderDatabaseModel.ID));
            }
        }

        cursor.close();

        return id;
    }

    public String getLastOrderId(String token) {
        String query = OrderDatabaseModel.CREATE_BY + " = ? AND " + OrderDatabaseModel.STATUS_FLAG + " = 1";
        String[] parameter = { token };
        Cursor cursor = context.getContentResolver().query(dbUriOrder, null,
                query, null, null);

        String id = null;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToLast();
                id = cursor.getString(cursor
                        .getColumnIndex(OrderDatabaseModel.ID));
            }
        }

        cursor.close();

        return id;
    }

    public String getLastReceipt() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");

        String query = OrderDatabaseModel.STATUS_FLAG + " = 1";
        Cursor cursor = context.getContentResolver().query(dbUriOrder, null,
                query, null, null);

        String receipt = null;
        Order order = null;

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToLast();

                order = new Order();
                com.hoqii.sales.selfservice.core.LogInformation log = getLogInformationDefault(cursor);


                order.setId(cursor.getString(cursor
                        .getColumnIndex(OrderDatabaseModel.ID)));
                order.setReceiptNumber(cursor.getString(cursor
                        .getColumnIndex(OrderDatabaseModel.RECIEPT_NUMBER)));
                order.setOrderType(cursor.getString(cursor
                        .getColumnIndex(OrderDatabaseModel.ORDER_TYPE)));
                order.setLogInformation(log);
            }
        }

        cursor.close();

        if(order == null) {
            receipt = "00001";
        } else {
            Date lastOrderDate = new Date(order.getLogInformation().getCreateDate().getTime());
            Date currentDate = new Date();

            String formatLastOrderDate = dateFormat.format(lastOrderDate);
            String formatCurrentDate = dateFormat.format(currentDate);

            if (!formatCurrentDate.equals(formatLastOrderDate)) {
                receipt = "00001";
            } else {
                String lastOrderReceipt = order.getReceiptNumber().substring(order.getReceiptNumber().length() - 5);
                int newOrderReceipt = Integer.valueOf(lastOrderReceipt) + 1;
                String newReceipt = String.valueOf(newOrderReceipt);

                String zeros = "";
                for (int a = newReceipt.length(); a < 5; a++) {
                    zeros += "0";
                }

                receipt = zeros + newReceipt;
            }
        }

        return receipt;
    }

    public Order findOrderById(String id) {

        String criteria = OrderDatabaseModel.ID + " = ?";
        String[] parameter = { id };
        Cursor cursor = context.getContentResolver().query(dbUriOrder, null,
                criteria, parameter, null);

        Order order = new Order();

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                com.hoqii.sales.selfservice.core.LogInformation log = getLogInformationDefault(cursor);

                order.setLogInformation(log);
                order.setId(cursor.getString(cursor.getColumnIndex(OrderDatabaseModel.ID)));
                order.setRefId(cursor.getString(cursor.getColumnIndex(OrderDatabaseModel.REF_ID)));
                order.setReceiptNumber(cursor.getString(cursor.getColumnIndex(OrderDatabaseModel.RECIEPT_NUMBER)));
                order.setOrderType(cursor.getString(cursor.getColumnIndex(OrderDatabaseModel.ORDER_TYPE)));
                order.getContact().setId(cursor.getString(cursor.getColumnIndex(OrderDatabaseModel.CONTACT_ID)));

                Log.d(getClass().getSimpleName(), "findOrderById.OrderID ::" + order.getId());
                Log.d(getClass().getSimpleName(), "findOrderById.OrderRefID ::" + order.getRefId());
                Log.d(getClass().getSimpleName(), "findOrderById.ReceiptNumber ::" + order.getReceiptNumber());
                Log.d(getClass().getSimpleName(), "findOrderById.ContactID ::" + order.getContact().getId());

            }
        }
        cursor.close();

        return order;
    }

    public List<Order> getOrders() {
        Cursor cursor = context.getContentResolver().query(dbUriOrder, null,
                null, null, null);

        List<Order> orders = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Order order = new Order();
                com.hoqii.sales.selfservice.core.LogInformation log = getLogInformationDefault(cursor);

                order.setLogInformation(log);
                order.setId(cursor.getString(cursor
                        .getColumnIndex(OrderDatabaseModel.ID)));
                order.setReceiptNumber(cursor.getString(cursor
                        .getColumnIndex(OrderDatabaseModel.RECIEPT_NUMBER)));
                order.setOrderType(cursor.getString(cursor
                        .getColumnIndex(OrderDatabaseModel.ORDER_TYPE)));
            }
        }
        cursor.close();

        return orders;
    }

    public List<Order> getHistoryOrders(int limit) {
        String criteria = OrderDatabaseModel.STATUS_FLAG + " = 1";
        String ordered = OrderDatabaseModel.CREATE_DATE + " DESC LIMIT "
                + limit;
        Cursor cursor = context.getContentResolver().query(dbUriOrder, null,
                criteria, null, ordered);

        List<Order> orders = new ArrayList<Order>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Order order = new Order();
                com.hoqii.sales.selfservice.core.LogInformation log = getLogInformationDefault(cursor);

                order.setLogInformation(log);
                order.setId(cursor.getString(cursor
                        .getColumnIndex(OrderDatabaseModel.ID)));
                order.setReceiptNumber(cursor.getString(cursor
                        .getColumnIndex(OrderDatabaseModel.RECIEPT_NUMBER)));
                order.setOrderType(cursor.getString(cursor
                        .getColumnIndex(OrderDatabaseModel.ORDER_TYPE)));

                orders.add(order);
            }
        }
        cursor.close();

        return orders;
    }

    public List<String> findAllIdOrder() {
        String query = OrderDatabaseModel.SYNC_STATUS + " = ?";
        String[] parameter = { "0" };

        Cursor cursor = context.getContentResolver().query(dbUriOrder, null, query, parameter, null);

        List<String> orderIdes = new ArrayList<String>();

        if(cursor != null) {
            if(cursor.getCount() > 0) {
                try {
                    while (cursor.moveToNext()) {
                        orderIdes.add(cursor.getString(cursor.getColumnIndex(OrderDatabaseModel.ID)));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        cursor.close();

        return orderIdes;
    }

    public void updateRefIdOrder(long createDate1, long createDate2) {
        ContentValues values = new ContentValues();
        values.put(OrderDatabaseModel.REF_ID, "");

        String where = OrderDatabaseModel.CREATE_DATE + " > " + createDate1 + " and " + OrderDatabaseModel.CREATE_DATE + " < " + createDate2;
        context.getContentResolver().update(dbUriOrder, values, where, null);
    }

    public void updateSyncStatusById(String id) {
        Log.d(getClass().getSimpleName(), "updateSyncStatusById :: Success1");

        ContentValues values = new ContentValues();
        values.put(OrderDatabaseModel.SYNC_STATUS, 1);
        values.put(OrderDatabaseModel.STATUS_FLAG, 1);

        context.getContentResolver().update(dbUriOrder, values, OrderDatabaseModel.ID + " = ? ", new String[]{id});
        Log.d(getClass().getSimpleName(), "updateSyncStatusById :: Success2");
    }

}
