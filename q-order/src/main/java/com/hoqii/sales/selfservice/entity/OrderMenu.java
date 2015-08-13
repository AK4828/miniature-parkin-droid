package com.hoqii.sales.selfservice.entity;


import com.hoqii.sales.selfservice.core.DefaultPersistence;

public class OrderMenu extends DefaultPersistence {
	private Order order = new Order();
	private int qty;
	private double sellPrice;
	private Product product = new Product();
	private String description;

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

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}
