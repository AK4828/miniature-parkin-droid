package com.meruvian.pxc.selfservice.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.meruvian.pxc.selfservice.core.commons.User;

/**
 * Created by akm on 04/02/16.
 */
public class Point {
    @JsonProperty("points")
    private double point;

    public double getPoint() {
        return point;
    }

    public void setPoint(double point) {
        this.point = point;
    }
}
