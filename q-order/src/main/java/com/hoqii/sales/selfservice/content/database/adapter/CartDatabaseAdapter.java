package com.hoqii.sales.selfservice.content.database.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;

import com.hoqii.sales.selfservice.content.MidasContentProvider;
import com.hoqii.sales.selfservice.content.database.model.DefaultPersistenceModel;
import com.hoqii.sales.selfservice.content.database.model.CartDatabaseModel;
import com.hoqii.sales.selfservice.entity.Cart;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by meruvian on 24/07/15.
 */
public class CartDatabaseAdapter extends DefaultDatabaseAdapter {
    private Uri dbUriCart = Uri.parse(MidasContentProvider.CONTENT_PATH
            + MidasContentProvider.TABLES[8]);

    private Context context;

    public CartDatabaseAdapter(Context context) {
        this.context = context;
    }

    public String saveCart(Cart cart) {

        UUID uuid = UUID.randomUUID();
        String id = String.valueOf(uuid);

        ContentValues values = new ContentValues();
        values.put(DefaultPersistenceModel.ID, id);
        values.put(DefaultPersistenceModel.SITE_ID, cart.getSiteId());
        values.put(DefaultPersistenceModel.CREATE_BY, cart.getLogInformation().getCreateBy());
        values.put(DefaultPersistenceModel.CREATE_DATE, cart.getLogInformation().getCreateDate().getTime());
        values.put(DefaultPersistenceModel.UPDATE_BY, cart.getLogInformation().getLastUpdateBy());
        values.put(DefaultPersistenceModel.UPDATE_DATE, cart.getLogInformation().getLastUpdateDate().getTime());
        values.put(DefaultPersistenceModel.STATUS_FLAG, 0);
        values.put(DefaultPersistenceModel.SYNC_STATUS, 0);
        values.put(CartDatabaseModel.RECIEPT_NUMBER, cart.getReceiptNumber());

        context.getContentResolver().insert(dbUriCart, values);

        return id;
    }

    public void updateCart(String cartId) {
        ContentValues values = new ContentValues();

        values.put(CartDatabaseModel.STATUS_FLAG, 1);

        context.getContentResolver().update(dbUriCart, values,
                CartDatabaseModel.ID + " = ?", new String[]{cartId});
    }

