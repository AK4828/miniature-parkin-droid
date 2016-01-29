package com.meruvian.pxc.selfservice.entity;


import com.meruvian.pxc.selfservice.core.DefaultPersistence;
import com.meruvian.pxc.selfservice.core.commons.Site;

/**
 * Created by miftakhul on 1/21/16.
 */
public class SitePoint extends DefaultPersistence {
    private Site site = new Site();
    private Site siteFrom = new Site();
    private double point;

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public Site getSiteFrom() {
        return siteFrom;
    }

    public void setSiteFrom(Site siteFrom) {
        this.siteFrom = siteFrom;
    }

    public double getPoint() {
        return point;
    }

    public void setPoint(double point) {
        this.point = point;
    }
}
