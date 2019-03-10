/**
 * 
 */
package com.ccomp.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * handles request to index.jsp (the main page)
 */
@SuppressWarnings("serial")
@WebServlet("/")
public final class indexServlet extends HttpServlet {

    @Override
    public void init() {
        // TODO read Cloud SQL database
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	// TODO modify the request to set additional data, which can be used in JSP
    	req.setAttribute("indexServlet.testData", "This text comes from the servlet.");
    	req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }
	
}
