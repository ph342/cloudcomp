package com.ccomp.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import javax.sql.DataSource;

public final class Item {

	private int item_nr;
	private String name; // invariant: <=64 chars
	private String descr; // <=255 chars
	private double price; // 15,2
	private String curr; // <=3 chars
	private int image_key;

	public static Item factory(int item_nr, DataSource ds) throws SQLException {
		try (Connection conn = ds.getConnection()) {
			PreparedStatement selectClause = conn
					.prepareStatement("select * from item where item_nr = " + item_nr + ";");
			ResultSet result = selectClause.executeQuery();
			if (result.first())
				return new Item(result.getInt("item_nr"), result.getString("name"), result.getString("description"),
						result.getDouble("price"), result.getString("currency"), result.getInt("image_key"));
		}
		throw new SQLException("No item found.");
	}

	public static List<Item> loadAllItems(DataSource ds) throws SQLException {

		List<Item> ret = new Vector<Item>();

		try (Connection conn = ds.getConnection()) {
			PreparedStatement selectClause = conn.prepareStatement("select * from item;");
			ResultSet result = selectClause.executeQuery();
			while (result.next()) {
				ret.add(new Item(result.getInt("item_nr"), result.getString("name"), result.getString("description"),
						result.getDouble("price"), result.getString("currency"), result.getInt("image_key")));
			}
		}
		return ret;
	}

	private Item(int item_nr, String name, String descr, double price, String curr, int image_key) {
		this.item_nr = item_nr;
		this.name = name;
		this.descr = descr;
		this.price = price;
		this.curr = curr;
		this.image_key = image_key;
	}

	public int getItem_nr() {
		return item_nr;
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

	public int getImage_key() {
		return image_key;
	}
}
