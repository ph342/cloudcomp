package com.ccomp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import javax.sql.DataSource;

import com.ccomp.entities.Item;

public final class ItemDAO {

	public static Item findById(int itemNr, DataSource ds) throws SQLException {
		try (Connection conn = ds.getConnection()) {
			PreparedStatement selectClause = conn.prepareStatement("select * from item where item_nr = ?;");
			selectClause.setInt(1, itemNr);

			ResultSet result = selectClause.executeQuery();
			if (result.next()) {
				return new Item(result.getInt("item_nr"), result.getString("name"), result.getString("description"),
						result.getDouble("price"), result.getString("currency"), result.getInt("image_key"));
			} else
				throw new SQLException("No item found.");
		}
	}

	public static List<Item> findAllItems(DataSource ds) throws SQLException {
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
}
