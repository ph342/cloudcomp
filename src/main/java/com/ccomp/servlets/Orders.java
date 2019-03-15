package com.ccomp.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.ccomp.dao.OrderDAO;
import com.ccomp.entities.Order;
import com.ccomp.utilities.GCloudSQL;
import com.ccomp.utilities.ServletUtilities;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

@WebServlet("/Orders/*")
public class Orders extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// Get single Order OR all orders
	// METHOD 1:
	// in: orderNr in URI, /Orders/<orderNr>
	// out: Order entities
	// METHOD 2:
	// in: firebaseUid in URI, /Orders/firebaseUid/<firebaseUid>
	// out: Order entities
	// METHOD 3:
	// in: nothing
	// out: Order entities
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String pathInfo = req.getPathInfo();

			if (pathInfo == null || pathInfo.equals("/")) {
				// get all orders from model
				List<Order> allOrders = OrderDAO
						.findAllOrders((DataSource) req.getServletContext().getAttribute(GCloudSQL.conn));

				ServletUtilities.sendAsJson(resp, allOrders);
				return;
			}

			String[] splits = pathInfo.split("/");

			if (splits.length != 2 && (splits.length != 3 || !splits[1].equals("firebaseUid"))) {
				resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}

			if (splits.length == 2) {
				// Method 1, get orders by Order ID

				int orderNr = Integer.valueOf(splits[1]); // Order ID

				Order order = OrderDAO.findById(orderNr,
						(DataSource) req.getServletContext().getAttribute(GCloudSQL.conn));

				ServletUtilities.sendAsJson(resp, order);
				return;

			} else {
				// Method 2, get orders by FirebaseUid
				String firebaseUid = splits[2]; // FirebaseUID

				List<Order> orders = OrderDAO.findByCustomer(firebaseUid,
						(DataSource) req.getServletContext().getAttribute(GCloudSQL.conn));
				ServletUtilities.sendAsJson(resp, orders);
				return;
			}

		} catch (Exception e) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	// Add new Order to DB
	// in: Order entity in payload (not necessary: orderNr, timestamp, flNew)
	// out: Order entity
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String pathInfo = req.getPathInfo();

			if (pathInfo != null && !pathInfo.equals("/")) {
				resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}

			StringBuilder buffer = new StringBuilder();
			BufferedReader reader = req.getReader();
			String line;
			while ((line = reader.readLine()) != null)
				buffer.append(line);

			Order order = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
			    @Override
			    public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
			        Instant instant = Instant.ofEpochMilli(json.getAsJsonPrimitive().getAsLong());
			        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
			    }
			}).create().fromJson(buffer.toString(), Order.class);
			
			order.organiseOrderItemCounters();
			order.setFlNew(true);
			order.setTimestamp(LocalDateTime.now());

			OrderDAO.insertOrder(order, (DataSource) req.getServletContext().getAttribute(GCloudSQL.conn));

			ServletUtilities.sendAsJson(resp, order);
		} catch (SQLException e) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	// Update existing Order in DB
	// in: Order entity in payload (not necessary: orderNr, timestamp, flNew)
	// out: Order entity
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String pathInfo = req.getPathInfo();

			if (pathInfo != null && !pathInfo.equals("/")) {
				resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}

			StringBuilder buffer = new StringBuilder();
			BufferedReader reader = req.getReader();
			String line;
			while ((line = reader.readLine()) != null)
				buffer.append(line);

			Order order = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
			    @Override
			    public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
			        Instant instant = Instant.ofEpochMilli(json.getAsJsonPrimitive().getAsLong());
			        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
			    }
			}).create().fromJson(buffer.toString(), Order.class);
			
			order.organiseOrderItemCounters();
			order.setFlNew(false);
			order.setTimestamp(LocalDateTime.now());

			OrderDAO.updateOrder(order, (DataSource) req.getServletContext().getAttribute(GCloudSQL.conn));

			ServletUtilities.sendAsJson(resp, order);
		} catch (SQLException e) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	// Delete existing Order
	// in: orderId in URI
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String pathInfo = req.getPathInfo();

			if (pathInfo == null || pathInfo.equals("/")) {
				// only single read
				resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}

			String[] splits = pathInfo.split("/");

			if (splits.length != 2) {
				resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}

			int orderNr = Integer.valueOf(splits[1]); // Order ID

			OrderDAO.deleteOrder(orderNr, (DataSource) req.getServletContext().getAttribute(GCloudSQL.conn));
		} catch (SQLException e) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
}
