package com.ccomp.entities;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Customer implements Serializable {

	private String firebaseUid;

	public Customer(String firebaseUid) {
		this.firebaseUid = firebaseUid;
	}

	public Customer() {
	}

	public String getFirebaseUid() {
		return firebaseUid;
	}

	public void setFirebaseUid(String firebaseUid) {
		this.firebaseUid = firebaseUid;
	}
}
