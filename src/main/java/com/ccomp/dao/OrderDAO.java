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
			PreparedStatement selectOrder = conn.prepareStatement("select * from \"Order\" where order_nr = ?;");
			selectOrder.setInt(1, orderNr);

			PreparedStatement selectOrderItems = conn.prepareStatement("select * from orderitem where order_nr = ?;");
			selectOrderItems.setInt(1, orderNr);

			ResultSet resultOrder = selectOrder.executeQuery();
			ResultSet resultOrderItems = selectOrderItems.executeQuery();

			if (resultOrder.next()) {
				List<OrderItem> items = new Vector<OrderItem>();

				while (resultOrderItems.next())
					items.add(
							new OrderItem(resultOrderItems.getInt("item_counter"), resultOrderItems.getInt("item_nr")));

				return new Order(resultOrder.getInt("order_nr"),
						resultOrder.getObject("timestamp", LocalDateTime.class), resultOrder.getString("status"),
						resultOrder.getString("firebase_uid"), items, false);

			} else
				throw new SQLException("No order found.");
		}
	}

	public static List<Order> findByCustomer(String firebaseUid, DataSource ds) throws SQLException {
		try (Connection conn = ds.getConnection()) {
			PreparedStatement selectOrder = conn
					.prepareStatement("select * from \"Order\" o join orderitem oi on o.order_nr = oi.order_nr "
							+ "where o.firebase_uid = ? order by o.order_nr;");
			selectOrder.setString(1, firebaseUid);

			ResultSet resultOrder = selectOrder.executeQuery();
			List<Order> ret = new Vector<Order>();
			Order order;

			if (!resultOrder.next())
				throw new SQLException("No orders found.");

			order = new Order(resultOrder.getInt("order_nr"), resultOrder.getObject("timestamp", LocalDateTime.class),
					resultOrder.getString("status"), resultOrder.getString("firebase_uid"), false);

			ret.add(order);

			List<OrderItem> items = new Vector<OrderItem>();
			order.setItems(items);

			items.add(new OrderItem(resultOrder.getInt("item_counter"), resultOrder.getInt("item_nr")));

			while (resultOrder.next()) {
				if (order.getOrderNr() != resultOrder.getInt("order_nr")) {
					order = new Order(resultOrder.getInt("order_nr"),
							resultOrder.getObject("timestamp", LocalDateTime.class), resultOrder.getString("status"),
							resultOrder.getString("firebase_uid"), false);
					ret.add(order);
					items = new Vector<OrderItem>();
					order.setItems(items);
					items.add(new OrderItem(resultOrder.getInt("item_counter"), resultOrder.getInt("item_nr")));
				}

				items.add(new OrderItem(resultOrder.getInt("item_counter"), resultOrder.getInt("item_nr")));
			}
			return ret;
		}
	}

	public static void insertOrder(Order order, DataSource ds) throws SQLException {
		// this method is only accessible for new orders
		if (!order.isFlNew())
			return;

		try (Connection conn = ds.getConnection()) {
			try {
				// disable Commit
				conn.setAutoCommit(false);

				PreparedStatement insertOrder = conn
						.prepareStatement("insert into \"Order\" values (default, ?, ?, ?);");
				insertOrder.setObject(1, order.getTimestamp());
				insertOrder.setString(2, order.getStatus());
				insertOrder.setString(3, order.getFirebaseUid());

				List<PreparedStatement> allItems = new ArrayList<PreparedStatement>();

				for (OrderItem i : order.getItems()) {
					PreparedStatement insertOrderItem = conn
							.prepareStatement("insert into orderitem values (?, ?, ?);");
					insertOrderItem.setInt(1, order.getOrderNr());
					insertOrderItem.setInt(2, i.getOrderItemCounter());
					insertOrderItem.setInt(3, i.getItemNr());

					allItems.add(insertOrderItem);
				}

				// insert Order
				insertOrder.execute();
				// insert Items
				for (PreparedStatement ps : allItems) {
					ps.execute();
				}
				conn.commit();

				// the order was committed => what's the new key?
				order.setFlNew(false);
				PreparedStatement selectOrderNr = conn
						.prepareStatement("select order_nr from \"Order\" where timestamp = ?;");
				selectOrderNr.setObject(1, order.getTimestamp());
				order.setOrderNr(selectOrderNr.executeQuery().getInt(1));

			} catch (SQLException e) {
				conn.rollback();
				throw e;
			}
		}
	}

	public static void updateOrder(Order order, DataSource ds) throws SQLException {
		// this method is only accessible for old orders
		if (order.isFlNew())
			return;

		try (Connection conn = ds.getConnection()) {
			try {
				// disable Commit
				conn.setAutoCommit(false);

				PreparedStatement updateOrder = conn.prepareStatement(
						"update \"Order\" set timestamp = ?, status = ?, firebase_uid = ? where order_nr = ?;");
				updateOrder.setObject(1, order.getTimestamp());
				updateOrder.setString(2, order.getStatus());
				updateOrder.setString(3, order.getFirebaseUid());
				updateOrder.setInt(4, order.getOrderNr());

				PreparedStatement deleteOrderItems = conn.prepareStatement("delete from orderitem where order_nr = ?;");
				deleteOrderItems.setInt(1, order.getOrderNr());

				List<PreparedStatement> allItems = new ArrayList<PreparedStatement>();

				for (OrderItem i : order.getItems()) {
					PreparedStatement insertOrderItem = conn
							.prepareStatement("insert into orderitem values (?, ?, ?);");
					insertOrderItem.setInt(1, order.getOrderNr());
					insertOrderItem.setInt(2, i.getOrderItemCounter());
					insertOrderItem.setInt(3, i.getItemNr());

					allItems.add(insertOrderItem);
				}

				// update Order header
				updateOrder.execute();
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

				PreparedStatement deleteOrder = conn.prepareStatement("delete from \"Order\" where order_nr = ?;");
				deleteOrder.setInt(1, order.getOrderNr());
				PreparedStatement deleteOrderItems = conn.prepareStatement("delete from orderitem where order_nr = ?;");
				deleteOrderItems.setInt(1, order.getOrderNr());

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
