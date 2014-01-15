package com.xenoage.zong.webserver.model.requests;

import static com.xenoage.utils.CheckUtils.checkNotNull;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.zong.webserver.Server;
import com.xenoage.zong.webserver.actions.CursorAction;

/**
 * Request to retrieve cursor positions.
 * 
 * Example:
 * <pre>{"action":"cursor", "id":"985d576a-e3b1-437e-9dce-569a423c43fd"}</pre>
 * 
 * @author Andreas Wenger
 */
public class CursorRequest
	extends Request {

	@NonNull public final String id;


	public CursorRequest(String id) {
		this.id = id;
	}

	@Override public void check() {
		checkNotNull(id);
	}

	@Override public void respond(Server server, HttpServletResponse response)
		throws SQLException, IOException {
		new CursorAction().perform(this, server, response);
	}

}
