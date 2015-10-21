package org.meruvian.esales.collector.content.database.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;

import org.meruvian.esales.collector.content.MidasContentProvider;
import org.meruvian.esales.collector.content.database.model.DefaultPersistenceModel;
import org.meruvian.esales.collector.content.database.model.OrderDatabaseModel;
import org.meruvian.esales.collector.content.database.model.OrderMenuImeiDatabaseModel;
import org.meruvian.esales.collector.core.LogInformation;
import org.meruvian.esales.collector.entity.OrderMenu;
import org.meruvian.esales.collector.entity.OrderMenuImei;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by meruvian on 24/07/15.
 */
public class OrderMenuImeiDatabaseAdapter extends DefaultDatabaseAdapter {
    private Uri dbUriOrderMenuImei = Uri.parse(MidasContentProvider.CONTENT_PATH
            + MidasContentProvider.TABLES[12]);

    private Context context;
//    private ProductDatabaseAdapter productDbAdapter;
//    private ProductStoreDatabaseAdapter productStoreDbAdapter;

    public OrderMenuImeiDatabaseAdapter(Context context) {
        this.context = context;

        /*productDbAdapter = new ProductDatabaseAdapter(context);
        productStoreDbAdapter = new ProductStoreDatabaseAdapter(context);*/
    }

    public void saveOrderMenu(OrderMenuImei orderMenuImei) {
        ContentValues contentValues = new ContentValues();

        if (orderMenuImei.getId() != null) {
            contentValues.put(OrderMenuImeiDatabaseModel.UPDATE_BY, orderMenuImei.getLogInformation()
                    .getLastUpdateBy());
            contentValues.put(OrderMenuImeiDatabaseModel.UPDATE_DATE, orderMenuImei.getLogInformation()
                    .getLastUpdateDate().getTime());

            contentValues.put(OrderMenuImeiDatabaseModel.ORDER_MENU_ID, orderMenuImei.getOrderMenu().getId());
            contentValues.put(OrderMenuImeiDatabaseModel.IMEI, orderMenuImei.getImei());

            contentValues.put(DefaultPersistenceModel.STATUS_FLAG, 1);
            contentValues.put(DefaultPersistenceModel.SYNC_STATUS, 0);

            context.getContentResolver().update(dbUriOrderMenuImei, contentValues,
                    OrderMenuImeiDatabaseModel.ID + " = ?", new String[] { orderMenuImei.getId() });
        } else {
            UUID uuid = UUID.randomUUID();
            String id = String.valueOf(uuid);

            contentValues.put(OrderMenuImeiDatabaseModel.ID, id);
            contentValues.put(OrderMenuImeiDatabaseModel.CREATE_BY, orderMenuImei.getLogInformation()
                    .getCreateBy());
            contentValues.put(OrderMenuImeiDatabaseModel.CREATE_DATE, orderMenuImei.getLogInformation()
                    .getCreateDate().getTime());
            contentValues.put(OrderMenuImeiDatabaseModel.UPDATE_BY, orderMenuImei.getLogInformation()
                    .getLastUpdateBy());
            contentValues.put(OrderMenuImeiDatabaseModel.UPDATE_DATE, orderMenuImei.getLogInformation()
                    .getLastUpdateDate().getTime());
            contentValues.put(OrderMenuImeiDatabaseModel.SITE_ID, orderMenuImei.getLogInformation().getSite());

            contentValues.put(OrderMenuImeiDatabaseModel.ORDER_MENU_ID, orderMenuImei.getOrderMenu().getId());
            contentValues.put(OrderMenuImeiDatabaseModel.IMEI, orderMenuImei.getImei());

            contentValues.put(DefaultPersistenceModel.STATUS_FLAG, 1);
            contentValues.put(DefaultPersistenceModel.SYNC_STATUS, 0);

            context.getContentResolver().insert(dbUriOrderMenuImei, contentValues);
        }
    }

