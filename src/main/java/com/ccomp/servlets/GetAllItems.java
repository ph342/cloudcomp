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
import com.google.gson.Gson;

@SuppressWarnings("serial")
@WebServlet("/getAllItems")
public final class GetAllItems extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			// get data from model
			List<Item> allItems = ItemDAO.findAllItems((DataSource) req.getServletContext().getAttribute(GCloudSQL.conn));
			
			// set data in the response
	        resp.setContentType("application/json");
	        resp.setCharacterEncoding("UTF-8");

			resp.getWriter().write(new Gson().toJson(allItems));
		} catch (SQLException e) {
			resp.setStatus(500);
			resp.setContentType("text/plain");
			resp.setCharacterEncoding("UTF-8");
			resp.getWriter().print(e.getMessage());
			return;
		}
	}
}
