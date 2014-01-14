package com.xenoage.zong.webserver.actions;

import static com.xenoage.zong.webserver.util.Database.stmt;
import static com.xenoage.zong.webserver.util.Database.unixTime;

import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;

import com.xenoage.zong.webserver.Server;
import com.xenoage.zong.webserver.model.requests.AudioRequest;
import com.xenoage.zong.webserver.model.requests.Request;


/**
 * Responds to an {@link AudioRequest}.
 * 
 * @author Andreas Wenger
 */
public class AudioAction
	extends RetryAction
{


	@Override public boolean performTry(Request request, Server server,
		HttpServletResponse response)
		throws SQLException, IOException
	{
		AudioRequest audioRequest = getAs(AudioRequest.class, request);
		Connection db = server.getDBConnection();

		//get ID of document
		PreparedStatement stmtID = stmt(db, "SELECT id FROM docs WHERE public_id = ?", audioRequest.id);
		ResultSet resID = stmtID.executeQuery();
		if (!resID.next()) {
			stmtID.close();
			return false;
		}
		int docID = resID.getInt(1);
		stmtID.close();

		//deliver audio data
		PreparedStatement stmtAudio = stmt(db, "SELECT audio FROM audio WHERE doc_id = ?"
			+ " AND format = ?", docID, audioRequest.format);
		ResultSet resAudio = stmtAudio.executeQuery();
		if (!resAudio.next()) {
			stmtAudio.close();
			return false;
		}
		Blob blob = resAudio.getBlob(1);
		byte[] imageData = blob.getBytes(1, (int) blob.length());
		stmtAudio.close();
		//header
		response.setHeader("Content-Type",
			(audioRequest.format.equals("OGG") ? "application/ogg" : "audio/mpeg"));
		response.setHeader("Content-Disposition", "attachment; filename=\"" + audioRequest.id + 
			(audioRequest.format.equals("OGG") ? ".ogg" : ".mp3") + "\"");
		//data
		response.getOutputStream().write(imageData);

		//update access time
		PreparedStatement stmtTime = stmt(db, "UPDATE docs SET last_access = ? WHERE id = ?",
			unixTime(), docID);
		stmtTime.executeUpdate();
		stmtTime.close();
		return true;
	}

}
