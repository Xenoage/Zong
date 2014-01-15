package com.xenoage.zong.webserver.util;

import static com.xenoage.utils.kernel.Range.range;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Database helper methods.
 * 
 * @author Andreas Wenger
 */
public class Database {

	/**
	 * Returns true, if at least one row in the given table with the given
	 * conditions exist. "?" are replaced by the given conditions.
	 */
	public static boolean exists(Connection db, String table, String conditions,
		Object... conditionParams)
		throws SQLException {
		PreparedStatement stmt = db.prepareStatement("SELECT * FROM " + table + " WHERE " + conditions);
		for (int i : range(conditionParams))
			stmt.setObject(i + 1, conditionParams[i]);
		ResultSet res = stmt.executeQuery();
		boolean ret = res.next();
		stmt.close();
		return ret;
	}

	/**
	 * Inserts the given row into the database.
	 */
	public static void insert(Connection db, String table, String columns, Object... values)
		throws SQLException {
		StringBuilder valuesString = new StringBuilder();
		for (int i = 0; i < values.length - 1; i++)
			valuesString.append("?, ");
		valuesString.append("?");

		PreparedStatement stmt = db.prepareStatement("INSERT INTO " + table + " (" + columns +
			") VALUES (" + valuesString + ")");
		for (int i : range(values))
			stmt.setObject(i + 1, values[i]);
		stmt.executeUpdate();
		stmt.close();
	}

	/**
	 * Deletes rows from the given database.
	 */
	public static void delete(Connection db, String table, String where, Object... values)
		throws SQLException {
		PreparedStatement stmt = db.prepareStatement("DELETE FROM " + table + " WHERE " + where);
		for (int i : range(values))
			stmt.setObject(i + 1, values[i]);
		stmt.executeUpdate();
		stmt.close();
	}

	/**
	 * Creates a {@link PreparedStatement}.
	 */
	public static PreparedStatement stmt(Connection db, String sql, Object... values)
		throws SQLException {
		PreparedStatement stmt = db.prepareStatement(sql);
		for (int i : range(values))
			stmt.setObject(i + 1, values[i]);
		return stmt;
	}

	/**
	 * Gets the current unix time.
	 */
	public static long unixTime() {
		return System.currentTimeMillis() / 1000;
	}

}
