package com.ccomp.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("serial")
public class Order implements Serializable {

	private int orderNr;
	private LocalDateTime timestamp;
	private String status;
	private String firebaseUid;
	private List<OrderItem> items;

	public Order() {
	}

	public Order(int orderNr, LocalDateTime timestamp, String status, String firebaseUid, List<OrderItem> items) {
		this.orderNr = orderNr;
		this.timestamp = timestamp;
		this.status = status;
		this.firebaseUid = firebaseUid;
		this.items = items;
	}

	public void organiseOrderItemCounters() {
		int orderItemCounter = 0;

		for (OrderItem i : items) {
			i.setOrderItemCounter(++orderItemCounter);
		}
	}

	public OrderItem addOrderItem(int itemNr) {
		int orderItemCounter = 0;

		// find biggest counter
		for (OrderItem i : items) {
			if (i.getOrderItemCounter() > orderItemCounter)
				orderItemCounter = i.getOrderItemCounter();
		}
		OrderItem ret = new OrderItem(++orderItemCounter, itemNr);
		this.items.add(ret);

		return ret;
	}

	public void removeOrderItem(int orderItemCounter) {
		Iterator<OrderItem> it = this.items.iterator();
		OrderItem item;
		while (it.hasNext()) {
			item = it.next();
			if (item.getOrderItemCounter() == orderItemCounter) {
				it.remove();
				return;
			}
		}
		// rearrange the item numbers
		this.organiseOrderItemCounters();
	}

	public int getOrderNr() {
		return orderNr;
	}

	public void setOrderNr(int orderNr) {
		this.orderNr = orderNr;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFirebaseUid() {
		return firebaseUid;
	}

	public void setFirebaseUid(String firebaseUid) {
		this.firebaseUid = firebaseUid;
	}

	public List<OrderItem> getItems() {
		return items;
	}

	public void setItems(List<OrderItem> items) {
		this.items = items;
	}
}
