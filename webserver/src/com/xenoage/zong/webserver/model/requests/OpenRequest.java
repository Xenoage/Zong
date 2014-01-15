package com.xenoage.zong.webserver.model.requests;

import static com.xenoage.utils.CheckUtils.checkNotEmpty;
import static com.xenoage.utils.CheckUtils.checkNotNull;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonEmpty;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.zong.webserver.Server;
import com.xenoage.zong.webserver.actions.OpenAction;
import com.xenoage.zong.webserver.model.Scaling;

/**
 * Request to open a document.
 * 
 * Example:
 * <pre>{"action":"open", "url":"http://www.zong-music.com/demo/BeetAnGeSample.xml",
 *   "requestedID":"4110334f-785d-4a3d-9414-efb96387df88",
 *   "scalings":[
 *     {"dpi":72},
 *     {"dpi":144},
 *     {"widthpx":700}]}</pre>
 *     
 * The requested ID is optional and should only be used for demonstration purposes.
 * If none is given, a random ID is generated. If one is given for an new URL
 * but it already exists, an error is thrown.
 * 
 * @author Andreas Wenger
 */
public class OpenRequest
	extends Request {

	@NonNull public final String url;
	@MaybeNull public final UUID requestedID;
	@NonEmpty public final List<Scaling> scalings;


	public OpenRequest(String url, UUID requestedID, List<Scaling> scalings) {
		this.url = url;
		this.requestedID = requestedID;
		this.scalings = scalings;
	}

	@Override public void check() {
		checkNotNull(url);
		checkNotEmpty(scalings);
	}

	@Override public void respond(Server server, HttpServletResponse response)
		throws SQLException, IOException {
		new OpenAction().perform(this, server, response);
	}

}
