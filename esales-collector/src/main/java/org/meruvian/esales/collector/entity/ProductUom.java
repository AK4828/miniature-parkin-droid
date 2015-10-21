package org.meruvian.esales.collector.entity;

import org.meruvian.esales.collector.core.DefaultPersistence;

/**
 * Created by meruvian on 30/07/15.
 */
public class ProductUom extends DefaultPersistence {
    private String name;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
