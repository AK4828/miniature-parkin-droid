package com.meruvian.pxc.selfservice.content.database.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.meruvian.pxc.selfservice.content.MidasContentProvider;
import com.meruvian.pxc.selfservice.content.database.model.SupplierDatabaseModel;
import com.meruvian.pxc.selfservice.entity.Product;
import com.meruvian.pxc.selfservice.entity.Supplier;


/**
 * Created by miftakhul on 11/23/15.
 */
public class SupplierDatabaseAdapter extends DefaultDatabaseAdapter{

    private Uri dbUriSupplier = Uri.parse(MidasContentProvider.CONTENT_PATH + MidasContentProvider.TABLES[17]);

    private Context context;

    public Supplier findSupplierById(String id){
        String criteria = SupplierDatabaseModel.ID + " = ? ";

        Cursor cursor = context.getContentResolver().query(dbUriSupplier, null, criteria, new String[] {id}, null);

        Supplier supplier = null;

        if (cursor != null){
            if (cursor.getCount() > 0){
                cursor.moveToFirst();

                supplier = new Supplier();
                supplier.setId(cursor.getString(cursor.getColumnIndex(SupplierDatabaseModel.ID)));
                supplier.setName(cursor.getString(cursor.getColumnIndex(SupplierDatabaseModel.NAME)));
                supplier.setPhone(cursor.getString(cursor.getColumnIndex(SupplierDatabaseModel.PHONE)));
                supplier.setEmail(cursor.getString(cursor.getColumnIndex(SupplierDatabaseModel.EMAIL)));
                supplier.setComphany(cursor.getString(cursor.getColumnIndex(SupplierDatabaseModel.COMPHANY)));
            }
        }

        return supplier;
    }

    public void saveProduct(Product product){
        ContentValues values = new ContentValues();

    }

}
