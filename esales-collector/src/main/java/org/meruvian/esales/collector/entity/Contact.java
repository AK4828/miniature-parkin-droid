package org.meruvian.esales.collector.entity;

import org.meruvian.esales.collector.core.DefaultPersistence;
import org.meruvian.esales.collector.core.commons.User;

/**
 * Created by meruvian on 03/08/15.
 */
public class Contact extends DefaultPersistence {
    private boolean defaultContact = false;
    private User user = new User();
    private String recipient;
    private String phone;
    private String city;
    private String subDistrict;
    private String province;
    private String zip;
    private String address;
    private String contactName;

    public boolean isDefaultContact() {
        return defaultContact;
    }

    public void setDefaultContact(boolean defaultContact) {
        this.defaultContact = defaultContact;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSubDistrict() {
        return subDistrict;
    }

    public void setSubDistrict(String subDistrict) {
        this.subDistrict = subDistrict;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    @Override
    public String toString() {
        return contactName;
    }
}
