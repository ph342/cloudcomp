package com.ccomp.entities;

import java.io.Serializable;

@SuppressWarnings("serial")
public class BasketItem implements Serializable {

	private int basketItemCounter;
	private int itemNr;

	public BasketItem() {
	}

	public BasketItem(int basketCounter, int itemNr) {
		this.basketItemCounter = basketCounter;
		this.itemNr = itemNr;
	}

	public int getBasketItemCounter() {
		return basketItemCounter;
	}

	public void setBasketItemCounter(int basketItemCounter) {
		this.basketItemCounter = basketItemCounter;
	}

	public int getItemNr() {
		return itemNr;
	}

	public void setItem_nr(int itemNr) {
		this.itemNr = itemNr;
	}
}