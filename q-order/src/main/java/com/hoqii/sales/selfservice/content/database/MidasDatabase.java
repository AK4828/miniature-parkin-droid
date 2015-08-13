package com.hoqii.sales.selfservice.content.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hoqii.sales.selfservice.content.database.model.CampaignDatabaseModel;
import com.hoqii.sales.selfservice.content.database.model.CampaignDetailDatabaseModel;
import com.hoqii.sales.selfservice.content.database.model.CartDatabaseModel;
import com.hoqii.sales.selfservice.content.database.model.CartMenuDatabaseModel;
import com.hoqii.sales.selfservice.content.database.model.CategoryDatabaseModel;
import com.hoqii.sales.selfservice.content.database.model.ContactDatabaseModel;
import com.hoqii.sales.selfservice.content.database.model.DefaultPersistenceModel;
import com.hoqii.sales.selfservice.content.database.model.OrderDatabaseModel;
import com.hoqii.sales.selfservice.content.database.model.OrderMenuDatabaseModel;
import com.hoqii.sales.selfservice.content.database.model.ProductDatabaseModel;
import com.hoqii.sales.selfservice.content.database.model.ProductUomDatabaseModel;

/**
 * Created by meruvian on 29/01/15.
 */

public class MidasDatabase extends SQLiteOpenHelper {
    public static final String DATABASE = "hoqii_sales";
    private static final int VERSION = 1;

    public static final String CATEGORY_TABLE = "category";
    public static final String PRODUCT_TABLE = "product";
    public static final String CAMPAIGN_TABLE = "campaign";
    public static final String CAMPAIGN_DETAIL_TABLE = "campaign_detail";
    public static final String ORDER_TABLE = "sales_order";
    public static final String ORDER_MENU_TABLE = "order_menu";

    public static final String PRODUCT_UOM = "product_uom";
    public static final String CONTACT = "contact";

    public static final String CART_TABLE = "shopping_cart";
    public static final String CART_MENU_TABLE = "cart_menu";

    private Context context;

    public MidasDatabase(Context context) {
        super(context, DATABASE, null, VERSION);

        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + CATEGORY_TABLE + "("
                + CategoryDatabaseModel.ID + " TEXT PRIMARY KEY, "
                + DefaultPersistenceModel.CREATE_BY + " TEXT, "
                + DefaultPersistenceModel.CREATE_DATE + " INTEGER, "
                + DefaultPersistenceModel.UPDATE_BY + " TEXT, "
                + DefaultPersistenceModel.UPDATE_DATE + " INTEGER, "
                + DefaultPersistenceModel.REF_ID + " TEXT, "
                + DefaultPersistenceModel.STATUS_FLAG + " INTEGER, "
                + DefaultPersistenceModel.SITE_ID + " TEXT, "
                + DefaultPersistenceModel.SYNC_STATUS + " INTEGER, "
                + CategoryDatabaseModel.PARENT_CATEGORY_ID + " TEXT, "
                + CategoryDatabaseModel.CATEGORY_NAME + " TEXT)");

        db.execSQL("CREATE TABLE " + PRODUCT_TABLE + "("
                + ProductDatabaseModel.ID + " TEXT PRIMARY KEY, "
                + DefaultPersistenceModel.CREATE_BY + " TEXT, "
                + DefaultPersistenceModel.CREATE_DATE + " INTEGER, "
                + DefaultPersistenceModel.UPDATE_BY + " TEXT, "
                + DefaultPersistenceModel.UPDATE_DATE + " INTEGER, "
                + DefaultPersistenceModel.STATUS_FLAG + " INTEGER, "
                + DefaultPersistenceModel.REF_ID + " TEXT, "
                + DefaultPersistenceModel.SITE_ID + " TEXT, "
                + DefaultPersistenceModel.SYNC_STATUS + " INTEGER, "
                + ProductDatabaseModel.NAME + " TEXT, "
                + ProductDatabaseModel.DESCRIPTION + " TEXT, "
                + ProductDatabaseModel.PRICE + " TEXT, "
                + ProductDatabaseModel.IMAGE + " INTEGER, "
                + ProductDatabaseModel.PARENT_CATEGORY_ID + " TEXT,"
                + ProductDatabaseModel.CATEGORY_ID + " TEXT,"
                + ProductDatabaseModel.PRODUCT_VALUE + " TEXT,"
                + ProductDatabaseModel.MIN_QUANTITY + " TEXT,"
                + ProductDatabaseModel.MAX_QUANTITY + " TEXT,"
                + ProductDatabaseModel.UOM_ID + " TEXT, "
                + ProductDatabaseModel.CODE + " TEXT, "
                + ProductDatabaseModel.FG + " INTEGER, "
                + ProductDatabaseModel.SELL_ABLE + " INTEGER, "
                + ProductDatabaseModel.COMPOSITION_STATUS + " INTEGER)");

