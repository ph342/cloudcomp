/**
 * 
 */
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

import com.ccomp.model.Item;

/**
 * handles request to index.jsp (the main page)
 */
@SuppressWarnings("serial")
@WebServlet("/")
public final class indexServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// modify the request to set additional data, which can be used in JSP

		try {
			// get data from model
			List<Item> allItems = Item.loadAllItems((DataSource) req.getServletContext().getAttribute(GCloudSQL.conn));
			
			// set data in the request
			req.setAttribute("indexServlet.allItems", allItems);
			
		} catch (SQLException e) {
			resp.setStatus(500);
			resp.setContentType("text/plain");
			resp.setCharacterEncoding("UTF-8");
			resp.getWriter().print(e.getMessage());
			return;
		}

		// forward to JSP
		req.getRequestDispatcher("/index.jsp").forward(req, resp);
	}
}
