package com.xenoage.zong.webserver.model.requests;

import static com.xenoage.utils.CheckUtils.checkNotNull;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.zong.webserver.Server;
import com.xenoage.zong.webserver.actions.PageAction;
import com.xenoage.zong.webserver.model.Scaling;

/**
 * Request to retrieve a rendered page.
 * 
 * Example:
 * <pre>{"action":"page", "id":"985d576a-e3b1-437e-9dce-569a423c43fd",
 *   "scaling":{"dpi":72}, "page":0}</pre>
 * 
 * @author Andreas Wenger
 */
public class PageRequest
	extends Request {

	@NonNull public final String id;
	@NonNull public final Scaling scaling;
	public final int page;


	public PageRequest(String id, Scaling scaling, int page) {
		this.id = id;
		this.scaling = scaling;
		this.page = page;
	}

	@Override public void check() {
		checkNotNull(id);
		checkNotNull(scaling);
	}

	@Override public void respond(Server server, HttpServletResponse response)
		throws SQLException, IOException {
		new PageAction().perform(this, server, response);
	}

}
