package com.meruvian.pxc.selfservice.entity;

import com.meruvian.pxc.selfservice.core.DefaultPersistence;
import com.meruvian.pxc.selfservice.core.commons.User;

/**
 * Created by meruvian on 05/10/15.
 */
public class Assigment extends DefaultPersistence {
    public enum AssigmentStatus {
        OPEN, ONDEB, CLOSE
    }

    private User collector = new User();
    private AssigmentStatus status = AssigmentStatus.OPEN;

    public User getCollector() {
        return collector;
    }

    public void setCollector(User collector) {
        this.collector = collector;
    }

    public AssigmentStatus getStatus() {
        return status;
    }

    public void setStatus(AssigmentStatus status) {
        this.status = status;
    }
}
