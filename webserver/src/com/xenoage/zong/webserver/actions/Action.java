package com.xenoage.zong.webserver.actions;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;

import com.xenoage.zong.webserver.Webserver;
import com.xenoage.zong.webserver.model.requests.Request;

/**
 * Interface for all actions on the server side, like for opening
 * a file, or requesting pages or audio files.
 * 
 * @author Andreas Wenger
 */
public abstract class Action {

	@SuppressWarnings("unchecked") public <T extends Request> T getAs(Class<? extends Request> cls,
		Request request) {
		if (!cls.isInstance(request))
			throw new IllegalArgumentException("Must be a " + cls.getSimpleName());
		return (T) request;
	}

	/**
	 * Performs this action for the given request.
	 */
	public abstract void perform(Request request, Webserver server, HttpServletResponse response)
		throws SQLException, IOException;

}
