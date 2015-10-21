package org.meruvian.esales.collector.content.database.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import org.meruvian.esales.collector.SignageVariables;
import org.meruvian.esales.collector.content.MidasContentProvider;
import org.meruvian.esales.collector.content.database.model.DefaultPersistenceModel;
import org.meruvian.esales.collector.content.database.model.ProductStoreDatabaseModel;
import org.meruvian.esales.collector.entity.Product;
import org.meruvian.esales.collector.entity.ProductStore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by meruvian on 09/09/15.
 */
public class ProductStoreDatabaseAdapter  {
    private Uri dbUriProductStore = Uri.parse(MidasContentProvider.CONTENT_PATH
        + MidasContentProvider.TABLES[10]);

    private Context context;
    private ProductDatabaseAdapter productDbAdapter;

    public ProductStoreDatabaseAdapter(Context context) {
        this.context = context;
        productDbAdapter = new ProductDatabaseAdapter(context);
    }

    public ProductStore findActiveProductStoreById(String id) {
        String criteria = DefaultPersistenceModel.ID + " = ?  AND "
            + DefaultPersistenceModel.STATUS_FLAG + " = " + SignageVariables.ACTIVE;

        Cursor cursor = context.getContentResolver().query(dbUriProductStore, null, criteria,
            new String[] { id }, null);

        ProductStore productStore = null;

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                productStore = new ProductStore();
                productStore.setId(cursor.getString(cursor.getColumnIndex(DefaultPersistenceModel.ID)));
                productStore.setSellPrice(cursor.getDouble(cursor.getColumnIndex(ProductStoreDatabaseModel.SELL_PRICE)));
                productStore.getProduct().setId(cursor.getString(cursor.getColumnIndex(ProductStoreDatabaseModel.PRODUCT_ID)));
            }
        }

        cursor.close();

        return productStore;
    }

    public ProductStore findProductStoreByRefId(String refId) {
        String criteria = DefaultPersistenceModel.REF_ID + " = ? ";

        Cursor cursor = context.getContentResolver().query(dbUriProductStore, null, criteria,
                new String[] { refId }, null);

        ProductStore productStore = null;

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                productStore = new ProductStore();
                Product product = productDbAdapter.findAllProductByRefId(cursor.getString
                        (cursor.getColumnIndex(ProductStoreDatabaseModel.PRODUCT_ID)));

                productStore.setId(cursor.getString(cursor.getColumnIndex(DefaultPersistenceModel.ID)));
                productStore.setSellPrice(cursor.getDouble(cursor.getColumnIndex(ProductStoreDatabaseModel.SELL_PRICE)));
                productStore.setProduct(product);
            }
        }

        cursor.close();

        return productStore;
    }

    public ProductStore findProductStoreById(String id) {
        String criteria = DefaultPersistenceModel.ID + " = ? ";

        Cursor cursor = context.getContentResolver().query(dbUriProductStore, null, criteria,
                new String[] { id }, null);

        ProductStore productStore = null;

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                productStore = new ProductStore();
                Product product = productDbAdapter.findAllProductByRefId(cursor.getString
                        (cursor.getColumnIndex(ProductStoreDatabaseModel.PRODUCT_ID)));

                productStore.setId(cursor.getString(cursor.getColumnIndex(DefaultPersistenceModel.ID)));
                productStore.setSellPrice(cursor.getDouble(cursor.getColumnIndex(ProductStoreDatabaseModel.SELL_PRICE)));
                productStore.setProduct(product);
            }
        }

        cursor.close();

        return productStore;
    }

    public ProductStore findProductStoreByProductId(String productId) {
        String criteria = ProductStoreDatabaseModel.PRODUCT_ID + " = ?  AND "
            + DefaultPersistenceModel.STATUS_FLAG + " = " + SignageVariables.ACTIVE;

        Cursor cursor = context.getContentResolver().query(dbUriProductStore, null, criteria,
            new String[] { productId }, null);

        ProductStore productStore = null;

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                productStore = new ProductStore();
                productStore.setId(cursor.getString(cursor.getColumnIndex(DefaultPersistenceModel.ID)));
                productStore.setSellPrice(cursor.getDouble(cursor.getColumnIndex(ProductStoreDatabaseModel.SELL_PRICE)));
                productStore.getProduct().setId(cursor.getString(cursor.getColumnIndex(ProductStoreDatabaseModel.PRODUCT_ID)));
            }
        }

        cursor.close();

        return productStore;
    }

    public void saveProductStores(List<ProductStore> productStores) {
        for (ProductStore productStore : productStores) {
            ContentValues values = new ContentValues();

            if(findProductStoreById(productStore.getId()) == null) {
                values.put(ProductStoreDatabaseModel.ID, productStore.getId());
                values.put(ProductStoreDatabaseModel.PRODUCT_ID, productStore.getProduct().getId());
                values.put(ProductStoreDatabaseModel.SELL_PRICE, productStore.getSellPrice());
                values.put(ProductStoreDatabaseModel.STATUS_FLAG , 1);

                context.getContentResolver().insert(dbUriProductStore, values);
            } else {
                values.put(ProductStoreDatabaseModel.PRODUCT_ID, productStore.getProduct().getId());
                values.put(ProductStoreDatabaseModel.SELL_PRICE, productStore.getSellPrice());
                values.put(ProductStoreDatabaseModel.STATUS_FLAG , 1);

                context.getContentResolver().update(dbUriProductStore, values, ProductStoreDatabaseModel.ID + " = ?", new String[] { productStore.getId() });
            }
        }
    }

    public void saveProductStore(ProductStore productStore) {
        ContentValues values = new ContentValues();

        if(findProductStoreById(productStore.getId()) == null) {
            values.put(ProductStoreDatabaseModel.ID, productStore.getId());
            values.put(ProductStoreDatabaseModel.PRODUCT_ID, productStore.getProduct().getId());
            productDbAdapter.saveProduct(productStore.getProduct());

            values.put(ProductStoreDatabaseModel.SELL_PRICE, productStore.getSellPrice());
            values.put(ProductStoreDatabaseModel.STATUS_FLAG , 1);

            context.getContentResolver().insert(dbUriProductStore, values);
        } else {
            values.put(ProductStoreDatabaseModel.PRODUCT_ID, productStore.getProduct().getId());
            productDbAdapter.saveProduct(productStore.getProduct());

            values.put(ProductStoreDatabaseModel.SELL_PRICE, productStore.getSellPrice());
            values.put(ProductStoreDatabaseModel.STATUS_FLAG , 1);

            context.getContentResolver().update(dbUriProductStore, values, ProductStoreDatabaseModel.REF_ID + " = ?", new String[] { productStore.getId() });
        }
    }

    public List<String> getAllProductStoreId() {
        String query = ProductStoreDatabaseModel.STATUS_FLAG + " = " + SignageVariables.ACTIVE;
        Cursor cursor = context.getContentResolver().query(dbUriProductStore, null, query, null, null);

        List<String> productStores = new ArrayList<String>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                productStores.add(cursor.getString(cursor
                    .getColumnIndex(ProductStoreDatabaseModel.ID)));
            }
        }

        cursor.close();

        return productStores;
    }

    public void deleteProductStores(List<String> ids) {
        for(String id : ids) {
            context.getContentResolver().delete(dbUriProductStore, ProductStoreDatabaseModel.ID + " = ? ", new String[] { id });
        }
    }

    public void voidProductStore(String id) {
        ContentValues values = new ContentValues();
        values.put(ProductStoreDatabaseModel.STATUS_FLAG, 0);

        context.getContentResolver().update(dbUriProductStore, values, ProductStoreDatabaseModel.ID + " = ? ", new String[] { id });
    }

    public void voidProductStores(List<String> ids) {
        for(String id : ids) {
            ContentValues values = new ContentValues();
            values.put(ProductStoreDatabaseModel.STATUS_FLAG, 0);

            context.getContentResolver().update(dbUriProductStore, values, ProductStoreDatabaseModel.ID + " = ? ", new String[] { id });
        }
    }

    public void voidAllProductStores() {
        ContentValues values = new ContentValues();
        values.put(ProductStoreDatabaseModel.STATUS_FLAG, 0);

        context.getContentResolver().update(dbUriProductStore, values, null, null);
    }
}
