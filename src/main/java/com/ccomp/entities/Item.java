package com.ccomp.entities;

import java.io.Serializable;

@SuppressWarnings("serial")
public final class Item implements Serializable {

	private int itemNr;
	private String name; // invariant: <=64 chars
	private String descr; // <=255 chars
	private double price; // 15,2
	private String curr; // <=3 chars
	private int imageKey;
	private String imageUri;

	public Item() {
	}

	public Item(int itemNr, String name, String descr, double price, String curr, int imageKey) {
		this.itemNr = itemNr;
		this.name = name;
		this.descr = descr;
		this.price = price;
		this.curr = curr;
		this.imageKey = imageKey;

		// http://storage.googleapis.com/[BUCKET_NAME]/[OBJECT_NAME]
		this.imageUri = "http://storage.googleapis.com/cloudcwkstorage/images/items/" + String.valueOf(this.imageKey)
				+ ".jpg";
	}

	public int getItemNr() {
		return itemNr;
	}

	public String getName() {
		return name;
	}

	public String getDescr() {
		return descr;
	}

	public double getPrice() {
		return price;
	}

	public String getCurr() {
		return curr;
	}

	public int getImageKey() {
		return imageKey;
	}

	public String getImageUri() {
		return imageUri;
	}
}
