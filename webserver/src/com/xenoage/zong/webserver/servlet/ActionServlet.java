package com.xenoage.zong.webserver.servlet;

import static com.xenoage.zong.webserver.util.Response.writeError;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xenoage.utils.base.exceptions.ThrowableUtils;
import com.xenoage.zong.webserver.Server;
import com.xenoage.zong.webserver.actions.Action;
import com.xenoage.zong.webserver.model.requests.Request;


/**
 * Servlet that responses to all {@link Request}s by executing
 * the corresponding {@link Action}s.
 * 
 * @author Andreas Wenger
 */
public class ActionServlet
	extends HttpServlet
{


	@Override protected void doGet(HttpServletRequest httpRequest,
		HttpServletResponse httpResponse)
		throws ServletException
	{
		doPost(httpRequest, httpResponse);
	}


	@Override protected void doPost(HttpServletRequest httpRequest,
		HttpServletResponse httpResponse)
		throws ServletException
	{
		try {
			Request request;
			String data = httpRequest.getParameter("data");
			if (data != null) {
				request = Request.read(data);
				request.respond(Server.instance, httpResponse);
			}
			else {
				writeError(httpResponse, "Parameter data missing");
			}
		} catch (RuntimeException ex) {
			writeError(httpResponse, ThrowableUtils.getStackTrace(ex));
			return;
		} catch (Exception ex) {
			writeError(httpResponse, "Internal Server Error: " + ex.getMessage()
				+ "\n\n" + "Stack trace:\n" + ThrowableUtils.getStackTrace(ex));
			return;
		}
	}


}
