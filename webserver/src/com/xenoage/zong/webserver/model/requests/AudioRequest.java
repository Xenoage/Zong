package com.xenoage.zong.webserver.model.requests;

import static com.xenoage.utils.base.CheckUtils.checkNotNull;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;

import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.zong.webserver.Server;
import com.xenoage.zong.webserver.actions.AudioAction;


/**
 * Request to retrieve a tile.
 * 
 * Example:
 * <pre>{"action":"audio", "id":"985d576a-e3b1-437e-9dce-569a423c43fd", "format":"OGG"}</pre>
 * 
 * @author Andreas Wenger
 */
public class AudioRequest
	extends Request
{

	@NeverNull public final String id;
	@NeverNull public final String format;


	public AudioRequest(String id, String format)
	{
		this.id = id;
		this.format = format;
	}
	
	
	@Override public void check()
	{
		checkNotNull(id);
		checkNotNull(format);
	}


	@Override public void respond(Server server, HttpServletResponse response)
		throws SQLException, IOException
	{
		new AudioAction().perform(this, server, response);
	}

}