    public String getCartId() {
        String query = CartDatabaseModel.STATUS_FLAG + " = 0";
        Cursor cursor = context.getContentResolver().query(dbUriCart, null, query, null, null);

        String id = null;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToLast();
                id = cursor.getString(cursor.getColumnIndex(CartDatabaseModel.ID));
            }
        }

        cursor.close();
        return id;
    }

    public String getLastCartId() {
        String query = CartDatabaseModel.STATUS_FLAG + " = 1";
        Cursor cursor = context.getContentResolver().query(dbUriCart, null,
                query, null, null);

        String id = null;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToLast();
                id = cursor.getString(cursor
                        .getColumnIndex(CartDatabaseModel.ID));
            }
        }

        cursor.close();

        return id;
    }

    public String getLastCartIdByFlag(String token,  int flag) {
        String query = CartDatabaseModel.CREATE_BY + " = ? AND " + CartDatabaseModel.STATUS_FLAG + " = ?";
        String[] parameter = { token , String.valueOf(flag)};
        Cursor cursor = context.getContentResolver().query(dbUriCart, null,
                query, parameter, null);

        String id = null;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToLast();
                id = cursor.getString(cursor
                        .getColumnIndex(CartDatabaseModel.ID));
            }
        }

        cursor.close();

        return id;
    }

    public String getLastCartId(String token) {
        String query = CartDatabaseModel.CREATE_BY + " = ? AND " + CartDatabaseModel.STATUS_FLAG + " = 1";
        String[] parameter = { token };
        Cursor cursor = context.getContentResolver().query(dbUriCart, null,
                query, null, null);

        String id = null;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToLast();
                id = cursor.getString(cursor
                        .getColumnIndex(CartDatabaseModel.ID));
            }
        }

        cursor.close();

        return id;
    }

    public String getLastReceipt() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");

        String query = CartDatabaseModel.STATUS_FLAG + " = 1";
        Cursor cursor = context.getContentResolver().query(dbUriCart, null,
                query, null, null);

        String receipt = null;
        Cart cart = null;

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToLast();

                cart = new Cart();
                com.hoqii.sales.selfservice.core.LogInformation log = getLogInformationDefault(cursor);


                cart.setId(cursor.getString(cursor
                        .getColumnIndex(CartDatabaseModel.ID)));
                cart.setReceiptNumber(cursor.getString(cursor
                        .getColumnIndex(CartDatabaseModel.RECIEPT_NUMBER)));
                cart.setLogInformation(log);
            }
        }

        cursor.close();

        if(cart == null) {
            receipt = "00001";
        } else {
            Date lastCartDate = new Date(cart.getLogInformation().getCreateDate().getTime());
            Date currentDate = new Date();

            String formatLastCartDate = dateFormat.format(lastCartDate);
            String formatCurrentDate = dateFormat.format(currentDate);

            if (!formatCurrentDate.equals(formatLastCartDate)) {
                receipt = "00001";
            } else {
                String lastCartReceipt = cart.getReceiptNumber().substring(cart.getReceiptNumber().length() - 5);
                int newCartReceipt = Integer.valueOf(lastCartReceipt) + 1;
                String newReceipt = String.valueOf(newCartReceipt);

                String zeros = "";
                for (int a = newReceipt.length(); a < 5; a++) {
                    zeros += "0";
                }

                receipt = zeros + newReceipt;
            }
        }

        return receipt;
    }

    public Cart findCartById(String id) {

        String criteria = CartDatabaseModel.ID + " = ?";
        String[] parameter = { id };
        Cursor cursor = context.getContentResolver().query(dbUriCart, null,
                criteria, parameter, null);

        Cart cart = new Cart();

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                com.hoqii.sales.selfservice.core.LogInformation log = getLogInformationDefault(cursor);

                cart.setLogInformation(log);
                cart.setId(cursor.getString(cursor
                        .getColumnIndex(CartDatabaseModel.ID)));
                cart.setReceiptNumber(cursor.getString(cursor
                        .getColumnIndex(CartDatabaseModel.RECIEPT_NUMBER)));

            }
        }
        cursor.close();

        return cart;
    }

    public List<Cart> getCarts() {
        Cursor cursor = context.getContentResolver().query(dbUriCart, null,
                null, null, null);

        List<Cart> carts = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Cart cart = new Cart();
                com.hoqii.sales.selfservice.core.LogInformation log = getLogInformationDefault(cursor);

                cart.setLogInformation(log);
                cart.setId(cursor.getString(cursor
                        .getColumnIndex(CartDatabaseModel.ID)));
                cart.setReceiptNumber(cursor.getString(cursor
                        .getColumnIndex(CartDatabaseModel.RECIEPT_NUMBER)));
            }
        }
        cursor.close();

        return carts;
    }

    public List<Cart> getHistoryCarts(int limit) {
        String criteria = CartDatabaseModel.STATUS_FLAG + " = 1";
        String carted = CartDatabaseModel.CREATE_DATE + " DESC LIMIT "
                + limit;
        Cursor cursor = context.getContentResolver().query(dbUriCart, null,
                criteria, null, carted);

        List<Cart> carts = new ArrayList<Cart>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Cart cart = new Cart();
                com.hoqii.sales.selfservice.core.LogInformation log = getLogInformationDefault(cursor);

                cart.setLogInformation(log);
                cart.setId(cursor.getString(cursor
                        .getColumnIndex(CartDatabaseModel.ID)));
                cart.setReceiptNumber(cursor.getString(cursor
                        .getColumnIndex(CartDatabaseModel.RECIEPT_NUMBER)));

                carts.add(cart);
            }
        }
        cursor.close();

        return carts;
    }

    public List<String> findAllIdCart() {
        String query = CartDatabaseModel.SYNC_STATUS + " = ?";
        String[] parameter = { "0" };

        Cursor cursor = context.getContentResolver().query(dbUriCart, null, query, parameter, null);

        List<String> cartIdes = new ArrayList<String>();

        if(cursor != null) {
            if(cursor.getCount() > 0) {
                try {
                    while (cursor.moveToNext()) {
                        cartIdes.add(cursor.getString(cursor.getColumnIndex(CartDatabaseModel.ID)));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        cursor.close();

        return cartIdes;
    }

    public void updateRefIdCart(long createDate1, long createDate2) {
        ContentValues values = new ContentValues();
        values.put(CartDatabaseModel.REF_ID, "");

        String where = CartDatabaseModel.CREATE_DATE + " > " + createDate1 + " and " + CartDatabaseModel.CREATE_DATE + " < " + createDate2;
        context.getContentResolver().update(dbUriCart, values, where, null);
    }

    public void updateSyncStatusById(String id) {
        ContentValues values = new ContentValues();
        values.put(CartDatabaseModel.SYNC_STATUS, 1);

        context.getContentResolver().update(dbUriCart, values, CartDatabaseModel.ID + " = ? ", new String[] { id });
    }

}
