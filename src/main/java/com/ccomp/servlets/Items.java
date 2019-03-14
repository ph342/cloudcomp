package com.ccomp.servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.ccomp.dao.ItemDAO;
import com.ccomp.entities.Item;
import com.ccomp.utilities.GCloudSQL;
import com.ccomp.utilities.ServletUtilities;

@WebServlet("/Items/*")
public final class Items extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	// Get single or all Items
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String pathInfo = req.getPathInfo();

			if (pathInfo == null || pathInfo.equals("/")) {
				// get data from model
				List<Item> allItems = ItemDAO
						.findAllItems((DataSource) req.getServletContext().getAttribute(GCloudSQL.conn));

				ServletUtilities.sendAsJson(resp, allItems);
				return;
			}

			String[] splits = pathInfo.split("/");

			if (splits.length != 2) {
				resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}

			String itemNr = splits[1]; // Item ID

			Item item = ItemDAO.findById(Integer.valueOf(itemNr),
					(DataSource) req.getServletContext().getAttribute(GCloudSQL.conn));

			if (item == null) {
				resp.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}

			ServletUtilities.sendAsJson(resp, item);
		} catch (SQLException e) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
}