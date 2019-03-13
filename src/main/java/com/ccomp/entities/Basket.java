package com.ccomp.entities;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("serial")
public class Basket implements Serializable {

	private String firebaseUid;
	private List<BasketItem> items;

	public Basket() {
	}

	public Basket(String firebaseUid, List<BasketItem> items) {
		this.firebaseUid = firebaseUid;
		this.items = items;
	}

	public void organiseBasketItemCounters() {
		int basketItemCounter = 0;

		for (BasketItem i : items) {
			i.setBasketItemCounter(++basketItemCounter);
		}
	}

	public BasketItem addBasketItem(int itemNr) {
		int basketItemCounter = 0;

		// find biggest counter
		for (BasketItem i : items) {
			if (i.getBasketItemCounter() > basketItemCounter)
				basketItemCounter = i.getBasketItemCounter();
		}
		BasketItem ret = new BasketItem(++basketItemCounter, itemNr);
		this.items.add(ret);

		return ret;
	}

	public void removeBasketItem(int basketItemCounter) {
		Iterator<BasketItem> it = this.items.iterator();
		BasketItem item;
		while (it.hasNext()) {
			item = it.next();
			if (item.getBasketItemCounter() == basketItemCounter) {
				it.remove();
				return;
			}
		}
		// rearrange the item numbers
		this.organiseBasketItemCounters();
	}

	public String getFirebase_uid() {
		return firebaseUid;
	}

	public void setFirebase_uid(String firebaseUid) {
		this.firebaseUid = firebaseUid;
	}

	public List<BasketItem> getItems() {
		return items;
	}

	public void setItems(List<BasketItem> items) {
		this.items = items;
	}
}
