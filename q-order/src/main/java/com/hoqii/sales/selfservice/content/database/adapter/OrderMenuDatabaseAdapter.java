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
import com.hoqii.sales.selfservice.content.database.model.OrderMenuDatabaseModel;
import com.hoqii.sales.selfservice.entity.Order;
import com.hoqii.sales.selfservice.entity.OrderMenu;
import com.hoqii.sales.selfservice.entity.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by meruvian on 24/07/15.
 */
public class OrderMenuDatabaseAdapter {
    private Uri dbUriOrderMenu = Uri.parse(MidasContentProvider.CONTENT_PATH
            + MidasContentProvider.TABLES[5]);

    private Context context;
    private ProductDatabaseAdapter productDbAdapter;

    public OrderMenuDatabaseAdapter(Context context) {
        this.context = context;

        productDbAdapter = new ProductDatabaseAdapter(context);
    }

    public void saveOrderMenu(OrderMenu orderMenu) {
        ContentValues contentValues = new ContentValues();

        if (orderMenu.getId() != null) {

            Log.d("OMMMM" , "111111111");

            contentValues.put(OrderMenuDatabaseModel.UPDATE_BY, orderMenu
                    .getLogInformation().getLastUpdateBy());
            contentValues.put(OrderMenuDatabaseModel.UPDATE_DATE, orderMenu
                    .getLogInformation().getLastUpdateDate().getTime());
            contentValues.put(OrderMenuDatabaseModel.QUANTITY,
                    orderMenu.getQty());
            contentValues.put(OrderMenuDatabaseModel.DESC,
                    orderMenu.getDescription());
            contentValues.put(DefaultPersistenceModel.SYNC_STATUS, 0);

            context.getContentResolver().update(dbUriOrderMenu, contentValues,
                    OrderMenuDatabaseModel.ID + " = ?",
                    new String[] { orderMenu.getId() });
        } else {
            UUID uuid = UUID.randomUUID();
            String id = String.valueOf(uuid);

            Log.d("OMMMM" , "22222222222");

            contentValues.put(OrderMenuDatabaseModel.ID, id);
            contentValues.put(OrderMenuDatabaseModel.CREATE_BY, orderMenu
                    .getLogInformation().getCreateBy());
            contentValues.put(OrderMenuDatabaseModel.CREATE_DATE, orderMenu
                    .getLogInformation().getCreateDate().getTime());
            contentValues.put(OrderMenuDatabaseModel.UPDATE_BY, orderMenu
                    .getLogInformation().getLastUpdateBy());
            contentValues.put(OrderMenuDatabaseModel.UPDATE_DATE, orderMenu
                    .getLogInformation().getLastUpdateDate().getTime());
            contentValues.put(OrderMenuDatabaseModel.QUANTITY,
                    orderMenu.getQty());
            contentValues.put(OrderMenuDatabaseModel.PRODUCT_ID, orderMenu.getProduct().getId());
            contentValues.put(OrderMenuDatabaseModel.ORDER_ID, orderMenu.getOrder().getId());
            contentValues.put(OrderMenuDatabaseModel.DESC, orderMenu.getDescription());
            contentValues.put(OrderMenuDatabaseModel.PRICE, orderMenu.getSellPrice());
            contentValues.put(DefaultPersistenceModel.SYNC_STATUS, 0);

            context.getContentResolver().insert(dbUriOrderMenu, contentValues);
        }
    }

