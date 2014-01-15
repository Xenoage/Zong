package com.xenoage.zong.webserver.model;

import static com.xenoage.zong.webserver.util.Database.insert;
import static com.xenoage.zong.webserver.util.Database.stmt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Information about a scaled page.
 * 
 * @author Andreas Wenger
 */
public class ScaledPage {

	public final int docID;
	public final int page;
	/** scaling in 72dpi/10000 space (e.g. return=5000 means 36dpi) */
	public final int scaling;
	public final int widthPx;
	public final int heightPx;


	public ScaledPage(int docID, int page, int scaling, int widthPx, int heightPx) {
		this.docID = docID;
		this.page = page;
		this.scaling = scaling;
		this.widthPx = widthPx;
		this.heightPx = heightPx;
	}

	/**
	 * Loads the {@link ScaledPage} with the given attributes from the database.
	 * If it can not be found, null is returned.
	 */
	public static ScaledPage fromDB(Connection db, int docID, int page, int scaling)
		throws SQLException {
		PreparedStatement stmt = stmt(db, "SELECT doc_id, page, scaling, widthpx, heightpx "
			+ "FROM scaledpageinfos WHERE doc_id = ? AND page = ? AND scaling = ?", docID, page, scaling);
		ResultSet res = stmt.executeQuery();
		if (!res.next())
			return null;
		ScaledPage ret = new ScaledPage(res.getInt(1), res.getInt(2), res.getInt(3), res.getInt(4),
			res.getInt(5));
		stmt.close();
		return ret;
	}

	public void insertIntoDB(Connection db)
		throws SQLException {
		insert(db, "scaledpageinfos", "doc_id, page, scaling, widthpx, heightpx", docID, page, scaling,
			widthPx, heightPx);
	}

}
