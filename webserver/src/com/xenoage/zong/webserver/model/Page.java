package com.xenoage.zong.webserver.model;

import static com.xenoage.zong.webserver.util.Database.insert;
import static com.xenoage.zong.webserver.util.Database.stmt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Information about a page.
 * 
 * @author Andreas Wenger
 */
public class Page {

	public final int docID, page;
	public final float width, height; //in mm


	public Page(int docID, int page, float width, float height) {
		this.docID = docID;
		this.page = page;
		this.width = width;
		this.height = height;
	}

	public static Page fromDB(Connection db, int docID, int page)
		throws SQLException {
		PreparedStatement stmt = stmt(db, "SELECT doc_id, page, width, height "
			+ "FROM pageinfos WHERE doc_id = ? AND page = ?", docID, page);
		ResultSet res = stmt.executeQuery();
		res.next();
		Page ret = new Page(res.getInt(1), res.getInt(2), res.getFloat(3), res.getFloat(4));
		stmt.close();
		return ret;
	}

	public void insertIntoDB(Connection db)
		throws SQLException {
		insert(db, "pageinfos", "doc_id, page, width, height", docID, page, width, height);
	}

}
