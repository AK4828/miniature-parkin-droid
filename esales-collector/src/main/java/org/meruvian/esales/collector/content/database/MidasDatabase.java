package org.meruvian.esales.collector.content.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.meruvian.esales.collector.content.database.model.AssigmentDatabaseModel;
import org.meruvian.esales.collector.content.database.model.AssigmentDetailDatabaseModel;
import org.meruvian.esales.collector.content.database.model.AssigmentDetailItemDatabaseModel;
import org.meruvian.esales.collector.content.database.model.CampaignDatabaseModel;
import org.meruvian.esales.collector.content.database.model.CampaignDetailDatabaseModel;
import org.meruvian.esales.collector.content.database.model.CartDatabaseModel;
import org.meruvian.esales.collector.content.database.model.CartMenuDatabaseModel;
import org.meruvian.esales.collector.content.database.model.CategoryDatabaseModel;
import org.meruvian.esales.collector.content.database.model.ContactDatabaseModel;
import org.meruvian.esales.collector.content.database.model.DefaultPersistenceModel;
import org.meruvian.esales.collector.content.database.model.OrderDatabaseModel;
import org.meruvian.esales.collector.content.database.model.OrderMenuDatabaseModel;
import org.meruvian.esales.collector.content.database.model.OrderMenuImeiDatabaseModel;
import org.meruvian.esales.collector.content.database.model.ProductDatabaseModel;
import org.meruvian.esales.collector.content.database.model.ProductStoreDatabaseModel;
import org.meruvian.esales.collector.content.database.model.ProductUomDatabaseModel;
import org.meruvian.esales.collector.content.database.model.SettleDatabaseModel;
import org.meruvian.esales.collector.content.database.model.UserDatabaseModel;

/**
 * Created by meruvian on 29/01/15.
 */

public class MidasDatabase extends SQLiteOpenHelper {
    public static final String DATABASE = "e_sales_collector";
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

    public static final String PRODUCT_STORE_TABLE = "product_store";
    public static final String USER_TABLE = "user_store";

    public static final String ORDER_MENU_IMEI_TABLE = "order_menu_imei_table";

        public static final String ASSIGMENT_TABLE = "assigment";
        public static final String ASSIGMENT_DETAIL_TABLE = "assigment_detail";
        public static final String ASSIGMENT_DETAIL_ITEM_TABLE = "assigment_detail_item";
        public static final String SETTLE_TABLE = "settle";

    private Context context;

