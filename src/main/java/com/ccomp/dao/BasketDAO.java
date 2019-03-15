package com.ccomp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.sql.DataSource;

import com.ccomp.entities.Basket;
import com.ccomp.entities.BasketItem;

public final class BasketDAO {

	public static Basket findById(String firebaseUid, DataSource ds) throws SQLException {
		try (Connection conn = ds.getConnection()) {
			PreparedStatement selectBasket = conn.prepareStatement("select * from basket where firebase_uid = ?;");
			selectBasket.setString(1, firebaseUid);
			PreparedStatement selectBasketItems = conn
					.prepareStatement("select * from basketitem where firebase_uid = ?;");
			selectBasketItems.setString(1, firebaseUid);

			ResultSet resultBasket = selectBasket.executeQuery();
			ResultSet resultBasketItems = selectBasketItems.executeQuery();

			if (resultBasket.next()) {
				List<BasketItem> items = new Vector<BasketItem>();

				while (resultBasketItems.next())
					items.add(new BasketItem(resultBasketItems.getInt("item_counter"),
							resultBasketItems.getInt("item_nr")));

				return new Basket(firebaseUid, items);

			} else
				return null;
		}
	}

	public static void modifyBasket(Basket basket, DataSource ds) throws SQLException {
		// update and insert
		try (Connection conn = ds.getConnection()) {
			try {
				// disable Commit
				conn.setAutoCommit(false);

				PreparedStatement insertBasket = conn.prepareStatement(
						"insert into basket (firebase_uid) values (?) on conflict (firebase_uid) do nothing;");
				insertBasket.setString(1, basket.getFirebase_uid());

				PreparedStatement deleteBasketItems = conn
						.prepareStatement("delete from basketitem where firebase_uid = ?;");
				deleteBasketItems.setString(1, basket.getFirebase_uid());

				List<PreparedStatement> allItems = new ArrayList<PreparedStatement>();

				for (BasketItem i : basket.getItems()) {
					PreparedStatement insertItem = conn.prepareStatement("insert into basketitem values (?, ?, ?);");
					insertItem.setString(1, basket.getFirebase_uid());
					insertItem.setInt(2, i.getBasketItemCounter());
					insertItem.setInt(3, i.getItemNr());

					allItems.add(insertItem);
				}

				// update Basket
				insertBasket.execute();
				// delete Items
				deleteBasketItems.execute();
				// insert Items
				for (PreparedStatement ps : allItems) {
					ps.execute();
				}
				conn.commit();
			} catch (SQLException e) {
				conn.rollback();
				throw e;
			}
		}
	}

	public static void deleteBasket(String firebaseUid, DataSource ds) throws SQLException {
		try (Connection conn = ds.getConnection()) {
			try {
				conn.setAutoCommit(false);

				PreparedStatement deleteBasket = conn.prepareStatement("delete from basket where firebase_uid = ?;");
				deleteBasket.setString(1, firebaseUid);
				PreparedStatement deleteBasketItems = conn
						.prepareStatement("delete from basketitem where firebase_uid = ?;");
				deleteBasketItems.setString(1, firebaseUid);

				deleteBasketItems.execute();
				deleteBasket.execute();

				conn.commit();
			} catch (SQLException e) {
				conn.rollback();
				throw e;
			}
		}
	}
}