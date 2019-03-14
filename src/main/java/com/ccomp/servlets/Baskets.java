package com.ccomp.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.ccomp.dao.BasketDAO;
import com.ccomp.entities.Basket;
import com.ccomp.utilities.GCloudSQL;
import com.ccomp.utilities.ServletUtilities;
import com.google.gson.Gson;

@WebServlet("/Baskets/*")
public final class Baskets extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// Get single Basket
	// in: firebaseUid in URI
	// out: Basket entity
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

			String firebaseUid = splits[1]; // Firebase

			Basket basket = BasketDAO.findById(firebaseUid,
					(DataSource) req.getServletContext().getAttribute(GCloudSQL.conn));

			if (basket == null) {
				resp.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}

			ServletUtilities.sendAsJson(resp, basket);
		} catch (SQLException e) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	// Add new Basket to DB
	// in: Basket entity in payload
	// out: Basket entity
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

			Basket basket = new Gson().fromJson(buffer.toString(), Basket.class);

			BasketDAO.modifyBasket(basket, (DataSource) req.getServletContext().getAttribute(GCloudSQL.conn));

			ServletUtilities.sendAsJson(resp, basket);
		} catch (SQLException e) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	// Update existing Basket in DB
	// in: Basket entity in payload
	// out: Basket entity
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Implementation is the same in this case
		doPost(req, resp);
	}

	// Delete existing Basket
	// in: firebaseUid in URI
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

			String firebaseUid = splits[1]; // Firebase

			BasketDAO.deleteBasket(firebaseUid, (DataSource) req.getServletContext().getAttribute(GCloudSQL.conn));
		} catch (SQLException e) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
}
