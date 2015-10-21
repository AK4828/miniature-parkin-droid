package com.hoqii.sales.selfservice.entity;

import com.hoqii.sales.selfservice.core.DefaultPersistence;

public class OrderMenu extends DefaultPersistence {
	public enum OrderMenuStatus {
		ORDER, CANCELED
	}

	private Order order = new Order();
	private int qty;
	private double sellPrice;
	private ProductStore product = new ProductStore();
	private String description;
	private String imei;
	private OrderMenuStatus status = OrderMenuStatus.ORDER;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public ProductStore getProduct() {
		return product;
	}

	public void setProduct(ProductStore product) {
		this.product = product;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public OrderMenuStatus getStatus() {
		return status;
	}

	public void setStatus(OrderMenuStatus status) {
		this.status = status;
	}
}
