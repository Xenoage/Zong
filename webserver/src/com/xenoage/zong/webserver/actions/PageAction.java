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

import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.webserver.Server;
import com.xenoage.zong.webserver.model.ScaledPage;
import com.xenoage.zong.webserver.model.requests.PageRequest;
import com.xenoage.zong.webserver.model.requests.Request;

/**
 * Responds with the tile image for the given document and page index.
 * 
 * @author Andreas Wenger
 */
public class PageAction
	extends RetryAction {

	@Override public boolean performTry(Request request, Server server, HttpServletResponse response)
		throws SQLException, IOException {
		PageRequest pageRequest = getAs(PageRequest.class, request);
		Connection db = server.getDBConnection();

		//get ID and number of pages of document
		PreparedStatement stmtID = stmt(db, "SELECT id, pages FROM docs WHERE public_id = ?",
			pageRequest.id);
		ResultSet resID = stmtID.executeQuery();
		if (!resID.next()) {
			stmtID.close();
			return false;
		}
		int docID = resID.getInt(1);
		int pages = resID.getInt(2);
		stmtID.close();

		//unknown page?
		if (pageRequest.page < 0 || pageRequest.page >= pages) {
			throw new IOException("unknown page");
		}

		//load size of first page
		//if it does not exist (yet), wait a little bit and try again
		PreparedStatement stmtFirstPageSize = stmt(db,
			"SELECT width, height FROM pageinfos WHERE doc_id = ? AND page = 0", docID);
		ResultSet resFirstPageSize = stmtFirstPageSize.executeQuery();
		if (!resFirstPageSize.next()) {
			stmtFirstPageSize.close();
			return false;
		}
		Size2f firstPageSize = new Size2f(resFirstPageSize.getFloat(1), resFirstPageSize.getFloat(2));
		stmtFirstPageSize.close();

		//unknown scaling?
		int scaling = pageRequest.scaling.convertTo10000(firstPageSize);
		boolean scalingExists = (ScaledPage.fromDB(db, docID, 0, scaling) != null);
		if (!scalingExists) {
			throw new IOException("unknown scaling");
		}

		//deliver page image
		PreparedStatement stmtImage = stmt(db, "SELECT image FROM pages WHERE doc_id = ?"
			+ " AND page = ? AND scaling = ?", docID, pageRequest.page, scaling);
		ResultSet resImage = stmtImage.executeQuery();
		if (!resImage.next()) {
			stmtImage.close();
			return false;
		}
		Blob blob = resImage.getBlob(1);
		byte[] imageData = blob.getBytes(1, (int) blob.length());
		stmtImage.close();
		response.setHeader("Content-Type", "image/png");
		response.getOutputStream().write(imageData);

		//update access time
		PreparedStatement stmtTime = stmt(db, "UPDATE docs SET last_access = ? WHERE id = ?",
			unixTime(), docID);
		stmtTime.executeUpdate();
		stmtTime.close();
		return true;
	}

}
