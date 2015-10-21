package org.meruvian.esales.collector.content.database.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;

import org.meruvian.esales.collector.content.MidasContentProvider;
import org.meruvian.esales.collector.content.database.model.DefaultPersistenceModel;
import org.meruvian.esales.collector.content.database.model.OrderDatabaseModel;
import org.meruvian.esales.collector.content.database.model.OrderMenuDatabaseModel;
import org.meruvian.esales.collector.core.LogInformation;
import org.meruvian.esales.collector.entity.Order;
import org.meruvian.esales.collector.entity.OrderMenu;
import org.meruvian.esales.collector.entity.Product;
import org.meruvian.esales.collector.entity.ProductStore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by meruvian on 24/07/15.
 */
public class OrderMenuDatabaseAdapter extends DefaultDatabaseAdapter {
    private Uri dbUriOrderMenu = Uri.parse(MidasContentProvider.CONTENT_PATH
            + MidasContentProvider.TABLES[5]);

    private Context context;
    private ProductDatabaseAdapter productDbAdapter;
    private ProductStoreDatabaseAdapter productStoreDbAdapter;

    public OrderMenuDatabaseAdapter(Context context) {
        this.context = context;

        productDbAdapter = new ProductDatabaseAdapter(context);
        productStoreDbAdapter = new ProductStoreDatabaseAdapter(context);
    }

    public void saveOrderMenus(List<OrderMenu> orderMenus) {
        for (OrderMenu orderMenu : orderMenus) {
            ContentValues contentValues = new ContentValues();

            if (findOrderMenuByRefId(orderMenu.getId()) != null) {
                contentValues.put(OrderMenuDatabaseModel.UPDATE_BY, orderMenu.getLogInformation()
                        .getLastUpdateBy());
                contentValues.put(OrderMenuDatabaseModel.UPDATE_DATE, orderMenu.getLogInformation()
                        .getLastUpdateDate().getTime());
                contentValues.put(OrderMenuDatabaseModel.QUANTITY, orderMenu.getQty());

                contentValues.put(OrderMenuDatabaseModel.DESC, orderMenu.getDescription());
                contentValues.put(OrderMenuDatabaseModel.STATUS, orderMenu.getStatus().name());

                contentValues.put(DefaultPersistenceModel.SYNC_STATUS, 0);

                context.getContentResolver().update(dbUriOrderMenu, contentValues,
                        OrderMenuDatabaseModel.REF_ID + " = ?", new String[]{orderMenu.getId()});
            } else {
                UUID uuid = UUID.randomUUID();
                String id = String.valueOf(uuid);

                contentValues.put(OrderMenuDatabaseModel.ID, id);
                contentValues.put(OrderMenuDatabaseModel.CREATE_BY, orderMenu.getLogInformation()
                        .getCreateBy());
                contentValues.put(OrderMenuDatabaseModel.CREATE_DATE, orderMenu.getLogInformation()
                        .getCreateDate().getTime());
                contentValues.put(OrderMenuDatabaseModel.UPDATE_BY, orderMenu.getLogInformation()
                        .getLastUpdateBy());
                contentValues.put(OrderMenuDatabaseModel.UPDATE_DATE, orderMenu.getLogInformation()
                        .getLastUpdateDate().getTime());
                contentValues.put(OrderMenuDatabaseModel.SITE_ID, orderMenu.getLogInformation().getSite());
                contentValues.put(OrderMenuDatabaseModel.QUANTITY, orderMenu.getQty());
                contentValues.put(OrderMenuDatabaseModel.PRODUCT_STORE_ID, orderMenu.getProduct().getId());

                productStoreDbAdapter.saveProductStore(orderMenu.getProduct());

                contentValues.put(OrderMenuDatabaseModel.ORDER_ID, orderMenu.getOrder().getId());
                contentValues.put(OrderMenuDatabaseModel.DESC, orderMenu.getDescription());
                contentValues.put(OrderMenuDatabaseModel.PRICE, orderMenu.getSellPrice());
                contentValues.put(OrderMenuDatabaseModel.STATUS, orderMenu.getStatus().name());
                contentValues.put(OrderMenuDatabaseModel.REF_ID, orderMenu.getId());

                contentValues.put(DefaultPersistenceModel.SYNC_STATUS, 0);

                context.getContentResolver().insert(dbUriOrderMenu, contentValues);
            }
        }
    }

