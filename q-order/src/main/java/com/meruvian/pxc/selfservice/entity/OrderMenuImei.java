package com.meruvian.pxc.selfservice.entity;

import com.meruvian.pxc.selfservice.core.DefaultPersistence;

/**
 * Created by meruvian on 17/09/15.
 */
public class OrderMenuImei extends DefaultPersistence {
    private OrderMenu orderMenu = new OrderMenu();
    private String imei;

    public OrderMenu getOrderMenu() {
        return orderMenu;
    }

    public void setOrderMenu(OrderMenu orderMenu) {
        this.orderMenu = orderMenu;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }
}
