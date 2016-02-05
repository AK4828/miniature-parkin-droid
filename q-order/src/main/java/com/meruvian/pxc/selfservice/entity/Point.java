package com.meruvian.pxc.selfservice.entity;

import com.meruvian.pxc.selfservice.core.commons.User;

/**
 * Created by akm on 04/02/16.
 */
public class Point {
    private User user;
    private double point;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getPoint() {
        return point;
    }

    public void setPoint(double point) {
        this.point = point;
    }
}
