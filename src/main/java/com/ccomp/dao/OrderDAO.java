package com.ccomp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.sql.DataSource;

import com.ccomp.entities.Order;
import com.ccomp.entities.OrderItem;

public final class OrderDAO {

	public static Order findById(int orderNr, DataSource ds) throws SQLException {
		try (Connection conn = ds.getConnection()) {
			PreparedStatement selectOrder = conn
					.prepareStatement("select * from order where order_nr = " + orderNr + ";");
			PreparedStatement selectOrderItems = conn
					.prepareStatement("select * from orderitem where order_nr = " + orderNr + ";");

			ResultSet resultOrder = selectOrder.executeQuery();
			ResultSet resultOrderItems = selectOrderItems.executeQuery();

			if (resultOrder.first()) {
				List<OrderItem> items = new Vector<OrderItem>();

				while (resultOrderItems.next())
					items.add(
							new OrderItem(resultOrderItems.getInt("item_counter"), resultOrderItems.getInt("item_nr")));

				return new Order(resultOrder.getInt("order_nr"),
						resultOrder.getObject("timestamp", LocalDateTime.class), resultOrder.getString("status"),
						resultOrder.getString("firebase_uid"), items);

			} else
				throw new SQLException("No item found.");
		}
	}

	public static void modifyOrder(Order order, DataSource ds) throws SQLException {
		try (Connection conn = ds.getConnection()) {
			try {
				// disable Commit
				conn.setAutoCommit(false);

				PreparedStatement deleteOrder = conn
						.prepareStatement("delete from order where order_nr = " + order.getOrderNr() + ";");
				PreparedStatement insertOrder = conn.prepareStatement("insert into order values (" + order.getOrderNr()
						+ ", ?, " + order.getStatus() + ", " + order.getFirebaseUid() + ");");
				insertOrder.setObject(1, order.getTimestamp());

				PreparedStatement deleteOrderItems = conn
						.prepareStatement("delete from orderitem where order_nr = " + order.getOrderNr() + ";");

				List<PreparedStatement> allItems = new ArrayList<PreparedStatement>();

				for (OrderItem i : order.getItems()) {
					allItems.add(conn.prepareStatement("insert into orderitem values (" + order.getOrderNr() + ", "
							+ i.getOrderItemCounter() + ", " + i.getItemNr() + ";"));
				}

				// delete Order
				deleteOrder.execute();
				// insert Order
				insertOrder.execute();
				// delete Items
				deleteOrderItems.execute();
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

	public static void deleteOrder(Order order, DataSource ds) throws SQLException {
		try (Connection conn = ds.getConnection()) {
			try {
				conn.setAutoCommit(false);

				PreparedStatement deleteOrder = conn
						.prepareStatement("delete from order where order_nr = " + order.getOrderNr() + ";");
				PreparedStatement deleteOrderItems = conn
						.prepareStatement("delete from orderitem where order_nr = " + order.getOrderNr() + ";");
				deleteOrder.execute();
				deleteOrderItems.execute();

				conn.commit();
			} catch (SQLException e) {
				conn.rollback();
				throw e;
			}
		}
	}
}