        db.execSQL("CREATE TABLE " + CAMPAIGN_TABLE + "("
                + CategoryDatabaseModel.ID + " TEXT PRIMARY KEY, "
                + DefaultPersistenceModel.CREATE_BY + " TEXT, "
                + DefaultPersistenceModel.CREATE_DATE + " INTEGER, "
                + DefaultPersistenceModel.UPDATE_BY + " TEXT, "
                + DefaultPersistenceModel.UPDATE_DATE + " INTEGER, "
                + DefaultPersistenceModel.REF_ID + " TEXT, "
                + DefaultPersistenceModel.STATUS_FLAG + " INTEGER, "
                + DefaultPersistenceModel.SITE_ID + " TEXT, "
                + DefaultPersistenceModel.SYNC_STATUS + " INTEGER, "
                + CampaignDatabaseModel.NAME + " TEXT, "
                + CampaignDatabaseModel.DESCRIPTION + " TEXT, "
                + CampaignDatabaseModel.SHOW_ON_ANDROID + " INTEGER)");

        db.execSQL("CREATE TABLE " + CAMPAIGN_DETAIL_TABLE + "("
                + CategoryDatabaseModel.ID + " TEXT PRIMARY KEY, "
                + DefaultPersistenceModel.CREATE_BY + " TEXT, "
                + DefaultPersistenceModel.CREATE_DATE + " INTEGER, "
                + DefaultPersistenceModel.UPDATE_BY + " TEXT, "
                + DefaultPersistenceModel.UPDATE_DATE + " INTEGER, "
                + DefaultPersistenceModel.REF_ID + " TEXT, "
                + DefaultPersistenceModel.STATUS_FLAG + " INTEGER, "
                + DefaultPersistenceModel.SITE_ID + " TEXT, "
                + DefaultPersistenceModel.SYNC_STATUS + " INTEGER, "
                + CampaignDetailDatabaseModel.CAMPAIGN_ID + " TEXT, "
                + CampaignDetailDatabaseModel.DESCRIPTION + " TEXT, "
                + CampaignDetailDatabaseModel.PATH + " TEXT)");

        db.execSQL("CREATE TABLE " + ORDER_TABLE + " ("
                + DefaultPersistenceModel.ID + " TEXT, "
                + DefaultPersistenceModel.CREATE_BY + " TEXT, "
                + DefaultPersistenceModel.CREATE_DATE + " INTEGER, "
                + DefaultPersistenceModel.UPDATE_BY + " TEXT, "
                + DefaultPersistenceModel.UPDATE_DATE + " INTEGER, "
                + DefaultPersistenceModel.STATUS_FLAG + " INTEGER, "
                + DefaultPersistenceModel.SITE_ID + " TEXT, "
                + DefaultPersistenceModel.REF_ID + " TEXT, "
                + DefaultPersistenceModel.SYNC_STATUS + " INTEGER, "
                + OrderDatabaseModel.ORDER_TYPE + " TEXT, "
                + OrderDatabaseModel.CONTACT_ID + " TEXT, "
                + OrderDatabaseModel.RECIEPT_NUMBER + " TEXT)");

        db.execSQL("CREATE TABLE " + ORDER_MENU_TABLE + " ("
                + DefaultPersistenceModel.ID + " TEXT, "
                + DefaultPersistenceModel.CREATE_BY + " TEXT, "
                + DefaultPersistenceModel.CREATE_DATE + " INTEGER, "
                + DefaultPersistenceModel.UPDATE_BY + " TEXT, "
                + DefaultPersistenceModel.UPDATE_DATE + " INTEGER, "
                + DefaultPersistenceModel.SITE_ID + " TEXT, "
                + DefaultPersistenceModel.STATUS_FLAG + " INTEGER, "
                + DefaultPersistenceModel.SYNC_STATUS + " INTEGER, "
                + DefaultPersistenceModel.REF_ID + " TEXT, "
                + OrderMenuDatabaseModel.QUANTITY + " INTEGER, "
                + OrderMenuDatabaseModel.DELIVERY_STATUS + " INTEGER, "
                + OrderMenuDatabaseModel.PRODUCT_ID + " TEXT, "
                + OrderMenuDatabaseModel.ORDER_ID + " TEXT, "
                + OrderMenuDatabaseModel.DISCOUNT_NOMINAL + " TEXT, "
                + OrderMenuDatabaseModel.DISCOUNT_PERCENT + " TEXT, "
                + OrderMenuDatabaseModel.DISCOUNT_NAME + " TEXT, "
                + OrderMenuDatabaseModel.DESC + " TEXT, "
                + OrderMenuDatabaseModel.PRICE + " TEXT)");