    public void saveOrderMenu(OrderMenu orderMenu) {
        ContentValues contentValues = new ContentValues();

        if (orderMenu.getId() != null) {
            contentValues.put(OrderMenuDatabaseModel.UPDATE_BY, orderMenu.getLogInformation()
                    .getLastUpdateBy());
            contentValues.put(OrderMenuDatabaseModel.UPDATE_DATE, orderMenu.getLogInformation()
                    .getLastUpdateDate().getTime());
            contentValues.put(OrderMenuDatabaseModel.QUANTITY, orderMenu.getQty());

            contentValues.put(OrderMenuDatabaseModel.DESC, orderMenu.getDescription());
            contentValues.put(OrderMenuDatabaseModel.STATUS, orderMenu.getStatus().name());

            contentValues.put(DefaultPersistenceModel.SYNC_STATUS, 0);

            context.getContentResolver().update(dbUriOrderMenu, contentValues,
                    OrderMenuDatabaseModel.ID + " = ?", new String[]{orderMenu.getId()});
        } else {
            UUID uuid = UUID.randomUUID();
            String id = String.valueOf(uuid);

            contentValues.put(OrderMenuDatabaseModel.ID, id);
            contentValues.put(OrderMenuDatabaseModel.CREATE_BY, orderMenu.getLogInformation()
                    .getCreateBy());
            contentValues.put(OrderMenuDatabaseModel.CREATE_DATE, orderMenu.getLogInformation()
                    .getCreateDate().getTime());
            contentValues.put(OrderMenuDatabaseModel.UPDATE_BY, orderMenu.getLogInformation()
                    .getLastUpdateBy());
            contentValues.put(OrderMenuDatabaseModel.UPDATE_DATE, orderMenu.getLogInformation()
                    .getLastUpdateDate().getTime());
            contentValues.put(OrderMenuDatabaseModel.SITE_ID, orderMenu.getLogInformation().getSite());
            contentValues.put(OrderMenuDatabaseModel.QUANTITY, orderMenu.getQty());
            contentValues.put(OrderMenuDatabaseModel.PRODUCT_STORE_ID, orderMenu.getProduct().getId());
            contentValues.put(OrderMenuDatabaseModel.ORDER_ID, orderMenu.getOrder().getId());
            contentValues.put(OrderMenuDatabaseModel.DESC, orderMenu.getDescription());
            contentValues.put(OrderMenuDatabaseModel.PRICE, orderMenu.getSellPrice());

            contentValues.put(OrderMenuDatabaseModel.STATUS, orderMenu.getStatus().name());

            contentValues.put(DefaultPersistenceModel.SYNC_STATUS, 0);

            context.getContentResolver().insert(dbUriOrderMenu, contentValues);
        }
    }

    public List<String> findOrderMenuIdesByOrderId(String orderId) {
        String query = OrderMenuDatabaseModel.ORDER_ID + " = ?";
        String[] parameter = {orderId};

        Cursor cursor = context.getContentResolver().query(dbUriOrderMenu, null, query, parameter, null);

        List<String> orderMenuIdes = new ArrayList<String>();

        if (cursor != null) {
            if (cursor.getCount() > 0) {
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
        String[] parameter = {id};
        Cursor cursor = context.getContentResolver().query(dbUriOrderMenu,
                null, criteria, parameter, null);

        OrderMenu orderMenu = new OrderMenu();

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                LogInformation log = getLogInformationDefault(cursor);

                ProductStore productStore = productStoreDbAdapter.findProductStoreById(cursor.getString(cursor.getColumnIndex(OrderMenuDatabaseModel.PRODUCT_STORE_ID)));
                Product product = productDbAdapter.findAllProductById(productStore.getProduct().getId());
                productStore.setProduct(product);
//                ProductStore product = new ProductStore();
//                product.setId(cursor.getString(cursor.getColumnIndex(OrderMenuDatabaseModel.PRODUCT_STORE_ID)));

                Order order = new Order();
                order.setId(cursor.getString(cursor.getColumnIndex(OrderMenuDatabaseModel.ORDER_ID)));

                orderMenu.setLogInformation(log);
                orderMenu.setId(cursor.getString(cursor.getColumnIndex(OrderMenuDatabaseModel.ID)));
                orderMenu.setQty(cursor.getInt(cursor.getColumnIndex(OrderMenuDatabaseModel.QUANTITY)));
                orderMenu.setProduct(productStore);
                orderMenu.setOrder(order);
                orderMenu.setSellPrice(cursor.getLong(cursor.getColumnIndex(OrderMenuDatabaseModel.PRICE)));
                orderMenu.setDescription(cursor.getString(cursor.getColumnIndex(OrderMenuDatabaseModel.DESC)));
            }
        }
        cursor.close();

        return orderMenu;
    }

