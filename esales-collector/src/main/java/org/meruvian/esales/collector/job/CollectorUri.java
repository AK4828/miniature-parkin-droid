package org.meruvian.esales.collector.job;

/**
 * Created by meruvian on 30/07/15.
 */
public class CollectorUri {
    public static final String ORDER = "/api/orders";
    public static final String UPDATE_ORDER = "/api/orders/%s";
    public static final String ORDER_MENU = "/api/orders/%s/menus";
    public static final String CONTACT = "/api/contacts";
    public static final String UPDATE_CONTACT = "/api/contacts/%s";

    public static final String GET_AGENTS = "/api/order/collectors/agent/%s";
    public static final String GET_BUYERS = "/api/order/collectors/agent/%s/buyers";
    public static final String GET_BUYERS_ORDERS = "/api/order/collectors/agent/%s/buyers/%s/orders";
    public static final String GET_ORDER_MENUS = "/api/orders/%s/menus";


    public static final String GET_ASSIGMENT_DETAIL = "/api/assigments/details/agents/%s";
    public static final String GET_ASSIGMENT = "/api/assigments/collectors/%s";
    public static final String GET_ASG_DETAIL = "/api/assigments/%s/detail";
    public static final String PUT_ASG_DETAIL = "/api/assigments/detail/%s";

    public static final String GET_ASG_ITEM_BY_AGENT = "/api/assigments/agents/%s/items";
    public static final String GET_ASG_ITEM = "/api/assigments/%s/items";
    public static final String POST_ASG_ITEM = "/api/assigments/items";

    public static final String GET_SETTLE = "/api/settle/list/%s";


}
