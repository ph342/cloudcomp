package com.ccomp.utilities;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class ServletUtilities {

	// a utility method to send objects as JSON response
	public static void sendAsJson(HttpServletResponse resp, Object obj) throws IOException {

		resp.setContentType("application/json");

		PrintWriter out = resp.getWriter();

		out.print(new Gson().toJson(obj));
		out.flush();
	}
}