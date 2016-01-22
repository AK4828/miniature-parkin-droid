package com.meruvian.pxc.selfservice.entity;


import com.meruvian.pxc.selfservice.core.DefaultPersistence;

public class Cart extends DefaultPersistence {
	private String receiptNumber;
	private String name;
	private String handphone;
	private String phoneNumber;
	private String address;
	private String email;
	private boolean sameAddress;
	private String nameReceiver;
	private String handphoneReceiver;
	private String phoneNumberReceiver;
	private String addressReceiver;
	private String emailReceiver;
    private String orderType;
    private String siteId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHandphone() {
		return handphone;
	}

	public void setHandphone(String handphone) {
		this.handphone = handphone;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isSameAddress() {
		return sameAddress;
	}

	public void setSameAddress(boolean sameAddress) {
		this.sameAddress = sameAddress;
	}

	public String getNameReceiver() {
		return nameReceiver;
	}

	public void setNameReceiver(String nameReceiver) {
		this.nameReceiver = nameReceiver;
	}

	public String getHandphoneReceiver() {
		return handphoneReceiver;
	}

	public void setHandphoneReceiver(String handphoneReceiver) {
		this.handphoneReceiver = handphoneReceiver;
	}

	public String getPhoneNumberReceiver() {
		return phoneNumberReceiver;
	}

	public void setPhoneNumberReceiver(String phoneNumberReceiver) {
		this.phoneNumberReceiver = phoneNumberReceiver;
	}

	public String getAddressReceiver() {
		return addressReceiver;
	}

	public void setAddressReceiver(String addressReceiver) {
		this.addressReceiver = addressReceiver;
	}

	public String getEmailReceiver() {
		return emailReceiver;
	}

	public void setEmailReceiver(String emailReceiver) {
		this.emailReceiver = emailReceiver;
	}

	public String getReceiptNumber() {
		return receiptNumber;
	}

	public void setReceiptNumber(String receiptNumber) {
		this.receiptNumber = receiptNumber;
	}

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

}