        db.execSQL("CREATE TABLE " + CART_TABLE + " ("
                + DefaultPersistenceModel.ID + " TEXT, "
                + DefaultPersistenceModel.CREATE_BY + " TEXT, "
                + DefaultPersistenceModel.CREATE_DATE + " INTEGER, "
                + DefaultPersistenceModel.UPDATE_BY + " TEXT, "
                + DefaultPersistenceModel.UPDATE_DATE + " INTEGER, "
                + DefaultPersistenceModel.STATUS_FLAG + " INTEGER, "
                + DefaultPersistenceModel.SITE_ID + " TEXT, "
                + DefaultPersistenceModel.REF_ID + " TEXT, "
                + DefaultPersistenceModel.SYNC_STATUS + " INTEGER, "
                + CartDatabaseModel.ORDER_TYPE + " TEXT, "
                + CartDatabaseModel.RECIEPT_NUMBER + " TEXT)");

        db.execSQL("CREATE TABLE " + CART_MENU_TABLE + " ("
                + DefaultPersistenceModel.ID + " TEXT, "
                + DefaultPersistenceModel.CREATE_BY + " TEXT, "
                + DefaultPersistenceModel.CREATE_DATE + " INTEGER, "
                + DefaultPersistenceModel.UPDATE_BY + " TEXT, "
                + DefaultPersistenceModel.UPDATE_DATE + " INTEGER, "
                + DefaultPersistenceModel.SITE_ID + " TEXT, "
                + DefaultPersistenceModel.STATUS_FLAG + " INTEGER, "
                + DefaultPersistenceModel.SYNC_STATUS + " INTEGER, "
                + DefaultPersistenceModel.REF_ID + " TEXT, "
                + CartMenuDatabaseModel.QUANTITY + " INTEGER, "
                + CartMenuDatabaseModel.DELIVERY_STATUS + " INTEGER, "
                + CartMenuDatabaseModel.PRODUCT_ID + " TEXT, "
                + CartMenuDatabaseModel.CART_ID + " TEXT, "
                + CartMenuDatabaseModel.DISCOUNT_NOMINAL + " TEXT, "
                + CartMenuDatabaseModel.DISCOUNT_PERCENT + " TEXT, "
                + CartMenuDatabaseModel.DISCOUNT_NAME + " TEXT, "
                + CartMenuDatabaseModel.DESC + " TEXT, "
                + CartMenuDatabaseModel.PRICE + " TEXT)");

        db.execSQL("CREATE TABLE " + PRODUCT_UOM + "("
                + DefaultPersistenceModel.ID + " TEXT PRIMARY KEY, "
                + DefaultPersistenceModel.CREATE_BY + " TEXT, "
                + DefaultPersistenceModel.CREATE_DATE + " INTEGER, "
                + DefaultPersistenceModel.UPDATE_BY + " TEXT, "
                + DefaultPersistenceModel.UPDATE_DATE + " INTEGER, "
                + DefaultPersistenceModel.REF_ID + " TEXT, "
                + DefaultPersistenceModel.STATUS_FLAG + " INTEGER, "
                + DefaultPersistenceModel.SITE_ID + " TEXT, "
                + DefaultPersistenceModel.SYNC_STATUS + " INTEGER, "
                + ProductUomDatabaseModel.NAME + " TEXT, "
                + ProductUomDatabaseModel.DESCRIPTION + " TEXT)");

        db.execSQL("CREATE TABLE " + CONTACT + "("
                + DefaultPersistenceModel.ID + " TEXT PRIMARY KEY, "
                + DefaultPersistenceModel.CREATE_BY + " TEXT, "
                + DefaultPersistenceModel.CREATE_DATE + " INTEGER, "
                + DefaultPersistenceModel.UPDATE_BY + " TEXT, "
                + DefaultPersistenceModel.UPDATE_DATE + " INTEGER, "
                + DefaultPersistenceModel.REF_ID + " TEXT, "
                + DefaultPersistenceModel.STATUS_FLAG + " INTEGER, "
                + DefaultPersistenceModel.SITE_ID + " TEXT, "
                + DefaultPersistenceModel.SYNC_STATUS + " INTEGER, "
                + ContactDatabaseModel.DEFAULT_CONTACT + " TEXT, "
                + ContactDatabaseModel.USER_ID + " TEXT, "
                + ContactDatabaseModel.RECIPIENT + " TEXT, "
                + ContactDatabaseModel.PHONE + " TEXT, "
                + ContactDatabaseModel.CITY + " TEXT, "
                + ContactDatabaseModel.SUB_DISTRICT + " TEXT, "
                + ContactDatabaseModel.PROVINCE + " TEXT, "
                + ContactDatabaseModel.ZIP + " TEXT, "
                + ContactDatabaseModel.CONTACT_NAME + " TEXT, "
                + ContactDatabaseModel.ADDRESS + " TEXT)");



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {

        }
    }
}
