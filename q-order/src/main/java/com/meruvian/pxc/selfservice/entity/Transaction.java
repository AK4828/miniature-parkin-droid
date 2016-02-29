package com.meruvian.pxc.selfservice.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by akm on 09/02/16.
 */
public class Transaction {

    @JsonProperty("status")
    private String status;
    @JsonProperty("orderId")
    private String orderId;

    public Transaction(String status, String orderId) {
        this.status = status;
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}

