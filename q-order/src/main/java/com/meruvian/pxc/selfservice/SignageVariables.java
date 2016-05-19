package com.meruvian.pxc.selfservice;

/**
 * Created by ludviantoovandi on 28/01/15.
 */
public class SignageVariables {
    public static final String PREFS_SERVER = "APP_SERVER";
    public static final String PGA_APP_ID = "419c6697-14b7-4853-880e-b68e3731e316";
    public static final String PGA_API_SECRET = "s3cr3t";
    public static final String SERVER_URL = "http://point.demo.meruvian.org";
//    public static final String SERVER_URL = "http://192.168.2.213:8090";

    public static final String FXPC_APP_ID = "ef1157dc-39de-4bf1-b1aa-dae9c15d6d60";
    public static final String FXPC_APP_SECRET = "4ipZr2tK3feVx3DjkFm7KQXHTKtW3tL9PnGephjnSbEJhfrxZlRR5j58SxmZco4Q";
    public static final String FXPC_AUTH_URL = "http://fxpc.demo.meruvian.org/oauth/authorize";
    public static final String FXPC_REQUEST_ME = "http://fxpc.demo.meruvian.org/api/users/me";
    public static final String FXPC_CALLBACK = "midas://social_login";

    public static final String PGA_REQUEST_TOKEN = "/oauth/token";
    public static final String PXC_REQUEST_TOKEN = "/api/partner/fxpc/token";
    public static final String PGA_CURRENT_ME = "/api/users/me";
    public static final String PGA_CURRENT_SITE = "/api/sites/me";
    public static final String PGA_CURRENT_ROLE = "/api/users/me/roles";

    public static final String PUBLIC_FOLDER = "signage";

    public static final String ACTIVE = "1";
    public static final String INACTIVE = "0";


    public static final int CATEGORY_GET_TASK = 1;
    public static final int PRODUCT_GET_TASK = 2;
    public static final int PRODUCT_DETAIL_TASK = 3;
    public static final int IMAGE_PRODUCT_TASK = 4;

    public static final int CAMPAIGN_GET_TASK = 5;
    public static final int CAMPAIGN_DETAIL_TASK = 6;
    public static final int IMAGE_CAMPAIGN_PRODUCT_TASK = 7;

    public static final int PRODUCT_UOM_GET_TASK = 8;
    public static final int REQUEST_ORDER = 9;
    public static final int CONTACT_GET_TASK = 10;

    public static final int CATEGORY_ELEMENTS_TASK = 11;
    public static final int PRODUCT_ELEMENTS_TASK = 12;

    public static final int PRODUCT_STORE_GET_TASK = 13;
    public static final int PRODUCT_STORE_ELEMENTS_TASK = 14;
    public static final int SELLER_ORDER_GET_TASK = 15;
    public static final int SELLER_ORDER_MENU_GET_TASK = 15;

    public static final int FXPC_REQUEST_ACCESS = 16;
    public static final int FXPC_REQUEST_TOKEN_TASK = 17;
    public static final int FXPC_REQUEST_ME_TASK = 18;
    public static final int FXPC_REFRESH_TOKEN_TASK = 19;


}