    public List<String> findOrderImeiIdesByMenuId(String menuId) {
        String query = OrderMenuImeiDatabaseModel.ORDER_MENU_ID + " = ?";
        String[] parameter = { menuId };

        Cursor cursor = context.getContentResolver().query(dbUriOrderMenuImei, null, query, parameter, null);

        List<String> orderImeiIdes = new ArrayList<String>();

        if(cursor != null) {
            if(cursor.getCount() > 0) {
                try {
                    while (cursor.moveToNext()) {
                        orderImeiIdes.add(cursor.getString(cursor.getColumnIndex(OrderDatabaseModel.ID)));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        cursor.close();

        return orderImeiIdes;
    }

    public OrderMenuImei findOrderMenuById(String id) {
        String criteria = OrderMenuImeiDatabaseModel.ID + " = ?";
        String[] parameter = { id };
        Cursor cursor = context.getContentResolver().query(dbUriOrderMenuImei,
                null, criteria, parameter, null);

        OrderMenuImei orderMenuImei = new OrderMenuImei();

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                LogInformation log = getLogInformationDefault(cursor);

                OrderMenu orderMenu = new OrderMenu();
                orderMenu.setId(cursor.getString(cursor.getColumnIndex(OrderMenuImeiDatabaseModel.ORDER_MENU_ID)));

                orderMenuImei.setLogInformation(log);
                orderMenuImei.setId(cursor.getString(cursor.getColumnIndex(OrderMenuImeiDatabaseModel.ID)));
                orderMenuImei.setOrderMenu(orderMenu);
                orderMenuImei.setImei(cursor.getString(cursor.getColumnIndex(OrderMenuImeiDatabaseModel.IMEI)));
            }
        }
        cursor.close();

        return orderMenuImei;
    }

    public List<OrderMenuImei> findOrderImeiByMenuId(String menuId) {
        String criteria = OrderMenuImeiDatabaseModel.ORDER_MENU_ID + " = ?";
        String[] parameter = { menuId };
        Cursor cursor = context.getContentResolver().query(dbUriOrderMenuImei,
                null, criteria, parameter, null);

        List<OrderMenuImei> orderMenus = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                OrderMenuImei orderMenuImei = new OrderMenuImei();
                LogInformation log = getLogInformationDefault(cursor);

                OrderMenu orderMenu = new OrderMenu();
                orderMenu.setId(cursor.getString(cursor.getColumnIndex(OrderMenuImeiDatabaseModel.ORDER_MENU_ID)));

                orderMenuImei.setLogInformation(log);
                orderMenuImei.setId(cursor.getString(cursor.getColumnIndex(OrderMenuImeiDatabaseModel.ID)));
                orderMenuImei.setOrderMenu(orderMenu);
                orderMenuImei.setImei(cursor.getString(cursor.getColumnIndex(OrderMenuImeiDatabaseModel.IMEI)));

                orderMenus.add(orderMenuImei);
            }
        }

        cursor.close();

        return orderMenus;
    }

    public void deleteOrderImei(String id) {
        String criteria = OrderMenuImeiDatabaseModel.ID + " = ?";
        String[] parameter = { id };

        context.getContentResolver()
                .delete(dbUriOrderMenuImei, criteria, parameter);

    }

    public OrderMenuImei getOrderImeiById(String orderMenuId) {
        String criteria = OrderMenuImeiDatabaseModel.ID + " = ?";
        String[] parameter = { orderMenuId };
        Cursor cursor = context.getContentResolver().query(dbUriOrderMenuImei,
                null, criteria, parameter, null);

        OrderMenuImei orderMenuImei = new OrderMenuImei();

        if (cursor != null) {
            while (cursor.moveToNext()) {

                LogInformation log = getLogInformationDefault(cursor);

                OrderMenu orderMenu = new OrderMenu();
                orderMenu.setId(cursor.getString(cursor.getColumnIndex(OrderMenuImeiDatabaseModel.ORDER_MENU_ID)));

                orderMenuImei.setLogInformation(log);
                orderMenuImei.setId(cursor.getString(cursor.getColumnIndex(OrderMenuImeiDatabaseModel.ID)));
                orderMenuImei.setOrderMenu(orderMenu);
                orderMenuImei.setImei(cursor.getString(cursor.getColumnIndex(OrderMenuImeiDatabaseModel.IMEI)));
            }
        }

        cursor.close();

        return orderMenuImei;
    }

    public String getOrderImeiId() {
        Cursor cursor = context.getContentResolver().query(dbUriOrderMenuImei,
                null, null, null,
                OrderMenuImeiDatabaseModel.CREATE_DATE + " DESC LIMIT 1");

        String id = null;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                id = cursor.getString(cursor
                        .getColumnIndex(OrderMenuImeiDatabaseModel.ID));
            }
        }

        cursor.close();

        return id;
    }

    public void updateSyncStatusById(String id) {
        ContentValues values = new ContentValues();
        values.put(OrderDatabaseModel.SYNC_STATUS, 1);

        context.getContentResolver().update(dbUriOrderMenuImei, values, OrderDatabaseModel.ID + " = ? ", new String[] { id });
    }

}
