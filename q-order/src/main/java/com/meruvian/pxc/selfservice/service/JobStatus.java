package com.meruvian.pxc.selfservice.service;

/**
 * Created by akm on 04/02/16.
 */
public interface JobStatus {
    static final int ADDED = 0;
    static final int SUCCESS = 1;
    static final int ABORTED = 2;
    static final int SYSTEM_ERROR = 3;
    static final int USER_ERROR = 4;
}