    public List<String> findOrderMenuIdesByOrderId(String orderId) {
        String query = OrderMenuDatabaseModel.ORDER_ID + " = ?";
        String[] parameter = { orderId };

        Cursor cursor = context.getContentResolver().query(dbUriOrderMenu, null, query, parameter, null);

        List<String> orderMenuIdes = new ArrayList<String>();

        if(cursor != null) {
            if(cursor.getCount() > 0) {
                try {
                    while (cursor.moveToNext()) {
                        orderMenuIdes.add(cursor.getString(cursor.getColumnIndex(OrderDatabaseModel.ID)));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        cursor.close();

        return orderMenuIdes;
    }

    public OrderMenu findOrderMenuById(String id) {
        String criteria = OrderMenuDatabaseModel.ID + " = ?";
        String[] parameter = { id };
        Cursor cursor = context.getContentResolver().query(dbUriOrderMenu,
                null, criteria, parameter, null);

        OrderMenu orderMenu = new OrderMenu();

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                Product product = productDbAdapter.findProductById(cursor.getString(cursor.getColumnIndex(OrderMenuDatabaseModel.PRODUCT_ID)));
//                product.setId();

                Order order = new Order();
                order.setId(cursor.getString(cursor.getColumnIndex(OrderMenuDatabaseModel.ORDER_ID)));

                orderMenu.setId(cursor.getString(cursor
                        .getColumnIndex(OrderMenuDatabaseModel.ID)));
                orderMenu.setQty(cursor.getInt(cursor
                        .getColumnIndex(OrderMenuDatabaseModel.QUANTITY)));
                orderMenu.setProduct(product);
                orderMenu.setOrder(order);
                orderMenu.setSellPrice(cursor.getLong(cursor.getColumnIndex(OrderMenuDatabaseModel.PRICE)));
                orderMenu.setDescription(cursor.getString(cursor.getColumnIndex(OrderMenuDatabaseModel.DESC)));
            }
        }

        cursor.close();

        return orderMenu;
    }

    public List<OrderMenu> findOrderMenuByOrderId(String orderId) {
        String criteria = OrderMenuDatabaseModel.ORDER_ID + " = ?";
        String[] parameter = { orderId };
        Cursor cursor = context.getContentResolver().query(dbUriOrderMenu,
                null, criteria, parameter, null);

        List<OrderMenu> orderMenus = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Product product = productDbAdapter.findProductById(cursor.getString(cursor.getColumnIndex(OrderMenuDatabaseModel.PRODUCT_ID)));
//                product.setId();

                Order order = new Order();
                order.setId(cursor.getString(cursor.getColumnIndex(OrderMenuDatabaseModel.ORDER_ID)));

                OrderMenu orderMenu = new OrderMenu();
                orderMenu.setId(cursor.getString(cursor
                        .getColumnIndex(OrderMenuDatabaseModel.ID)));
                orderMenu.setQty(cursor.getInt(cursor
                        .getColumnIndex(OrderMenuDatabaseModel.QUANTITY)));
                orderMenu.setProduct(product);
                orderMenu.setOrder(order);
                orderMenu.setSellPrice(cursor.getLong(cursor.getColumnIndex(OrderMenuDatabaseModel.PRICE)));
                orderMenu.setDescription(cursor.getString(cursor.getColumnIndex(OrderMenuDatabaseModel.DESC)));

                orderMenus.add(orderMenu);
            }
        }

        cursor.close();

        return orderMenus;
    }

    public void deleteOrderMenu(String menuId) {
        String criteria = OrderMenuDatabaseModel.ID + " = ?";
        String[] parameter = { menuId };

        context.getContentResolver()
                .delete(dbUriOrderMenu, criteria, parameter);

    }

    public OrderMenu getOrderMenuById(String orderMenuId) {
        String criteria = OrderMenuDatabaseModel.ID + " = ?";
        String[] parameter = { orderMenuId };
        Cursor cursor = context.getContentResolver().query(dbUriOrderMenu,
                null, criteria, parameter, null);

        OrderMenu orderMenu = new OrderMenu();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Product product = new Product();
                product.setId((cursor.getString(cursor
                        .getColumnIndex(OrderMenuDatabaseModel.PRODUCT_ID))));

                Order order = new Order();
                order.setId((cursor.getString(cursor
                        .getColumnIndex(OrderMenuDatabaseModel.ORDER_ID))));

                orderMenu.setId(cursor.getString(cursor
                        .getColumnIndex(OrderMenuDatabaseModel.ID)));
                orderMenu.setQty(cursor.getInt(cursor
                        .getColumnIndex(OrderMenuDatabaseModel.QUANTITY)));
                orderMenu.setProduct(product);
                orderMenu.setOrder(order);
                orderMenu.setSellPrice(cursor.getLong(cursor.getColumnIndex(OrderMenuDatabaseModel.PRICE)));
                orderMenu.setDescription(cursor.getString(cursor.getColumnIndex(OrderMenuDatabaseModel.DESC)));

            }
        }

        cursor.close();

        return orderMenu;
    }

    public String getOrderMenuId() {
        Cursor cursor = context.getContentResolver().query(dbUriOrderMenu,
                null, null, null,
                OrderMenuDatabaseModel.CREATE_DATE + " DESC LIMIT 1");

        String id = null;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                id = cursor.getString(cursor
                        .getColumnIndex(OrderMenuDatabaseModel.ID));
            }
        }

        cursor.close();

        return id;
    }

    public void updateSyncStatusById(String id) {
        ContentValues values = new ContentValues();
        values.put(OrderDatabaseModel.SYNC_STATUS, 1);

        context.getContentResolver().update(dbUriOrderMenu, values, OrderDatabaseModel.ID + " = ? ", new String[] { id });
    }

}