    public OrderMenu findOrderMenuByRefId(String refId) {
        String criteria = OrderMenuDatabaseModel.REF_ID + " = ?";
        String[] parameter = { refId };
        Cursor cursor = context.getContentResolver().query(dbUriOrderMenu,
                null, criteria, parameter, null);

        OrderMenu orderMenu = null;

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                orderMenu = new OrderMenu();
                LogInformation log = getLogInformationDefault(cursor);

                ProductStore productStore = productStoreDbAdapter.findProductStoreById(cursor.getString(cursor.getColumnIndex(OrderMenuDatabaseModel.PRODUCT_STORE_ID)));
                Product product = productDbAdapter.findAllProductById(productStore.getProduct().getId());
                productStore.setProduct(product);

                Order order = new Order();
                order.setId(cursor.getString(cursor.getColumnIndex(OrderMenuDatabaseModel.ORDER_ID)));

                orderMenu.setLogInformation(log);
                orderMenu.setId(cursor.getString(cursor.getColumnIndex(OrderMenuDatabaseModel.ID)));
                orderMenu.setQty(cursor.getInt(cursor.getColumnIndex(OrderMenuDatabaseModel.QUANTITY)));
                orderMenu.setProduct(productStore);
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
        String[] parameter = {orderId};
        Cursor cursor = context.getContentResolver().query(dbUriOrderMenu,
                null, criteria, parameter, null);

        List<OrderMenu> orderMenus = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                ProductStore productStore = productStoreDbAdapter.findProductStoreById(cursor
                        .getString(cursor.getColumnIndex(OrderMenuDatabaseModel.PRODUCT_STORE_ID)));
                Product product = productDbAdapter.findAllProductById(productStore.getProduct().getId());
                productStore.setProduct(product);

                Order order = new Order();
                order.setId(cursor.getString(cursor.getColumnIndex(OrderMenuDatabaseModel.ORDER_ID)));

                OrderMenu orderMenu = new OrderMenu();
                orderMenu.setId(cursor.getString(cursor
                        .getColumnIndex(OrderMenuDatabaseModel.ID)));
                orderMenu.setQty(cursor.getInt(cursor
                        .getColumnIndex(OrderMenuDatabaseModel.QUANTITY)));
                orderMenu.setProduct(productStore);
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
        String[] parameter = {menuId};

        context.getContentResolver()
                .delete(dbUriOrderMenu, criteria, parameter);

    }

    public OrderMenu getOrderMenuById(String orderMenuId) {
        String criteria = OrderMenuDatabaseModel.ID + " = ?";
        String[] parameter = {orderMenuId};
        Cursor cursor = context.getContentResolver().query(dbUriOrderMenu,
                null, criteria, parameter, null);

        OrderMenu orderMenu = new OrderMenu();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Product product = new Product();
                ProductStore productStore = productStoreDbAdapter.findProductStoreById(cursor
                        .getString(cursor.getColumnIndex(OrderMenuDatabaseModel.PRODUCT_STORE_ID)));
                product = productDbAdapter.findAllProductById(productStore.getProduct().getId());
                productStore.setProduct(product);

                Order order = new Order();
                order.setId((cursor.getString(cursor
                        .getColumnIndex(OrderMenuDatabaseModel.ORDER_ID))));

                orderMenu.setId(cursor.getString(cursor
                        .getColumnIndex(OrderMenuDatabaseModel.ID)));
                orderMenu.setQty(cursor.getInt(cursor
                        .getColumnIndex(OrderMenuDatabaseModel.QUANTITY)));
                orderMenu.setProduct(productStore);
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

        context.getContentResolver().update(dbUriOrderMenu, values, OrderDatabaseModel.ID + " = ? ", new String[]{id});
    }

}
