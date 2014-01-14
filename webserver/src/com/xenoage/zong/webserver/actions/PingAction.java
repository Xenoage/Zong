package com.xenoage.zong.webserver.actions;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;

import com.xenoage.zong.webserver.Server;
import com.xenoage.zong.webserver.model.requests.Request;


/**
 * Sends "pong" to show that the server is alive.
 * 
 * @author Andreas Wenger
 */
public class PingAction
	extends Action
{


	@Override public void perform(Request request, Server server,
		HttpServletResponse response)
		throws SQLException, IOException
	{
		response.setContentType("text/plain");
		response.getWriter().write("pong");
	}
	
}