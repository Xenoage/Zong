package com.xenoage.zong.webserver.model.requests;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;

import com.xenoage.zong.webserver.Webserver;
import com.xenoage.zong.webserver.actions.PingAction;

/**
 * Request to check if the server is online.
 * 
 * Example:
 * <pre>{"action":"ping"}</pre>
 * 
 * @author Andreas Wenger
 */
public class PingRequest
	extends Request {

	@Override public void respond(Webserver server, HttpServletResponse response)
		throws SQLException, IOException {
		new PingAction().perform(this, server, response);
	}

}
