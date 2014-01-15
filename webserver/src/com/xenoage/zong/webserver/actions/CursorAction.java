package com.xenoage.zong.webserver.actions;

import static com.xenoage.zong.webserver.util.Database.stmt;
import static com.xenoage.zong.webserver.util.Database.unixTime;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;

import com.xenoage.zong.webserver.Server;
import com.xenoage.zong.webserver.model.requests.CursorRequest;
import com.xenoage.zong.webserver.model.requests.Request;
import com.xenoage.zong.webserver.util.Response;

/**
 * Responds to a {@link CursorRequest}.
 * 
 * @author Andreas Wenger
 */
public class CursorAction
	extends RetryAction {

	@Override public boolean performTry(Request request, Server server, HttpServletResponse response)
		throws SQLException, IOException {
		CursorRequest cursorRequest = getAs(CursorRequest.class, request);
		Connection db = server.getDBConnection();

		//get ID of document
		PreparedStatement stmtID = stmt(db, "SELECT id FROM docs WHERE public_id = ?", cursorRequest.id);
		ResultSet resID = stmtID.executeQuery();
		if (!resID.next()) {
			stmtID.close();
			return false;
		}
		int docID = resID.getInt(1);
		stmtID.close();

		//deliver cursor data
		PreparedStatement stmtCursor = stmt(db, "SELECT cursors FROM cursors WHERE doc_id = ?", docID);
		ResultSet resCursor = stmtCursor.executeQuery();
		if (!resCursor.next()) {
			stmtCursor.close();
			return false;
		}
		String sCursor = resCursor.getString(1);
		response.setHeader("Content-Type", Response.mimetypeJson);
		response.getWriter().print(sCursor);
		stmtCursor.close();

		//update access time
		PreparedStatement stmtTime = stmt(db, "UPDATE docs SET last_access = ? WHERE id = ?",
			unixTime(), docID);
		stmtTime.executeUpdate();
		stmtTime.close();
		return true;
	}

}
