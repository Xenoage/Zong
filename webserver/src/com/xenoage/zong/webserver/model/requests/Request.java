package com.xenoage.zong.webserver.model.requests;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xenoage.zong.webserver.Server;

/**
 * A request to the server.
 * 
 * @author Andreas Wenger
 */
public abstract class Request {

	private static HashMap<String, Class<? extends Request>> classes = new HashMap<String, Class<? extends Request>>();

	static {
		classes.put("audio", AudioRequest.class);
		classes.put("cursor", CursorRequest.class);
		classes.put("open", OpenRequest.class);
		classes.put("page", PageRequest.class);
		classes.put("ping", PingRequest.class);
	}


	public static Request read(String data)
		throws ServletException {
		try {
			JsonObject o = (JsonObject) (new JsonParser().parse(data));
			String action = o.getAsJsonPrimitive("action").getAsString();
			//find Java class
			Class<? extends Request> cls = classes.get(action);
			//convert JSON object to Java class
			if (cls == null)
				throw new ServletException("unknown action: " + action);
			Request request = Server.instance.getGson().fromJson(o, cls);
			//check integrity
			request.check();
			return request;
		} catch (Exception ex) {
			throw new ServletException("incorrect request");
		}
	}

	public void check()
		throws IllegalStateException {
	}

	public abstract void respond(Server server, HttpServletResponse response)
		throws SQLException, IOException;

}
