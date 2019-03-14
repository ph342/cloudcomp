package com.ccomp.entities;

import java.io.Serializable;

@SuppressWarnings("serial")
public class OrderItem implements Serializable {

	private int orderItemCounter;
	private int itemNr;
	
	public OrderItem() {}

	public OrderItem(int orderItemCounter, int itemNr) {
		this.orderItemCounter = orderItemCounter;
		this.itemNr = itemNr;
	}

	public int getOrderItemCounter() {
		return orderItemCounter;
	}

	public void setOrderItemCounter(int orderItemCounter) {
		this.orderItemCounter = orderItemCounter;
	}

	public int getItemNr() {
		return itemNr;
	}

	public void setItemNr(int itemNr) {
		this.itemNr = itemNr;
	}
}