    public MidasDatabase(Context context) {
        super(context, DATABASE, null, VERSION);

        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + CATEGORY_TABLE + "("
                    + DefaultPersistenceModel.ID + " TEXT PRIMARY KEY, "
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
                    + DefaultPersistenceModel.ID + " TEXT PRIMARY KEY, "
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
                    + DefaultPersistenceModel.ID + " TEXT PRIMARY KEY, "
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
                    + DefaultPersistenceModel.ID + " TEXT PRIMARY KEY, "
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
                    + OrderDatabaseModel.RECIEPT_NUMBER + " TEXT, "
                    + OrderDatabaseModel.STATUS + " TEXT)");

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
                    + OrderMenuDatabaseModel.PRODUCT_STORE_ID + " TEXT, "
                    + OrderMenuDatabaseModel.ORDER_ID + " TEXT, "
                    + OrderMenuDatabaseModel.DISCOUNT_NOMINAL + " TEXT, "
                    + OrderMenuDatabaseModel.DISCOUNT_PERCENT + " TEXT, "
                    + OrderMenuDatabaseModel.DISCOUNT_NAME + " TEXT, "
                    + OrderMenuDatabaseModel.DESC + " TEXT, "
                    + OrderMenuDatabaseModel.PRICE + " TEXT, "
                    + OrderMenuDatabaseModel.STATUS + " TEXT)");

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

            db.execSQL("CREATE TABLE " + PRODUCT_STORE_TABLE + "("
                    + DefaultPersistenceModel.ID + " TEXT PRIMARY KEY, "
                    + DefaultPersistenceModel.CREATE_BY + " TEXT, "
                    + DefaultPersistenceModel.CREATE_DATE + " INTEGER, "
                    + DefaultPersistenceModel.UPDATE_BY + " TEXT, "
                    + DefaultPersistenceModel.UPDATE_DATE + " INTEGER, "
                    + DefaultPersistenceModel.REF_ID + " TEXT, "
                    + DefaultPersistenceModel.STATUS_FLAG + " INTEGER, "
                    + DefaultPersistenceModel.SITE_ID + " TEXT, "
                    + DefaultPersistenceModel.SYNC_STATUS + " INTEGER, "
                    + ProductStoreDatabaseModel.PRODUCT_ID + " TEXT, "
                    + ProductStoreDatabaseModel.SELL_PRICE + " TEXT, "
                    + ProductStoreDatabaseModel.INCENTIVE + " TEXT, "
                    + ProductStoreDatabaseModel.STOCK + " TEXT)");

            db.execSQL("CREATE TABLE " + USER_TABLE + "("
                    + DefaultPersistenceModel.ID + " TEXT PRIMARY KEY, "
                    + DefaultPersistenceModel.CREATE_BY + " TEXT, "
                    + DefaultPersistenceModel.CREATE_DATE + " INTEGER, "
                    + DefaultPersistenceModel.UPDATE_BY + " TEXT, "
                    + DefaultPersistenceModel.UPDATE_DATE + " INTEGER, "
                    + DefaultPersistenceModel.REF_ID + " TEXT, "
                    + DefaultPersistenceModel.STATUS_FLAG + " INTEGER, "
                    + DefaultPersistenceModel.SITE_ID + " TEXT, "
                    + DefaultPersistenceModel.SYNC_STATUS + " INTEGER, "
                    + UserDatabaseModel.USERNAME + " TEXT, "
                    + UserDatabaseModel.PASSWORD + " TEXT, "
                    + UserDatabaseModel.EMAIL + " TEXT, "
                    + UserDatabaseModel.NAME_PREFIX + " TEXT, "
                    + UserDatabaseModel.NAME_FIRST + " TEXT, "
                    + UserDatabaseModel.NAME_MIDDLE + " TEXT, "
                    + UserDatabaseModel.NAME_LAST + " TEXT, "
                    + UserDatabaseModel.ADDRESS_STREET1 + " TEXT, "
                    + UserDatabaseModel.ADDRESS_STREET2 + " TEXT, "
                    + UserDatabaseModel.ADDRESS_CITY + " TEXT, "
                    + UserDatabaseModel.ADDRESS_STATE + " TEXT, "
                    + UserDatabaseModel.ADDRESS_ZIP + " TEXT, "
                    + UserDatabaseModel.BANK_NAME + " TEXT, "
                    + UserDatabaseModel.ACCOUNT_NUMBER + " TEXT, "
                    + UserDatabaseModel.ACCOUNT_NAME + " TEXT, "
                    + UserDatabaseModel.PHONE + " TEXT, "
                    + UserDatabaseModel.UPLINE + " TEXT, "
                    + UserDatabaseModel.REFERENCE + " TEXT, "
                    + UserDatabaseModel.AGENT_CODE + " TEXT)");

            db.execSQL("CREATE TABLE " + ORDER_MENU_IMEI_TABLE + "("
                    + DefaultPersistenceModel.ID + " TEXT PRIMARY KEY, "
                    + DefaultPersistenceModel.CREATE_BY + " TEXT, "
                    + DefaultPersistenceModel.CREATE_DATE + " INTEGER, "
                    + DefaultPersistenceModel.UPDATE_BY + " TEXT, "
                    + DefaultPersistenceModel.UPDATE_DATE + " INTEGER, "
                    + DefaultPersistenceModel.REF_ID + " TEXT, "
                    + DefaultPersistenceModel.STATUS_FLAG + " INTEGER, "
                    + DefaultPersistenceModel.SITE_ID + " TEXT, "
                    + DefaultPersistenceModel.SYNC_STATUS + " INTEGER, "
                    + OrderMenuImeiDatabaseModel.ORDER_MENU_ID + " TEXT, "
                    + OrderMenuImeiDatabaseModel.IMEI + " TEXT)");

            db.execSQL("CREATE TABLE " + ASSIGMENT_TABLE + "("
                    + DefaultPersistenceModel.ID + " TEXT PRIMARY KEY, "
                    + DefaultPersistenceModel.CREATE_BY + " TEXT, "
                    + DefaultPersistenceModel.CREATE_DATE + " INTEGER, "
                    + DefaultPersistenceModel.UPDATE_BY + " TEXT, "
                    + DefaultPersistenceModel.UPDATE_DATE + " INTEGER, "
                    + DefaultPersistenceModel.REF_ID + " TEXT, "
                    + DefaultPersistenceModel.STATUS_FLAG + " INTEGER, "
                    + DefaultPersistenceModel.SITE_ID + " TEXT, "
                    + DefaultPersistenceModel.SYNC_STATUS + " INTEGER, "
                    + AssigmentDatabaseModel.COLLECTOR_ID + " TEXT, "
                    + AssigmentDatabaseModel.STATUS + " TEXT)");

            db.execSQL("CREATE TABLE " + ASSIGMENT_DETAIL_TABLE + "("
                    + DefaultPersistenceModel.ID + " TEXT PRIMARY KEY, "
                    + DefaultPersistenceModel.CREATE_BY + " TEXT, "
                    + DefaultPersistenceModel.CREATE_DATE + " INTEGER, "
                    + DefaultPersistenceModel.UPDATE_BY + " TEXT, "
                    + DefaultPersistenceModel.UPDATE_DATE + " INTEGER, "
                    + DefaultPersistenceModel.REF_ID + " TEXT, "
                    + DefaultPersistenceModel.STATUS_FLAG + " INTEGER, "
                    + DefaultPersistenceModel.SITE_ID + " TEXT, "
                    + DefaultPersistenceModel.SYNC_STATUS + " INTEGER, "
                    + AssigmentDetailDatabaseModel.ASSIGMENT_ID + " TEXT, "
                    + AssigmentDetailDatabaseModel.AGENT_ID + " TEXT, "
                    + AssigmentDetailDatabaseModel.STATUS + " TEXT)");

            db.execSQL("CREATE TABLE " + ASSIGMENT_DETAIL_ITEM_TABLE + "("
                    + DefaultPersistenceModel.ID + " TEXT PRIMARY KEY, "
                    + DefaultPersistenceModel.CREATE_BY + " TEXT, "
                    + DefaultPersistenceModel.CREATE_DATE + " INTEGER, "
                    + DefaultPersistenceModel.UPDATE_BY + " TEXT, "
                    + DefaultPersistenceModel.UPDATE_DATE + " INTEGER, "
                    + DefaultPersistenceModel.REF_ID + " TEXT, "
                    + DefaultPersistenceModel.STATUS_FLAG + " INTEGER, "
                    + DefaultPersistenceModel.SITE_ID + " TEXT, "
                    + DefaultPersistenceModel.SYNC_STATUS + " INTEGER, "
                    + AssigmentDetailItemDatabaseModel.ASSIGMENT_DETAIL_ID + " TEXT, "
                    + AssigmentDetailItemDatabaseModel.PRODUCT_ID + " TEXT, "
                    + AssigmentDetailItemDatabaseModel.QUANTITY + " TEXT)");

            db.execSQL("CREATE TABLE " + SETTLE_TABLE + "("
                    + DefaultPersistenceModel.ID + " TEXT PRIMARY KEY, "
                    + DefaultPersistenceModel.CREATE_BY + " TEXT, "
                    + DefaultPersistenceModel.CREATE_DATE + " INTEGER, "
                    + DefaultPersistenceModel.UPDATE_BY + " TEXT, "
                    + DefaultPersistenceModel.UPDATE_DATE + " INTEGER, "
                    + DefaultPersistenceModel.REF_ID + " TEXT, "
                    + DefaultPersistenceModel.STATUS_FLAG + " INTEGER, "
                    + DefaultPersistenceModel.SITE_ID + " TEXT, "
                    + DefaultPersistenceModel.SYNC_STATUS + " INTEGER, "
                    + SettleDatabaseModel.ASSIGMENT_DETAIL_ID + " TEXT, "
                    + SettleDatabaseModel.PRODUCT_ID + " TEXT, "
                    + SettleDatabaseModel.QUANTITY + " TEXT, "
                    + SettleDatabaseModel.SELL_PRICE + " TEXT)");



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {

        }
    }
}
