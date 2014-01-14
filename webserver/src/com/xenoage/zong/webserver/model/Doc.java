package com.xenoage.zong.webserver.model;

import static com.xenoage.zong.webserver.util.Database.stmt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;


/**
 * A document.
 * 
 * @author Andreas Wenger
 */
public class Doc
{

	public final int id;
	public final String url;
	public final UUID publicID;
	public final int pages;
	public final int lastAccess;


	public Doc(int id, String url, UUID publicID, int pages, int lastAccess)
	{
		this.id = id;
		this.url = url;
		this.publicID = publicID;
		this.pages = pages;
		this.lastAccess = lastAccess;
	}


	public static Doc fromDB(Connection db, String url)
		throws SQLException
	{
		PreparedStatement stmt = stmt(db,
			"SELECT id, url, public_id, pages, last_access FROM docs WHERE url = ?", url);
		ResultSet res = stmt.executeQuery();
		res.next();
		Doc ret = new Doc(res.getInt(1), res.getString(2), (UUID)res.getObject(3), res.getInt(4),
			res.getInt(5));
		stmt.close();
		return ret;
	}


}
