package com.meruvian.pxc.selfservice.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.meruvian.midas.core.entity.DefaultEntity;

/**
 * Created by meruvian on 22/01/15.
 */
public class Category extends DefaultEntity {
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("parent")
    private Category parentCategory;
    private int status;

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

    public Category getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
