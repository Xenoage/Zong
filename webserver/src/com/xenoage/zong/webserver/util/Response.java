package com.xenoage.zong.webserver.util;

import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.warning;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

/**
 * This class writes the responses of the servlet.
 * 
 * @author Andreas Wenger
 */
public class Response {

	public static final String mimetypeJson = "application/json";


	/**
	 * Sends the given response JSON object with an added parameter
	 * <code>"status":"ok"</code>.
	 */
	public static void writeSuccess(HttpServletResponse response, JsonObject json) {
		json.addProperty("status", "ok");
		writeJson(response, json);
	}

	/**
	 * Reports an error.
	 * This is a JSON object with the parameter <code>"status":"error"</code>
	 * and <code>"message"</code> using the given error message.
	 */
	public static void writeError(HttpServletResponse response, String message) {
		JsonObject json = new JsonObject();
		json.addProperty("status", "error");
		json.addProperty("message", message);
		writeJson(response, json);
	}

	private static void writeJson(HttpServletResponse response, JsonObject json) {
		response.setContentType(mimetypeJson);
		try {
			response.getWriter().write(json.toString());
		} catch (IOException ex) {
			INSTANCE.log(Companion.warning("Could not send response", ex));
		}
	}

}
