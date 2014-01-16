package com.xenoage.zong.webserver.actions;

import static com.xenoage.utils.jse.thread.ThreadUtils.sleepS;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;

import com.xenoage.zong.webserver.Webserver;
import com.xenoage.zong.webserver.model.requests.Request;
import com.xenoage.zong.webserver.util.Response;

/**
 * Abstract {@link Action} that is repeated several times, if
 * its {@link #performTry(Request, Webserver, HttpServletResponse)} method fails.
 * It throws an {@link IOException} timout exception if all attempts fail.
 * 
 * @author Andreas Wenger
 */
public abstract class RetryAction
	extends Action {

	private static final int waitTimes[] = { 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 5, 5, 5, 5, 5 };


	@Override public void perform(Request request, Webserver server, HttpServletResponse response) {
		try {
			boolean success = false;
			for (int retryCount = 0; !success && retryCount <= waitTimes.length; retryCount++) {
				if (retryCount > 0)
					sleepS(waitTimes[retryCount - 1]);
				success = performTry(request, server, response);
			}
			if (!success)
				throw new IOException("timeout");
		} catch (Exception ex) {
			Response.writeError(response, ex.getMessage());
		}
	}

	/**
	 * Performs this action for the given request. If it returns false,
	 * it was not successfull and requests to be called again.
	 * If it returns true, the action is finished.
	 */
	public abstract boolean performTry(Request request, Webserver server, HttpServletResponse response)
		throws SQLException, IOException;

}
