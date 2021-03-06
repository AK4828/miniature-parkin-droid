package org.meruvian.esales.collector.entity;

import org.meruvian.esales.collector.core.DefaultPersistence;
import org.meruvian.esales.collector.core.commons.User;

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
